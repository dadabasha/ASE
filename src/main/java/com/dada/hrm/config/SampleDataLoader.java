package com.dada.hrm.config;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.dada.hrm.entity.ActivityStatus;
import com.dada.hrm.entity.AppUser;
import com.dada.hrm.entity.ApplicationStatus;
import com.dada.hrm.entity.BackupStatus;
import com.dada.hrm.entity.DailyActivity;
import com.dada.hrm.entity.UserRole;
import com.dada.hrm.repository.AppUserRepository;
import com.dada.hrm.repository.DailyActivityRepository;

@Component
public class SampleDataLoader implements CommandLineRunner {
    private final AppUserRepository userRepository;
    private final DailyActivityRepository activityRepository;

    public SampleDataLoader(AppUserRepository userRepository, DailyActivityRepository activityRepository) {
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            createUser("admin", "admin123", "System Admin", UserRole.ADMIN);
            createUser("engineer", "engineer123", "Asha Kumar", UserRole.ENGINEER);
            createUser("ravi", "engineer123", "Ravi Menon", UserRole.ENGINEER);
        }
        if (activityRepository.count() == 0) {
            createActivity(LocalDate.now(), "Asha Kumar", "SAP ERP", ApplicationStatus.WORKING, BackupStatus.SUCCESS, 8, 7, 1, ActivityStatus.COMPLETED);
            createActivity(LocalDate.now(), "Ravi Menon", "Payroll Portal", ApplicationStatus.NOT_WORKING, BackupStatus.FAILED, 6, 4, 3, ActivityStatus.PENDING);
            createActivity(LocalDate.now().minusDays(1), "Asha Kumar", "Inventory ERP", ApplicationStatus.WORKING, BackupStatus.NOT_CHECKED, 3, 3, 0, ActivityStatus.COMPLETED);
        }
    }

    private void createUser(String username, String password, String fullName, UserRole role) {
        AppUser user = new AppUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setRole(role);
        userRepository.save(user);
    }

    private void createActivity(LocalDate date, String engineer, String application, ApplicationStatus applicationStatus,
            BackupStatus backupStatus, int received, int resolved, int pending, ActivityStatus status) {
        DailyActivity activity = new DailyActivity();
        activity.setActivityDate(date);
        activity.setEngineerName(engineer);
        activity.setApplicationName(application);
        activity.setApplicationStatus(applicationStatus);
        activity.setServerHealthChecked(true);
        activity.setCpuUsage(42);
        activity.setRamUsage(68);
        activity.setDiskSpaceStatus("Healthy - 62% used");
        activity.setServicesChecked(true);
        activity.setDatabaseConnectivityChecked(true);
        activity.setBackupStatus(backupStatus);
        activity.setLogsChecked(true);
        activity.setErrorsFound(applicationStatus == ApplicationStatus.NOT_WORKING ? "API timeout observed" : "None");
        activity.setTicketsReceived(received);
        activity.setTicketsResolved(resolved);
        activity.setPendingTickets(pending);
        activity.setEscalatedIssues(pending > 0 ? "Shared with L2 team" : "None");
        activity.setRemarks("Routine daily support update");
        activity.setUpdatedBy(engineer);
        activity.setStatus(status);
        activityRepository.save(activity);
    }
}
