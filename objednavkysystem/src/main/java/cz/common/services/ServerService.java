package cz.common.services;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import cz.common.NovyKlientResponse;
import cz.common.model.Produkt;
import cz.common.model.ProduktObjednavka;
import cz.common.model.Kategorie;
import cz.common.model.Objednavka;

public interface ServerService extends Remote {
    NovyKlientResponse novyKlient(KlientService klientService) throws RemoteException;
    void odpojitKlienta(int id) throws RemoteException;

    void rozeslatProdukty() throws RemoteException;
    void rozeslatKategorie() throws RemoteException;
    void rozeslatObjednavky() throws RemoteException;

    ArrayList<Produkt> ziskatVsechnyProdukty() throws RemoteException;
    boolean vytvoritProdukt(Produkt produkt) throws RemoteException;
    boolean updateProdukt(Produkt produkt) throws RemoteException;

    ArrayList<Kategorie> ziskatVsechnyKategorie() throws RemoteException;
    boolean vytvoritKategorii(Kategorie kategorie) throws RemoteException;

    Map<Integer, ArrayList<ProduktObjednavka>> ziskatObjednavkyPodleStavu(int stav) throws RemoteException;
    boolean zmenitStavObjednavky(int id, int stav) throws RemoteException;
    void vytvoritObjednavku(Objednavka objednavka) throws RemoteException;
}
