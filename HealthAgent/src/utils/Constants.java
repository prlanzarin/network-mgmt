/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.snmp4j.smi.OID;

/**
 * Constants singleton class
 */
public final class Constants {

    /* humanCare            OBJECT IDENTIFIER ::= { XYZCorp 6 }*/
    public static final OID humanCare = new OID("1.3.6.1.3.57.6");
    
    /*
    -- General information regarding a monitored user.
    hcUserInfo           OBJECT IDENTIFIER ::= { humanCare 1 }
    */
    public static final OID hcUserInfo = new        OID("1.3.6.1.3.57.6.1");
    public static final OID usrName = new           OID("1.3.6.1.3.57.6.1.1.0");
    public static final OID usrAge = new            OID("1.3.6.1.3.57.6.1.2.0");
    public static final OID usrGender = new         OID("1.3.6.1.3.57.6.1.3.0");
    public static final OID usrLatitude = new       OID("1.3.6.1.3.57.6.1.4.0");
    public static final OID usrLongitude = new      OID("1.3.6.1.3.57.6.1.5.0");
    public static final OID usrOrientationX = new   OID("1.3.6.1.3.57.6.1.6.0");
    public static final OID usrOrientationY = new   OID("1.3.6.1.3.57.6.1.7.0");
    public static final OID usrOrientationZ = new   OID("1.3.6.1.3.57.6.1.8.0");
    
    /*
    -- Sensor information regarding the monitored user's body and health state.
    hcBody               OBJECT IDENTIFIER ::= { humanCare 2 }
     */
    public static final OID hcBody = new                    OID("1.3.6.1.3.57.6.2");
    public static final OID bdBloodPressure = new           OID("1.3.6.1.3.57.6.2.1.0");
    public static final OID bdTemperature = new             OID("1.3.6.1.3.57.6.2.2.0");
    public static final OID bdHeartRate = new               OID("1.3.6.1.3.57.6.2.3.0");
    public static final OID bdHeartRhythmLeadI= new         OID("1.3.6.1.3.57.6.2.4.0");
    public static final OID bdHeartRhythmLeadII = new       OID("1.3.6.1.3.57.6.2.5.0");
    public static final OID bdBloodGlucose = new            OID("1.3.6.1.3.57.6.2.6.0");
    public static final OID bdBloodOxygenSaturation = new   OID("1.3.6.1.3.57.6.2.7.0");
    
    /*
    -- Information about the environment in which the monitored user's current on.
    hcEnvironment        OBJECT IDENTIFIER ::= { humanCare 3 }
    */
    public static final OID hcEnvironment = new OID("1.3.6.1.3.57.6.3");
    public static final OID envHumidity = new   OID("1.3.6.1.3.57.6.3.1.0");
    public static final OID envTemperature= new OID("1.3.6.1.3.57.6.3.2.0");
    public static final OID envLuminosity = new OID("1.3.6.1.3.57.6.3.3.0");
    public static final OID envOxygen = new     OID("1.3.6.1.3.57.6.3.4.0");
    public static final OID envAlarm = new      OID("1.3.6.1.3.57.6.3.5.0");
    
    /*
    -- General information about sensors connected to the user's agent.
    hcSensor             OBJECT IDENTIFIER ::= { humanCare 4 }
    */
    public static final OID hcSensor = new          OID("1.3.6.1.3.57.6.4");
    public static final OID hcSensorNumber = new    OID("1.3.6.1.3.57.6.4.1.0");
    public static final OID hcSensorTable = new     OID("1.3.6.1.3.57.6.4.2");
    public static final OID hcSensorEntry = new     OID("1.3.6.1.3.57.6.4.2.1");
    public static final OID sensorIndex = new       OID("1.3.6.1.3.57.6.4.2.1.1");
    public static final OID sensorType = new        OID("1.3.6.1.3.57.6.4.2.1.2");
    public static final OID sensorLocation = new    OID("1.3.6.1.3.57.6.4.2.1.3");
    public static final OID sensorBatteryPower= new OID("1.3.6.1.3.57.6.4.2.1.4");
    public static final OID sensorBatteryAlert= new OID("1.3.6.1.3.57.6.4.2.1.5");
 
