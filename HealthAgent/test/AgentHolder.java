import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import utils.Constants;

public class AgentHolder {

    private final HashMap<String, SNMPManager> clients;

    String address = null;

    public AgentHolder() {
        clients = new HashMap<String, SNMPManager>();
    }

    public SNMPManager newAgent(String address) {
        SNMPManager newClient = new SNMPManager(address);
        clients.put(address, newClient);
        return newClient;
    }

    public SNMPManager getClientFromAddress(String address) {
        return clients.get(address);
    }

    public SNMPManager getClientFromUsername(String userName) {
        for (Map.Entry thisEntry : clients.entrySet()) {
            SNMPManager client = (SNMPManager) thisEntry.getValue();
            try {
                if (client.getAsString(Constants.usrName).equals(userName))
                    return client;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}