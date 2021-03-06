package agent;

import static agent.HealthAgent.simulation;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Agent {

    private static HealthAgent agent = null;
    private static String address = null;

    public static void main(String[] args) throws IOException {
        String ip = null;
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
            simulation = new FileInputStream("/home/prlanzarin/github/network-mgmt/PatientMonitorSimulator/patientData.txt");
        } catch (FileNotFoundException e) {
            System.out.println("File patientData.txt was not found on the same directory as this .jar");
            System.exit(1);
        }
        if (ip == null | port == null) {
            System.out.println("Could not parse IP and Port");
            System.exit(1);
        }
        address = "udp:" + ip + "/" + port.toString();

        agent = new HealthAgent(address, simulation);
        agent.start();

        System.out.println("This agent is running at " + address);

        //getting info from simulation file
        if (simulation != null) {
            while (true) {
                try {
                    simulation = new FileInputStream("/home/prlanzarin/github/network-mgmt/PatientMonitorSimulator/patientData.txt");
                } catch (FileNotFoundException e) {
                    System.out.println("File patientData.txt was not found on the same directory as this .jar");
                    System.exit(1);
                }
                InputStream buffer = new BufferedInputStream(simulation);
                ObjectInput input = null;
                try {
                    input = new ObjectInputStream(buffer);
                } catch (IOException ex) {
                    Logger.getLogger(HealthAgent.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (input != null) {
                    agent.generateMibData(buffer, input);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
