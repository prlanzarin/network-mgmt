package agent.model;

import agent.HealthAgent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import net.percederberg.mibble.Mib;
import net.percederberg.mibble.MibLoader;
import net.percederberg.mibble.MibLoaderException;
import net.percederberg.mibble.MibSymbol;
import net.percederberg.mibble.MibType;
import net.percederberg.mibble.MibValue;
import net.percederberg.mibble.MibValueSymbol;
import net.percederberg.mibble.snmp.SnmpAccess;
import net.percederberg.mibble.snmp.SnmpObjectType;
import net.percederberg.mibble.value.ObjectIdentifierValue;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.agent.mo.MOTable;
import org.snmp4j.smi.Gauge32;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.SMIConstants;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import utils.Constants;
import utils.Utils;

public class MibContainer {

    private Mib mib;
    private HashMap mibMappings;
    private HashMap<OID, MOScalar> scalarMappings;
    private MOTable sensorTable;
    private SortedSet<String> oids;
    private File mibFile;
    private static final Logger logger = Logger.getLogger(MibContainer.class);

    public MibContainer(String mibName) {
        this.oids = null;
        this.scalarMappings = new HashMap();
        this.sensorTable = null;
        this.mibFile = new File(mibName);

        try {
            this.setMib(loadMib(this.mibFile));
        } catch (FileNotFoundException e) {
            logger.error("MIB file could not be found");
            throw new RuntimeException(e);
        } catch (MibLoaderException | IOException e) {
            logger.error("MIB loading failed");
            throw new RuntimeException(e);
        }

        this.setMibMappings(extractOids(this.mib));
        this.oids = new TreeSet<>(mibMappings.keySet());
        System.out.println(oids);
    }

    /**
     * Does the MIB loading using Mibble; we use this library to automagically
     * extract/parse OID info so we can load it on a more general way.
     *
     * @param file MIB (.mib) file
     * @return Mib
     * @throws FileNotFoundException
     * @throws MibLoaderException
     * @throws IOException
     */
    private Mib loadMib(File file)
        throws FileNotFoundException, MibLoaderException, IOException {

        MibLoader loader = new MibLoader();

        loader.addDir(file.getParentFile());
        return loader.load(file);
    }

    /**
     * Method which takes a loaded MIB and generates a HashMap of OID ->
     * MibSymbol
     *
     * @param mib
     * @return HashMap
     * @throws IOException
     */
    private HashMap extractOids(Mib mib) {
        HashMap map = new HashMap();
        Iterator iter = mib.getAllSymbols().iterator();
        MibSymbol symbol;
        MibValue value;

        while (iter.hasNext()) {
            symbol = (MibSymbol) iter.next();
            value = extractOid(symbol);
            if (value != null) {
                map.put(value.toString(), symbol);
            }
        }
        return map;
    }

    /**
     * Method which takes a MibSymbol and extracts its ObjectIndentifierValue
     *
     * @param symbol
     * @return ObjectIdentifierValue
     * @throws IOException
     */
    private ObjectIdentifierValue extractOid(MibSymbol symbol) {
        MibValue value;

        if (symbol instanceof MibValueSymbol) {
            value = ((MibValueSymbol) symbol).getValue();
            if (value instanceof ObjectIdentifierValue) {
                return (ObjectIdentifierValue) value;
            }
        }
        return null;
    }

    /**
     * @return the MIB
     */
    public final Mib getMib() {
        return mib;
    }

    /**
     * @param mib the MIB to set
     */
    public final void setMib(Mib mib) {
        this.mib = mib;
    }

    /**
     * @return the mibMappings
     */
    public final HashMap getMibMappings() {
        return mibMappings;
    }

    /**
     * @return the scalar MOs mappings
     */
    public final HashMap getScalarMappings() {
        return scalarMappings;
    }

    /**
     * @return sorted set of OIDs
     */
    public final SortedSet<String> getOids() {
        return oids;
    }

    /**
     * @param mibMappings the mibMappings to set
     */
    public final void setMibMappings(HashMap mibMappings) {
        this.mibMappings = mibMappings;
    }

    /**
     * Method responsible for adding/loading an scalar managed object to our
     * loaded Mib; it uses our Scalar MO factory to do so.
     *
     * @param symbol MibSymbol
     * @param type MibType ASN.1 type
     * @param agent HealthAgent agent
     */
    public void addScalar(MibSymbol symbol, MibType type, HealthAgent agent) {
        MibType syntax = ((SnmpObjectType) type).getSyntax();
        SnmpAccess mode = ((SnmpObjectType) type).getAccess();

        OID mOid = Utils.extractSnmp4jOid(symbol);
        MOScalar smo = ScalarMOCreator.create(mOid,
            null, syntax.getName(), Utils.extractSnmp4jMode(mode));
        agent.registerManagedObject(smo);
        scalarMappings.put(mOid, smo);
    }

    /**
     * Method responsible for loading and populating our sensor table with
     * pre-defined information.
     *
     * @param agent
     */
    public void addSensorTable(HealthAgent agent) {
        TableMOCreator sensorTableFactory = new TableMOCreator(Constants.hcSensorEntry)
            .addColumnType(SMIConstants.SYNTAX_INTEGER, MOAccessImpl.ACCESS_READ_ONLY)
            .addColumnType(SMIConstants.SYNTAX_INTEGER, MOAccessImpl.ACCESS_READ_ONLY)
            .addColumnType(SMIConstants.SYNTAX_OCTET_STRING, MOAccessImpl.ACCESS_READ_ONLY)
            .addColumnType(SMIConstants.SYNTAX_GAUGE32, MOAccessImpl.ACCESS_READ_ONLY)
            .addColumnType(SMIConstants.SYNTAX_INTEGER, MOAccessImpl.ACCESS_READ_ONLY)
            // sensor x
            .addRowValue(new Integer32(1))
            .addRowValue(new Integer32(5))
            .addRowValue(new OctetString(""))
            .addRowValue(new Gauge32(10000000))
            .addRowValue(new Integer32(1500))
            // sensor y
            .addRowValue(new Integer32(2))
            .addRowValue(new Integer32(4))
            .addRowValue(new OctetString(""))
            .addRowValue(new Gauge32(10000000))
            .addRowValue(new Integer32(1500));
        //agent.registerManagedObject(new ManagedObject);
        this.sensorTable = sensorTableFactory.build();
        agent.registerManagedObject(this.sensorTable);
    }

    /**
     * Method responsible for updating a registered MO instance value.
     *
     * @param smo
     * @param value
     */
    public void updateScalar(MOScalar smo, Variable value) {
        smo.setValue(value);
    }

    public void updateTableValue(OID oid, VariableBinding vb) {
        sensorTable.setValue(vb);
    }

    private void printMibMappings(HashMap mibMappings) {
        for (Object name : mibMappings.keySet()) {
            String key = name.toString();
            String value = mibMappings.get(name).toString();
            System.out.println(key + " =====> " + value);
        }
    }
}
