package com.example.springreport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>{

    Report save(Report r);

    List<Report> findAll();

    Optional<Report> findById(Long id);

    @Query(nativeQuery = true,
            value = "SELECT * FROM Report r WHERE r.key = :key")
    List<Report> findReportFromUniqueIdNative(@Param("key") String key);

    @Query(nativeQuery =  true,
            value = "SELECT * FROM Report r WHERE r.date_first_seen = :dateFirstSeen")
    List<Report> findReportFromDateNative(@Param("dateFirstSeen") String dateFirstSeen);

}