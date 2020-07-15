package com.example.springreport;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class SpringReportApplication {
	public static String file1 = "scan-2020-05-07-15_29_12.csv";
	public static String file2 = "scan-2020-06-24-16_43_53.csv";

	public static void main(String[] args) {
		SpringApplication.run(SpringReportApplication.class, args);
	}

	@Bean
	public CommandLineRunner initDatabase(ReportRepository repository) {
		List<Report> reports = ReportParser.parseReport(file1);
		List<Report> reports2 = ReportParser.parseReport(file2);

		return (args) -> {
//			ingestReport(reports, repository);
			saveReportsToDb(reports2, repository);
		};
	}

	// Send report scan to database
	public void saveReportsToDb(List<Report> reports, ReportRepository repository) {
		for (Report r : reports) {
			Report reportInDb = repository.findReportFromUniqueIdNative(r.getKey());
			if (reportInDb == null) {
				repository.save(r);
			} else {
				// if unique id is the same but application is different, throw away.
				if (r.getApplication().equals(reportInDb.getApplication())) {
					// if the title column is different, update the unique id and insert
					if (!r.getTitle().equals(reportInDb.getTitle())) {
						r.setKey(r.getKey() + ":" + r.getTitle().hashCode());
						repository.save(r);

						System.out.println("Existing vulnerability with different title, " +
								"inserting a new row with key: " + r.getKey());
					} else {
						// todo fix logic
						System.out.println("Found an existing vulnerability in the db: " + r.getKey());
						// Update existing vulnerability and put it back
//						reportInDb.setScanTool(r.getScanTool());
//						reportInDb.setApplication(r.getApplication());
//						reportInDb.setComponent(r.getComponent());
//						reportInDb.setTitle(r.getTitle());
//						reportInDb.setSeverity(r.getSeverity());
//						reportInDb.setPriority(r.getPriority());
//						reportInDb.setFullText(r.getFullText());
//						reportInDb.setSeen("true");
//						repository.save(reportInDb);
					}
				}
			}
		}

	}
}
