


import java.io.IOException;

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
import utils.Constants;

public class SNMPManager {

    Snmp snmp = null;
    String address = null;
    private int reqNumber = 1;

    public SNMPManager(String add) {
        address = add;
    }

    public static void main(String[] args) throws IOException {
        /**
         * Port 161 is used for Read and Other operations Port 162 is used for
         * the trap generation
         */
        SNMPManager client = new SNMPManager("udp:127.0.0.1/161");
        client.start();
        
        String usrName = client.getAsString(Constants.usrName);
        System.out.println(usrName);
    }

    /**
     * Start the Snmp session. If you forget the listen() method you will not
     * get any answers because the communication is asynchronous and the
     * listen() method listens for answers.
     *
     * @throws IOException
     */
    void start() throws IOException {
        TransportMapping transport = new DefaultUdpTransportMapping();
        snmp = new Snmp(transport);
// Do not forget this line!
        transport.listen();
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
     * This method is capable of handling multiple OIDs
     * and generates get PDUs for them
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
}
