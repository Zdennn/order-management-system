package cz.common.model;

import java.io.Serializable;

public class Produkt implements Serializable{

    protected int id;
    protected int idKategorie;
    protected String nazev;
    protected double cena;

    public Produkt(int id, String nazev, double cena, int idKategorie) {
        this.id = id;
        this.idKategorie = idKategorie;
        this.nazev = nazev;
        this.cena = cena;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setidKategorie(int idKategorie) {
        this.idKategorie = idKategorie;
    }
    public int getidKategorie() {
        return idKategorie;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }
    public String getNazev() {
    return nazev;
    }

    public void setCena(Double cena) {
        this.cena = cena;
    }
    public Double getCena() {
        return cena;
    }
}
