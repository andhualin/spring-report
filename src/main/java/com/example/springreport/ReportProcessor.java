package com.example.springreport;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReportProcessor {

    public static List<Report> parseFile(String file) {
        List<Report> reports = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                // Splitting line by commas, can change later
                String[] fields = line.split(",");

                if (fields.length == 9) {
                    if (isDouble(fields[5]) && isDouble(fields[6]) && isBoolean(fields[8])) {
                        // remove whitespace in boolean field
                        String seen = fields[8].replaceAll("\\s","");

                        // search for date from file name
                        String dateFirstSeen = parseDate(file);

                        // default status
                        String status = "default";

                        // create new report and insert to db
                        Report report = new Report(fields[0], fields[1], fields[2], fields[3], fields[4],
                                fields[5], fields[6], fields[7], seen, dateFirstSeen, status);
                        reports.add(report);

                    } else {
                        System.err.print("LINE " + lineNumber + " (" + file +
                                ") has invalid data formatting. Will not be added to the database.\n");
                    }
                } else {
                    System.err.print("LINE " + lineNumber + " (" + file +
                            ") has invalid number of columns. Will not be added to the database.\n");
                }
                lineNumber++;
            }
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return reports;
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

    public static String parseDate(String file) {
        Pattern pattern = Pattern.compile("\\d\\d\\d\\d\\-\\d\\d\\-\\d\\d");
        Matcher matcher = pattern.matcher(file);
        if (matcher.find())
        {
            // set the date
            return matcher.group();
        }
        else
        {
            System.out.println("Could not find date, setting to default date");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return dtf.format(LocalDateTime.now());
        }
    }

    private static boolean isDouble(String string) {
        if (string.equals(" " ) || string.equals("")) return true;
        try {
            Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isBoolean(String string) {
        try {
            Boolean.parseBoolean(string);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}