    /*
    -- Data about the network connecting the user's agent to any kind of manager (in progress).
    hcNetwork            OBJECT IDENTIFIER ::= { humanCare 5 }
    */
    public static final OID hcNetwork = new OID("1.3.6.1.3.57.6.5");
    public static final OID nwType = new    OID("1.3.6.1.3.57.6.5.1.0");
    public static final OID nwSpeed = new   OID("1.3.6.1.3.57.6.5.2.0");
    
    public static final Map<String, OID> MONames;
    static {
        Map<String, OID> aMap = new HashMap<>();
        aMap.put("1.3.6.1.3.57.6.2.4", bdHeartRhythmLeadI);
        aMap.put("1.3.6.1.3.57.6.2.7", bdBloodOxygenSaturation);
        aMap.put("1.3.6.1.3.57.6.2.6", bdBloodGlucose);
        aMap.put("1.3.6.1.3.57.6.4", hcSensor);
        aMap.put("1.3.6.1.3.57.6.4.2.1.3", sensorBatteryPower);
        aMap.put("1.3.6.1.3.57.6.4.1", hcSensorNumber);
        aMap.put("1.3.6.1.3.57.6.5.2", nwSpeed);
        aMap.put("1.3.6.1.3.57.6.1.3", usrGender);
        aMap.put("1.3.6.1.3.57.6.2.1", bdBloodPressure);
        aMap.put("1.3.6.1.3.57.6.4.2.1.4", sensorBatteryAlert);
        aMap.put("1.3.6.1.3.57.6.4.2", hcSensorTable);
        aMap.put("1.3.6.1.3.57.6.2.2", bdTemperature);
        aMap.put("1.3.6.1.3.57.6.2.3", bdHeartRate);
        aMap.put("1.3.6.1.3.57.6.3.2", envTemperature);
        aMap.put("1.3.6.1.3.57.6.3.1", envHumidity);
        aMap.put("1.3.6.1.3.57.6.5", hcNetwork);
        aMap.put("1.3.6.1.3.57.6.4.2.1.1", sensorType);
        aMap.put("1.3.6.1.3.57.6.3.4", envOxygen);
        aMap.put("1.3.6.1.3.57.6.5.1", nwType);
        aMap.put("1.3.6.1.3.57.6.1.5", usrLongitude);
        aMap.put("1.3.6.1.3.57.6.1.1", usrName);
        aMap.put("1.3.6.1.3.57.6.1.4", usrLatitude);
        aMap.put("1.3.6.1.3.57.6.1.2", usrAge);
        aMap.put("1.3.6.1.3.57.6.1", hcUserInfo);
        aMap.put("1.3.6.1.3.57.6.2.5", bdHeartRhythmLeadII);
        aMap.put("1.3.6.1.3.57.6.3", hcEnvironment);
        aMap.put("1.3.6.1.3.57.6", humanCare);
        aMap.put("1.3.6.1.3.57.6.3.3", envLuminosity);
        aMap.put("1.3.6.1.3.57.6.2", hcBody);
        aMap.put("1.3.6.1.3.57.6.4.2.1", hcSensorEntry);
        aMap.put("1.3.6.1.3.57.6.4.2.1.2", sensorLocation);
        aMap.put("1.3.6.1.3.57.6.3.5", envAlarm);
        aMap.put("1.3.6.1.3.57.6.1.6", usrOrientationX);
        aMap.put("1.3.6.1.3.57.6.1.7", usrOrientationY);
        aMap.put("1.3.6.1.3.57.6.1.8", usrOrientationZ);
        MONames = Collections.unmodifiableMap(aMap);
    }
}
