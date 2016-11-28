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
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 *
 * @author amanda
 */
public class StatisticsView extends VerticalLayout implements View {
    StatisticsChart charts = new StatisticsChart();
    PatientDataManager patientDataManager = PatientDataManager.getInstance();
    
    public StatisticsView() {
        
        patientDataManager.calcGenderStatistic();
        setSizeFull();
        setMargin(true);     

        addComponent(buildHeader());
        Component content = buildContent();
        addComponent(content);
        setExpandRatio(content, 1);
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
        
        HorizontalLayout h = new HorizontalLayout();    

        h.addComponent(charts.genderChart());
       // h.addComponent(charts.getAgeDistChart());
        
        statisticsPanels.addComponent(h);
        return statisticsPanels;
    }
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
    
}
