import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Map;

import cz.common.NovyKlientResponse;
import cz.common.model.Kategorie;
import cz.common.model.Objednavka;
import cz.common.model.Produkt;
import cz.common.model.ProduktObjednavka;
import cz.common.services.KlientService;
import cz.common.services.ServerService;

public class OrderService extends UnicastRemoteObject implements KlientService {

    private ServerService serverService;
    private int idKlienta;

    protected OrderService() throws RemoteException {
        super();
    }

    @Override
    public void novyKlient() throws RemoteException {
         try {
            ServerService server = (ServerService) Naming.lookup("rmi://localhost:42069/system");
            NovyKlientResponse klres = server.novyKlient(this);
            this.idKlienta = klres.getId();
            this.serverService = klres.getServer();
            System.out.println("pripojen klinet č: " + idKlienta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void odpojitKlienta() {
        try {
            serverService.odpojitKlienta(idKlienta);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Produkt> ziskatProdukty() {
        try {
            return serverService.ziskatVsechnyProdukty();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }
    public ArrayList<Kategorie> ziskatKategorie() {
        try {
            return serverService.ziskatVsechnyKategorie();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void vytvoritObjednavku(Objednavka objednavka) {
        try {
            serverService.vytvoritObjednavku(objednavka);;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void prijemKategorii(ArrayList<Kategorie> arg0) throws RemoteException {

    }

    @Override
    public void prijemObjednavek(Map<Integer, Map<Integer, ArrayList<ProduktObjednavka>>> arg0) throws RemoteException {

    }

    @Override
    public void prijemProduktu(ArrayList<Produkt> arg0) throws RemoteException {

    }

}
