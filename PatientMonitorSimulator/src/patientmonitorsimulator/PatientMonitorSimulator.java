package patientmonitorsimulator;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import net.miginfocom.swing.MigLayout;
import org.snmp4j.smi.OID;

/**
 *
 * @author amanda
 */
public class PatientMonitorSimulator extends JFrame{

    JLabel patientData = new JLabel("Patient Data");
    JLabel namelb = new JLabel("Name: ");
    JLabel agelb = new JLabel("Age: ");
    JLabel genderlb = new JLabel("Gender: ");
    JLabel latitudelb = new JLabel("Latitude: ");
    JLabel longitudelb = new JLabel("Longitude: ");
    JLabel orientation = new JLabel("Orientation");
    JLabel xlb = new JLabel("x: ");
    JLabel ylb = new JLabel("y: ");
    JLabel zlb = new JLabel("z: ");
    
    JLabel monitoringData = new JLabel("Body Monitoring Data");
    JLabel bloodPressurelb = new JLabel("Blood Pressure: ");
    JLabel temperaturelb = new JLabel("Temperature: ");
    JLabel heartRatelb = new JLabel("Heart Rate: ");
    JLabel bloodGlucoselb = new JLabel("Blood Glucose");
    JLabel spo2lb = new JLabel("SpO2: ");
    
    JLabel enviromentData = new JLabel("Enviroment Monitoring Data");
    JLabel envHumiditylb = new JLabel("Humidity: ");
    JLabel envTemperaturelb = new JLabel("Temperature: ");
    JLabel envOxygenlb = new JLabel("Oxygen: ");
    JLabel envLuminositylb = new JLabel("Luminosity: ");
    
    /*
    * TODO: SENSORES e alarme env
    */
    
    JTextField name = new JTextField(10);        
    JTextField age = new JTextField(10);
    JTextField latitude = new JTextField(10);
    JTextField longitude = new JTextField(10);
    JTextField x = new JTextField(10);
    JTextField y = new JTextField(10);
    JTextField z = new JTextField(10);
    JTextField pressure = new JTextField(10);
    JTextField glucose = new JTextField(10);
    JTextField temperature = new JTextField(10);
    JTextField oxygen = new JTextField(10);
    JTextField heartRate = new JTextField(10);
    JTextField envTemperature = new JTextField(10);
    JTextField envOxygen = new JTextField(10);
    JTextField envHumidity = new JTextField(10);
    JTextField envLuminosity = new JTextField(10);
    
    JRadioButton optionF = new JRadioButton("Female");
    JRadioButton optionM = new JRadioButton("Male");
    ButtonGroup group = new ButtonGroup();
    
    JButton exportBt = new JButton("Export data");
    
    JPanel patPanel;
    JPanel bigpanel;
    JPanel measurPanel;
    JPanel envPanel;
    /*
    * TODO: SENSORES e alarme env
    */
    
