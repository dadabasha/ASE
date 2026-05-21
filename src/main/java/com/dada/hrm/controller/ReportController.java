package com.dada.hrm.controller;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dada.hrm.dto.ReportPeriod;
import com.dada.hrm.entity.DailyActivity;
import com.dada.hrm.service.ActivityService;
import com.dada.hrm.service.ReportExportService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ActivityService activityService;
    private final ReportExportService reportExportService;

    public ReportController(ActivityService activityService, ReportExportService reportExportService) {
        this.activityService = activityService;
        this.reportExportService = reportExportService;
    }

    @GetMapping
    public List<DailyActivity> report(
            @RequestParam ReportPeriod period,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        LocalDate base = date == null ? LocalDate.now() : date;
        LocalDate[] range = rangeFor(period, base);
        return activityService.report(range[0], range[1]);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> export(
            @RequestParam ReportPeriod period,
            @RequestParam(defaultValue = "excel") String format,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<DailyActivity> records = report(period, date);
        boolean pdf = "pdf".equalsIgnoreCase(format);
        byte[] content = pdf ? reportExportService.toPdf(records) : reportExportService.toExcel(records);
        String extension = pdf ? "pdf" : "xlsx";
        MediaType mediaType = pdf
                ? MediaType.APPLICATION_PDF
                : MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(period.name().toLowerCase() + "-report." + extension).build().toString())
                .body(content);
    }

    private LocalDate[] rangeFor(ReportPeriod period, LocalDate base) {
        return switch (period) {
            case DAILY -> new LocalDate[] { base, base };
            case WEEKLY -> {
                LocalDate start = base.with(DayOfWeek.MONDAY);
                yield new LocalDate[] { start, start.plusDays(6) };
            }
            case MONTHLY -> {
                YearMonth month = YearMonth.from(base);
                yield new LocalDate[] { month.atDay(1), month.atEndOfMonth() };
            }
        };
    }
}
