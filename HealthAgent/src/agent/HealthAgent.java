package agent;

import agent.model.MibContainer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.percederberg.mibble.MibSymbol;
import net.percederberg.mibble.MibType;
import net.percederberg.mibble.MibValueSymbol;

import org.snmp4j.TransportMapping;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.agent.mo.snmp.RowStatus;
import org.snmp4j.agent.mo.snmp.SnmpCommunityMIB;
import org.snmp4j.agent.mo.snmp.StorageType;
import org.snmp4j.agent.mo.snmp.VacmMIB;
import org.snmp4j.agent.security.MutableVACM;
import org.snmp4j.agent.test.TestAgent;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.security.USM;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.transport.TransportMappings;
import utils.Constants;
import utils.Utils;

public class HealthAgent extends TestAgent {

    private static String address = null;
    private static final String CONF_FILE = "conf.agent";
    private static final String BOOT_COUNTER_FILE = "bootCounter.agent";
    public static InputStream simulation = null;
    public static HashMap simulationData = null;
    /* MUST NOT FORGET TO LOAD PARENT MIB FIRST (XYZCorp) */
    private static final String MIB_FILE = "../HUMAN-CARE-MIB.mib";
    /* MIB MANAGEMENT CONTAINER USING MIBBLE */
    private MibContainer mib = null;
    final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    /**
     *
     * @param address mib network address (localhost for tests)
     * @throws IOException
     */
    public HealthAgent(String address, InputStream simulation) throws IOException {
        super(new File(CONF_FILE), new File(BOOT_COUNTER_FILE));
        this.simulation = simulation;
        this.address = address;
        this.mib = new MibContainer(MIB_FILE);
        this.simulationData = new HashMap<OID, String>();
    }

    /**
     *
     * @param address mib network address (localhost for tests)
     * @throws IOException
     */
    public HealthAgent(String address) throws IOException {
        super(new File(CONF_FILE), new File(BOOT_COUNTER_FILE));
        this.address = address;
        this.mib = new MibContainer(MIB_FILE);
    }

    /**
     * Community mappings for loaded MIB (SNMPv2 based)
     *
     * @param communityMIB
     */
    @Override
    protected void addCommunities(SnmpCommunityMIB communityMIB) {
        Variable[] com2sec = new Variable[]{new OctetString("public"),
            new OctetString("cpublic"), // security name
            getAgent().getContextEngineID(),
            new OctetString("public"), // default context name
            new OctetString(),
            new Integer32(StorageType.nonVolatile), // storage type
            new Integer32(RowStatus.active) // row status
    };
        MOTableRow row = communityMIB.getSnmpCommunityEntry().createRow(
            new OctetString("public2public").toSubIndex(true), com2sec);
        communityMIB.getSnmpCommunityEntry().addRow((SnmpCommunityMIB.SnmpCommunityEntryRow) row);

    }

    /**
     * Adds initial VACM configuration.
     *
     * @param vacm
     */
    @Override
    protected void addViews(VacmMIB vacm) {
        vacm.addGroup(SecurityModel.SECURITY_MODEL_SNMPv2c, new OctetString(
            "cpublic"), new OctetString("v1v2group"),
            StorageType.nonVolatile);

        vacm.addAccess(new OctetString("v1v2group"), new OctetString("public"),
            SecurityModel.SECURITY_MODEL_ANY, SecurityLevel.NOAUTH_NOPRIV,
            MutableVACM.VACM_MATCH_EXACT, new OctetString("fullReadView"),
            new OctetString("fullWriteView"), new OctetString(
                "fullNotifyView"), StorageType.nonVolatile);

        vacm.addViewTreeFamily(new OctetString("fullReadView"), new OID("1.3"),
            new OctetString(), VacmMIB.vacmViewIncluded,
            StorageType.nonVolatile);

        vacm.addViewTreeFamily(new OctetString("fullWriteView"), new OID("1.3"),
            new OctetString(), VacmMIB.vacmViewIncluded,
            StorageType.nonVolatile);

    }

