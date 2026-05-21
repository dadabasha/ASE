const fields = [
    'activityDate', 'engineerName', 'applicationName', 'applicationStatus',
    'serverHealthChecked', 'cpuUsage', 'ramUsage', 'diskSpaceStatus',
    'servicesChecked', 'databaseConnectivityChecked', 'backupStatus',
    'logsChecked', 'errorsFound', 'ticketsReceived', 'ticketsResolved',
    'pendingTickets', 'escalatedIssues', 'remarks', 'updatedBy', 'status'
];

let currentUser = null;

document.addEventListener('DOMContentLoaded', async () => {
    await loadCurrentUser();
    setDefaults();
    bindEvents();
    await Promise.all([loadStats(), loadActivities(), loadUsers(), loadReport()]);
});

async function loadCurrentUser() {
    const response = await fetch('/api/auth/me');
    if (!response.ok) {
        location.href = '/login';
        return;
    }
    currentUser = await response.json();
    document.getElementById('currentUser').textContent = `${currentUser.fullName} (${currentUser.role})`;
    document.getElementById('adminPanel').classList.toggle('d-none', currentUser.role !== 'ADMIN');
}

function bindEvents() {
    document.getElementById('logoutBtn').addEventListener('click', async () => {
        await fetch('/api/auth/logout', { method: 'POST' });
        location.href = '/login';
    });
    document.getElementById('activityForm').addEventListener('submit', saveActivity);
    document.getElementById('resetFormBtn').addEventListener('click', resetForm);
    document.getElementById('searchForm').addEventListener('submit', async (event) => {
        event.preventDefault();
        await loadActivities();
    });
    document.getElementById('userForm').addEventListener('submit', createUser);
    document.getElementById('loadReportBtn').addEventListener('click', loadReport);
    document.getElementById('excelBtn').addEventListener('click', () => exportReport('excel'));
    document.getElementById('pdfBtn').addEventListener('click', () => exportReport('pdf'));
}

function setDefaults() {
    const today = new Date().toISOString().slice(0, 10);
    document.getElementById('activityDate').value = today;
    document.getElementById('reportDate').value = today;
}

async function loadStats() {
    const stats = await fetchJson('/api/activities/dashboard');
    const cards = [
        ['Total today', stats.totalActivitiesToday, 'TOD'],
        ['Completed', stats.completedActivities, 'CMP'],
        ['Pending', stats.pendingActivities, 'PND'],
        ['Failed backups', stats.failedBackupCount, 'BKP'],
        ['Open tickets', stats.openTickets, 'OPN'],
        ['Resolved tickets', stats.resolvedTickets, 'RES']
    ];
    document.getElementById('statsGrid').innerHTML = cards.map(([label, value, code]) => `
        <div class="stat-card">
            <div class="stat-icon">${code}</div>
            <span>${label}</span>
            <strong>${value}</strong>
        </div>
    `).join('');
}

async function loadActivities() {
    const params = new URLSearchParams();
    addParam(params, 'date', valueOf('searchDate'));
    addParam(params, 'engineer', valueOf('searchEngineer'));
    addParam(params, 'application', valueOf('searchApplication'));
    addParam(params, 'status', valueOf('searchStatus'));
    const activities = await fetchJson(`/api/activities?${params}`);
    document.getElementById('recordsTable').innerHTML = activities.map(row => `
        <tr>
            <td>${row.activityDate}</td>
            <td><strong>${escapeHtml(row.engineerName)}</strong></td>
            <td><span class="app-name">${escapeHtml(row.applicationName)}</span></td>
            <td>${badge(row.applicationStatus)}</td>
            <td>${badge(row.backupStatus)}</td>
            <td>${row.ticketsResolved}/${row.ticketsReceived} resolved, ${row.pendingTickets} open</td>
            <td>${badge(row.status)}</td>
            <td class="text-end">
                <button class="btn btn-sm btn-soft-primary" onclick='editActivity(${JSON.stringify(row)})'>Edit</button>
                ${currentUser.role === 'ADMIN' ? `<button class="btn btn-sm btn-soft-danger" onclick="deleteActivity(${row.id})">Delete</button>` : ''}
            </td>
        </tr>
    `).join('');
}

