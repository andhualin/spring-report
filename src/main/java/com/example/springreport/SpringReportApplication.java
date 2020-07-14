package com.example.springreport;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringReportApplication {
	public static String file1 = "scan-2020-06-24-16_43_53.csv";
	public static String file2 = "sample.csv";
	public static void main(String[] args) {
		SpringApplication.run(SpringReportApplication.class, args);
	}

	@Bean
	public CommandLineRunner initDatabase(ReportRepository repository) {
		List<Report> reports = ReportParser.parseReport(file1);
		List<Report> reportstwo = ReportParser.parseReport(file2);
		reports.addAll(reportstwo);

		return (args) -> {
			// save Report csv to the database
			for (Report r : reports) {
				// check the database to see if the report already exists
				Report reportInDb = repository.findReportFromUniqueIdNative(r.getName());
				if (reportInDb == null) {
					repository.save(r);
				} else {
					// if the application name is different, throw it away.
					if (r.getApplication().equals(reportInDb.getApplication())) {
						// if the title column is different, then update the name and insert
						if (!r.getTitle().equals(reportInDb.getTitle())) {
							r.setName(r.getName() + ":" + r.getTitle().hashCode());
							repository.save(r);

							System.out.println("Existing vulnerability with different title, " +
									"inserting a new row with key: " + r.getName());
						}
					} else {
						System.out.println("Found an existing vulnerability in the db: " + r.getName());
						// update the fields
						reportInDb.setScanTool(r.getScanTool());
						reportInDb.setApplication(r.getApplication());
						reportInDb.setComponent(r.getComponent());
						reportInDb.setTitle(r.getTitle());
						reportInDb.setSeverity(r.getSeverity());
						reportInDb.setPriority(r.getPriority());
						reportInDb.setFullText(r.getFullText());
						reportInDb.setSeen("true");
						repository.save(reportInDb);
					}
				}
			}
		};
	}
}
