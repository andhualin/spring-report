package com.example.springreport;

import com.opencsv.CSVReader;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringReportApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringReportApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(ReportRepository repository) {
		List<Report> reports = ReportParser.parseReport("sample.csv");
		return (args) -> {
			// save Report csv to the database
			for (Report report : reports) {
				// todo: query the database to see if the report already exists
				repository.save(report);
			}
		};
	}
}
