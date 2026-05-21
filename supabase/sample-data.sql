insert into public.app_users (username, password, full_name, role) values
('admin', 'admin123', 'System Admin', 'ADMIN'),
('engineer', 'engineer123', 'Asha Kumar', 'ENGINEER'),
('ravi', 'engineer123', 'Ravi Menon', 'ENGINEER')
on conflict (username) do nothing;

insert into public.daily_activities (
    activity_date, engineer_name, application_name, application_status,
    server_health_checked, cpu_usage, ram_usage, disk_space_status,
    services_checked, database_connectivity_checked, backup_status,
    logs_checked, errors_found, tickets_received, tickets_resolved,
    pending_tickets, escalated_issues, remarks, updated_by, status
) values
(current_date, 'Asha Kumar', 'SAP ERP', 'WORKING', true, 42, 68, 'Healthy - 62% used', true, true, 'SUCCESS', true, 'None', 8, 7, 1, 'None', 'Routine daily support update', 'Asha Kumar', 'COMPLETED'),
(current_date, 'Ravi Menon', 'Payroll Portal', 'NOT_WORKING', true, 77, 81, 'Warning - 84% used', true, false, 'FAILED', true, 'API timeout observed', 6, 4, 3, 'Shared with L2 team', 'Vendor bridge opened', 'Ravi Menon', 'PENDING');
