/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humancare.monitor.realtime;

import static com.humancare.monitor.snmp.Constants.OID_S;
import com.humancare.monitor.snmp.PatientDataManager;
import com.humancare.monitor.snmp.PatientDataValidator;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.model.AxisTitle;
import com.vaadin.addon.charts.model.AxisType;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.DateTimeLabelFormats;
import com.vaadin.addon.charts.model.PlotOptionsSpline;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author amanda
 */
public class RealTimeCharts {
    
    private final int UPDATE_INTERVAL_TIME = 1000;
    PatientDataManager patientDataManager = PatientDataManager.getInstance();
    PatientDataValidator validate = new PatientDataValidator();
    
     // Heart Rate Chart attributes
    private Chart heartRatechart;
    private Configuration configurationHR;
    private DataSeries hrData;    
        
    
    protected Component getTemperaturePanel() {
        CssLayout panel = new CssLayout();
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        VerticalLayout horizontalLayout2 = new VerticalLayout();
        
        Label valuelb = new Label("0");
        valuelb.setId("g_value");
        valuelb.setHeight("60px");
        valuelb.addStyleName(ValoTheme.LABEL_H1);
        valuelb.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        valuelb.addStyleName(ValoTheme.LABEL_COLORED);
        
        Label symbollb = new Label();
        symbollb.setId("g_value");
        symbollb.setCaption("ºC");
        
        Label time = new Label();
        time.setId("time");
        time.setValue("Last Update:");
        
        new Thread(){
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");   
            
            public void run(){
                try {
                    while(true){                    
                    Thread.sleep(1000);   
                    Date resultdate = new Date(System.currentTimeMillis());
                    Double temperature = Integer.parseInt(patientDataManager.getByOID(OID_S.get("temperature"))) / 10.0;
                    //Double temperature = 370/10.0;
                    
                    if(validate.tempAlert(temperature)){
                        if(patientDataManager.getCurrentPatient() != null){
                            patientDataManager.getCurrentPatient().addTempAlertList(resultdate);
                        }
                        /*
                        * notification
                        */
                    }    
                    
                    valuelb.setValue(temperature.toString());
                    time.setValue("Last update: " + sdf.format(resultdate));
                    Thread.sleep(1000); 
                    }
                } catch (InterruptedException ex) {                    
                    System.out.println("Thread stopped");
                    ex.printStackTrace();
                }
            }
        }.start();
        
        horizontalLayout1.addComponents(valuelb, symbollb);
        horizontalLayout2.addComponent(time);
        
        panel.addComponent(horizontalLayout1);
        panel.addComponent(horizontalLayout2);
        
        return panel;
    }
    
    protected Component getSPO2Panel() {
        CssLayout panel = new CssLayout();
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        VerticalLayout horizontalLayout2 = new VerticalLayout();
        
        Label valuelb = new Label("0");
        valuelb.setId("g_value");
        valuelb.setHeight("60px");
        valuelb.addStyleName(ValoTheme.LABEL_H1);
        valuelb.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        valuelb.addStyleName(ValoTheme.LABEL_COLORED);
        
        Label symbollb = new Label();
        symbollb.setId("g_value");
        symbollb.setCaption("% SpO2");
        
        Label time = new Label();
        time.setId("time");
        time.setValue("Last Update:");
                
        new Thread(){
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");   
            
            public void run(){
                try {
                    while(true){                    
                    Thread.sleep(1000);   
                    Date resultdate = new Date(System.currentTimeMillis());
                    Integer spo2 = Integer.parseInt(patientDataManager.getByOID(OID_S.get("spo2")));
                    //Integer spo2 = 98;
                    
                    if(validate.spo2Alert(spo2)){
                        if(patientDataManager.getCurrentPatient() != null){
                            patientDataManager.getCurrentPatient().addSpo2AlertList(resultdate);
                        }
                        /*
                        * notification
                        */
                    }
                    valuelb.setValue(spo2.toString());
                    time.setValue("Last update: " + sdf.format(resultdate));
                    Thread.sleep(1000); 
                    }
                } catch (InterruptedException ex) {                    
                    System.out.println("Thread stopped");
                    ex.printStackTrace();
                }
            }
        }.start();
        
        horizontalLayout1.addComponents(valuelb, symbollb);
        horizontalLayout2.addComponent(time);
        
        panel.addComponent(horizontalLayout1);
        panel.addComponent(horizontalLayout2);
        
        return panel;
    }
    
