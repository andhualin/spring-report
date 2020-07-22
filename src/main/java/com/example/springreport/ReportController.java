package com.example.springreport;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@RestController
public class ReportController {
    private ReportRepository repository;

    ReportController(ReportRepository repository) {
        this.repository = repository;
    }

    // GET method to get all reports
    @GetMapping("/reports")
    public List<Report> getAllReport() {
        return repository.findAll();
    }


    // POST method to create a report
    @PostMapping("/reports")
    public Report createReport(@RequestBody Report report) {
        return repository.save(report);
    }

    // PUT method to update a report
    @PutMapping("/reports/{id}")
    public Report updateReport(@RequestBody Report newReport, @PathVariable Long id) {
        return repository.findById(id)
                .map(report -> {
                    report.setKey(newReport.getKey());
                    report.setScanTool(newReport.getScanTool());
                    report.setApplication(newReport.getApplication());
                    report.setComponent(newReport.getComponent());
                    report.setTitle(newReport.getTitle());
                    report.setSeverity(newReport.getSeverity());
                    report.setPriority(newReport.getPriority());
                    report.setFullText(newReport.getFullText());
                    report.setSeen(newReport.isSeen());
                    report.setDateFirstSeen(newReport.getDateFirstSeen());
                    return repository.save(report);
                })
                .orElseGet(() -> {
                    newReport.setId(id);
                    return repository.save(newReport);
                });
    }

    // GET report by id
    @GetMapping("/reports/{id}")
    public Report one(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new ReportNotFoundException(id));
    }

    // GET reports from a key
    @GetMapping(value = "/reports", params = "key")
    public List<Report> getAllReportByKeyNative(@RequestParam String key) {
        return repository.findReportFromUniqueIdNative(key);
    }

    // GET reports from a date
    @GetMapping(value = "/reports", params = "date")
    public List<Report> getAllReportByDateFirstSeen(@RequestParam String date) {
        return repository.findReportFromDateNative(date);
    }

    // GET reports from a date
    @GetMapping(value = "/reports/distinctDates")
    public List<String> getDistinctDates() {
        return repository.findDistinctDatesNative();
    }

}