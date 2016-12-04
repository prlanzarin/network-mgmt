/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humancare.monitor.snmp;

import com.humancare.monitor.entities.PatientData;
import com.humancare.monitor.entities.Sensor;
import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import static com.vaadin.ui.Notification.TYPE_HUMANIZED_MESSAGE;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
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
    private String address = null;
    private static int requestID = 1;
    private static final String PUBLIC_COMMUNITY = "public";

    private static final TransportMapping transport;
    private static final Snmp snmp;

    static {
        try {
            transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            transport.listen();
            System.out.println("INITIALIZING SNMP TRANPOSRT");
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize SNMP transport mapping!");
        }
    }

    protected Manager() {
    }

    public static Manager getInstance() {
        if (instance == null) {
            System.out.println("Getting new instance");
            instance = new Manager();
        }
        return instance;
    }

    public void configManager(String add, Integer port) {
        address = "udp:" + add + "/" + port;
    }

    /**
     * Method which takes a single OID and returns the response from the agent
     * as a String.
     *
     * @param oid
     * @return
     */
    public String getAsString(OID oid) {
        ResponseEvent event = null;
        try {
            event = get(new OID[]{oid});
        } catch (IOException ex) {
            return null;
        }
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
        Target target = getTarget(PUBLIC_COMMUNITY);
        if (target == null) {
            return null;
        }
        PDU pdu = new PDU();
        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }
        pdu.setType(PDU.GET);
        pdu.setRequestID(new Integer32(requestID++));
        ResponseEvent event = snmp.get(pdu, target);
        if (event != null) {
            return event;
        }
        return null;
    }

    /**
     * This method is capable of handling multiple OIDs and read multiple lines
     *
     * @param oids
     * @param maxRepetitions
     * @return
     * @throws IOException
     */
    public ResponseEvent getBulk(OID oid, int maxRepetitions) throws IOException {
        Target target = getTarget(PUBLIC_COMMUNITY);
        if (target == null) {
            return null;
        }
        PDU pdu = new PDU();
        pdu.setType(PDU.GETBULK);
        pdu.setMaxRepetitions(maxRepetitions);
        pdu.setNonRepeaters(0);

        ResponseEvent event = snmp.getBulk(pdu, target);
        if (event != null) {
            return event;
        } else {
            throw new RuntimeException("GETBULK timed out");
        }
    }

    /**
     * This method is set a mib octet string value according to its oid
     *
     * @param oid
     * @param value
     * @return
     * @throws IOException
     */
    public ResponseEvent set(OID oid, String value) throws IOException, RuntimeException {
        Target target = getTarget(PUBLIC_COMMUNITY);
        if (target == null) {
            return null;
        }
        // Create the PDU object
        PDU pdu = new PDU();
        Variable var = new OctetString(value);
        VariableBinding varBind = new VariableBinding(oid, var);
        pdu.add(varBind);
        pdu.setType(PDU.SET);
        pdu.setRequestID(new Integer32(requestID++));
        ResponseEvent response = snmp.set(pdu, target);
        if (response != null) {
            return response;
        } else {
            throw new RuntimeException("SET timed out");
        }
    }

    /**
     * This method is set a mib integer value according to its oid
     *
     * @param oid
     * @param value
     * @return
     * @throws IOException
     */
    public ResponseEvent set(OID oid, Integer value) {
        Target target = getTarget(PUBLIC_COMMUNITY);
        if (target == null) {
            return null;
        }
        // Create the PDU object
        PDU pdu = new PDU();
        Variable var = new Integer32(value);
        VariableBinding varBind = new VariableBinding(oid, var);
        pdu.add(varBind);
        pdu.setType(PDU.SET);
        pdu.setRequestID(new Integer32(requestID++));

        ResponseEvent response;
        try {
            response = snmp.set(pdu, target);
        } catch (IOException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (RuntimeException ex) {
            Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return response;
    }

    /**
     * This method returns a Target, which contains information about where the
     * data should be fetched and how.
     *
     * @param community Community being accessed in MIB
     * @return
     */
    private Target getTarget(String community) {
        if (address == null) {
            return null;
        }
        Address targetAddress = GenericAddress.parse(address);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(targetAddress);
        target.setRetries(2);
        target.setTimeout(800);
        target.setVersion(SnmpConstants.version2c);
        return target;
    }

    public PatientData getAllPatientInformation(OID[] oids) {

        PatientData patientData = new PatientData();
        ResponseEvent response = null;
        try {
            patientData.setReceivedDateAndTime(System.currentTimeMillis());
            response = get(oids);
        } catch (RuntimeException ex) {
            System.out.println("  [GET-PAT] Connection time out");
            return null;
        } catch (IOException ex) {
            System.out.println("Error findinding patient information");
            return null;
        }

        if (response != null && response.getResponse() != null) {
            PDU pdu = response.getResponse();
            patientData.setName(pdu.get(0).getVariable().toString());
            patientData.setAge(pdu.get(1).getVariable().toString());
            patientData.setGender(pdu.get(2).getVariable().toString());
            patientData.setLatitute(Double.parseDouble(pdu.get(3).getVariable().toString()));
            patientData.setLongitude(Double.parseDouble(pdu.get(4).getVariable().toString()));
            patientData.setX(Double.parseDouble(pdu.get(5).getVariable().toString()));
            patientData.setY(Double.parseDouble(pdu.get(6).getVariable().toString()));
            patientData.setZ(Double.parseDouble(pdu.get(7).getVariable().toString()));
            patientData.setBloodPressure(Integer.parseInt(pdu.get(8).getVariable().toString()));
            patientData.setTemperature(Double.parseDouble(pdu.get(9).getVariable().toString()) / 10.0);
            patientData.setHeartRate(Integer.parseInt(pdu.get(10).getVariable().toString()));
            patientData.setBloodGlucose(Integer.parseInt(pdu.get(11).getVariable().toString()));
            patientData.setSPO2(Integer.parseInt(pdu.get(12).getVariable().toString()));
        }

        return patientData;
    }

    public PatientData getEnvInformation(OID[] oids, PatientData patientData) {
        PDU pdu = null;
        try {
            ResponseEvent response = get(oids);
            pdu = response.getResponse();
        } catch (RuntimeException ex) {
            System.out.println("  [GET-ENV] Connection time out");
            return null;
        } catch (IOException ex) {
            System.out.println("Error to find environment information");
            return null;
        }
        if (pdu != null) {
            patientData.setEnvHumidity(Integer.parseInt(pdu.get(0).getVariable().toString()));
            patientData.setEnvTemperature(Integer.parseInt(pdu.get(1).getVariable().toString()));
            patientData.setEnvLuminosity(Integer.parseInt(pdu.get(2).getVariable().toString()));
            patientData.setEnvOxygen(Integer.parseInt(pdu.get(3).getVariable().toString()));
            patientData.setEnvAlarm(pdu.get(4).getVariable().toString().equals("SIM"));
        } else {
            return null;
        }

        return patientData;
    }

    public PatientData getNetInformation(OID[] oids, PatientData patientData) {
        try {

            ResponseEvent response = get(oids);
            PDU pdu = response.getResponse();
            patientData.setNwType(pdu.get(0).getVariable().toString());
            patientData.setNwSpeed(pdu.get(1).getVariable().toString());

        } catch (RuntimeException ex) {
            System.out.println("  [GET-NET] Connection time out");
            return null;
        } catch (IOException ex) {
            System.out.println("Error to find network information");
            return null;
        }

        return patientData;
    }

    public List<Sensor> getSensorInformation(OID oid, int maxRepetitions) {
        Target target = getTarget(PUBLIC_COMMUNITY);
        if (target == null) {
            return null;
        }
        List<Sensor> sensorList = new ArrayList<Sensor>();
        ResponseEvent response = null;

        PDU pdu = new PDU();
        pdu.setType(PDU.GETBULK);
        pdu.add(new VariableBinding(new OID(oid)));
        pdu.setMaxRepetitions(maxRepetitions);

        try {
            response = snmp.getBulk(pdu, target);
        } catch (RuntimeException ex) {
            System.out.println("  [GET-SEN] Connection time out");
        } catch (IOException ex) {
            System.out.println("Erro ao buscar informaçoes de sensores");
            return null;
        }

        PDU responsePDU = response.getResponse();
        Vector vec = responsePDU.getVariableBindings();
        int numberOfSensors = maxRepetitions / Constants.SENSOR_OBJECTS;
        for (int i = 0; i < numberOfSensors; i++) {
            Sensor sensor = new Sensor();
            VariableBinding vbType = (VariableBinding) vec.elementAt(i);
            VariableBinding vbLocation = (VariableBinding) vec.elementAt(i + (numberOfSensors * 2));
            VariableBinding vbBatteryPower = (VariableBinding) vec.elementAt(i + (numberOfSensors * 3));
            VariableBinding vbBatteryAlert = (VariableBinding) vec.elementAt(i + (numberOfSensors * 4));
            sensor.setType(vbType.getVariable().toString());
            sensor.setLocation(vbLocation.getVariable().toString());
            sensor.setBatteryPower(Integer.parseInt(vbBatteryPower.getVariable().toString()));
            sensor.setBatteryAlert(Integer.parseInt(vbBatteryAlert.getVariable().toString()));

            sensorList.add(sensor);
        }

        return sensorList;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
