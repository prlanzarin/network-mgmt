package agent.model;

import org.snmp4j.agent.MOAccess;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

public class ScalarMOCreator {

    public static MOScalar create(OID oid, Object value, String type, MOAccess mode) {
        return new MOScalar(new OID(oid.toString()), mode, getVariable(value, type));
    }

    private static Variable getVariable(Object value, String type) {
        switch (type) {
            case "OCTET STRING":
                return new OctetString(value != null ? "" : "");
            case "INTEGER":
                return new Integer32(value != null ? (Integer) value : 0);
            case "Gauge":
                return new Gauge32(value != null ? (Integer) value : 0);
            default:
                return null;

        }
    }
}
