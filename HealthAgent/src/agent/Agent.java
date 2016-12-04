package agent;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class Agent {

    private static HealthAgent agent = null;
    private static String address = null;

    public static void main(String[] args) throws IOException {
        String ip =  null;
        Integer port = null;
        if (args.length < 2) {
            System.out.println("Lack of arguments. java -jar agent <ip> <port>. "
                + "The HUMAN-CARE-MIB.mib and patientData.txt " 
                + "files must be within the same directory as this jar. Unexpected behaviour will "
                + "occur otherwise.");
            System.exit(1);
        }
        
        if (args[0].equals("-h")) {
            System.out.println("java -jar agent <ip> <port>. The HUMAN-CARE-MIB.mib and patientData.txt"
                + "files must be within the same directory as this jar. Unexpected behaviour will"
                + "occur otherwise.");
        }
        
        try {
            ip = args[0];
            port = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Argument" + args[1] + " must be an integer.");
            System.exit(1);
        }
        InputStream simulation = null;
        try {
            simulation = new FileInputStream("/home/amanda/NetBeansProjects/network-mgmt/PatientMonitorSimulator/patientData.txt");
        } catch (FileNotFoundException e) {
            System.out.println("File patientData.txt was not found on the same directory as this .jar");
            System.exit(1);
        }
        if(ip == null | port == null) {
            System.out.println("Could not parse IP and Port");
            System.exit(1);
        }
        address = "udp:" + ip + "/" + port.toString();
       
        agent = new HealthAgent(address, simulation);
        agent.start();

        System.out.println("This agent is running at " + address);
    }

}
