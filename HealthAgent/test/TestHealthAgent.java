import agent.HealthAgent;
import java.io.IOException;

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
        agent = new HealthAgent("udp:0.0.0.0/2001");
        agent.start();

        System.out.println("This agent is surely running, boyo!");
        // Setup the client to use our newly started agent
        client = new SNMPManager("udp:127.0.0.1/2001");
        // Set value
        //System.out.println(client.set(Constants.usrName, new OctetString("Michel Temer")).getResponse());
        // Get back Value which is set
        //System.out.println("GET: " + client.getAsString(Constants.usrName));
        System.out.println("GETBULK: " + client.getBulk(Constants.hcSensorEntry, 11));
    }

}
