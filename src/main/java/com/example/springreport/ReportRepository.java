package com.example.springreport;

import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PatchMapping;

import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>, QueryByExampleExecutor<Report> {
    Report save(Report r);
    List<Report> findAll();
    Report findReportById(long id);

    @Query(nativeQuery = true,
            value= "SELECT * FROM Report r WHERE r.name = :name and r.scan_tool = :scanTool"
                    + " and r.application = :application and r.component = :component"
                    + " and r.title = :title and r.severity = :severity and r.priority = :priority"
                    + " and r.full_text = :fullText and r.seen = :seen")
    Report findReportFromFieldsNative(
            @Param("name") String name, @Param("scanTool") String scanTool, @Param("application") String application,
            @Param("component") String component, @Param("title") String title,
            @Param("severity") String severity, @Param("priority") String priority,
            @Param("fullText") String fullText, @Param("seen") String seen);

    @Query(nativeQuery = true,
            value = "SELECT * FROM Report r WHERE r.name = :name")
    Report findReportFromUniqueIdNative(@Param("name") String name);
}