package agent;

import agent.model.MibContainer;
import agent.model.ScalarMOCreator;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.SortedSet;
import net.percederberg.mibble.MibSymbol;
import net.percederberg.mibble.MibType;
import net.percederberg.mibble.MibValueSymbol;
import net.percederberg.mibble.snmp.SnmpAccess;
import net.percederberg.mibble.snmp.SnmpObjectType;

import org.snmp4j.TransportMapping;
import org.snmp4j.agent.BaseAgent;
import org.snmp4j.agent.CommandProcessor;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.MOGroup;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.agent.mo.snmp.RowStatus;
import org.snmp4j.agent.mo.snmp.SnmpCommunityMIB;
import org.snmp4j.agent.mo.snmp.SnmpNotificationMIB;
import org.snmp4j.agent.mo.snmp.SnmpTargetMIB;
import org.snmp4j.agent.mo.snmp.StorageType;
import org.snmp4j.agent.mo.snmp.VacmMIB;
import org.snmp4j.agent.security.MutableVACM;
import org.snmp4j.mp.MPv3;
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
import utils.Utils;

public class HealthAgent extends BaseAgent {

    private final String address;
    private static final String CONF_FILE = "conf.agent";
    private static final String BOOT_COUNTER_FILE = "bootCounter.agent";
    /* MUST NOT FORGET TO LOAD PARENT MIB FIRST (XYZCorp) */
    private static final String MIB_FILE = "../HUMAN-CARE-MIB.mib";
    /* MIB MANAGEMENT CONTAINER USING MIBBLE */
    private MibContainer mib = null;

    /**
     *
     * @param address mib network address (localhost for tests)
     * @throws IOException
     */
    public HealthAgent(String address) throws IOException {
        super(new File(CONF_FILE), new File(BOOT_COUNTER_FILE),
            new CommandProcessor(new OctetString(MPv3.createLocalEngineID())));
        this.address = address;
        this.mib = new MibContainer(MIB_FILE);
    }

    /**
     * Adds community to security name mappings needed for SNMPv1 and SNMPv2c.
     * @param communityMIB
     */
    @Override
    protected void addCommunities(SnmpCommunityMIB communityMIB) {
        Variable[] com2sec = new Variable[]{new OctetString("public"),
            new OctetString("cpublic"), // security name
            getAgent().getContextEngineID(), // local engine ID
            new OctetString("public"), // default context name
            new OctetString(), // transport tag
            new Integer32(StorageType.nonVolatile), // storage type
            new Integer32(RowStatus.active) // row status
    };
        MOTableRow row = communityMIB.getSnmpCommunityEntry().createRow(
            new OctetString("public2public").toSubIndex(true), com2sec);
        communityMIB.getSnmpCommunityEntry().addRow((SnmpCommunityMIB.SnmpCommunityEntryRow) row);

    }

    /**
     * Adds initial notification targets and filters.
     * @param arg0
     * @param arg1
     */
    @Override
    protected void addNotificationTargets(SnmpTargetMIB arg0,
        SnmpNotificationMIB arg1) {
        // TODO Auto-generated method stub

    }

    /**
     * Adds all the necessary initial users to the USM.
     * @param arg0
     */
    @Override
    protected void addUsmUser(USM arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * Adds initial VACM configuration.
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

    }

    /**
     * Unregister the basic MIB modules from the agent's MOServer.
     */
    @Override
    protected void unregisterManagedObjects() {

    }

    /**
     * Register additional managed objects at the agent's server.
     */
    @Override
    protected void registerManagedObjects() {
        // TODO Auto-generated method stub

    }

    /**
     * Clients can register the MO they need
     * @param mo
     */
    public void registerManagedObject(ManagedObject mo) {
        try {
            server.register(mo, null);
        } catch (DuplicateRegistrationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void unregisterManagedObject(MOGroup moGroup) {
        moGroup.unregisterMOs(server, getContext(moGroup));
    }

    protected void initTransportMappings() throws IOException {
        transportMappings = new TransportMapping[1];
        Address addr = GenericAddress.parse(address);
        TransportMapping tm = TransportMappings.getInstance()
            .createTransportMapping(addr);
        transportMappings[0] = tm;
    }

    /**
     * Start method invokes some initialization methods needed to start the
     * agent
     *
     * @throws IOException
     */
    public void start() throws IOException {

        init();
        addShutdownHook();
        getServer().addContext(new OctetString("public"));
        finishInit();
        run();
        sendColdStartNotification();
        // Since BaseAgent registers some MIBs by default we need to unregister
        // one before we register our own sysDescr. Normally you would
        // override that method and register the MIBs that you need
        this.unregisterManagedObject(this.getSnmpv2MIB());
        this.setUp();
    }
    
    public MibContainer getMibContainer() {
        return this.mib;
    }
    
    /**
     * This method initializes the in-memory MIB at the agent
     * 
     */
    public void setUp() {
        HashMap mibMappings = mib.getMibMappings();
        SortedSet<String> mibOids = mib.getOids();
        
        for (String oid : mibOids) {
            MibSymbol symbol = (MibSymbol) mibMappings.get(oid);
            MibType type = ((MibValueSymbol) symbol).getType();

            if (((MibValueSymbol) symbol).isScalar()) {
                MibType syntax = ((SnmpObjectType) type).getSyntax();
                SnmpAccess mode = ((SnmpObjectType) type).getAccess();

                System.out.println("OBJECT TYPE (SCALAR) => " + syntax.getName()
                    + " OID => " + Utils.extractSnmp4jOid(symbol) + " MODE => " + mode.toString());

                MOScalar smo = ScalarMOCreator.create(Utils.extractSnmp4jOid(symbol),
                    null, syntax.getName(), Utils.extractSnmp4jMode(mode));
                this.registerManagedObject(smo);
            } else if (((MibValueSymbol) symbol).isTable()) {
                MibType syntax = ((SnmpObjectType) type).getSyntax();
                System.out.println("OBJECT TYPE (TABLE) => " + syntax.getName());
            } else if (((MibValueSymbol) symbol).isTableColumn()) {
                MibType syntax = ((SnmpObjectType) type).getSyntax();
                System.out.println("OBJECT TYPE (CLMN) => " + syntax.getName());
            } else if (((MibValueSymbol) symbol).isTableRow()) {
                MibType syntax = ((SnmpObjectType) type).getSyntax();
                System.out.println("OBJECT TYPE (ROW) => " + syntax.getName());
            } else {
                System.out.println("OBJECT TYPE => " + ((MibValueSymbol) symbol).getName());
            }
        }
    }
}