    protected Component getPressurePanel() {
        CssLayout panel = new CssLayout();
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        VerticalLayout horizontalLayout2 = new VerticalLayout();
        
        Label valuelb = new Label("0");
        valuelb.setId("g_value");
        valuelb.setHeight("60px");
        valuelb.addStyleName(ValoTheme.LABEL_H1);
        valuelb.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        valuelb.addStyleName(ValoTheme.LABEL_COLORED);
        
        Label symbollb = new Label();
        symbollb.setId("g_value");
        symbollb.setCaption("mmHg");
        
        Label time = new Label();
        time.setId("time");
        time.setValue("Last Update:");
                
        new Thread(){
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");   
            
            public void run(){
                try {
                    while(true){                    
                    Thread.sleep(1000);   
                    Date resultdate = new Date(System.currentTimeMillis());
                    String pressure = patientDataManager.getByOID(OID_S.get("bloodPressure"));
                    //String pressure = "12080";
                    int sistolic = Integer.parseInt(pressure.substring(0, 3));
                    int diastolic = Integer.parseInt(pressure.substring(3, 5));
                    
                    if(validate.pressureAlert(sistolic, diastolic)){
                        if(patientDataManager.getCurrentPatient() != null){
                            patientDataManager.getCurrentPatient().addPressureAlertList(resultdate);
                        }
                        /*
                        * notification
                        */
                    }                    
                    
                    String formatPressure = sistolic +"/" + diastolic;
                    valuelb.setValue(formatPressure);
                    time.setValue("Last update: " + sdf.format(resultdate));
                    Thread.sleep(1000); 
                    }
                } catch (InterruptedException ex) {                    
                    System.out.println("Thread stopped");
                    ex.printStackTrace();
                }
            }
        }.start();
        
        horizontalLayout1.addComponents(valuelb, symbollb);
        horizontalLayout2.addComponent(time);
        
        panel.addComponent(horizontalLayout1);
        panel.addComponent(horizontalLayout2);
        
        return panel;
    }
    
    protected Component getGlucosePanel() {
        CssLayout panel = new CssLayout();
        HorizontalLayout horizontalLayout1 = new HorizontalLayout();
        VerticalLayout horizontalLayout2 = new VerticalLayout();
        
        Label valuelb = new Label("0");
        valuelb.setId("g_value");
        valuelb.setHeight("60px");
        valuelb.addStyleName(ValoTheme.LABEL_H1);
        valuelb.addStyleName(ValoTheme.LABEL_NO_MARGIN);
        valuelb.addStyleName(ValoTheme.LABEL_COLORED);
        
        Label symbollb = new Label();
        symbollb.setId("g_value");
        symbollb.setCaption("mg/dL");
        
        Label time = new Label();
        time.setId("time");
        time.setValue("Last Update:");
                
        new Thread(){
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");   
            
            public void run(){
                try {
                    while(true){                    
                    Thread.sleep(1000);   
                    Date resultdate = new Date(System.currentTimeMillis());
                    Integer glucose = Integer.parseInt(patientDataManager.getByOID(OID_S.get("bloodGlucose")));
                    //Integer glucose = 250;
                    
                    if(validate.glucoseAlert(glucose)){
                        if(patientDataManager.getCurrentPatient() != null){
                            patientDataManager.getCurrentPatient().addGlucoseAlertList(resultdate);
                        }
                        /*
                        * notification
                        */
                    }                   
                    valuelb.setValue(glucose.toString());
                    time.setValue("Last update: " + sdf.format(resultdate));
                    Thread.sleep(1000); 
                    }
                } catch (InterruptedException ex) {                    
                    System.out.println("Thread stopped");
                    ex.printStackTrace();
                }
            }
        }.start();
        
        horizontalLayout1.addComponents(valuelb, symbollb);
        horizontalLayout2.addComponent(time);
        
        panel.addComponent(horizontalLayout1);
        panel.addComponent(horizontalLayout2);
        
        return panel;
    }
    
