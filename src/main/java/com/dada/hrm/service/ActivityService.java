package com.dada.hrm.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.dada.hrm.dto.DashboardStats;
import com.dada.hrm.entity.ActivityStatus;
import com.dada.hrm.entity.BackupStatus;
import com.dada.hrm.entity.DailyActivity;
import com.dada.hrm.repository.DailyActivityRepository;

import jakarta.persistence.criteria.Predicate;

@Service
public class ActivityService {
    private final DailyActivityRepository activityRepository;

    public ActivityService(DailyActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public DashboardStats dashboardStats() {
        LocalDate today = LocalDate.now();
        return new DashboardStats(
                activityRepository.countByActivityDate(today),
                activityRepository.countByActivityDateAndStatus(today, ActivityStatus.COMPLETED),
                activityRepository.countByActivityDateAndStatus(today, ActivityStatus.PENDING),
                activityRepository.countByActivityDateAndBackupStatus(today, BackupStatus.FAILED),
                activityRepository.openTicketsForDate(today),
                activityRepository.resolvedTicketsForDate(today));
    }

    public List<DailyActivity> search(LocalDate date, String engineer, String application, ActivityStatus status) {
        Specification<DailyActivity> spec = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (date != null) {
                predicate = cb.and(predicate, cb.equal(root.get("activityDate"), date));
            }
            if (engineer != null && !engineer.isBlank()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("engineerName")), "%" + engineer.toLowerCase() + "%"));
            }
            if (application != null && !application.isBlank()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("applicationName")), "%" + application.toLowerCase() + "%"));
            }
            if (status != null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), status));
            }
            return predicate;
        };
        return activityRepository.findAll(spec);
    }

    public List<DailyActivity> report(LocalDate start, LocalDate end) {
        Specification<DailyActivity> spec = (root, query, cb) -> cb.between(root.get("activityDate"), start, end);
        return activityRepository.findAll(spec);
    }
}
