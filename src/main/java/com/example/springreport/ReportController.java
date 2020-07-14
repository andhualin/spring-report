package com.example.springreport;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    // GET report by id
    @GetMapping("/reports/{id}")
    public Report getReportById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new ReportNotFoundException(id)); }

    // POST method to create a report
    @PostMapping("/reports")
    public Report createReport(@RequestBody Report report) {
        return repository.save(report);
    }
}
