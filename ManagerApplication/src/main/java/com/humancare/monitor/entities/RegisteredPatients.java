/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humancare.monitor.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author amanda
 */
public class RegisteredPatients {
    
    private String name;
    private String ip;   
    private Integer age;
    private String gender;
    
    private List<Date> glucoseAlertFrequency = new ArrayList<Date>();
    private List<Date> temperatureAlertFrequency = new ArrayList<Date>();
    private List<Date> pressureAlertFrequency  = new ArrayList<Date>();
    private List<Date> heartRateAlertFrequency = new ArrayList<Date>();
    private List<Date> spo2AlertFrequency = new ArrayList<Date>();
    
    public RegisteredPatients() {
    }
    
    public RegisteredPatients(String name, String ip, Integer age, String gender){
        this.name = name;
        this.ip = ip;
        this.age = age;
        this.gender = gender;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Date> getGlucoseAlertFrequency() {
        return glucoseAlertFrequency;
    }

    public void setGlucoseAlertFrequency(List<Date> glucoseAlertFrequency) {
        this.glucoseAlertFrequency = glucoseAlertFrequency;
    }

    public List<Date> getTemperatureAlertFrequency() {
        return temperatureAlertFrequency;
    }

    public void setTemperatureAlertFrequency(List<Date> temperatureAlertFrequency) {
        this.temperatureAlertFrequency = temperatureAlertFrequency;
    }

    public List<Date> getPressureAlertFrequency() {
        return pressureAlertFrequency;
    }

    public void setPressureAlertFrequency(List<Date> pressureAlertFrequency) {
        this.pressureAlertFrequency = pressureAlertFrequency;
    }

    public List<Date> getHeartRateALertFrequency() {
        return heartRateAlertFrequency;
    }

    public void setHeartRateALertFrequency(List<Date> heartRateALertFrequency) {
        this.heartRateAlertFrequency = heartRateALertFrequency;
    }

    public List<Date> getSpo2AlertFrequency() {
        return spo2AlertFrequency;
    }

    public void setSpo2AlertFrequency(List<Date> spo2AlertFrequency) {
        this.spo2AlertFrequency = spo2AlertFrequency;
    }
    
    public void addTempAlertList(Date time){
        temperatureAlertFrequency.add(time);
    }
    
    public void addGlucoseAlertList(Date time){
        glucoseAlertFrequency.add(time);
    }
    
    public void addPressureAlertList(Date time){
        pressureAlertFrequency.add(time);
    }
    
    public void addSpo2AlertList(Date time){
        spo2AlertFrequency.add(time);
    }
    
    public void addHeartRateAlertList(Date time){
        heartRateAlertFrequency.add(time);
    }
    
}
