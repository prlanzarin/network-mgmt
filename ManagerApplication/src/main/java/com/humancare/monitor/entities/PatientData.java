/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humancare.monitor.entities;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PatientData {   
   
    private String ip;
    
    private String name;
    private String age;
    private String gender;
    private double temperature;
    private int bloodPressure;
    private int bloodGlucose;
    private int heartRate;
    private int SPO2;
    private double latitute;
    private double longitude;
    private double x;
    private double y;
    private double z;
    private int envHumidity;
    private int envTemperature;
    private int envLuminosity;
    private int envOxygen;
    private boolean envAlarm;
    private int numberOfSensors;
    private List<Sensor> sensorsList;
    private String nwType;
    private String nwSpeed;
    private Date receivedDateAndTime;   
    
    public Date getReceivedDateAndTime() {
        return receivedDateAndTime;
    }

    public void setReceivedDateAndTime(Long receivedDateAndTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");    
        Date resultdate = new Date(receivedDateAndTime);
        this.receivedDateAndTime = resultdate;
    }    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(int bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public int getBloodGlucose() {
        return bloodGlucose;
    }

    public void setBloodGlucose(int bloodGlucose) {
        this.bloodGlucose = bloodGlucose;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public int getSPO2() {
        return SPO2;
    }

    public void setSPO2(int SPO2) {
        this.SPO2 = SPO2;
    }

    public double getLatitute() {
        return latitute;
    }

    public void setLatitute(double latitute) {
        this.latitute = latitute;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public int getEnvHumidity() {
        return envHumidity;
    }

    public void setEnvHumidity(int envHumidity) {
        this.envHumidity = envHumidity;
    }

    public int getEnvTemperature() {
        return envTemperature;
    }

    public void setEnvTemperature(int envTemperature) {
        this.envTemperature = envTemperature;
    }

    public int getEnvLuminosity() {
        return envLuminosity;
    }

    public void setEnvLuminosity(int envLuminosity) {
        this.envLuminosity = envLuminosity;
    }

    public int getEnvOxygen() {
        return envOxygen;
    }

    public void setEnvOxygen(int envOxygen) {
        this.envOxygen = envOxygen;
    }

    public boolean isEnvAlarm() {
        return envAlarm;
    }

    public void setEnvAlarm(boolean envAlarm) {
        this.envAlarm = envAlarm;
    }

    public int getNumberOfSensors() {
        return numberOfSensors;
    }

    public void setNumberOfSensors(int numberOfSensors) {
        this.numberOfSensors = numberOfSensors;
    }

    public List<Sensor> getSensorsList() {
        return sensorsList;
    }

    public void setSensorsList(List<Sensor> sensorsList) {
        this.sensorsList = sensorsList;
    }

    public String getNwType() {
        return nwType;
    }

    public void setNwType(String nwType) {
        this.nwType = nwType;
    }

    public String getNwSpeed() {
        return nwSpeed;
    }

    public void setNwSpeed(String nwSpeed) {
        this.nwSpeed = nwSpeed;
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
    
    
    
    
    
    
}
