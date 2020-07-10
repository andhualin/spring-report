package com.example.springreport.repositories;

import com.example.springreport.models.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    Report save(Report r);
    List<Report> findAll();
}
