package com.example.springreport.ui;

import com.example.springreport.Report;
import com.example.springreport.ReportParser;
import com.example.springreport.ReportRepository;
import com.example.springreport.SpringReportApplication;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CssImport("style-sheet.css")
@CssImport(value="grid.css", themeFor = "vaadin-grid")
@Route(value="main") // localhost:8080/main
@PageTitle("Reports UI")
@Theme(value = Lumo.class)
public class MainView extends VerticalLayout {

    private ReportRepository repository;
    Grid<Report> gridNew;
    Grid<Report> gridAll;

    public MainView(ReportRepository repository) {
        this.repository = repository;

        // Title
        Label title = new Label("Welcome to the Report Scan UI");
        title.getElement().getStyle().set("fontWeight","bold");

        // Grid that displays new entries found
        gridNew = new Grid<>(Report.class);
        gridNew.setVerticalScrollingEnabled(true);
        gridNew.setWidth("100%");
        listNewFound(gridNew);
        gridNew.removeColumnByKey("severity");
        gridNew.removeColumnByKey("priority");
        gridNew.removeColumnByKey("key");
        gridNew.removeColumnByKey("id");
        gridNew.removeColumnByKey("scanTool");
        gridNew.removeColumnByKey("fullText");
        gridNew.removeColumnByKey("dateFirstSeen");
        gridNew.setWidth("1600px");
        gridNew.getColumnByKey("application").setWidth("300px");
        gridNew.getColumnByKey("component").setWidth("550px");
        gridNew.getColumnByKey("title").setWidth("650px");
        gridNew.getColumnByKey("application").setClassNameGenerator(item -> {return "grid-column";});
        gridNew.getColumnByKey("component").setClassNameGenerator(item -> {return "grid-column";});
        gridNew.getColumnByKey("title").setClassNameGenerator(item -> {return "grid-column";});
        gridNew.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);

        // Grid that displays all entries in db
        gridAll = new Grid<>(Report.class);
        gridAll.setClassNameGenerator(item -> {return "grid";});
        gridAll.setVerticalScrollingEnabled(true);
        gridAll.setWidthFull();
        listAll(gridAll);
        gridAll.removeColumnByKey("severity");
        gridAll.removeColumnByKey("priority");
        gridAll.removeColumnByKey("key");
        gridAll.removeColumnByKey("id");
        gridAll.removeColumnByKey("scanTool");
        gridAll.removeColumnByKey("fullText");
        gridAll.removeColumnByKey("dateFirstSeen");
        gridAll.setWidth("1600px");
        gridAll.getColumnByKey("application").setWidth("300px");
        gridAll.getColumnByKey("component").setWidth("550px");
        gridAll.getColumnByKey("title").setWidth("650px");
        gridAll.getColumnByKey("application").setClassNameGenerator(item -> {return "grid-column";});
        gridAll.getColumnByKey("component").setClassNameGenerator(item -> {return "grid-column";});
        gridAll.getColumnByKey("title").setClassNameGenerator(item -> {return "grid-column";});
        gridAll.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        gridAll.setHeight("700px");

        Tab tab1 = new Tab("View All");
        Div page1 = new Div();
        page1.setWidth("100%");
        page1.setText("All entries from " +
                SpringReportApplication.file1 + " and " + SpringReportApplication.file2 + ": \n");
        page1.add(gridAll);
        page1.add(new Label("Total entries from both reports: " + repository.count()));

        Tab tab2 = new Tab("View New");
        Div page2 = new Div();
        page2.setWidthFull();
        page2.setVisible(false);
        page2.add(new Label("New vulnerabilities found from comparing " +
                SpringReportApplication.file2 + " against " + SpringReportApplication.file1 + ": \n"));
        page2.add(gridNew);

        Tab tab3 = new Tab("Update Entries");
        Div page3 = new Div();
        page3.setWidthFull();
        page3.setText("TODO: Search for entries and update them \n");
        page3.setVisible(false);

        Map<Tab, Component> tabsToPages = new HashMap<>();
        tabsToPages.put(tab1, page1);
        tabsToPages.put(tab2, page2);
        tabsToPages.put(tab3, page3);
        Tabs tabs = new Tabs(tab1, tab2, tab3);
        Div pages = new Div(page1, page2, page3);
        Set<Component> pagesShown = Stream.of(page1)
                .collect(Collectors.toSet());

        tabs.addSelectedChangeListener(event -> {
            pagesShown.forEach(page -> page.setVisible(false));
            pagesShown.clear();
            Component selectedPage = tabsToPages.get(tabs.getSelectedTab());
            selectedPage.setVisible(true);
            pagesShown.add(selectedPage);
        });

        add(title);
        add(tabs,pages);
    }

    private void listNewFound(Grid grid) {
        String date = ReportParser.parseDate(SpringReportApplication.file2);
        grid.setItems(repository.findReportFromDateNative(date));
    }

    private void listAll(Grid grid) {
        grid.setItems(repository.findAll());
        List<Report> list = repository.findAll();
    }
}
