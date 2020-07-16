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

public class ReportParser {

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

                        // create new report and insert to db
                        Report report = new Report(fields[0], fields[1], fields[2], fields[3], fields[4],
                                fields[5], fields[6], fields[7], seen, dateFirstSeen);
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
