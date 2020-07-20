package com.example.springreport;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.List;

@SpringBootApplication
public class SpringReportApplication {
	public static final String file1 = "scan-2020-06-24-16_43_53.csv";
	public static final String file2 = "scan-2020-06-25-16_43_53_fake.csv";

//	public static final String file1 = "scan-2020-05-07-15_29_12.csv";
//	public static final String file2 = "scan-2020-06-24-16_43_53.csv";

	public static void main(String[] args) {
		SpringApplication.run(SpringReportApplication.class, args);
	}

	@Bean
	public CommandLineRunner initDatabase(ReportRepository repository) {
		List<Report> reports = ReportProcessor.parseFile(file1);
		List<Report> reports2 = ReportProcessor.parseFile(file2);

		return (args) -> {
			ReportProcessor.saveReportsToDb(reports, repository);
			ReportProcessor.saveReportsToDb(reports2, repository);
		};
	}
}