package com.example.springreport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>, QueryByExampleExecutor<Report> {

    Report save(Report r);

    List<Report> findAll();

    Report findReportById(long id);

    @Query(nativeQuery = true,
            value= "SELECT * FROM Report r WHERE r.key = :key and r.scan_tool = :scanTool"
                    + " and r.application = :application and r.component = :component"
                    + " and r.title = :title and r.severity = :severity and r.priority = :priority"
                    + " and r.full_text = :fullText and r.seen = :seen")
    Report findReportFromFieldsNative(
            @Param("key") String key, @Param("scanTool") String scanTool, @Param("application") String application,
            @Param("component") String component, @Param("title") String title,
            @Param("severity") String severity, @Param("priority") String priority,
            @Param("fullText") String fullText, @Param("seen") String seen);

    @Query(nativeQuery = true,
            value = "SELECT * FROM Report r WHERE r.key = :key")
    Report findReportFromUniqueIdNative(@Param("key") String key);

    @Query(nativeQuery =  true,
            value = "SELECT * FROM Report r WHERE r.seen = :seen")
    List<Report> findReportFromSeenNative(@Param("seen") String seen);
}