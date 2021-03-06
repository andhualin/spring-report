package com.example.springreport.ui;

import com.example.springreport.Report;
import com.example.springreport.ReportRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route(value="main") // localhost:8080/main
@CssImport("./style-sheet.css")
@CssImport(value="./grid.css", themeFor = "vaadin-grid")
@PageTitle("Reports UI")
@Theme(value = Lumo.class)
public class MainView extends VerticalLayout {

    private ReportRepository repository;
    private Report selectedReport;

    public MainView(ReportRepository repository) {
        this.repository = repository;

        // Title
        Label title = new Label("Welcome to the Report Scan UI");
        title.getElement().getStyle().set("fontWeight","bold");

        // Grid that displays new entries found from latest report
        Grid<Report> gridNew = new Grid<>(Report.class);
        createGridWithLatestReports(gridNew);

        // Grid that displays all entries in db
        Grid<Report> gridAll = new Grid<>(Report.class);
        createGridWithAllReports(gridAll);

        // create search filters & add to gridAll
        List<Report> personList = repository.findAll();
        ListDataProvider<Report> dataProvider = new ListDataProvider<>(
                personList);
        gridAll.setDataProvider(dataProvider);
        HeaderRow filterRow = gridAll.appendHeaderRow();
        TextField applicationFilter = new TextField();
        TextField componentFilter = new TextField();
        TextField titleFilter = new TextField();
        TextField severityFilter = new TextField();
        TextField priorityFilter = new TextField();
        TextField statusFilter = new TextField();
        createColumnFilter(applicationFilter, dataProvider, filterRow, "application", gridAll);
        createColumnFilter(componentFilter, dataProvider, filterRow, "component", gridAll);
        createColumnFilter(titleFilter, dataProvider, filterRow, "title", gridAll);
        createColumnFilter(severityFilter, dataProvider, filterRow, "severity", gridAll);
        createColumnFilter(priorityFilter, dataProvider, filterRow, "priority", gridAll);
        createColumnFilter(statusFilter, dataProvider, filterRow, "status", gridAll);

        // checkbox to hide issues marked "Resolved"
//        Checkbox checkbox = new Checkbox();
//        checkbox.setLabel("Hide resolved issues");
//        checkbox.setValue(false);
//        checkbox.addValueChangeListener(event -> {
//            if (event.getValue() != null) {
//                if (checkbox.getValue().equals(true)) {
//                    dataProvider.addFilter(
////                            report -> StringUtils.containsNone(report.getStatus(), "Resolved")
//                            report -> StringUtils.containsIgnoreCase(report.getStatus(), "resolved")
//                    );
//                } else {
//                    dataProvider.setFilter(
//                            report -> StringUtils.containsIgnoreCase(report.getStatus(), "")
//                    );
//                }
//            }
//        });

        // create pop-up for selected row
        Button openDialogButton = new Button("View Selected Row");
        Dialog dialog = new Dialog();
        dialog.setWidth("1000px");
        dialog.setHeight("850px");
        Label reportId = new Label();
        Label reportDate = new Label();
        Label reportTitle = new Label();
        Label reportApp = new Label();
        Label reportComponent = new Label();
        Label reportFull = new Label();
        TextField priorityField = new TextField();
        TextField severityField = new TextField();
        TextField statusField = new TextField();
        Select<String> selectStatus = new Select<>();
        selectStatus.setLabel("Default Values:");
        selectStatus.setItems("New", "Won't Fix", "Won't Fix: False Positive", "Will Fix", "Ticket Created", "Resolved");
        selectStatus.addValueChangeListener(event -> statusField.setValue(event.getValue()));
        HorizontalLayout statusLayout = new HorizontalLayout(statusField, selectStatus);
        Button cancel = new Button("Cancel", event -> {
            dialog.close();
        });
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Button buttonSubmit = new Button("Update", event -> {
            if (selectedReport != null) {
                selectedReport.setSeverity(severityField.getValue());
                selectedReport.setPriority(priorityField.getValue());
                selectedReport.setStatus(statusField.getValue());
                repository.save(selectedReport);
                gridAll.getDataProvider().refreshAll();
            }
            selectStatus.setPlaceholder("");
            dialog.close();
        });
        buttonSubmit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout buttonOptions = new HorizontalLayout(buttonSubmit, cancel);
        VerticalLayout dialogLayout = new VerticalLayout(reportId, reportDate, reportApp, reportComponent, reportTitle, reportFull,
                severityField, priorityField, statusLayout, buttonOptions);
        dialog.add(dialogLayout);
        openDialogButton.addClickListener(event -> dialog.open());

        gridAll.addSelectionListener(selectionEvent -> {
            selectionEvent.getFirstSelectedItem().ifPresent(report -> {
                selectedReport = report;
                reportId.setText("Report Id: " + report.getId());
                reportDate.setText("Date First Seen: " + report.getDateFirstSeen());
                reportApp.setText("Application: " + report.getApplication());
                reportComponent.setText("Component: " + report.getComponent());
                reportTitle.setText("Title: " + report.getTitle());
                reportTitle.setText(("Full Text: " + report.getFullText()));

                severityField.setValue(report.getSeverity());
                severityField.setLabel("Severity");

                priorityField.setValue(report.getPriority());
                priorityField.setLabel("Priority");

                statusField.setValue(report.getStatus());
                statusField.setLabel("Status");
            });
        });

        // configure tabs
        Tab tab1 = new Tab("View All");
        Div page1 = new Div();
        page1.setWidth("100%");
        page1.setText(repository.count() + " entries found.");
        page1.add(gridAll);
        VerticalLayout vl = new VerticalLayout();
        vl.add(openDialogButton);
        page1.add(vl);

        Tab tab2 = new Tab("View Latest");
        Div page2 = new Div();
        page2.setWidthFull();
        page2.setVisible(false);
        page2.add(new Label("New vulnerabilities found in most recent scan from " + getMostRecentDate(repository)));
        page2.add(gridNew);

        Tab tab3 = new Tab("Cats");
        Div page3 = new Div();
        page3.setWidthFull();
        page3.setVisible(false);
        page3.setText("/ᐠ｡‸｡ᐟ\\");

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

    private void createColumnFilter(TextField textField, ListDataProvider<Report> dataProvider,
                                    HeaderRow filterRow, String columnKey, Grid<Report> gridAll) {
        if (columnKey.equals("application")) {
            textField.addValueChangeListener(event -> dataProvider.addFilter(
                    report -> StringUtils.containsIgnoreCase(report.getApplication(),
                            textField.getValue())));
            textField.setPlaceholder("Filter");
        } else if (columnKey.equals("component")) {
            textField.addValueChangeListener(event -> dataProvider.addFilter(
                    report -> StringUtils.containsIgnoreCase(report.getComponent(),
                            textField.getValue())));
        } else if (columnKey.equals("title")) {
            textField.addValueChangeListener(event -> dataProvider.addFilter(
                    report -> StringUtils.containsIgnoreCase(report.getTitle(),
                            textField.getValue())));
        } else if (columnKey.equals("priority")) {
            textField.addValueChangeListener(event -> dataProvider.addFilter(
                    report -> StringUtils.containsIgnoreCase(report.getPriority(),
                            textField.getValue())));
        } else if (columnKey.equals("severity")) {
            textField.addValueChangeListener(event -> dataProvider.addFilter(
                    report -> StringUtils.containsIgnoreCase(report.getSeverity(),
                            textField.getValue())));
        } else if (columnKey.equals("status")) {
            textField.addValueChangeListener(event -> dataProvider.addFilter(
                    report -> StringUtils.containsIgnoreCase(report.getStatus(),
                            textField.getValue())));
        }
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(gridAll.getColumnByKey(columnKey)).setComponent(textField);
        textField.setSizeFull();
    }

    private void createGridWithAllReports(Grid<Report> gridAll) {
        gridAll.setClassNameGenerator(item -> {return "grid";});
        gridAll.setVerticalScrollingEnabled(true);
        gridAll.setWidthFull();
        gridAll.setItems(repository.findAll());
        gridAll.removeColumnByKey("key");
        gridAll.removeColumnByKey("id");
        gridAll.removeColumnByKey("scanTool");
        gridAll.removeColumnByKey("fullText");
        gridAll.removeColumnByKey("dateFirstSeen");
        gridAll.setWidth("1600px");
        gridAll.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        gridAll.setHeight("650px");
        gridAll.setSelectionMode(Grid.SelectionMode.SINGLE);
        gridAll.getColumnByKey("application").setWidth("200px")
                .setClassNameGenerator(item -> {return "grid-column";});
        gridAll.getColumnByKey("component").setWidth("500px")
                .setSortable(false)
                .setClassNameGenerator(item -> {return "grid-column";});
        gridAll.getColumnByKey("title").setWidth("500px")
                .setSortable(false)
                .setClassNameGenerator(item -> {return "grid-column";});
        gridAll.getColumnByKey("severity").setWidth("70px")
                .setSortable(false)
                .setClassNameGenerator(item -> {return "grid-column";});
        gridAll.getColumnByKey("priority").setWidth("70px")
                .setSortable(false)
                .setClassNameGenerator(item -> {return "grid-column";});
        gridAll.getColumnByKey("status").setWidth("140px")
                .setSortable(false)
                .setClassNameGenerator(item -> {return "grid-column";});
        gridAll.setColumnOrder(gridAll.getColumnByKey("application"),
                gridAll.getColumnByKey("component"),
                gridAll.getColumnByKey("title"),
                gridAll.getColumnByKey("severity"),
                gridAll.getColumnByKey("priority"),
                gridAll.getColumnByKey("status"));
    }

    private void  createGridWithLatestReports(Grid<Report> gridNew) {
        gridNew.setVerticalScrollingEnabled(true);
        gridNew.setWidth("100%");
        setGridToLatestEntriesFound(gridNew);
        gridNew.removeColumnByKey("severity");
        gridNew.removeColumnByKey("priority");
        gridNew.removeColumnByKey("key");
        gridNew.removeColumnByKey("id");
        gridNew.removeColumnByKey("scanTool");
        gridNew.removeColumnByKey("fullText");
        gridNew.removeColumnByKey("dateFirstSeen");
        gridNew.removeColumnByKey("status");
        gridNew.setWidth("1300px");
        gridNew.getColumnByKey("application").setWidth("200px")
                .setSortable(false)
                .setClassNameGenerator(item -> {return "grid-column";});
        gridNew.getColumnByKey("component").setWidth("500px")
                .setSortable(false)
                .setClassNameGenerator(item -> {return "grid-column";});

        gridNew.getColumnByKey("title").setWidth("500px")
                .setSortable(false)
                .setClassNameGenerator(item -> {return "grid-column";});
        gridNew.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT);
        gridNew.setSelectionMode(Grid.SelectionMode.NONE);
    }

    private void setGridToLatestEntriesFound(Grid<Report> grid) {
        String date = getMostRecentDate(repository);
        grid.setItems(repository.findReportFromDateNative(date));
    }

    public static String getMostRecentDate(ReportRepository repo) {
        List<String> dateStrings = repo.findDistinctDatesNative();
        List<Date> dates = new ArrayList<>();
        // convert to Date Object
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for (String dateString : dateStrings) {
            try {
                Date date = formatter.parse(dateString);
                dates.add(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        // compare dates and find the most recent
        Date mostRecentDate = dates.get(0);
        for (Date date : dates) {
            if (date.compareTo(mostRecentDate) > 0) mostRecentDate = date;
        }
        return formatter.format(mostRecentDate);
    }
}
