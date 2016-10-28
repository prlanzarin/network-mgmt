package agent.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;

import net.percederberg.mibble.Mib;
import net.percederberg.mibble.MibLoader;
import net.percederberg.mibble.MibLoaderException;
import net.percederberg.mibble.MibSymbol;
import net.percederberg.mibble.MibValue;
import net.percederberg.mibble.MibValueSymbol;
import net.percederberg.mibble.value.ObjectIdentifierValue;

public class MibContainer {

    private Mib mib = null;
    private HashMap mibMappings = null;
    private File mibFile = null;
    private static final Logger logger = Logger.getLogger(MibContainer.class);

    public MibContainer(String mibName) {
        this.mibFile =new File(mibName);
        
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
    }

    private Mib loadMib(File file)
        throws FileNotFoundException, MibLoaderException, IOException {

        MibLoader loader = new MibLoader();

        loader.addDir(file.getParentFile());
        return loader.load(file);
    }

    private HashMap extractOids(Mib mib) {
        HashMap map = new HashMap();
        Iterator iter = mib.getAllSymbols().iterator();
        MibSymbol symbol;
        MibValue value;

        while (iter.hasNext()) {
            symbol = (MibSymbol) iter.next();
            value = extractOid(symbol);
            if (value != null) {
                map.put(symbol.getName(), value);
            }
        }
        return map;
    }

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
     * @param mibMappings the mibMappings to set
     */
    public final void setMibMappings(HashMap mibMappings) {
        this.mibMappings = mibMappings;
    }
}
