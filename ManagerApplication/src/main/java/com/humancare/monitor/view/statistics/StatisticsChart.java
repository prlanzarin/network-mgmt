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


public class StatisticsChart {
    
    private PatientDataManager patientDataManager = PatientDataManager.getInstance();
    private Configuration ageConfig;
    
    // genero
    public Component genderChart() {
        Chart chart = new Chart(ChartType.PIE);
        chart.setHeight("350px");
        chart.setWidth("33%");

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
    
    // idades
  /*  public Component getAgeDistChart() {
        Chart chart = new Chart(ChartType.COLUMN);
        chart.setHeight("350px");
        chart.setWidth("33%");


        ageConfig = chart.getConfiguration();

        ageConfig.setTitle("Age Distribution");
        ageConfig.getLegend().setEnabled(false);

        XAxis x = new XAxis();
        x.setType(AxisType.CATEGORY);
        ageConfig.addxAxis(x);

        YAxis y = new YAxis();
        y.setTitle("Total percent market share");
        ageConfig.addyAxis(y);

        PlotOptionsColumn column = new PlotOptionsColumn();
        column.setCursor(Cursor.POINTER);
        column.setDataLabels(new DataLabels(true));

        ageConfig.setPlotOptions(column);

        DataSeries series = new DataSeries();
        PlotOptionsColumn plotOptionsColumn = new PlotOptionsColumn();
        plotOptionsColumn.setColorByPoint(true);
        series.setPlotOptions(plotOptionsColumn);

        DataSeriesItem item10 = null; int count10 = 0;
        DataSeriesItem item20 = null; int count20 = 0;
        DataSeriesItem item30 = null; int count30 = 0;
        DataSeriesItem item40 = null; int count40 = 0;
        
        for(RegisteredPatients p : patientDataManager.getPatientList()){
            if(p.getAge()< 10){
                if(item10 == null){
                    count10++;
                    item10 = new DataSeriesItem("0 - 10", count10);                    
                }else{
                    count10++;
                }
            }        
        }
        
        item10.setY(count10);
        series.addItemWithDrilldown(item10);
        
        ageConfig.addSeries(series);
       

        return chart;
    }
    
    */
    
    // alertas de glicose por idade
    
    
    // historico de alertas e tipos do current patient -> talvez no dashboard

}
