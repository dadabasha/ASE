package com.dada.hrm.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "daily_activities")
public class DailyActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate activityDate;

    @Column(nullable = false, length = 120)
    private String engineerName;

    @Column(nullable = false, length = 140)
    private String applicationName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApplicationStatus applicationStatus;

    @Column(nullable = false)
    private boolean serverHealthChecked;

    @Column(nullable = false)
    private Integer cpuUsage;

    @Column(nullable = false)
    private Integer ramUsage;

    @Column(nullable = false, length = 120)
    private String diskSpaceStatus;

    @Column(nullable = false)
    private boolean servicesChecked;

    @Column(nullable = false)
    private boolean databaseConnectivityChecked;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BackupStatus backupStatus;

    @Column(nullable = false)
    private boolean logsChecked;

    @Column(length = 1000)
    private String errorsFound;

    @Column(nullable = false)
    private Integer ticketsReceived;

    @Column(nullable = false)
    private Integer ticketsResolved;

    @Column(nullable = false)
    private Integer pendingTickets;

    @Column(length = 1000)
    private String escalatedIssues;

    @Column(length = 1200)
    private String remarks;

    @Column(nullable = false, length = 120)
    private String updatedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ActivityStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(LocalDate activityDate) {
        this.activityDate = activityDate;
    }

    public String getEngineerName() {
        return engineerName;
    }

    public void setEngineerName(String engineerName) {
        this.engineerName = engineerName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    public boolean isServerHealthChecked() {
        return serverHealthChecked;
    }

    public void setServerHealthChecked(boolean serverHealthChecked) {
        this.serverHealthChecked = serverHealthChecked;
    }

    public Integer getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(Integer cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public Integer getRamUsage() {
        return ramUsage;
    }

    public void setRamUsage(Integer ramUsage) {
        this.ramUsage = ramUsage;
    }

    public String getDiskSpaceStatus() {
        return diskSpaceStatus;
    }

    public void setDiskSpaceStatus(String diskSpaceStatus) {
        this.diskSpaceStatus = diskSpaceStatus;
    }

    public boolean isServicesChecked() {
        return servicesChecked;
    }

    public void setServicesChecked(boolean servicesChecked) {
        this.servicesChecked = servicesChecked;
    }

    public boolean isDatabaseConnectivityChecked() {
        return databaseConnectivityChecked;
    }

    public void setDatabaseConnectivityChecked(boolean databaseConnectivityChecked) {
        this.databaseConnectivityChecked = databaseConnectivityChecked;
    }

    public BackupStatus getBackupStatus() {
        return backupStatus;
    }

    public void setBackupStatus(BackupStatus backupStatus) {
        this.backupStatus = backupStatus;
    }

    public boolean isLogsChecked() {
        return logsChecked;
    }

    public void setLogsChecked(boolean logsChecked) {
        this.logsChecked = logsChecked;
    }

    public String getErrorsFound() {
        return errorsFound;
    }

    public void setErrorsFound(String errorsFound) {
        this.errorsFound = errorsFound;
    }

    public Integer getTicketsReceived() {
        return ticketsReceived;
    }

    public void setTicketsReceived(Integer ticketsReceived) {
        this.ticketsReceived = ticketsReceived;
    }

    public Integer getTicketsResolved() {
        return ticketsResolved;
    }

    public void setTicketsResolved(Integer ticketsResolved) {
        this.ticketsResolved = ticketsResolved;
    }

    public Integer getPendingTickets() {
        return pendingTickets;
    }

    public void setPendingTickets(Integer pendingTickets) {
        this.pendingTickets = pendingTickets;
    }

    public String getEscalatedIssues() {
        return escalatedIssues;
    }

    public void setEscalatedIssues(String escalatedIssues) {
        this.escalatedIssues = escalatedIssues;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public ActivityStatus getStatus() {
        return status;
    }

    public void setStatus(ActivityStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
