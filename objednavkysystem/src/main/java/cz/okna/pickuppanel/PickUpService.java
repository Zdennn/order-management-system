package cz.okna.pickuppanel;

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

public class PickUpService extends UnicastRemoteObject implements KlientService{

    private ServerService serverService;
    private int idKlienta;
    private PickUpFrame frame;

    protected void setFrame(PickUpFrame frame){
        this.frame = frame;
    }

    protected PickUpService() throws RemoteException {
        super();
        novyKlient();
    }

    @Override
    public void novyKlient() {
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

    protected Map<Integer, ArrayList<ProduktObjednavka>> ziskatObjednavky(int id){
        try {
            return serverService.ziskatObjednavkyPodleStavu(id);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected boolean zmenitStavObjednavky(int id, int stav){
        try {
            return serverService.zmenitStavObjednavky(id, stav);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void prijemProduktu(ArrayList<Produkt> produkty) throws RemoteException {

    }

    @Override
    public void prijemKategorii(ArrayList<Kategorie> kategorie) throws RemoteException {

    }

    @Override
    public void prijemObjednavek(Map<Integer, Map<Integer, ArrayList<ProduktObjednavka>>> objednavky) throws RemoteException {
        frame.nacti(objednavky);
    }
    
}
