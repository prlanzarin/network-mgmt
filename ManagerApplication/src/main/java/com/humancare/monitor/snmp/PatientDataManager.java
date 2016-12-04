/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.humancare.monitor.snmp;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.humancare.monitor.domain.DashboardNotification;
import com.humancare.monitor.entities.PatientData;
import com.humancare.monitor.entities.RegisteredPatients;
import com.humancare.monitor.entities.Sensor;
import static com.humancare.monitor.snmp.Constants.OID_S;
import com.humancare.monitor.view.dashboard.DashboardCharts;
import com.vaadin.server.Page;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Notification;
import static com.vaadin.ui.Notification.TYPE_HUMANIZED_MESSAGE;
import com.vaadin.ui.TextField;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.OID;

/*
 * 
 * Class to manage patient data and communicate with SNMP Manager
 * Its an intermediary between view and snmp manager
 */
public class PatientDataManager {

    private static PatientDataManager instance = null;

    private final OID PATIENT_INFO_OIDS[] = {OID_S.get("usrName"), OID_S.get("usrAge"), OID_S.get("usrGender"), OID_S.get("usrLatitude"),
        OID_S.get("usrLongitude"), OID_S.get("usrX"), OID_S.get("usrY"), OID_S.get("usrZ"), OID_S.get("bloodPressure"), OID_S.get("temperature"),
        OID_S.get("heartRate"), OID_S.get("bloodGlucose"), OID_S.get("spo2")};

    private final OID ENV_OIDS[] = {OID_S.get("envHumidity"), OID_S.get("envTemperature"), OID_S.get("envLuminosity"), OID_S.get("envOxygen"), OID_S.get("envAlarm")};
    private final OID NET_OIDS[] = {OID_S.get("nwType"), OID_S.get("nwSpeed")};
    private final OID SENSOR_OIDS[] = {OID_S.get("sensorType"), OID_S.get("sensorLocation"), OID_S.get("sensorBatteryPower"), OID_S.get("sensorBatteryAlert")};

    private PatientData patientData = new PatientData();

    private final List<PatientData> patientDB = new ArrayList<>();

    // current patient selected by user
    private RegisteredPatients currentPatient;

    // list of patients registered by manager
    private List<RegisteredPatients> registPatientList;

    private static Collection<DashboardNotification> notifications = new ArrayList();

    private int femPatientsNumber;
    private int malePatientsNumber;

    private Manager MANAGER = Manager.getInstance();

    protected PatientDataManager() {
    }

    public static PatientDataManager getInstance() {
        if (instance == null) {
            instance = new PatientDataManager();
        }
        return instance;
    }

    public String getByOID(OID oid) {
        return MANAGER.getAsString(oid);
    }

    // TODO: avaliar parametros necessarios
    public PatientData getAllPatientInfo() {
        patientData = MANAGER.getAllPatientInformation(PATIENT_INFO_OIDS);
        //save new patient in patientMemory
        if (patientData != null) {
            patientDB.add(patientData);
        }
        return patientData;
    }

    public PatientData getEnvInfo() {
        patientData = MANAGER.getEnvInformation(ENV_OIDS, patientData);
        return patientData;
    }

    public PatientData getNetInfo() {
        patientData = MANAGER.getNetInformation(NET_OIDS, patientData);
        return patientData;
    }

    public List<Sensor> getSensors(int numberOfSensors) {
        return MANAGER.getSensorInformation(OID_S.get("hcSensorEntry"), numberOfSensors * 5);

    }

    public void addPatientToMemory(RegisteredPatients regPatient) {
        if (registPatientList == null) {
            registPatientList = new ArrayList<>();
        }

        registPatientList.add(regPatient);

        DashboardCharts updateCharts = DashboardCharts.getInstance();
        updateCharts.initPatientSelect();

    }

