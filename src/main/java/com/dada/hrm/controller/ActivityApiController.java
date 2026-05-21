package com.dada.hrm.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dada.hrm.dto.DashboardStats;
import com.dada.hrm.entity.ActivityStatus;
import com.dada.hrm.entity.DailyActivity;
import com.dada.hrm.repository.DailyActivityRepository;
import com.dada.hrm.service.ActivityService;
import com.dada.hrm.service.SessionAccessService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/activities")
public class ActivityApiController {
    private final DailyActivityRepository activityRepository;
    private final ActivityService activityService;
    private final SessionAccessService sessionAccessService;

    public ActivityApiController(DailyActivityRepository activityRepository, ActivityService activityService,
            SessionAccessService sessionAccessService) {
        this.activityRepository = activityRepository;
        this.activityService = activityService;
        this.sessionAccessService = sessionAccessService;
    }

    @GetMapping
    public List<DailyActivity> list(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String engineer,
            @RequestParam(required = false) String application,
            @RequestParam(required = false) ActivityStatus status) {
        if (date == null && engineer == null && application == null && status == null) {
            return activityRepository.findTop50ByOrderByActivityDateDescIdDesc();
        }
        return activityService.search(date, engineer, application, status);
    }

    @GetMapping("/{id}")
    public DailyActivity get(@PathVariable Long id) {
        return activityRepository.findById(id).orElseThrow();
    }

    @PostMapping
    public DailyActivity create(@RequestBody DailyActivity activity) {
        return activityRepository.save(activity);
    }

    @PutMapping("/{id}")
    public DailyActivity update(@PathVariable Long id, @RequestBody DailyActivity request) {
        request.setId(id);
        return activityRepository.save(request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, HttpSession session) {
        sessionAccessService.requireAdmin(session);
        activityRepository.deleteById(id);
    }

    @GetMapping("/dashboard")
    public DashboardStats dashboard() {
        return activityService.dashboardStats();
    }
}
