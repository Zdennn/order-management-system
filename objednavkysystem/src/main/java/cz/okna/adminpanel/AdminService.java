package cz.okna.adminpanel;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Map;

import cz.common.NovyKlientResponse;
import cz.common.model.Kategorie;
import cz.common.model.Produkt;
import cz.common.model.ProduktObjednavka;
import cz.common.services.KlientService;
import cz.common.services.ServerService;

public class AdminService extends UnicastRemoteObject implements KlientService {

    private ServerService serverService;
    private int id;

    protected AdminService() throws RemoteException {
        super();
        novyKlient();
    }

    @Override
    public void novyKlient() {
        try {
            ServerService server = (ServerService) Naming.lookup("rmi://localhost:42069/system");
            NovyKlientResponse klres = server.novyKlient(this);
            this.id = klres.getId();
            this.serverService = klres.getServer();
            System.out.println("pripojen klinet č: " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void odpojitKlienta() {
        try {
            serverService.odpojitKlienta(id);
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

    public boolean vytvoritProdukt(Produkt produkt) {
        try {
            return serverService.vytvoritProdukt(produkt);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateProdukt(Produkt produkt) {
        try {
            return serverService.updateProdukt(produkt);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
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

    public boolean vytvoritKategorii(Kategorie kategorie) {
        try {
            return serverService.vytvoritKategorii(kategorie);
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void prijemProduktu(ArrayList<Produkt> produkty) throws RemoteException {
        
    }
    @Override
    public void prijemKategorii(ArrayList<Kategorie> kategorie) throws RemoteException {
        
    }
    @Override
    public void prijemObjednavek(Map<Integer, Map<Integer, ArrayList<ProduktObjednavka>>> objednavky) throws RemoteException {
        
    }

}