    /*
    * Method to filter patient data by date
    * Used to show weekly and monthly charts starting by current date
    *
    * @param numberOfDays -  7 (weekly chart), 30 (monthly), ..
     */
    public List<PatientData> selectPatientInfoByDate(int numberOfDays) {
        // excluir linha abaixo - para testes
        // getAllPatientInfo();
        List<PatientData> filteredList = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        Date currentDate = new Date(System.currentTimeMillis());
        cal.setTime(currentDate);
        cal.add(Calendar.DAY_OF_YEAR, -numberOfDays);

        if (!patientDB.isEmpty() && currentPatient != null) {
            patientDB.stream().filter((p) -> (currentPatient.getName().
                equals(p.getName()))).forEach((p) -> {
                Calendar temp = Calendar.getInstance();
                temp.setTime(p.getReceivedDateAndTime());
                if (temp.after(cal)) {
                    filteredList.add(p);
                }
            });
        }

        return filteredList;

    }

    // Add new agent calling set function in Manager for each oid
    public boolean addPatientToMib(FormLayout form) {
        String name = null, ip = null, gender = null;
        Integer age = null, port = null;
        for (Component c : form) {
            if (c instanceof TextField) {
                TextField f = (TextField) c;
                switch (f.getId()) {
                    case "ip":
                        ip = f.getValue();
                        break;
                    case "port":
                        port = Integer.parseInt(f.getValue());
                        break;
                    case "usrName":
                        name = f.getValue();
                        break;
                    case "usrAge":
                        age = Integer.parseInt(f.getValue());
                        break;
                    case "usrGender":
                        gender = f.getValue();
                        break;
                    default:
                        break;
                }
            }
        }

        // Register new client info at agent
        MANAGER.configManager(ip, port);
        try {
            if (MANAGER.set(OID_S.get("usrName"), name) == null
                || MANAGER.set(OID_S.get("usrAge"), age) == null
                || MANAGER.set(OID_S.get("usrGender"), gender) == null) {
                return false;
            }
        }  catch (IOException ex) {
            System.out.println("  [ADD-P] Error gathering agent connection");
            return false;
        }

        // add patient to application memory with its ip
        RegisteredPatients regPatient = new RegisteredPatients(name, ip, port, age, gender);
        addPatientToMemory(regPatient);
        if (currentPatient != null) {
            MANAGER.configManager(currentPatient.getIp(), currentPatient.getPort());
        }
        return true;
    }

    public void calcGenderStatistic() {
        femPatientsNumber = 0;
        malePatientsNumber = 0;

        if (registPatientList != null) {
            registPatientList.stream().forEach((p) -> {
                if (p.getGender().equals("F")) {
                    femPatientsNumber++;
                } else {
                    malePatientsNumber++;
                }
            });
        }

    }

    public static void addNotification(String name, String action, String time, String content) {
        DashboardNotification n1 = new DashboardNotification();
        n1.setId(1);
        n1.setFirstName(name);
        n1.setLastName(" - ");
        n1.setAction(action);
        n1.setPrettyTime(time);
        n1.setContent(content);
        notifications.add(n1);

    }

    public Collection<DashboardNotification> getNotifications() {
        for (DashboardNotification notification : notifications) {
            notification.setRead(true);
        }
        return Collections.unmodifiableCollection(notifications);
    }

    public static int getCountNotifications() {
        Predicate<DashboardNotification> unreadPredicate = new Predicate<DashboardNotification>() {
            @Override
            public boolean apply(DashboardNotification input) {
                return !input.isRead();
            }
        };
        return Collections2.filter(notifications, unreadPredicate).size();
    }

    public RegisteredPatients getCurrentPatient() {
        return currentPatient;
    }

    public void setCurrentPatient(RegisteredPatients currentPatient) {
        System.out.println("Setting current patient to " + currentPatient.toString());
        this.currentPatient = currentPatient;
    }

    public List<RegisteredPatients> getPatientList() {
        return registPatientList;
    }

    public void setPatientList(List<RegisteredPatients> patientList) {
        registPatientList = patientList;
    }

    public int getFemPatientsNumber() {
        return femPatientsNumber;
    }

    public void setFemPatientsNumber(int femPatientsNumber) {
        this.femPatientsNumber = femPatientsNumber;
    }

    public int getMalePatientsNumber() {
        return malePatientsNumber;
    }

    public void setMalePatientsNumber(int malePatientsNumber) {
        this.malePatientsNumber = malePatientsNumber;
    }

}
