/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humancare.monitor.realtime;

import com.humancare.monitor.component.SparklineChart;
import com.humancare.monitor.data.dummy.DummyDataGenerator;
import com.humancare.monitor.entities.RegisteredPatients;
import com.humancare.monitor.snmp.PatientDataManager;
import static com.humancare.monitor.view.dashboard.DashboardView.TITLE_ID;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Responsive;
import com.vaadin.server.data.ListDataSource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author amanda
 */
public class RealTimeView extends VerticalLayout implements View {

    private final PatientDataManager patientDataManager = PatientDataManager.getInstance();
    private ComboBox<RegisteredPatients> patientSelect;
    private final RealTimeCharts realTimeCharts;
    private CssLayout panels;
    
    public RealTimeView(){
        this.realTimeCharts = new RealTimeCharts();
        
        setSizeFull();
        setMargin(true);
        addStyleName("real-time");
        
        addComponent(buildHeader());  
        Component content = buildContent();
        addComponents(content);
        setExpandRatio(content, 1);
        
    }   
    
    private Component buildHeader(){
        
        HorizontalLayout header = new HorizontalLayout();
        header.addStyleName("viewheader");
        header.setSpacing(true);
        Responsive.makeResponsive(header);

        Label titleLabel = new Label("Real Time Monitoring");
        titleLabel.setSizeUndefined();
        titleLabel.addStyleName(ValoTheme.LABEL_H1);
        titleLabel.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        header.addComponent(titleLabel);

        return header;
    
    }
        
    private Component buildContent() {
        
        panels = new CssLayout();
        panels.addStyleName("dashboard-panels");
        Responsive.makeResponsive(panels);
       
        HorizontalLayout h = new HorizontalLayout();        
        h.addComponents(realTimeCharts.getTemperaturePanel());
        h.addComponent(realTimeCharts.getSPO2Panel());        
        h.addComponent(realTimeCharts.getPressurePanel());
        h.addComponent(realTimeCharts.getGlucosePanel());
        
        HorizontalLayout h2 = new HorizontalLayout();
        h2.addComponent(realTimeCharts.getHeartRateChart());
        
        panels.addComponent(h);
        panels.addComponent(h2);
        

        return panels;
    }
    
   
    
    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        
    }
   
    
}
