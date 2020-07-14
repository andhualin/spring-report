package com.example.springreport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition="TEXT")
    private String name;

    @Column(columnDefinition="TEXT")
    private String scanTool;

    @Column(columnDefinition="TEXT")
    private String application;

    @Column(columnDefinition="TEXT")
    private String component;

    @Column(columnDefinition="TEXT")
    private String title;

    @Column(columnDefinition="TEXT")
    private String severity;

    @Column(columnDefinition="TEXT")
    private String priority;

    @Column(columnDefinition="TEXT")
    private String fullText;

    @Column(columnDefinition="TEXT")
    private String seen;

//    private String dateFirstSeen;

    public Report() {}

    public Report(String name,
                  String scantool,
                  String application,
                  String component,
                  String title,
                  String severity,
                  String priority,
                  String fullText,
                  String seen) {
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

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String isSeen() {
        return seen;
    }

    public void setSeen(String seen) {
        this.seen = seen;
    }

//    public String getDateFirstSeen() { return dateFirstSeen; }
//
//    public void setDateFirstSeen(String dateFirstSeen) { this.dateFirstSeen = dateFirstSeen; }
}