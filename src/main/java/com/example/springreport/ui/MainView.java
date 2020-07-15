package com.example.springreport.ui;

import com.example.springreport.Report;
import com.example.springreport.ReportRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route(value="main") // localhost:8080/main
public class MainView extends VerticalLayout {
    private ReportRepository repository;
    Grid<Report> grid;
    TextField filter;
    private Button addNewBtn;

    public MainView() {
        this.grid = new Grid<>(Report.class);
        this.filter = new TextField();
        this.addNewBtn = new Button("whatup", VaadinIcon.PLUS.create());

        HorizontalLayout actions = new HorizontalLayout(filter, addNewBtn);
        add(actions, grid);

//        grid.setHeight("200px");
//        grid.setColumns("id", "application", "component", "full_text", "key",
//                "priority", "scan_tool", "seen", "severity", "title");
//        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

        filter.setPlaceholder("Welcome to The Reports UI");
    }
}