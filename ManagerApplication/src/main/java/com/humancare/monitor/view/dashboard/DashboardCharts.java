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
    
    // SPO Chart attributes
    private Chart spo2Chart;
    private Configuration spo2Config;
    private DataSeries spo2Data;
    
    // Blood Pressure Chart attributes
    private Chart pressureChart;
    private Configuration pressureConfig;
    private DataSeries pressureData;
    
    // Blood Glucose Chart attributes
    private Chart glucoseChart;
    private Configuration glucoseConfig;
    private DataSeries glucoseData;
    
    // Env Temp Chart attributes
    private Chart envTempChart;
    private Configuration envTempConfig;
    private DataSeries envTempData;
    
    // Env HUmidity Chart attributes
    private Chart envHumidityChart;
    private Configuration envHumidityConfig;
    private DataSeries envHumidityData;
    
     // Env HUmidity Chart attributes
    private Chart envOxyChart;
    private Configuration envOxyConfig;
    private DataSeries envOxyData;
    
    
    // General attributes
    private int numberOfDays;
    private List<PatientData> filteredData;
    
    public DashboardCharts(int numberOfDays){
        this.numberOfDays = numberOfDays;
        getDataByDate();
    }
    
      
    protected Component getTemperatureChart() {
        temperatureChart = new Chart();
        temperatureChart.setHeight("350px");
        temperatureChart.setWidth("33%");

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
        heartRatechart.setHeight("350px");
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
    
    protected Component getSPO2Chart() {
        spo2Chart = new Chart();
        spo2Chart.setHeight("350px");
        spo2Chart.setWidth("33%");

        spo2Config = spo2Chart.getConfiguration();
        spo2Config.getChart().setType(ChartType.SPLINE);
        spo2Config.getTitle().setText("Oxygen Saturation");
        spo2Config.getTooltip().setFormatter("");
        spo2Config.getxAxis().setType(AxisType.DATETIME);
        spo2Config.getxAxis().setDateTimeLabelFormats(
                new DateTimeLabelFormats("%H %a", "%H"));

        YAxis yAxis = spo2Config.getyAxis();
        yAxis.setTitle(new AxisTitle("SpO2 (%)"));

        spo2Config.getTooltip().setFormatter(
                        "'<b>'+ this.series.name +'</b><br/>\'+ Highcharts.dateFormat('%H %a', this.x) +': '+ this.y +' %'");

        spo2Data = new DataSeries();
        spo2Data.setPlotOptions(new PlotOptionsSpline());
        // TODO: pegar primeiro da lista se estiver null
        spo2Data.setName(patientDataManager.getCurrentPatient() != null ? patientDataManager.getCurrentPatient().getName() : "NO patient selected");
        
        updateSPO2Data();
        
        spo2Config.addSeries(spo2Data);
        spo2Chart.drawChart(spo2Config);
        return spo2Chart;
    }
    
    public void updateSPO2Data(){
        for(PatientData p : filteredData){
            DataSeriesItem item = new DataSeriesItem(p.getReceivedDateAndTime(), p.getSPO2());
            // add item, select updateChartImmediately option
            spo2Data.add(item, true, false);                
        } 
    }
    
    protected Component getPressureChart() {
        pressureChart = new Chart();
        pressureChart.setHeight("350px");
        pressureChart.setWidth("33%");

        pressureConfig = pressureChart.getConfiguration();
        pressureConfig.getChart().setType(ChartType.SPLINE);
        pressureConfig.getTitle().setText("Blood Pressure");
        pressureConfig.getTooltip().setFormatter("");
        pressureConfig.getxAxis().setType(AxisType.DATETIME);
        pressureConfig.getxAxis().setDateTimeLabelFormats(
                new DateTimeLabelFormats("%H %a", "%H"));

        YAxis yAxis = pressureConfig.getyAxis();
        yAxis.setTitle(new AxisTitle("Blood Pressure (mmHg)"));

        pressureConfig.getTooltip().setFormatter(
                        "'<b>'+ this.series.name +'</b><br/>\'+ Highcharts.dateFormat('%H %a', this.x) +': '+ this.y +' mmHg'");

        pressureData = new DataSeries();
        pressureData.setPlotOptions(new PlotOptionsSpline());
        // TODO: pegar primeiro da lista se estiver null
        pressureData.setName(patientDataManager.getCurrentPatient() != null ? patientDataManager.getCurrentPatient().getName() : "NO patient selected");
        
        updatePressureData();
        
        pressureConfig.addSeries(pressureData);
        pressureChart.drawChart(pressureConfig);
        return pressureChart;
    }
    
    public void updatePressureData(){
        for(PatientData p : filteredData){
            DataSeriesItem item = new DataSeriesItem(p.getReceivedDateAndTime(), p.getBloodPressure());
            // add item, select updateChartImmediately option
            pressureData.add(item, true, false);                
        } 
    }
    
    protected Component getGlucoseChart() {
        glucoseChart = new Chart();
        glucoseChart.setHeight("350px");
        glucoseChart.setWidth("33%");

        glucoseConfig = glucoseChart.getConfiguration();
        glucoseConfig.getChart().setType(ChartType.SPLINE);
        glucoseConfig.getTitle().setText("Blood Glucose");
        glucoseConfig.getTooltip().setFormatter("");
        glucoseConfig.getxAxis().setType(AxisType.DATETIME);
        glucoseConfig.getxAxis().setDateTimeLabelFormats(
                new DateTimeLabelFormats("%H %a", "%H"));

        YAxis yAxis = glucoseConfig.getyAxis();
        yAxis.setTitle(new AxisTitle("Blood Glucose (mg/dL)"));

        glucoseConfig.getTooltip().setFormatter(
                        "'<b>'+ this.series.name +'</b><br/>\'+ Highcharts.dateFormat('%H %a', this.x) +': '+ this.y +' mg/dL'");

        glucoseData = new DataSeries();
        glucoseData.setPlotOptions(new PlotOptionsSpline());
        // TODO: pegar primeiro da lista se estiver null
        glucoseData.setName(patientDataManager.getCurrentPatient() != null ? patientDataManager.getCurrentPatient().getName() : "NO patient selected");
        
        updateGlucoseData();
        
        glucoseConfig.addSeries(glucoseData);
        glucoseChart.drawChart(glucoseConfig);
        return glucoseChart;
    }
    
    public void updateGlucoseData(){
        for(PatientData p : filteredData){
            DataSeriesItem item = new DataSeriesItem(p.getReceivedDateAndTime(), p.getBloodGlucose());
            // add item, select updateChartImmediately option
            glucoseData.add(item, true, false);                
        } 
    }
    
    protected Component getEnvTempChart() {
        envTempChart = new Chart();
        envTempChart.setHeight("350px");
        envTempChart.setWidth("33%");

        envTempConfig = envTempChart.getConfiguration();
        envTempConfig.getChart().setType(ChartType.SPLINE);
        envTempConfig.getTitle().setText("Enviroment Temperature");
        envTempConfig.getTooltip().setFormatter("");
        envTempConfig.getxAxis().setType(AxisType.DATETIME);
        envTempConfig.getxAxis().setDateTimeLabelFormats(
                new DateTimeLabelFormats("%H %a", "%H"));

        YAxis yAxis = envTempConfig.getyAxis();
        yAxis.setTitle(new AxisTitle("Enviroment Temperature (°C)"));

        envTempConfig.getTooltip().setFormatter(
                        "'<b>'+ this.series.name +'</b><br/>\'+ Highcharts.dateFormat('%H %a', this.x) +': '+ this.y +' °C'");

        envTempData = new DataSeries();
        envTempData.setPlotOptions(new PlotOptionsSpline());
        // TODO: pegar primeiro da lista se estiver null
        envTempData.setName(patientDataManager.getCurrentPatient() != null ? patientDataManager.getCurrentPatient().getName() : "NO patient selected");
        
        updateEnvTempData();
        
        envTempConfig.addSeries(envTempData);
        envTempChart.drawChart(envTempConfig);
        return envTempChart;
    }
    
    public void updateEnvTempData(){
        for(PatientData p : filteredData){
            DataSeriesItem item = new DataSeriesItem(p.getReceivedDateAndTime(), p.getEnvTemperature());
            // add item, select updateChartImmediately option
            envTempData.add(item, true, false);                
        } 
    }
    
    protected Component getEnvLuminosityChart() {
        envHumidityChart = new Chart();
        envHumidityChart.setHeight("350px");
        envHumidityChart.setWidth("33%");

        envHumidityConfig = envHumidityChart.getConfiguration();
        envHumidityConfig.getChart().setType(ChartType.SPLINE);
        envHumidityConfig.getTitle().setText("Enviroment Humidity");
        envHumidityConfig.getTooltip().setFormatter("");
        envHumidityConfig.getxAxis().setType(AxisType.DATETIME);
        envHumidityConfig.getxAxis().setDateTimeLabelFormats(
                new DateTimeLabelFormats("%H %a", "%H"));

        YAxis yAxis = envHumidityConfig.getyAxis();
        yAxis.setTitle(new AxisTitle("Enviroment Humidity (%)"));

        envHumidityConfig.getTooltip().setFormatter(
                        "'<b>'+ this.series.name +'</b><br/>\'+ Highcharts.dateFormat('%H %a', this.x) +': '+ this.y +' %'");

        envHumidityData = new DataSeries();
        envHumidityData.setPlotOptions(new PlotOptionsSpline());
        // TODO: pegar primeiro da lista se estiver null
        envHumidityData.setName(patientDataManager.getCurrentPatient() != null ? patientDataManager.getCurrentPatient().getName() : "NO patient selected");
        
        updateEnvHumidityData();
        
        envHumidityConfig.addSeries(envHumidityData);
        envHumidityChart.drawChart(envHumidityConfig);
        return envHumidityChart;
    }
    
    public void updateEnvHumidityData(){
        for(PatientData p : filteredData){
            DataSeriesItem item = new DataSeriesItem(p.getReceivedDateAndTime(), p.getEnvHumidity());
            // add item, select updateChartImmediately option
            envHumidityData.add(item, true, false);                
        } 
    }
    
    protected Component getEnvOxyChart() {
        envOxyChart = new Chart();
        envOxyChart.setHeight("350px");
        envOxyChart.setWidth("33%");

        envOxyConfig = envOxyChart.getConfiguration();
        envOxyConfig.getChart().setType(ChartType.SPLINE);
        envOxyConfig.getTitle().setText("Enviroment Humidity");
        envOxyConfig.getTooltip().setFormatter("");
        envOxyConfig.getxAxis().setType(AxisType.DATETIME);
        envOxyConfig.getxAxis().setDateTimeLabelFormats(
                new DateTimeLabelFormats("%H %a", "%H"));

        YAxis yAxis = envOxyConfig.getyAxis();
        yAxis.setTitle(new AxisTitle("Enviroment Humidity (%)"));

        envOxyConfig.getTooltip().setFormatter(
                        "'<b>'+ this.series.name +'</b><br/>\'+ Highcharts.dateFormat('%H %a', this.x) +': '+ this.y +' %'");

        envOxyData = new DataSeries();
        envOxyData.setPlotOptions(new PlotOptionsSpline());
        // TODO: pegar primeiro da lista se estiver null
        envOxyData.setName(patientDataManager.getCurrentPatient() != null ? patientDataManager.getCurrentPatient().getName() : "NO patient selected");
        
        updateEnvOxyData();
        
        envOxyConfig.addSeries(envOxyData);
        envOxyChart.drawChart(envOxyConfig);
        return envOxyChart;
    }
    
    public void updateEnvOxyData(){
        for(PatientData p : filteredData){
            DataSeriesItem item = new DataSeriesItem(p.getReceivedDateAndTime(), p.getEnvHumidity());
            // add item, select updateChartImmediately option
            envOxyData.add(item, true, false);                
        } 
    }
    
    /*
    * Method gets patient data according to number of days defined by user
    */
    public void getDataByDate(){
        filteredData = patientDataManager.selectPatientInfoByDate(numberOfDays);    
    }   
    
    
   

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }
    
    

    
}
