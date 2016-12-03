import java.io.IOException;
import java.util.Vector;

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
 * This is a test class in which we developed a simple manager to test our agent.
 * 
 */
public class SNMPManager {

    private static final TransportMapping transport;
    private static final Snmp snmp;

    static {
        try {
            transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            transport.listen();
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize SNMP transport mapping!");
        }
    }

    String address = null;
    private int reqNumber = 1;

    public SNMPManager(String add) {
        address = add;
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
        System.out.println(event.getResponse().toString());
        return event.getResponse().get(0).getVariable().toString();
    }

    /**
     * This method is capable of handling multiple OIDs and generates get PDUs
     * for them
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
        pdu.setRequestID(new Integer32(reqNumber));
        reqNumber++;
        ResponseEvent event = snmp.get(pdu, getTarget("public"));
        if (event != null) {
            return event;
        }
        throw new RuntimeException("GET timed out");
    }

    /**
     * Generates a set request for the given OID
     *
     * @param oid
     * @param var
     * @return
     * @throws IOException
     */
    public ResponseEvent set(OID oid, Variable var) throws IOException {
        ResponseEvent event = null;
        PDU pdu = new PDU();
        VariableBinding varBind = new VariableBinding(oid, var);
        pdu.add(varBind);
        pdu.setType(PDU.SET);
        pdu.setRequestID(new Integer32(reqNumber));
        reqNumber++;
        event = snmp.set(pdu, getTarget("public"));

        if (event != null) {
            System.out.println("SET PDU for " + oid + " with " + var);
            return event;
        }
        throw new RuntimeException("SET_TIMEOUT");
    }

    public ResponseEvent getBulk(OID oid, int maxRep) throws IOException {
        PDU pdu = new PDU();
        pdu.setType(PDU.GETBULK);

        pdu.add(new VariableBinding(new OID(oid)));
        pdu.setMaxRepetitions(maxRep);
        ResponseEvent response = snmp.getBulk(pdu, getTarget("public"));
        if (response != null) {
            if (response.getResponse().getErrorStatusText().equalsIgnoreCase("Success")) {
                PDU pduresponse = response.getResponse();
                Vector vec = pduresponse.getVariableBindings();
                for (int i = 0; i < vec.size(); i++) {
                    VariableBinding vb = null;
                    vb = (VariableBinding) vec.elementAt(i);
                    System.out.println(i + " ---- " + vb.toString());
                }
            }
            return response;
        } else {
            throw new RuntimeException("GETBULK timed out");
        }
    }

    /**
     * This method returns a Target, which contains information about where the
     * data should be fetched and how.
     *
     * @return
     */
    private Target getTarget(String community) {
        Address targetAddress = GenericAddress.parse(address);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(community));
        target.setAddress(targetAddress);
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version2c);
        return target;
    }

    public String getAddress() {
        return this.address;
    }
}
