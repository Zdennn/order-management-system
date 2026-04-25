package cz.common.model;

public class ProduktObjednavka extends Produkt {

    private int pocet;

    public ProduktObjednavka(int id, String nazev, double cena, int idKategorie, int pocet) {
        super(id, nazev, cena, idKategorie);
        this.pocet = pocet;
    }

    public int getPocet() {
        return pocet;
    }

    public void setPocet(int pocet) {
        this.pocet = pocet;
    }
}
