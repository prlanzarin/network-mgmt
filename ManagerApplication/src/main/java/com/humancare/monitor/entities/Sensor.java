/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humancare.monitor.entities;

/**
 *
 * @author amanda
 */
public class Sensor {
    
    private String type;
    private String location;
    private int batteryPower;
    private String batteryAlert;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getBatteryPower() {
        return batteryPower;
    }

    public void setBatteryPower(int batteryPower) {
        this.batteryPower = batteryPower;
    }

    public String getBatteryAlert() {
        return batteryAlert;
    }

    public void setBatteryAlert(String batteryAlert) {
        this.batteryAlert = batteryAlert;
    }
    
    
    
    
}