    Map<OID, String> oidValues = new HashMap<OID, String>();
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        PatientMonitorSimulator simulator = new PatientMonitorSimulator();
        simulator.init();
        
    }
    
    public void init(){
        setTitle("Patient Monitor Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        
        pressure.setToolTipText("mmHg (ex: 120/80 format: 12080)");
        temperature.setToolTipText("ºC (ex: 36,8  format: 368)");
        envTemperature.setToolTipText("ºC Integer (ex: 36,8  format: 37)");
        glucose.setToolTipText("mg/dL (ex: 80 mg/dL format: 80");
        oxygen.setToolTipText("% (ex: 90% format: 90)");
        heartRate.setToolTipText("bpm (ex: 60 bpm format: 60)");
        envHumidity.setToolTipText("% (ex: 40% format: 40)");
        latitude.setToolTipText("30.0346 °S format: 300346");
        longitude.setToolTipText("51.2177° W format: 512177");
        
        optionF.setText("F");
        optionM.setText("M");
        group.add(optionF); 
        group.add(optionM);
        
        exportBt.addActionListener(new ActionListener(){
          public void actionPerformed(ActionEvent e){
            populateHashTable();
            exportFile();
          }
        });
        
        bigpanel = new JPanel(new MigLayout());
        JPanel measurPanel = new JPanel(new MigLayout());
        JPanel envPanel = new JPanel(new MigLayout());
        patPanel = new JPanel(new MigLayout());
        
        //Patient panel
        TitledBorder borderPatient = new TitledBorder("Patient Data");
        borderPatient.setTitleJustification(TitledBorder.CENTER);
        borderPatient.setTitlePosition(TitledBorder.TOP);
        
        patPanel.setBorder(borderPatient);
        patPanel.add(namelb);
        patPanel.add(name);
        patPanel.add(agelb);
        patPanel.add(age);
        patPanel.add(genderlb);
        patPanel.add(optionF);
        patPanel.add(optionM, "wrap");
        patPanel.add(latitudelb);
        patPanel.add(latitude);
        patPanel.add(longitudelb);
        patPanel.add(longitude ,"wrap");
        patPanel.add(orientation ,"wrap");
        patPanel.add(xlb);
        patPanel.add(x, "wrap");
        patPanel.add(ylb);
        patPanel.add(y, "wrap");
        patPanel.add(zlb);
        patPanel.add(z, "wrap");
        
        // Measure Panel
        TitledBorder borderMeasur = new TitledBorder("Monitoring Data");
        borderMeasur.setTitleJustification(TitledBorder.CENTER);
        borderMeasur.setTitlePosition(TitledBorder.TOP);
        measurPanel.setPreferredSize(new Dimension(580, 100));
        
        measurPanel.setBorder(borderMeasur);
        measurPanel.add(bloodPressurelb);
        measurPanel.add(pressure);
        measurPanel.add(temperaturelb);
        measurPanel.add(temperature, "wrap");
        measurPanel.add(heartRatelb);
        measurPanel.add(heartRate);
        measurPanel.add(bloodGlucoselb);
        measurPanel.add(glucose, "wrap");
        measurPanel.add(spo2lb);
        measurPanel.add(oxygen, "wrap");
        
        
        // Enviroment panel
        TitledBorder borderEnv = new TitledBorder("Enviroment Monitoring Data");
        borderEnv.setTitleJustification(TitledBorder.CENTER);
        borderEnv.setTitlePosition(TitledBorder.TOP);
        envPanel.setPreferredSize(new Dimension(580, 100));
        
        envPanel.setBorder(borderEnv);
        envPanel.add(envHumiditylb);
        envPanel.add(envHumidity);
        envPanel.add(envTemperaturelb);
        envPanel.add(envTemperature, "wrap");
        envPanel.add(envOxygenlb);
        envPanel.add(envOxygen);
        envPanel.add(envLuminositylb);
        envPanel.add(envLuminosity, "wrap");    
        
        bigpanel.add(patPanel, "wrap");
        bigpanel.add(measurPanel, "wrap");
        bigpanel.add(envPanel, "wrap");
        bigpanel.add(exportBt, "align center");
        //panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(bigpanel);
        
        setVisible(true);        
    }
    
    
    public void populateHashTable(){
        oidValues.clear();
        oidValues.put(Constants.usrName, name.getText());
        oidValues.put(Constants.usrAge, age.getText());
        oidValues.put(Constants.usrGender, optionF.isSelected() ? "F": "M");
        oidValues.put(Constants.usrLatitude, latitude.getText());
        oidValues.put(Constants.usrLongitude, longitude.getText());
        oidValues.put(Constants.usrOrientationX, x.getText());
        oidValues.put(Constants.usrOrientationY, y.getText());
        oidValues.put(Constants.usrOrientationZ, z.getText());
    
        oidValues.put(Constants.bdBloodGlucose, glucose.getText());
        oidValues.put(Constants.bdBloodOxygenSaturation, oxygen.getText());
        oidValues.put(Constants.bdBloodPressure, pressure.getText());
        oidValues.put(Constants.bdHeartRate, heartRate.getText());
        oidValues.put(Constants.bdTemperature, temperature.getText());
        
        oidValues.put(Constants.envHumidity, envHumidity.getText());
        oidValues.put(Constants.envOxygen, envOxygen.getText());
        oidValues.put(Constants.envTemperature, envTemperature.getText());
        oidValues.put(Constants.envLuminosity, envLuminosity.getText());
    
    }
    
    // Save file with hashtable information
    public void exportFile(){
        FileOutputStream fos = null;
        
        try {
            fos = new FileOutputStream("patientData.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(oidValues);
            oos.close();
            
            JOptionPane.showMessageDialog(bigpanel,
            "Exported data!",
            "Success",            
            JOptionPane.PLAIN_MESSAGE);
            
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(bigpanel,
            "Error exporting data " + ex.getMessage(),
            "Error",            
            JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(bigpanel,
            "Error exporting data " + ex.getMessage(),
            "Error", 
            JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                fos.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    
    }
    
}
