package cz.common.services;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;

import cz.common.model.Kategorie;
import cz.common.model.Produkt;
import cz.common.model.ProduktObjednavka;

public interface KlientService extends Remote {
    void prijemProduktu(ArrayList<Produkt> produkty) throws RemoteException;
    void prijemKategorii(ArrayList<Kategorie> kategorie) throws RemoteException;
    
    void prijemObjednavek(Map<Integer, Map<Integer, ArrayList<ProduktObjednavka>>> objednavky) throws RemoteException;

    void novyKlient() throws RemoteException;
    void odpojitKlienta() throws RemoteException;
}
