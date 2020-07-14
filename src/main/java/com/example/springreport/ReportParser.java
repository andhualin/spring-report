package com.example.springreport;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ReportParser {
    public static List<Report> parseReport(String file) {
        List<Report> reports = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            int invalidEntries = 0;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                // Splitting line by commas, can change later
                String[] fields = line.split(",");

                if (fields.length == 9) {
                    if (isDouble(fields[5]) && isDouble(fields[6]) && isBoolean(fields[8])) {
                        Report report = new Report(fields[0], fields[1], fields[2], fields[3], fields[4],
                                fields[5], fields[6], fields[7], fields[8]);
                        reports.add(report);
                    } else {
                        invalidEntries++;
                        System.err.print("LINE " + lineNumber +
                                " has invalid data formatting. Will not be added to the database.\n");
                    }
                } else {
                    invalidEntries++;
                    System.err.print("LINE " + lineNumber +
                            " has invalid number of columns. Will not be added to the database.\n");
                }
                lineNumber++;
            }
            System.err.println("TOTAL INVALID ENTRIES: " + invalidEntries);
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

    // todo: delete no longer used
    public static List<Report> parseCSVData(String file) {
        List<String[]> reportFields = new ArrayList<>();
        List<Report> reports = new ArrayList<>();
        try {
            FileReader filereader = new FileReader(file);
            CSVReader csvReader = new CSVReader(filereader);
            String[] nextLine;

            // read line by line
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine.length == 9) { // some entries are shorter or longer
                    reportFields.add(nextLine);
                    Report report = new Report(reportFields.get(0)[0],reportFields.get(0)[1],reportFields.get(0)[2],
                            reportFields.get(0)[3],reportFields.get(0)[4],reportFields.get(0)[5],reportFields.get(0)[6],
                            reportFields.get(0)[7],reportFields.get(0)[8]);
                    reports.add(report);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return reports;
    }
}