    /**
     * Method responsible for managed objects registration. Clients can call
     * this, but we only use it to load our MIB's MOs.
     *
     * @param mo
     */
    public void registerManagedObject(ManagedObject mo) {
        try {
            server.register(mo, null);
        } catch (DuplicateRegistrationException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Method responsible for unloading/removing an entry to a MO at our MIB.
     *
     * @param moGroup
     */
    public void unregisterManagedObject(MOGroup moGroup) {
        moGroup.unregisterMOs(server, getContext(moGroup));
    }

    /**
     * Method responsible for initializing the agent's transport mappings (where
     * the agent should listen to incoming SNMP polls, IP and PORT)
     *
     * @throws IOException
     */
    @Override
    protected void initTransportMappings() throws IOException {
        transportMappings = new TransportMapping[1];
        Address addr = GenericAddress.parse(this.address);
        TransportMapping tm = TransportMappings.getInstance()
            .createTransportMapping(addr);
        transportMappings[0] = tm;
    }

    /**
     * Main loop responsible for agent's MIB setup and executor that extracts
     * simulation info from a file.
     *
     * @throws IOException
     */
    public void start() throws IOException {
        init();
        addShutdownHook();
        getServer().addContext(new OctetString("public"));
        finishInit();
        // removing standard MIB that comes with TestAgent
        this.unregisterManagedObject(this.getSnmpv2MIB());
        run();
        sendColdStartNotification();

        //getting info from simulation file
        if (simulation != null) {

            this.executorService.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {

                    InputStream buffer = new BufferedInputStream(simulation);
                    ObjectInput input = null;
                    try {
                        input = new ObjectInputStream(buffer);
                    } catch (IOException ex) {
                        Logger.getLogger(HealthAgent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (input != null) {
                        generateMibData(buffer, input);
                    }
                }
            }, 0, 2, TimeUnit.MILLISECONDS
            );
        }
    }

    public MibContainer getMibContainer() {
        return this.mib;
    }

    /**
     * This method initializes the in-memory MIB at the agent.
     *
     */
    @Override
    protected void registerSnmpMIBs() {
        HashMap mibMappings = mib.getMibMappings();
        SortedSet<String> mibOids = mib.getOids();

        for (String oid : mibOids) {
            MibSymbol symbol = (MibSymbol) mibMappings.get(oid);
            MibType type = ((MibValueSymbol) symbol).getType();

            if (((MibValueSymbol) symbol).isScalar()) {
                mib.addScalar(symbol, type, this);
            }
        }
        this.mib.addSensorTable(this);
    }

    /**
     * Method responsible for getting info from simulation file and updating our
     * MO's values.
     *
     */
    private void generateMibData(InputStream buffer, ObjectInput input) {
        HashMap scalarMappings = mib.getScalarMappings();
        SortedSet<String> mibOids = mib.getOids();

        try {
            simulationData = (HashMap<OID, String>) input.readObject();
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Error reading sim data");
        } catch (IOException ex) {
            throw new RuntimeException("Error reading sim data");
        }

        Iterator entries = scalarMappings.entrySet().iterator();
        while (entries.hasNext()) {
            Entry thisEntry = (Entry) entries.next();
            OID oid = (OID) thisEntry.getKey();
            MOScalar smo = (MOScalar) thisEntry.getValue();

            if (oid.equals(Constants.bdBloodPressure)) {
                mib.updateScalar(smo, Utils.genGaugeScalarData(oid, simulationData));
            } else if (oid.equals(Constants.usrLatitude)
                || oid.equals(Constants.usrLongitude)) {
                mib.updateScalar(smo, Utils.genIntegerScalarData(oid, simulationData));
            } else if (oid.equals(Constants.usrOrientationX)
                || oid.equals(Constants.usrOrientationY)
                || oid.equals(Constants.usrOrientationZ)) {
                mib.updateScalar(smo, Utils.genIntegerScalarData(oid, simulationData));
            } else if (oid.equals(Constants.bdTemperature)) {
                mib.updateScalar(smo, Utils.genIntegerScalarData(oid, simulationData));
            } else if (oid.equals(Constants.bdHeartRate)) {
                mib.updateScalar(smo, Utils.genGaugeScalarData(oid, simulationData));
            } else if (oid.equals(Constants.bdHeartRhythmLeadI)
                || oid.equals(Constants.bdHeartRhythmLeadII)) {
                mib.updateScalar(smo, Utils.genGaugeScalarData(oid, simulationData));
            } else if (oid.equals(Constants.bdBloodGlucose)) {
                mib.updateScalar(smo, Utils.genGaugeScalarData(oid, simulationData));
            } else if (oid.equals(Constants.bdBloodOxygenSaturation)) {
                mib.updateScalar(smo, Utils.genGaugeScalarData(oid, simulationData));
            } else if (oid.equals(Constants.envHumidity)) {
                mib.updateScalar(smo, Utils.genGaugeScalarData(oid, simulationData));
            } else if (oid.equals(Constants.envTemperature)) {
                mib.updateScalar(smo, Utils.genIntegerScalarData(oid, simulationData));
            } else if (oid.equals(Constants.envLuminosity)) {
                mib.updateScalar(smo, Utils.genGaugeScalarData(oid, simulationData));
            } else if (oid.equals(Constants.envOxygen)) {
                mib.updateScalar(smo, Utils.genGaugeScalarData(oid, simulationData));
            }
        }
    }
}
