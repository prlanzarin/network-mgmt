/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humancare.monitor.view.sensors;

import com.humancare.monitor.entities.Sensor;
import com.humancare.monitor.snmp.Constants;
import static com.humancare.monitor.snmp.Constants.OID_S;
import com.humancare.monitor.snmp.Manager;
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
import com.vaadin.v7.ui.Grid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 *
 * @author amanda
 */
public class SensorsView extends VerticalLayout implements View {
    
    private PatientDataManager patientDataManager = PatientDataManager.getInstance();
    private Manager manager = Manager.getInstance();
    private CssLayout panels;
    
    public SensorsView() {
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

        Label titleLabel = new Label("Sensors Information");
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

        panels.addComponent(buildTable());

        return panels;
    }    
    
    private Component buildTable(){
        VerticalLayout layout = new VerticalLayout();
        Grid grid = new Grid();
        // Define columns
        grid.addColumn("Type", String.class);
        grid.addColumn("Location", String.class);
        grid.addColumn("Batery", String.class);
        grid.addColumn("Batery Alert", String.class);       
        
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");           
        String numberOfsensorsString = patientDataManager.getByOID(OID_S.get("hcSensorNumber"));
        int numberOfSensors = 0;
        if(numberOfsensorsString != null){
            numberOfSensors = Integer.parseInt(numberOfsensorsString);
        }
        System.out.println("Number of sensors: " + numberOfSensors);
        
        // calls getBulk operation to get sensors data
        List<Sensor> sensors = patientDataManager.getSensors(numberOfSensors);
        
        if(sensors != null && !sensors.isEmpty()){
            sensors.stream().forEach((s) -> {
                grid.addRow(s.getType(), s.getLocation(), s.getBatteryPower() + "%", s.getBatteryPower() < 14 ? "Alert!!" : "-");
                
                if(s.getBatteryPower() < 14){
                    PatientDataManager.addNotification(s.getType(), 
                                        "Sensor battery Alert!", sdf.format(new Date(System.currentTimeMillis())), "Low battery: " + s.getBatteryPower() +"%");       
                    //manager.set(Constants.sensorBatteryAlert.append(s.getIndex), 1);
                }
            });
        }      
        
        layout.addComponent(grid);
        return layout;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
    }
    
}
