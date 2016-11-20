


import agent.HealthAgent;
import agent.model.MibContainer;
import agent.model.ScalarMOCreator;
import java.io.IOException;
import java.util.HashMap;
import java.util.SortedSet;
import net.percederberg.mibble.MibSymbol;
import net.percederberg.mibble.MibType;
import net.percederberg.mibble.MibValueSymbol;
import net.percederberg.mibble.snmp.SnmpAccess;
import net.percederberg.mibble.snmp.SnmpObjectType;
import org.snmp4j.PDU;
import org.snmp4j.agent.BaseAgent;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.event.ResponseEvent;

import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import utils.Constants;
import utils.Utils;

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
        
        // Setup the client to use our newly started agent
        client = new SNMPManager("udp:127.0.0.1/2001");
        client.start();
        // Set value
        System.out.println(client.set(Constants.usrName, new OctetString("Michel Temer")).getResponse());
        // Get back Value which is set
        System.out.println("GET: " + client.getAsString(Constants.usrName));
    }

}