    // TODO:GET REAL DATA E TUDO MAIS
    protected Component getHeartRateChart() {
        heartRatechart = new Chart();
        heartRatechart.setHeight("250px");
        heartRatechart.setWidth("33%");
        
        configurationHR = heartRatechart.getConfiguration();
        configurationHR.getChart().setType(ChartType.SPLINE);
        configurationHR.getTitle().setText(
                "Heart Rate");
        configurationHR.getTooltip().setFormatter("");
        configurationHR.getxAxis().setType(AxisType.DATETIME);
        // format is: Hour Date of week
        configurationHR.getxAxis().setDateTimeLabelFormats(
                new DateTimeLabelFormats("%H %a", "%H"));
        //configuration.getTooltip().setXDateFormat("%d.%m. %Y %H:%M");

        YAxis yAxis = configurationHR.getyAxis();
        yAxis.setTitle(new AxisTitle("Heart Rate (bpm)"));
        yAxis.setMin(0);

        configurationHR.getTooltip().setFormatter(
                        "'<b>'+ this.series.name +'</b><br/>\'+ Highcharts.dateFormat('%H %a', this.x) +': '+ this.y +' bpm'");
        

        hrData = new DataSeries();
        hrData.setPlotOptions(new PlotOptionsSpline());
        hrData.setName(patientDataManager.getCurrentPatient() != null ? patientDataManager.getCurrentPatient().getName() : "NO patient selected");
        for (int i = 0; i <= 10; i++) {
            hrData.add(new DataSeriesItem(System.currentTimeMillis(), 0));
        }
        runWhileAttached(heartRatechart, new Runnable() {
            @Override
            public void run() {
                int heartValue = Integer.parseInt(patientDataManager.getByOID(OID_S.get("heartRate")));
                //int heartValue = 80;
                Long time = System.currentTimeMillis();
                hrData.add(new DataSeriesItem(time, heartValue), true, true);
                
                if(validate.heartRateAlert(heartValue)){
                    if(patientDataManager.getCurrentPatient() != null){
                        patientDataManager.getCurrentPatient().addHeartRateAlertList(new Date(time));
                    }

                    /*
                    * notification
                    */
                }   
            }
        }, 1000, 1000);

        configurationHR.setSeries(hrData);

        heartRatechart.drawChart(configurationHR);
        return heartRatechart;
    }

    
    /**
     * Runs given task repeatedly until the reference component is attached
     *
     * @param component
     * @param task
     * @param interval
     * @param initialPause
     *            a timeout after tas is started
     */
    public static void runWhileAttached(final Component component,
            final Runnable task, final int interval, final int initialPause) {
        // Until reliable push available in our demo servers
        UI.getCurrent().setPollInterval(interval);

        final Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(initialPause);
                    while (component.getUI() != null) {
                        Future<Void> future = component.getUI().access(task);
                        future.get();
                        Thread.sleep(interval);
                    }
                } catch (InterruptedException e) {
                } catch (ExecutionException e) {
                    Logger.getLogger(this.getClass().getName())
                            .log(Level.WARNING,
                                    "Stopping repeating command due to an exception",
                                    e);
                } catch (com.vaadin.ui.UIDetachedException e) {
                } catch (Exception e) {
                    Logger.getLogger(this.getClass().getName())
                            .log(Level.WARNING,
                                    "Unexpected exception while running scheduled update",
                                    e);
                }
                Logger.getLogger(this.getClass().getName()).log(Level.INFO,
                        "Thread stopped");
            };
        };
        thread.start();
    }

}
