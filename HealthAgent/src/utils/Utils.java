/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import net.percederberg.mibble.MibSymbol;
import net.percederberg.mibble.MibValue;
import net.percederberg.mibble.MibValueSymbol;
import net.percederberg.mibble.snmp.SnmpAccess;
import net.percederberg.mibble.value.ObjectIdentifierValue;
import org.snmp4j.agent.MOAccess;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;

public final class Utils {
    
    
        /**
     * Method which takes a MibSymbol and extracts its @OID
     *
     * @param symbol
     * @return ObjectIdentifierValue
     */
    public static final OID extractSnmp4jOid(MibSymbol symbol) {
        MibValue value;
        String oidString;

        if (symbol instanceof MibValueSymbol) {
            value = ((MibValueSymbol) symbol).getValue();
            if (value instanceof ObjectIdentifierValue) {
               oidString = ((ObjectIdentifierValue) value).toString();
               return Constants.MONames.get(oidString);
            }
        }
        return null;
    }
    
    public static final MOAccess extractSnmp4jMode(SnmpAccess mode) {
        switch(mode.toString()) {
            case "read-only":
                return MOAccessImpl.ACCESS_READ_ONLY;
            case "read-write":
                return MOAccessImpl.ACCESS_READ_WRITE;
            case "write-only":
                return MOAccessImpl.ACCESS_WRITE_ONLY;
            case "read-create":
                return MOAccessImpl.ACCESS_READ_CREATE;
            default:
                return null;
        }
    }
    
    Integer32 fakeIntegerData(OID oid) {
        return new Integer32(3);
    }
    
    Gauge32 fakeGaugeData(OID oid) {
        return new Gauge32(3);
    }
}
