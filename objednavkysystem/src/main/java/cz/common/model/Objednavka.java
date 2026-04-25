package cz.common.model;

import java.io.Serializable;
import java.util.Map;

public class Objednavka implements Serializable{

    private int id;
    private int stav;
    private Map<Integer, Integer> idProdukty;//idProdukt, počet

    public Objednavka(int id, int stav, Map<Integer, Integer> idProdukty) {
        this.id = id;
        this.stav = stav;
        this.idProdukty = idProdukty;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStav() {
        return stav;
    }

    public void setStav(int stav) {
        this.stav = stav;
    }

    public Map<Integer, Integer> getProdukty() {
        return idProdukty;
    }

    public void setProdukty(Map<Integer, Integer> idProdukty) {
        this.idProdukty = idProdukty;
    }
}