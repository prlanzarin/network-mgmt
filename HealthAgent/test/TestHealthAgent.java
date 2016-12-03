import agent.HealthAgent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import utils.Constants;

public class TestHealthAgent {

    public static void main(String[] args) throws IOException {
        TestHealthAgent client = new TestHealthAgent("udp:127.0.0.1/161");
        client.init();
    }

    HealthAgent agent = null;
    /**
     * This is the client which we have created earlier
     */
    SNMPManager client = null;

    String address = null;

    /**
     * Constructor
     *
     * @param add
     */
    public TestHealthAgent(String add) {
        address = add;
    }

    private void init() throws IOException {
        InputStream simulation = new FileInputStream("/home/prlanzarin/github/network-mgmt/PatientMonitorSimulator/patientData.txt");
        agent = new HealthAgent("udp:0.0.0.0/2001", simulation);
        agent.start();

        System.out.println("This agent is running at address");
    }

}
