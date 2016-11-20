/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humancare.monitor.realtime;

import com.humancare.monitor.component.SparklineChart;
import com.humancare.monitor.data.dummy.DummyDataGenerator;
import com.humancare.monitor.entities.PatientData;
import com.humancare.monitor.entities.RegisteredPatients;
import com.humancare.monitor.event.DashboardEventBus;
import com.humancare.monitor.snmp.PatientDataManager;
import static com.humancare.monitor.view.dashboard.DashboardView.TITLE_ID;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.AxisTitle;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataLabels;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.ListSeries;
import com.vaadin.addon.charts.model.PlotOptionsLine;
import com.vaadin.addon.charts.model.PlotOptionsSpline;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.data.ListDataSource;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.util.List;

/**
 *
 * @author amanda
 */
public class RealTimeView extends VerticalLayout implements View {

    private PatientDataManager patientDataManager = PatientDataManager.getInstance();
    private PatientData patientData;
    private ComboBox<RegisteredPatients> patientSelect;
    private RealTimeCharts realTimeCharts = new RealTimeCharts();
    private CssLayout panels;
    
    public RealTimeView(){
        
        setSizeFull();
        setMargin(true);
        addStyleName("real-time");
        
        addComponent(buildHeader());  
        //addComponent(buildSparklines());  
        
       // Component content = buildContent();
        addComponent(realTimeCharts.getHeartRateChart());
       // setExpandRatio(content, 1);
        
        initPatientSelect();
        
    }   
    
    private Component buildContent() {
        
        panels = new CssLayout();
        panels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(panels);

        panels.addComponent(realTimeCharts.getHeartRateChart());

        return panels;
    }
          
    private Component buildHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);

        // add title and combo box to header
        Label titleLabel;
        titleLabel = new Label("Real Time Monitor");
        titleLabel.setId(TITLE_ID);
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponents(titleLabel, buildPatientSelector());

        return header;
    }
    
    // Get all registered patients and put in combo box component
    private void initPatientSelect(){
        List<RegisteredPatients> regPatients = patientDataManager.getPatientList();
        if(regPatients != null){
            patientSelect.setDataSource(new ListDataSource<>(regPatients));
        }
        
    }
    
    private Component buildPatientSelector() {
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.addStyleName("patientSelector");
        toolbar.setSpacing(true);

        patientSelect = new ComboBox<>();
        patientSelect.setItemCaptionGenerator(RegisteredPatients::getName);
        patientSelect.addShortcutListener(
                new ShortcutListener("OK", ShortcutAction.KeyCode.ENTER, null) {
                    @Override
                    public void handleAction(final Object sender,
                            final Object target) {
                        updateCharts(patientSelect.getValue());
                    }
                });

        final Button ok = new Button("OK");
        ok.setEnabled(false);
        ok.addStyleName(ValoTheme.BUTTON_PRIMARY);

        CssLayout group = new CssLayout(patientSelect, ok);
        group.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        toolbar.addComponent(group);

        patientSelect.addSelectionListener(
                event -> ok.setEnabled(event.getValue() != null));


        return toolbar;
    }
    
    //todo: atualizar graficos apos selecionar um paciente
    private void updateCharts(RegisteredPatients regPatients){
    }
    
    private Component buildSparklines() {
        CssLayout sparks = new CssLayout();
        sparks.addStyleName("sparks");
        sparks.setWidth("100%");
        Responsive.makeResponsive(sparks);
        
        //TODO: arrumar chamada       
        //patientData = patientDataManager.getAllPatientInfo();

        SparklineChart s = new SparklineChart("Heart Rate", "bpm", "",
                DummyDataGenerator.chartColors[0], 2, 10, 20);
        sparks.addComponent(s);

        s = new SparklineChart("Temperature", "°C", "",
                DummyDataGenerator.chartColors[2], 2, 10, 36);
        sparks.addComponent(s);

        s = new SparklineChart("Blood Pressure", "mmHg", "",
                DummyDataGenerator.chartColors[3], 10, 30, 120);
        sparks.addComponent(s);

        s = new SparklineChart("Blood Glucose", "mg/dL", "",
                DummyDataGenerator.chartColors[4], 50, 34, 100);
        sparks.addComponent(s);
        
        s = new SparklineChart("SPO2", "%", "",
                DummyDataGenerator.chartColors[5], 50, 34, 100);
        sparks.addComponent(s);

        return sparks;
    }
   
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        
    }
    
}
