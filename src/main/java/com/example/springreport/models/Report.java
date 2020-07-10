package com.example.springreport.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String scanTool;
    private String application;
    private String component;
    private String title;
    private double severity;
    private double priority;
    private String fullText;
    private boolean seen;

    protected Report() {}

    public Report(String name, String scantool, String application, String component, String title, double severity,
                  double priority, String fullText, boolean seen) {
        this.name = name;
        this.scanTool = scantool;
        this.application = application;
        this.component = component;
        this.title = title;
        this.severity = severity;
        this.priority = priority;
        this.fullText = fullText;
        this.seen = seen;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScanTool() {
        return scanTool;
    }

    public void setScanTool(String scanTool) {
        this.scanTool = scanTool;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getSeverity() {
        return severity;
    }

    public void setSeverity(double severity) {
        this.severity = severity;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}
