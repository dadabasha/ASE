package com.dada.hrm.dto;

public record DashboardStats(
        long totalActivitiesToday,
        long completedActivities,
        long pendingActivities,
        long failedBackupCount,
        long openTickets,
        long resolvedTickets) {
}
