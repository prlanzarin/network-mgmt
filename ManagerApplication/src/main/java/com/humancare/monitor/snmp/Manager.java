/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humancare.monitor.snmp;

import com.humancare.monitor.entities.PatientData;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 *
 * @author amanda
 */
public class Manager {

    private static Manager instance = null;
    private Snmp snmp = null;
    private String address = null;    
    private static int requestID = 1;
    
    
    protected Manager(){}
    
    public static Manager getInsance(){
        if(instance == null){
            instance = new Manager();
        }
        return instance;        
    }
    
    public void configManager(String add) {
         address = add;
         try {           
            //Manager client = new Manager("udp:127.0.0.1/161");
            instance.start();
         } catch (IOException ex) {
            ex.printStackTrace();        }
    
    }

    /**
     * Start the Snmp session. If you forget the listen() method you will not
     * get any answers because the communication is asynchronous and the
     * listen() method listens for answers.
     *
     * @throws IOException
     */
    void start() throws IOException {
        TransportMapping transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
        // Do not forget this line!
        transport.listen();
    }

    /**
     * Method which takes a single OID and returns the response from the agent
     * as a String.
     *
     * @param oid
     * @return
     * @throws IOException
     */
    public String getAsString(OID oid) throws IOException {
        ResponseEvent event = get(new OID[]{oid});
        return event.getResponse().get(0).getVariable().toString();
    }

