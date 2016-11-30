/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humancare.monitor.view.statistics;

import com.humancare.monitor.snmp.PatientDataManager;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author amanda
 */
public class StatisticsView extends Panel implements View {
    StatisticsChart charts = new StatisticsChart();
    PatientDataManager patientDataManager = PatientDataManager.getInstance();
    
    public StatisticsView() {
        patientDataManager.calcGenderStatistic();
        
        VerticalLayout layout = new VerticalLayout();      
        setSizeFull();
        setContent(layout);
        layout.setMargin(true);     
        layout.addStyleName("dashboard-view");        
        Responsive.makeResponsive(layout);

        layout.addComponent(buildHeader());
        Component content = buildContent();
        layout.addComponent(content);
        layout.setExpandRatio(content, 1);
    }
    
    private Component buildHeader(){
        
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);

        Label titleLabel = new Label("Statistics");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        return header;
    
    }
    
    private Component buildContent() {        
        CssLayout statisticsPanels = new CssLayout();
        statisticsPanels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(statisticsPanels);
        
        HorizontalLayout h1 = new HorizontalLayout();    

        h1.addComponent(charts.genderChart());
        h1.addComponent(charts.getAverageHeartRateAlertChart());
        
        HorizontalLayout h2 = new HorizontalLayout();    
        h2.addComponent(charts.getAverageTemperatureAlertChart());
        h2.addComponent(charts.getAveragePressureAlertChart());
        
        HorizontalLayout h3 = new HorizontalLayout();    
        h3.addComponent(charts.getAverageGlucoseAlertChart());
        h3.addComponent(charts.getAverageSpo2AlertChart());
        
        statisticsPanels.addComponent(h1);
        statisticsPanels.addComponent(h2);
        statisticsPanels.addComponent(h3);
        return statisticsPanels;
    }
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
    
}
