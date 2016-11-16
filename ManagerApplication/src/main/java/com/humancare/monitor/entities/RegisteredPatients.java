/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humancare.monitor.entities;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author amanda
 */
public class RegisteredPatients {
    
    private String name;
    private String ip;
    private static List<RegisteredPatients> patientList;
    
    public RegisteredPatients() {
    }
    
    public RegisteredPatients(String name, String ip){
        this.name = name;
        this.ip = ip;
    }
    
    public void addPatient(String name, String ip){
        if(patientList == null){
            patientList = new ArrayList<RegisteredPatients>();
        }
        
        patientList.add(new RegisteredPatients(name, ip));
        
        
    }

    public List<RegisteredPatients> getPatientList() {        
        addPatient("teste", "ble");
        return patientList;
    }

    public void setPatientList(List<RegisteredPatients> patientList) {
        this.patientList = patientList;
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
    
    
    
}
