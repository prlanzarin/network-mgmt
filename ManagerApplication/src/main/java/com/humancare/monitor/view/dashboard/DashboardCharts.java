/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humancare.monitor.view.dashboard;

import com.humancare.monitor.entities.PatientData;
import com.humancare.monitor.snmp.PatientDataManager;
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
import java.util.List;

/**
 *
 * @author amanda
 */
public class DashboardCharts {
    
    PatientDataManager patientDataManager = PatientDataManager.getInstance();
    
    // Heart Rate Chart attributes
    private Chart heartRatechart;
    private Configuration configurationHR;
    private DataSeries hrData;    
    
    // Temperature Chart attributes
    private Chart temperatureChart;
    private Configuration temperatureConfig;
    private DataSeries temperatureData;
    
    
    // General attributes
    private int numberOfDays;
    private List<PatientData> filteredData;
    
    public DashboardCharts(int numberOfDays){
        this.numberOfDays = numberOfDays;
        getDataByDate();
    }
    
      
    //TODO: get real data
    protected Component getTemperatureChart() {
        temperatureChart = new Chart();
        temperatureChart.setHeight("400px");
        temperatureChart.setWidth("40%");

        temperatureConfig = temperatureChart.getConfiguration();
        temperatureConfig.getChart().setType(ChartType.SPLINE);
        temperatureConfig.getTitle().setText("Temperature");
        temperatureConfig.getTooltip().setFormatter("");
        temperatureConfig.getxAxis().setType(AxisType.DATETIME);
        temperatureConfig.getxAxis().setDateTimeLabelFormats(
                new DateTimeLabelFormats("%H %a", "%H"));

        YAxis yAxis = temperatureConfig.getyAxis();
        yAxis.setTitle(new AxisTitle("Temperature (°C)"));

        temperatureConfig.getTooltip().setFormatter(
                        "'<b>'+ this.series.name +'</b><br/>\'+ Highcharts.dateFormat('%H %a', this.x) +': '+ this.y +' °C'");

        temperatureData = new DataSeries();
        temperatureData.setPlotOptions(new PlotOptionsSpline());
        // TODO: pegar primeiro da lista se estiver null
        temperatureData.setName(patientDataManager.getCurrentPatient() != null ? patientDataManager.getCurrentPatient().getName() : "NO patient selected");
        
        updateTemperatureData();
        
        temperatureConfig.addSeries(temperatureData);
        temperatureChart.drawChart(temperatureConfig);
        return temperatureChart;
    }
    
    public void updateTemperatureData(){
        for(PatientData p : filteredData){
            DataSeriesItem item = new DataSeriesItem(p.getReceivedDateAndTime(), p.getTemperature());
            // add item, select updateChartImmediately option
            temperatureData.add(item, true, false);                
        } 
    }
    
    
    protected Component getHeartRateChart() {
        heartRatechart = new Chart();
        heartRatechart.setHeight("400px");
        heartRatechart.setWidth("40%");
        
        configurationHR = heartRatechart.getConfiguration();
        configurationHR.getChart().setType(ChartType.SPLINE);
        configurationHR.getTitle().setText(
                "Heart Rate");
        configurationHR.getTooltip().setFormatter("");
        configurationHR.getxAxis().setType(AxisType.DATETIME);
        // format is: Hour Date of week
        configurationHR.getxAxis().setDateTimeLabelFormats(
                new DateTimeLabelFormats("%H %a", "%H"));

        YAxis yAxis = configurationHR.getyAxis();
        yAxis.setTitle(new AxisTitle("Heart Rate (bpm)"));
        yAxis.setMin(0);

        configurationHR.getTooltip().setFormatter(
                        "'<b>'+ this.series.name +'</b><br/>\'+ Highcharts.dateFormat('%H %a', this.x) +': '+ this.y +' bpm'");

        hrData = new DataSeries();
        hrData.setPlotOptions(new PlotOptionsSpline());
        // TODO: pegar primeiro da lista se estiver null
        hrData.setName(patientDataManager.getCurrentPatient() != null ? patientDataManager.getCurrentPatient().getName() : "NO patient selected");
        
        updateHeartRatedata();
        
        configurationHR.addSeries(hrData);
        heartRatechart.drawChart(configurationHR);
        return heartRatechart;
    }
    
    /*
    * Method that feeds heart rate dataSeries chart
    */ 
    public void updateHeartRatedata(){
        for(PatientData p : filteredData){
            DataSeriesItem item = new DataSeriesItem(p.getReceivedDateAndTime(), p.getHeartRate());
            // add item, select updateChartImmediately option
            hrData.add(item, true, false);                
        }       
        
    }
    
    /*
    * Method gets patient data according to number of days defined by user
    */
    public void getDataByDate(){
        filteredData = patientDataManager.selectPatientByDate(numberOfDays);    
    }   
    
    
   

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }
    
    

    
}
