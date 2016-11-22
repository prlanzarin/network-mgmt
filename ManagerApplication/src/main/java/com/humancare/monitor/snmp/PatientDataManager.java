/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humancare.monitor.snmp;

import com.humancare.monitor.entities.PatientData;
import com.humancare.monitor.entities.RegisteredPatients;
import com.humancare.monitor.entities.Sensor;
import static com.humancare.monitor.snmp.Manager.OID_S;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.snmp4j.smi.OID;

/*
 * 
 * Class to manage patient data and communicate with SNMP Manager
 * Its an intermediary between view and snmp manager
 */
public class PatientDataManager {
    
    private static PatientDataManager instance = null;
    
    private OID PATIENT_INFO_OIDS[] = {OID_S.get("usrName"), OID_S.get("usrAge"), OID_S.get("usrGender"), OID_S.get("usrName"), OID_S.get("usrLatitude"),
            OID_S.get("usrLongitude"), OID_S.get("usrX"), OID_S.get("usrY"), OID_S.get("usrZ"),OID_S.get("bloodPressure"), OID_S.get("temperature"),
            OID_S.get("heartRate"), OID_S.get("bloodGlucose"), OID_S.get("spo2")};
    
    private OID ENV_OIDS[] = {OID_S.get("envHumidity"), OID_S.get("envTemperature"), OID_S.get("envLuminosity"), OID_S.get("envOxygen"), OID_S.get("envAlarm")}; 
    private OID NET_OIDS[] = {OID_S.get("nwType"), OID_S.get("nwSpeed")};
    private OID SENSOR_OIDS[] = {OID_S.get("sensorType"), OID_S.get("sensorLocation"), OID_S.get("sensorBatteryPower"), OID_S.get("sensorBatteryAlert")};
    
    private PatientData patientData = new PatientData();
    
    private List<PatientData> patientDB = new ArrayList<PatientData>();  
    
    // current patient selected by user
    private RegisteredPatients currentPatient;
    
    // list of patients registered by manager
    private List<RegisteredPatients> registPatientList;
    
    private Manager MANAGER = Manager.getInsance();   
    
    
    protected PatientDataManager(){}
    
    public static PatientDataManager getInstance(){
        if(instance == null){
            instance = new PatientDataManager();
        }           
        return instance;
    }
        
    public String getByOID(OID oid){
        return MANAGER.getAsString(oid);
    }
    
    // TODO: avaliar parametros necessarios
    public PatientData getAllPatientInfo(){
        patientData = MANAGER.getAllPatientInformation(PATIENT_INFO_OIDS);
        
        //save new patient in patientMemory
        patientDB.add(patientData);
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
    
    public List<Sensor> getSensors(int maxRepetitions){
        return MANAGER.getSensorInformation(SENSOR_OIDS, maxRepetitions);         
    
    }
    
    public void addPatientToMemory(RegisteredPatients regPatient){
        if(registPatientList == null){
            registPatientList = new ArrayList<RegisteredPatients>();
        }
        
        registPatientList.add(regPatient);        
        
    }
    
    /*
    * Method to filter patient data by date
    * Used to show weekly and monthly charts starting by current date
    *
    * @param numberOfDays -  7 (weekly chart), 30 (monthly), ..
    */
    public List<PatientData> selectPatientInfoByDate(int numberOfDays){     
        // excluir linha abaixo - para testes
        // getAllPatientInfo();
        List<PatientData> filteredList = new ArrayList<PatientData>();
        
        Calendar cal = Calendar.getInstance();
        Date currentDate = new Date(System.currentTimeMillis());
        cal.setTime(currentDate);
        cal.add(Calendar.DAY_OF_YEAR, -numberOfDays);
        
        if(!patientDB.isEmpty()){
            for(PatientData p : patientDB){
               // if(currentPatient.getName().equals(p.getName())){   -- isso ou fazer mapa <nome, objeto do paciente>
                Calendar temp = Calendar.getInstance();
                temp.setTime(p.getReceivedDateAndTime());
                if(temp.after(cal)){
                    filteredList.add(p);
                }
            }
        }
        
        return filteredList;
        
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
        addPatientToMemory(regPatient);
    }

    public RegisteredPatients getCurrentPatient() {
        return currentPatient;
    }

    public void setCurrentPatient(RegisteredPatients currentPatient) {
        this.currentPatient = currentPatient;
    }  
    
    public List<RegisteredPatients> getPatientList() {
        return registPatientList;
    }

    public void setPatientList(List<RegisteredPatients> patientList) {
        registPatientList = patientList;
    }
    
    
}
