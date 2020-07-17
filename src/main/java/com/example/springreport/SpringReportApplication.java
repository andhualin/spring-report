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
	private static final String file0 = "scan-2020-05-07-15_29_12.csv";

	public static void main(String[] args) {
		SpringApplication.run(SpringReportApplication.class, args);
	}

	@Bean
	public CommandLineRunner initDatabase(ReportRepository repository) {
		List<Report> reports = ReportParser.parseFile(file1);
		List<Report> reports2 = ReportParser.parseFile(file2);

		return (args) -> {
			saveReportsToDb(reports, repository);
			saveReportsToDb(reports2, repository);
		};
	}

	// Send report scan to database
	public static void saveReportsToDb(List<Report> reports, ReportRepository repository) {
		for (Report r : reports) {
			List<Report> reportsMatch = repository.findReportFromUniqueIdNative(r.getKey());
			if (reportsMatch.isEmpty()) {
				repository.save(r);
			} else {
				Report reportMatch = reportsMatch.get(0);
				// only do checks within the same file
				if (reportMatch.getDateFirstSeen().equals(r.getDateFirstSeen())) {
					dedupe(r, reportMatch, repository);
				}
				// if the entry is from a different file, don't do anything
			}
		}
		// testing - hardcoded
//		List<Report> earlyDate = repository.findReportFromDateNative("2020-06-24");
//		System.out.println("06-24 vulnerabilities: " + earlyDate.size());
//		List<Report> lateDate = repository.findReportFromDateNative("2020-06-25");
//		System.out.println("06-25 vulnerabilities: " + lateDate.size());
	}

	public static void dedupe(Report r, Report reportMatch, ReportRepository repository) {
		// if unique id is the same but application is different, throw away.
		if (r.getApplication().equals(reportMatch.getApplication())) {
			// if the title column is different, update the unique id and insert
			if (!r.getTitle().equals(reportMatch.getTitle())) {
				r.setKey(r.getKey() + ":" + r.getTitle().hashCode());
				// check db again based on newly generated key
				List<Report> reportsMatch = repository.findReportFromUniqueIdNative(r.getKey());
				if (reportsMatch.isEmpty()) {
					repository.save(r);
				}
				System.out.println("Key already exists in file with different title. " +
						"Inserting new row with key: " + r.getKey());
			}
		}
	}
}
