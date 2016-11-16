/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humancare.monitor.snmp;

import com.humancare.monitor.entities.PatientData;
import static com.humancare.monitor.DashboardUI.MANAGER;
import com.humancare.monitor.entities.RegisteredPatients;
import static com.humancare.monitor.snmp.Manager.OID_S;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.snmp4j.smi.OID;

/**
 *
 * @author amanda
 */
public class PatientDataManager {
    
     private OID PATIENT_INFO_OIDS[] = {OID_S.get("usrName"), OID_S.get("usrAge"), OID_S.get("usrGender"), OID_S.get("usrName"), OID_S.get("usrLatitude"),
            OID_S.get("usrLongitude"), OID_S.get("usrX"), OID_S.get("usrY"), OID_S.get("usrZ"),OID_S.get("bloodPressure"), OID_S.get("temperature"),
            OID_S.get("heartRate"), OID_S.get("bloodGlucose"), OID_S.get("spo2")};
    
    private OID ENV_OIDS[] = {OID_S.get("envHumidity"), OID_S.get("envTemperature"), OID_S.get("envLuminosity"), OID_S.get("envOxygen"), OID_S.get("envAlarm")};
    
    
    private OID NET_OIDS[] = {OID_S.get("nwType"), OID_S.get("nwSpeed")};
    
    private PatientData patientData = new PatientData();
    private RegisteredPatients regPatients = new RegisteredPatients();
    
    public PatientDataManager(){}
    
    // TODO: avaliar parametros necessarios
    public PatientData getAllPatientInfo(){
        patientData = MANAGER.getAllPatientInformation(PATIENT_INFO_OIDS);
        return patientData;                
    }
    
    public PatientData getEnvInfo(){
        patientData = MANAGER.getEnvInformation(ENV_OIDS, patientData);
        return patientData;
    }
    
    public PatientData getNetInfo(){
        patientData = MANAGER.getNetInformation(NET_OIDS, patientData);
        return patientData;    
    }
    
    public List<RegisteredPatients> getRegisteredPatients(){
        return regPatients.getPatientList();
    }
    
    // Add new agent calling set function in Manager for each oid
    // TODO: improve call
    public void addPatientToMib(FormLayout form){
        String name = null, ip = null;
        for(Component c : form) {
            if(c instanceof TextField) {
                TextField f = (TextField) c;               
                if(!f.getId().equals("ip")){
                    System.out.println(f.getId() +" - "+ f.getValue());
                    try {
                        MANAGER.set(OID_S.get(f.getId()), f.getValue());
                    } catch (IOException ex) {
                        Notification.show("Error saving "+f.getId() +" - "+ f.getValue()+ " information");
                    }
                }else{
                    ip = f.getValue();
                }
                if(f.getId().equals("usrName")){
                    name = f.getValue();
                }                
            }
        }
        
        // add patient to application memory with its ip
        RegisteredPatients regPatient = new RegisteredPatients(name, ip);
    }
    
}
