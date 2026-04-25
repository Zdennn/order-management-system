package cz.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Server {
    public static void main(String[] args) throws Exception {
        try {
            ServerServiceImpl server = new ServerServiceImpl();
            LocateRegistry.createRegistry(42069).rebind("system", server);

            System.out.println("Server běží...");
        } catch (RemoteException e) {
            System.out.println("Chyba při spouštění serveru.");
        }
    }
}
