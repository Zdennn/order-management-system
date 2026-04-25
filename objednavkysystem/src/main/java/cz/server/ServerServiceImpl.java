package cz.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cz.common.NovyKlientResponse;
import cz.common.db.SqlMethods;
import cz.common.model.Kategorie;
import cz.common.model.Objednavka;
import cz.common.model.Produkt;
import cz.common.model.ProduktObjednavka;
import cz.common.services.KlientService;
import cz.common.services.ServerService;

public class ServerServiceImpl extends UnicastRemoteObject implements ServerService {

    private Map<Integer, KlientService> klienti = new HashMap<>();
    private SqlMethods sql;

    protected ServerServiceImpl() throws RemoteException {
        super();
        klienti = new HashMap<>();
        this.sql = new SqlMethods();
    }

    @Override
    synchronized public NovyKlientResponse novyKlient(KlientService klientService) throws RemoteException {
        int id = klienti.size();
        klienti.put(id, klientService);
        NovyKlientResponse klres = new NovyKlientResponse(id, this);
        System.out.println("Připojil se klient č: " + id);
        return klres;
    }

    @Override
    synchronized public void odpojitKlienta(int id) throws RemoteException {
        klienti.remove(id);
        System.out.println("Odpojil se klient č: " + id);
    }

    @Override
    synchronized public void rozeslatProdukty() throws RemoteException {
        for (KlientService klientService : klienti.values()) {
            klientService.prijemProduktu(ziskatVsechnyProdukty());
        }
    }

    @Override
    synchronized public void rozeslatKategorie() throws RemoteException {
        for (KlientService klientService : klienti.values()) {
            klientService.prijemKategorii(ziskatVsechnyKategorie());
        }
    }

    @Override
    synchronized public void rozeslatObjednavky() throws RemoteException {
        Map<Integer, ArrayList<ProduktObjednavka>> objednavky1 = ziskatObjednavkyPodleStavu(1);
        Map<Integer, ArrayList<ProduktObjednavka>> objednavky2 = ziskatObjednavkyPodleStavu(2);
        Map<Integer, Map<Integer, ArrayList<ProduktObjednavka>>> objednavky = new HashMap();
        objednavky.put(1, objednavky1);
        objednavky.put(2, objednavky2);

        for (KlientService klientService : klienti.values()) {
            new Thread(() -> {
                try {
                    klientService.prijemObjednavek(objednavky);
                } catch (RemoteException e) {
                    System.err.println("Chyba při notifikaci klienta: " + e.getMessage());
                }
            }).start();
        }
    }

    @Override
    synchronized public ArrayList<Produkt> ziskatVsechnyProdukty() throws RemoteException {
        return sql.nacistProdukty();
    }

    @Override
    synchronized public boolean vytvoritProdukt(Produkt produkt) throws RemoteException {
        System.out.println("Byl vytvořen nový produkt: " + produkt.getNazev());
        boolean vysledek = sql.vytvoritProdukt(produkt);
        rozeslatProdukty();
        return vysledek;
    }

    @Override
    synchronized public boolean updateProdukt(Produkt produkt) throws RemoteException {
        System.out.println("Byl změněn nový produkt: " + produkt.getNazev());
        boolean vysledek = sql.updateProdukt(produkt);
        rozeslatProdukty();
        return vysledek;
    }

    @Override
    synchronized public ArrayList<Kategorie> ziskatVsechnyKategorie() throws RemoteException {
        return sql.nacistKategorie();
    }

    @Override
    synchronized public boolean vytvoritKategorii(Kategorie kategorie) throws RemoteException {
        System.out.println("Byla vytvořena nová kategorie: " + kategorie.getNazev());
        boolean vysledek = sql.vytvoritKategorii(kategorie);
        rozeslatKategorie();
        return vysledek;
    }

    @Override
    synchronized public Map<Integer, ArrayList<ProduktObjednavka>> ziskatObjednavkyPodleStavu(int stav)
            throws RemoteException {
        return sql.nacistObjednavkyPodleStavu(stav);
    }

    @Override
    synchronized public boolean zmenitStavObjednavky(int id, int stav) throws RemoteException {
        System.out.println("Objednávka č: " + id + " byla změněna");
        boolean vysledek = sql.zmenitStavObjednavky(id, stav);
        rozeslatObjednavky();
        return vysledek;
    }

    @Override
    public void vytvoritObjednavku(Objednavka objednavka) throws RemoteException {
        System.out.println("Objednávka byla vytvořena");
        sql.vytvoritObjednavku(objednavka);
        rozeslatObjednavky();
    }

}
