package utils;

import net.percederberg.mibble.MibSymbol;
import net.percederberg.mibble.MibValue;
import net.percederberg.mibble.MibValueSymbol;
import net.percederberg.mibble.snmp.SnmpAccess;
import net.percederberg.mibble.value.ObjectIdentifierValue;
import org.snmp4j.agent.MOAccess;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.Variable;

public final class Utils {

    /**
     * Method which takes a MibSymbol and extracts its OID
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

    /**
     * Auxiliary method for converting Mibble access modes to SNMP4J access modes.
     * @param mode SnmpAccess mibble
     * @return MOAccess snmp4j
     */
    public static final MOAccess extractSnmp4jMode(SnmpAccess mode) {
        switch (mode.toString()) {
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

    public static Variable genAccelerometerData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static Variable genEnvOxygenData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static Variable genLumData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static Variable genEnvTempData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static Variable genHumidityData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static Variable genSaturationData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static Variable genGlucoseData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static Variable genLeadData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static Variable genRateData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static Variable genBdTemperatureData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static Variable genBloodPressureData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public static Variable genLocationData() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}