async function saveActivity(event) {
    event.preventDefault();
    const id = valueOf('activityId');
    const payload = Object.fromEntries(fields.map(field => [field, normalizeValue(field, valueOf(field))]));
    const response = await fetch(id ? `/api/activities/${id}` : '/api/activities', {
        method: id ? 'PUT' : 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    if (!response.ok) {
        alert('Unable to save activity. Please check required fields.');
        return;
    }
    resetForm();
    await Promise.all([loadStats(), loadActivities(), loadReport()]);
}

function editActivity(row) {
    document.getElementById('activityId').value = row.id;
    fields.forEach(field => {
        const input = document.getElementById(field);
        if (input) input.value = row[field] ?? '';
    });
    window.scrollTo({ top: 0, behavior: 'smooth' });
}

async function deleteActivity(id) {
    if (!confirm('Delete this activity record?')) return;
    await fetch(`/api/activities/${id}`, { method: 'DELETE' });
    await Promise.all([loadStats(), loadActivities(), loadReport()]);
}

function resetForm() {
    document.getElementById('activityForm').reset();
    document.getElementById('activityId').value = '';
    setDefaults();
    if (currentUser) {
        document.getElementById('updatedBy').value = currentUser.fullName;
        if (currentUser.role === 'ENGINEER') document.getElementById('engineerName').value = currentUser.fullName;
    }
}

async function createUser(event) {
    event.preventDefault();
    const payload = {
        fullName: valueOf('newFullName'),
        username: valueOf('newUsername'),
        password: valueOf('newPassword'),
        role: valueOf('newRole')
    };
    const response = await fetch('/api/admin/users', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    });
    if (!response.ok) {
        alert('Unable to create user. Username may already exist.');
        return;
    }
    document.getElementById('userForm').reset();
    await loadUsers();
}

async function loadUsers() {
    if (!currentUser || currentUser.role !== 'ADMIN') return;
    const users = await fetchJson('/api/admin/users');
    document.getElementById('userList').innerHTML = users.map(user => `
        <div class="user-row">
            <strong>${escapeHtml(user.fullName)}</strong>
            <div class="text-secondary small">${escapeHtml(user.username)} | ${user.role}</div>
        </div>
    `).join('');
}

async function loadReport() {
    const period = valueOf('reportPeriod');
    const date = valueOf('reportDate');
    const records = await fetchJson(`/api/reports?period=${period}&date=${date}`);
    document.getElementById('reportOutput').innerHTML = records.length ? records.map(row => `
        <div class="report-row">
            <strong>${row.activityDate} - ${escapeHtml(row.applicationName)}</strong>
            <div>${escapeHtml(row.engineerName)} | ${row.status} | Backup: ${row.backupStatus} | Open tickets: ${row.pendingTickets}</div>
        </div>
    `).join('') : '<div class="text-secondary">No report records found for this period.</div>';
}

function exportReport(format) {
    const period = valueOf('reportPeriod');
    const date = valueOf('reportDate');
    downloadReport(`/api/reports/export?period=${period}&date=${date}&format=${format}`, `${period.toLowerCase()}-report.${format === 'pdf' ? 'pdf' : 'xlsx'}`);
}

async function downloadReport(url, fallbackName) {
    const response = await fetch(url, { credentials: 'same-origin' });
    if (!response.ok) {
        alert('Unable to export report. Please login again and try once more.');
        return;
    }
    const blob = await response.blob();
    const disposition = response.headers.get('Content-Disposition') || '';
    const match = disposition.match(/filename="?([^"]+)"?/i);
    const filename = match ? match[1] : fallbackName;
    const href = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = href;
    link.download = filename;
    document.body.appendChild(link);
    link.click();
    link.remove();
    URL.revokeObjectURL(href);
}

async function fetchJson(url) {
    const response = await fetch(url);
    if (!response.ok) throw new Error(`Request failed: ${url}`);
    return response.json();
}

function addParam(params, key, value) {
    if (value) params.set(key, value);
}

function valueOf(id) {
    return document.getElementById(id).value;
}

function normalizeValue(field, value) {
    if (['serverHealthChecked', 'servicesChecked', 'databaseConnectivityChecked', 'logsChecked'].includes(field)) {
        return value === 'true';
    }
    if (['cpuUsage', 'ramUsage', 'ticketsReceived', 'ticketsResolved', 'pendingTickets'].includes(field)) {
        return Number(value || 0);
    }
    return value;
}

function badge(value) {
    const label = String(value).replaceAll('_', ' ');
    const css = value === 'SUCCESS' || value === 'COMPLETED' || value === 'WORKING'
        ? 'badge-good'
        : value === 'PENDING' || value === 'NOT_CHECKED'
            ? 'badge-warn'
            : 'badge-bad';
    return `<span class="badge-status ${css}">${label}</span>`;
}

function escapeHtml(value) {
    return String(value ?? '').replace(/[&<>"']/g, char => ({
        '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
    }[char]));
}
