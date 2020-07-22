package com.example.springreport;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.List;

@SpringBootApplication
public class SpringReportApplication {
	private static String file0 = "scan-2020-05-07-15_29_12.csv";
	private static final String file1 = "scan-2020-06-24-16_43_53.csv";
	private static final String file2 = "scan-2020-06-25-16_43_53_fake.csv";
	private static final String file3 = "scan-2020-06-29-16_43_53_fake.csv";

	public static void main(String[] args) {

		SpringApplication.run(SpringReportApplication.class, args);
	}

	@Bean
	public CommandLineRunner initDatabase(ReportRepository repository) {
		List<Report> reports1 = ReportProcessor.parseFile(file1);
		List<Report> reports2 = ReportProcessor.parseFile(file2);
		List<Report> reports0 = ReportProcessor.parseFile(file0);
		List<Report> reports3 = ReportProcessor.parseFile(file3);
		return (args) -> {
			// order matters
			ReportProcessor.saveReportsToDb(reports0, repository);
			ReportProcessor.saveReportsToDb(reports1, repository);
			ReportProcessor.saveReportsToDb(reports2, repository);
			ReportProcessor.saveReportsToDb(reports3, repository);
		};
	}
}