    /**
     * This method is capable of handling multiple OIDs
     *
     * @param oids
     * @return
     * @throws IOException
     */
    public ResponseEvent get(OID oids[]) throws IOException {
        PDU pdu = new PDU();
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }
        pdu.setType(PDU.GET);
        ResponseEvent event = snmp.send(pdu, getTarget(), null);
        if (event != null) {
            return event;
        }
        throw new RuntimeException("GET timed out");
    }
    
    
    /**
     * This method is set a mib octet string value according to its oid
     *
     * @param oid 
     * @param value
     * @return
     * @throws IOException
     */
    public ResponseEvent set(OID oid, String value) throws IOException{
        Target target = getTarget();
        
        // Create the PDU object
        PDU pdu = new PDU();        
        Variable var = new OctetString(value);
        VariableBinding varBind = new VariableBinding(oid, var);
        pdu.add(varBind);
        pdu.setType(PDU.SET);
        pdu.setRequestID(new Integer32(requestID++));
        
        ResponseEvent response = snmp.set(pdu, target);        
        return response;
    
    }
    
     
    /**
     * This method is set a mib integer value according to its oid
     *
     * @param oid 
     * @param value
     * @return
     * @throws IOException
     */
    public ResponseEvent set(OID oid, Integer value) throws IOException{
        Target target = getTarget();
        // Create the PDU object
        PDU pdu = new PDU();        
        Variable var = new Integer32(value);
        VariableBinding varBind = new VariableBinding(oid, var);
        pdu.add(varBind);
        pdu.setType(PDU.SET);
        pdu.setRequestID(new Integer32(requestID++));
        
        ResponseEvent response = snmp.set(pdu, target);        
        return response;
    
    }

    /**
     * This method returns a Target, which contains information about where the
     * data should be fetched and how.
     *
     * @return
     */
    private Target getTarget() {
        Address targetAddress = GenericAddress.parse(address);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("public"));
        target.setAddress(targetAddress);
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version2c);
        return target;
    }
    
    public PatientData getAllPatientInformation(OID[] oids){
        
        PatientData patientData = new PatientData();
    //    try {
            patientData.setReceivedDateAndTime(System.currentTimeMillis());
            
    /*         ResponseEvent response = get(oids);
            
            PDU pdu = response.getResponse();
            patientData.setName(pdu.get(0).getVariable().toString());
            patientData.setAge(pdu.get(1).getVariable().toString());
            patientData.setGender(pdu.get(2).getVariable().toString());
            patientData.setLatitute(Float.parseFloat(pdu.get(3).getVariable().toString()));
            patientData.setLongitude(Float.parseFloat(pdu.get(4).getVariable().toString()));
            patientData.setX(Float.parseFloat(pdu.get(5).getVariable().toString()));
            patientData.setY(Float.parseFloat(pdu.get(6).getVariable().toString()));
            patientData.setZ(Float.parseFloat(pdu.get(7).getVariable().toString()));
            patientData.setBloodPressure(Float.parseFloat(pdu.get(8).getVariable().toString()));
            patientData.setTemperature(Float.parseFloat(pdu.get(9).getVariable().toString()));
            patientData.setHeartRate(Float.parseFloat(pdu.get(10).getVariable().toString()));
            patientData.setBloodGlucose(Float.parseFloat(pdu.get(11).getVariable().toString())); 
            patientData.setSPO2(Integer.parseInt(pdu.get(12).getVariable().toString()));
      */
               
            
            patientData.setName("Teste");
            patientData.setAge("22");
            patientData.setGender("Feminino");
            patientData.setLatitute((float)10.6);
            patientData.setLongitude((float)-50.3);
            patientData.setX((float) 2.6);
            patientData.setY((float) 5.6);
            patientData.setZ((float) 8);
            patientData.setBloodPressure((float) 11.3);
            patientData.setTemperature((float) 36.5);
            patientData.setHeartRate(80);
            patientData.setBloodGlucose((float) 50);
            patientData.setSPO2(98);
      //  } catch (IOException ex) {
        //    ex.printStackTrace();
        //}
    
        return patientData;
    }
    
    public PatientData getEnvInformation(OID[] oids, PatientData patientData){
        try {
            
            ResponseEvent response = get(oids);            
            PDU pdu = response.getResponse();
            
            patientData.setEnvHumidity(Integer.parseInt(pdu.get(0).getVariable().toString()));
            patientData.setEnvTemperature(Integer.parseInt(pdu.get(1).getVariable().toString()));
            patientData.setEnvLuminosity(Integer.parseInt(pdu.get(2).getVariable().toString()));
            patientData.setEnvOxygen(Integer.parseInt(pdu.get(3).getVariable().toString()));
            patientData.setEnvAlarm(pdu.get(4).getVariable().toString().equals("SIM"));
            
        } catch (IOException ex) {
            System.out.println("Erro ao buscar informações de ambiente");
            ex.printStackTrace();
            return null;
        }
    
        return patientData;
    }
    
    PatientData getNetInformation(OID[] oids, PatientData patientData) {
        try {
            
            ResponseEvent response = get(oids);            
            PDU pdu = response.getResponse();
            
            patientData.setNwType(pdu.get(0).getVariable().toString());
            patientData.setNwSpeed(pdu.get(1).getVariable().toString());
            
        } catch (IOException ex) {
            System.out.println("Erro ao buscar informações de rede");
            ex.printStackTrace();
            return null;
        }
    
        return patientData;
    }
    
    
    
    public static final Map<String, OID> OID_S = new HashMap<String, OID>() {{
        put("humanCare", new OID(".1.3.6.1.3.57.6"));
        put("hcUserInfo", new OID(".1.3.6.1.3.57.6.1"));
        put("hcBody", new OID(".1.3.6.1.3.57.6.2"));
        put("hcEnvironment", new OID(".1.3.6.1.3.57.6.3"));
        put("hcSensor", new OID(".1.3.6.1.3.57.6.4"));
        put("hcNetwork", new OID(".1.3.6.1.3.57.6.5"));
        put("usrName", new OID(".1.3.6.1.3.57.6.1.1"));
        put("usrAge", new OID(".1.3.6.1.3.57.6.1.2"));
        put("usrGender", new OID(".1.3.6.1.3.57.6.1.3"));
        put("usrLatitude", new OID(".1.3.6.1.3.57.6.1.4"));
        put("usrLongitude", new OID(".1.3.6.1.3.57.6.1.5"));
        put("usrX", new OID(".1.3.6.1.3.57.6.1.6"));
        put("usrY", new OID(".1.3.6.1.3.57.6.1.7"));
        put("usrZ", new OID(".1.3.6.1.3.57.6.1.8"));
        put("bloodPressure", new OID(".1.3.6.1.3.57.6.2.1"));
        put("temperature", new OID(".1.3.6.1.3.57.6.2.2"));
        put("heartRate", new OID(".1.3.6.1.3.57.6.2.3"));
        put("bdHeartRhythmLeadI", new OID(".1.3.6.1.3.57.6.2.4"));
        put("bdHeartRhythmLeadII", new OID(".1.3.6.1.3.57.6.2.5"));
        put("bloodGlucose", new OID(".1.3.6.1.3.57.6.2.6"));
        put("spo2", new OID(".1.3.6.1.3.57.6.2.7"));
        put("envHumidity", new OID(".1.3.6.1.3.57.6.3.1"));
        put("envTemperature", new OID(".1.3.6.1.3.57.6.3.2"));
        put("envLuminosity", new OID(".1.3.6.1.3.57.6.3.3"));
        put("envOxygen", new OID(".1.3.6.1.3.57.6.3.4"));
        put("envAlarm", new OID(".1.3.6.1.3.57.6.3.5"));
        put("hcSensorNumber", new OID(".1.3.6.1.3.57.6.4.1"));
        put("hcSensorTable", new OID(".1.3.6.1.3.57.6.4.2"));
        put("hcSensorEntry", new OID(".1.3.6.1.3.57.6.4.2.1"));
        put("sensorType", new OID(".1.3.6.1.3.57.6.4.2.1.1"));
        put("sensorLocation", new OID(".1.3.6.1.3.57.6.4.2.1.2"));
        put("sensorBatteryPower", new OID(".1.3.6.1.3.57.6.4.2.1.3"));
        put("sensorBatteryAlert", new OID(".1.3.6.1.3.57.6.4.2.1.4"));
        put("nwType", new OID(".1.3.6.1.3.57.6.5.1"));
        put("nwSpeed", new OID(".1.3.6.1.3.57.6.5.2"));
        
    }};

    

}
