package com.dada.hrm.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.dada.hrm.entity.ActivityStatus;
import com.dada.hrm.entity.BackupStatus;
import com.dada.hrm.entity.DailyActivity;

public interface DailyActivityRepository extends JpaRepository<DailyActivity, Long>, JpaSpecificationExecutor<DailyActivity> {
    List<DailyActivity> findTop50ByOrderByActivityDateDescIdDesc();

    long countByActivityDate(LocalDate date);

    long countByActivityDateAndStatus(LocalDate date, ActivityStatus status);

    long countByActivityDateAndBackupStatus(LocalDate date, BackupStatus backupStatus);

    @Query("select coalesce(sum(a.pendingTickets), 0) from DailyActivity a where a.activityDate = ?1")
    long openTicketsForDate(LocalDate date);

    @Query("select coalesce(sum(a.ticketsResolved), 0) from DailyActivity a where a.activityDate = ?1")
    long resolvedTicketsForDate(LocalDate date);
}
