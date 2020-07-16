package com.example.springreport.ui;

import com.example.springreport.Report;
import com.example.springreport.ReportParser;
import com.example.springreport.ReportRepository;
import com.example.springreport.SpringReportApplication;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.io.InputStream;
import java.util.List;

@Route(value="main") // localhost:8080/main
@Theme(value = Lumo.class)
public class MainView extends VerticalLayout {

    private ReportRepository repository;
    Grid<Report> grid;

    public MainView(ReportRepository repository) {
        this.repository = repository;

//        MenuBar menuBar = new MenuBar();
//        Grid gridTest = new Grid();
//        Div message = new Div(new Grid(Report.class), gridTest);
//
//        MenuItem allReports = menuBar.addItem("All Reports");
//        MenuItem newVuln = menuBar.addItem("New Vulnerabilities");
////        MenuItem cat = menuBar.addItem("Cats");
//        menuBar.addItem("Cats", e ->
//                gridTest.getColumnByKey("id").setWidth("300px"))
//        ;
//        add(menuBar);

        Label title = new Label("Welcome to the Report Scan UI");
        title.getElement().getStyle().set("fontWeight","bold");
        this.add(title);

        // visualize
        this.add(new Label("new vulnerabilities found from comparing " +
                SpringReportApplication.file2 + " against " + SpringReportApplication.file1 + ": "));
        // add them
        this.grid = new Grid<>(Report.class);
        grid.setVerticalScrollingEnabled(true);
        this.add(grid);
        listNewFound();

        this.add(new Label("Total entries from both reports: " + repository.count()));
    }

    private void listNewFound() {
        // this is hardcoded
        String date = ReportParser.parseDate(SpringReportApplication.file2);
        grid.setItems(repository.findReportFromDateNative(date));
    }
}

//        MemoryBuffer memoryBuffer = new MemoryBuffer();
//        Upload upload = new Upload(memoryBuffer);
//        upload.addFinishedListener(e -> {
//            InputStream inputStream = memoryBuffer.getInputStream();
//            // read the contents of the buffered memory
//            // from inputStream
//        });
//        upload.setDropLabel(new Label("Upload file 1"));
//        this.add(upload);
