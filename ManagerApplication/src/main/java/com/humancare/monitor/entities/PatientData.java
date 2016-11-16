/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humancare.monitor.entities;

import static com.humancare.monitor.DashboardUI.MANAGER;
import static com.humancare.monitor.snmp.Manager.OID_S;
import java.util.List;
import org.snmp4j.smi.OID;

/**
 *
 * @author amanda
 */
public class PatientData {   
   
    private String ip;
    
    private String name;
    private String age;
    private String gender;
    private float temperature;
    private float bloodPressure;
    private float bloodGlucose;
    private String heartRate;
    private String SPO2;
    private float latitute;
    private float longitude;
    private float x;
    private float y;
    private float z;
    private String envHumidity;
    private String envTemperature;
    private String envLuminosity;
    private String envOxygen;
    private boolean envAlarm;
    private int numberOfSensors;
    private List<Sensor> sensorsList;
    private String nwType;
    private String nwSpeed;
    

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

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(float bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public float getBloodGlucose() {
        return bloodGlucose;
    }

    public void setBloodGlucose(float bloodGlucose) {
        this.bloodGlucose = bloodGlucose;
    }

    public String getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(String heartRate) {
        this.heartRate = heartRate;
    }

    public String getSPO2() {
        return SPO2;
    }

    public void setSPO2(String SPO2) {
        this.SPO2 = SPO2;
    }

    public float getLatitute() {
        return latitute;
    }

    public void setLatitute(float latitute) {
        this.latitute = latitute;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public String getEnvHumidity() {
        return envHumidity;
    }

    public void setEnvHumidity(String envHumidity) {
        this.envHumidity = envHumidity;
    }

    public String getEnvTemperature() {
        return envTemperature;
    }

    public void setEnvTemperature(String envTemperature) {
        this.envTemperature = envTemperature;
    }

    public String getEnvLuminosity() {
        return envLuminosity;
    }

    public void setEnvLuminosity(String envLuminosity) {
        this.envLuminosity = envLuminosity;
    }

    public String getEnvOxygen() {
        return envOxygen;
    }

    public void setEnvOxygen(String envOxygen) {
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
