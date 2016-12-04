package agent.model;

import org.snmp4j.agent.MOAccess;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;

/**
 * Factory class for generating scalar managed objects so they can be loaded at
 * a MIB.
 *
 */
public final class ScalarMOCreator {

    /**
     * Creates a new scalar managed object instance value.
     *
     * @param oid Object ID
     * @param value Instance value
     * @param type ASN.1 type
     * @param mode r/rw/read-only
     * @return
     */
    public static MOScalar create(OID oid, Object value, String type, MOAccess mode) {
        return new MOScalar(new OID(oid.toString()), mode, getVariable(value, type));
    }

    /**
     * Auxiliary method that generates a SNPM4J Variable for each specific SNMP
     * type
     *
     * @param value
     * @param type
     * @return
     */
    public static Variable getVariable(Object value, String type) {
        switch (type) {
            case "OCTET STRING":
                return new OctetString(value != null ? "" : "");
            case "INTEGER":
                int intValue = 0;
                if(value != null && !value.equals(""))
                    intValue = Integer.parseInt(value.toString());
                return new Integer32(intValue);
            case "Gauge":
                long longValue = 0;
                if(value != null && !value.equals(""))
                    longValue = Long.parseLong(value.toString());
                return new Gauge32(longValue);
            default:
                return null;
        }
    }
}
