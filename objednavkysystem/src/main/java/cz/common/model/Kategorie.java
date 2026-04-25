package cz.common.model;

import java.io.Serializable;

public class Kategorie implements Serializable{

    protected Integer id;
    protected String nazev;

    public Kategorie(Integer id, String nazev){
        this.id = id;
        this.nazev = nazev;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNazev() {
        return nazev;
    }
    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

}
