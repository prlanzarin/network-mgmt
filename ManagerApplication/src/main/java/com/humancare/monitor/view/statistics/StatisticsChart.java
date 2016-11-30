/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humancare.monitor.view.statistics;

import com.humancare.monitor.entities.RegisteredPatients;
import com.humancare.monitor.snmp.PatientDataManager;
import com.vaadin.addon.charts.Chart;
import com.vaadin.addon.charts.PointClickEvent;
import com.vaadin.addon.charts.PointClickListener;
import com.vaadin.addon.charts.model.AxisType;
import com.vaadin.addon.charts.model.ChartType;
import com.vaadin.addon.charts.model.Configuration;
import com.vaadin.addon.charts.model.Cursor;
import com.vaadin.addon.charts.model.DataLabels;
import com.vaadin.addon.charts.model.DataSeries;
import com.vaadin.addon.charts.model.DataSeriesItem;
import com.vaadin.addon.charts.model.PlotOptionsColumn;
import com.vaadin.addon.charts.model.PlotOptionsPie;
import com.vaadin.addon.charts.model.XAxis;
import com.vaadin.addon.charts.model.YAxis;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class StatisticsChart {
    
    private PatientDataManager patientDataManager = PatientDataManager.getInstance();
    private List<RegisteredPatients> patientWithHeartAlert = new ArrayList<RegisteredPatients>();
    private List<RegisteredPatients> patientWithGlucoseAlert = new ArrayList<RegisteredPatients>();
    private List<RegisteredPatients> patientWithPressureAlert = new ArrayList<RegisteredPatients>();
    private List<RegisteredPatients> patientWithTemperatureAlert = new ArrayList<RegisteredPatients>();
    private List<RegisteredPatients> patientWithSpo2Alert = new ArrayList<RegisteredPatients>();
    
    public StatisticsChart(){
        RegisteredPatients  p =  new RegisteredPatients("teste", "10", 80, "F");
        p.addPressureAlertList(new Date(System.currentTimeMillis()));
        p.addHeartRateAlertList(new Date(System.currentTimeMillis()));
        patientDataManager.addPatientToMemory(p);
        
        p =  new RegisteredPatients("teste2", "10", 70, "F");
        p.addHeartRateAlertList(new Date(System.currentTimeMillis()));
        patientDataManager.addPatientToMemory(p);
        
         p =  new RegisteredPatients("teste2", "10", 72, "F");
        p.addHeartRateAlertList(new Date(System.currentTimeMillis()));
        patientDataManager.addPatientToMemory(p);
        p =  new RegisteredPatients("teste2", "10", 75, "F");
        p.addHeartRateAlertList(new Date(System.currentTimeMillis()));
        patientDataManager.addPatientToMemory(p);
        
        populatePatientAlertList();
        
    
    }
    
    public void populatePatientAlertList(){
        // sort by age
        Collections.sort(patientDataManager.getPatientList());
        
        for(RegisteredPatients p: patientDataManager.getPatientList()){
            System.out.println(p.getAge());
            if(!p.getGlucoseAlertFrequency().isEmpty()){
                patientWithGlucoseAlert.add(p);
            }
            
            if(!p.getHeartRateALertFrequency().isEmpty()){
                patientWithHeartAlert.add(p);
            }
            
            if(!p.getPressureAlertFrequency().isEmpty()){
                patientWithPressureAlert.add(p);
            }
            
            if(!p.getSpo2AlertFrequency().isEmpty()){
                patientWithSpo2Alert.add(p);
            }
            
            if(!p.getTemperatureAlertFrequency().isEmpty()){
                patientWithTemperatureAlert.add(p);
            }        
        
        }
    }
    
    /*
    *  Chart of patient distribution by gender
    */
    public Component genderChart() {
        Chart chart = new Chart(ChartType.PIE);
        chart.setHeight("300px");
        chart.setWidth("500px");

        Configuration conf = chart.getConfiguration();
        conf.setTitle("Gender Distribution");

        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        DataLabels dataLabels = new DataLabels();
        dataLabels.setEnabled(true);
        dataLabels
                .setFormatter("'<b>'+ this.point.name +'</b>: '+ this.percentage +' %'");
        plotOptions.setDataLabels(dataLabels);
        conf.setPlotOptions(plotOptions);

        final DataSeries series = new DataSeries();
        series.add(new DataSeriesItem("Female", patientDataManager.getFemPatientsNumber()));
        series.add(new DataSeriesItem("Male", patientDataManager.getMalePatientsNumber()));
        conf.setSeries(series);

        chart.addPointClickListener(new PointClickListener() {

            @Override
            public void onClick(PointClickEvent event) {
                Notification.show("Click: "
                        + series.get(event.getPointIndex()).getName());
            }
        });

        chart.drawChart(conf);
        return chart;
    }
    
    /*
    *  Chart to show average heart rate alert distribution by age
    */
    public Component getAverageHeartRateAlertChart() {        
        
        Chart chart = new Chart(ChartType.COLUMN);
        chart.setHeight("300px");
        chart.setWidth("500px");

        Configuration averageHRConfig = chart.getConfiguration();
        averageHRConfig.setTitle("Average Age-Heart Rate Alert");
        averageHRConfig.getLegend().setEnabled(false);

        XAxis x = new XAxis();
        x.setType(AxisType.CATEGORY);
        averageHRConfig.addxAxis(x);

        YAxis y = new YAxis();
        y.setTitle("Number of alerts");
        averageHRConfig.addyAxis(y);

        PlotOptionsColumn column = new PlotOptionsColumn();
        column.setCursor(Cursor.POINTER);
        column.setDataLabels(new DataLabels(true));

        averageHRConfig.setPlotOptions(column);
        averageHRConfig.getTooltip().setFormatter(
                        "'<b>'+ this.point.name +' years old: </b><br/>\'+ this.y +' alerts'");

        DataSeries series = new DataSeries();
        PlotOptionsColumn plotOptionsColumn = new PlotOptionsColumn();
        plotOptionsColumn.setColorByPoint(true);
        series.setPlotOptions(plotOptionsColumn);       
        
        DataSeriesItem item;
        Integer newValue = 0, lastValue = 0, lastAge = 0;
        
        for(RegisteredPatients p : patientWithHeartAlert){
            newValue = p.getHeartRateALertFrequency().size();            
            // calculates average
            if(p.getAge().equals(lastAge)){
                newValue = (p.getHeartRateALertFrequency().size() + lastValue)/2;
            }            
            item = new DataSeriesItem(p.getAge().toString(), newValue);
            series.addItemWithDrilldown(item);
            
            lastValue = newValue;
            lastAge = p.getAge();            
        }
        
        averageHRConfig.addSeries(series);
        return chart;
    }
    
    /*
    *  Chart to show average temperature alert distribution by age
    */
    public Component getAverageTemperatureAlertChart() {        
        
        Chart chart = new Chart(ChartType.COLUMN);
        chart.setHeight("300px");
        chart.setWidth("500px");

        Configuration averageTemperature = chart.getConfiguration();
        averageTemperature.setTitle("Average Age-Temperature Alert");
        averageTemperature.getLegend().setEnabled(false);

        XAxis x = new XAxis();
        x.setType(AxisType.CATEGORY);
        averageTemperature.addxAxis(x);

        YAxis y = new YAxis();
        y.setTitle("Number of alerts");
        averageTemperature.addyAxis(y);

        PlotOptionsColumn column = new PlotOptionsColumn();
        column.setCursor(Cursor.POINTER);
        column.setDataLabels(new DataLabels(true));

        averageTemperature.setPlotOptions(column);
        averageTemperature.getTooltip().setFormatter(
                        "'<b>'+ this.point.name +' years old: </b><br/>\'+ this.y +' alerts'");

        DataSeries series = new DataSeries();
        PlotOptionsColumn plotOptionsColumn = new PlotOptionsColumn();
        plotOptionsColumn.setColorByPoint(true);
        series.setPlotOptions(plotOptionsColumn);

        DataSeriesItem item;
        int newValue = 0, lastValue = 0, lastAge = 0;
        
        for(RegisteredPatients p : patientWithTemperatureAlert){
            newValue = p.getTemperatureAlertFrequency().size();            
            // calculates average
            if(p.getAge().equals(lastAge)){
                newValue = (p.getTemperatureAlertFrequency().size() + lastValue)/2;
            }
            item = new DataSeriesItem(p.getAge().toString(), newValue);
            series.addItemWithDrilldown(item);
            
            lastValue = newValue;
            lastAge = p.getAge();
        }
        
        averageTemperature.addSeries(series);
        return chart;
    }
    
    
    /*
    *  Chart to show average temperature alert distribution by age
    */
    public Component getAveragePressureAlertChart() {        
        
        Chart chart = new Chart(ChartType.COLUMN);
        chart.setHeight("300px");
        chart.setWidth("500px");

        Configuration averagePressureConfig = chart.getConfiguration();
        averagePressureConfig.setTitle("Average Age-Pressure Alert");
        averagePressureConfig.getLegend().setEnabled(false);

        XAxis x = new XAxis();
        x.setType(AxisType.CATEGORY);
        averagePressureConfig.addxAxis(x);

        YAxis y = new YAxis();
        y.setTitle("Number of alerts");
        averagePressureConfig.addyAxis(y);

        PlotOptionsColumn column = new PlotOptionsColumn();
        column.setCursor(Cursor.POINTER);
        column.setDataLabels(new DataLabels(true));

        averagePressureConfig.setPlotOptions(column);
        averagePressureConfig.getTooltip().setFormatter(
                        "'<b>'+ this.point.name +' years old: </b><br/>\'+ this.y +' alerts'");

        DataSeries series = new DataSeries();
        PlotOptionsColumn plotOptionsColumn = new PlotOptionsColumn();
        plotOptionsColumn.setColorByPoint(true);
        series.setPlotOptions(plotOptionsColumn);

        DataSeriesItem item;
        int newValue = 0, lastValue = 0, lastAge = 0;
        
        for(RegisteredPatients p : patientWithPressureAlert){
            newValue = p.getPressureAlertFrequency().size();            
            // calculates average
            if(p.getAge().equals(lastAge)){
                newValue = (p.getPressureAlertFrequency().size() + lastValue)/2;
            }
            item = new DataSeriesItem(p.getAge().toString(), newValue);
            series.addItemWithDrilldown(item);
            
            lastValue = newValue;
            lastAge = p.getAge();
        }
        
        averagePressureConfig.addSeries(series);
        return chart;
    }
    
    /*
    *  Chart to show average glucose alert distribution by age
    */
    public Component getAverageGlucoseAlertChart() {        
        
        Chart chart = new Chart(ChartType.COLUMN);
        chart.setHeight("300px");
        chart.setWidth("500px");

        Configuration averageGlucoseConfig = chart.getConfiguration();
        averageGlucoseConfig.setTitle("Average Age-Glucose Alert");
        averageGlucoseConfig.getLegend().setEnabled(false);

        XAxis x = new XAxis();
        x.setType(AxisType.CATEGORY);
        averageGlucoseConfig.addxAxis(x);

        YAxis y = new YAxis();
        y.setTitle("Number of alerts");
        averageGlucoseConfig.addyAxis(y);

        PlotOptionsColumn column = new PlotOptionsColumn();
        column.setCursor(Cursor.POINTER);
        column.setDataLabels(new DataLabels(true));

        averageGlucoseConfig.setPlotOptions(column);
        averageGlucoseConfig.getTooltip().setFormatter(
                        "'<b>'+ this.point.name +' years old: </b><br/>\'+ this.y +' alerts'");

        DataSeries series = new DataSeries();
        PlotOptionsColumn plotOptionsColumn = new PlotOptionsColumn();
        plotOptionsColumn.setColorByPoint(true);
        series.setPlotOptions(plotOptionsColumn);

        DataSeriesItem item;
        int newValue = 0, lastValue = 0, lastAge = 0;
        
        for(RegisteredPatients p : patientWithGlucoseAlert){
            newValue = p.getGlucoseAlertFrequency().size();            
            // calculates average
            if(p.getAge().equals(lastAge)){
                newValue = (p.getGlucoseAlertFrequency().size() + lastValue)/2;
            }
            item = new DataSeriesItem(p.getAge().toString(), newValue);
            series.addItemWithDrilldown(item);
            
            lastValue = newValue;
            lastAge = p.getAge();
        }
        
        averageGlucoseConfig.addSeries(series);
        return chart;
    }
    
    /*
    *  Chart to show average glucose alert distribution by age
    */
    public Component getAverageSpo2AlertChart() {        
        
        Chart chart = new Chart(ChartType.COLUMN);
        chart.setHeight("300px");
        chart.setWidth("500px");

        Configuration averageSpo2Config = chart.getConfiguration();
        averageSpo2Config.setTitle("Average Age-SpO2 Alert");
        averageSpo2Config.getLegend().setEnabled(false);

        XAxis x = new XAxis();
        x.setType(AxisType.CATEGORY);
        averageSpo2Config.addxAxis(x);

        YAxis y = new YAxis();
        y.setTitle("Number of alerts");
        averageSpo2Config.addyAxis(y);

        PlotOptionsColumn column = new PlotOptionsColumn();
        column.setCursor(Cursor.POINTER);
        column.setDataLabels(new DataLabels(true));

        averageSpo2Config.setPlotOptions(column);
        averageSpo2Config.getTooltip().setFormatter(
                        "'<b>'+ this.point.name +' years old: </b><br/>\'+ this.y +' alerts'");

        DataSeries series = new DataSeries();
        PlotOptionsColumn plotOptionsColumn = new PlotOptionsColumn();
        plotOptionsColumn.setColorByPoint(true);
        series.setPlotOptions(plotOptionsColumn);

        DataSeriesItem item;
        int newValue = 0, lastValue = 0, lastAge = 0;
        
        for(RegisteredPatients p : patientWithSpo2Alert){
            newValue = p.getSpo2AlertFrequency().size();            
            // calculates average
            if(p.getAge().equals(lastAge)){
                newValue = (p.getSpo2AlertFrequency().size() + lastValue)/2;
            }
            item = new DataSeriesItem(p.getAge().toString(), newValue);
            series.addItemWithDrilldown(item);
            
            lastValue = newValue;
            lastAge = p.getAge();
        }
        
        averageSpo2Config.addSeries(series);
        return chart;
    }

}
