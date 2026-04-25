package cz.common.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import cz.common.model.Kategorie;
import cz.common.model.Objednavka;
import cz.common.model.Produkt;
import cz.common.model.ProduktObjednavka;

public class SqlMethods {

    SqlWrapper sql = new SqlWrapper();

    public ArrayList<Produkt> nacistProdukty() {
        String query = "SELECT idProdukt, nazev, cena, Kategorie_idKategorie FROM produkt ORDER BY Kategorie_idKategorie ASC";

        try (ResultSet rs = sql.execQuery(query, new ArrayList<Param<?>>())) {

            ArrayList<Produkt> produkty = new ArrayList<>();

            while (rs.next()) {
                Produkt produkt = new Produkt(
                        rs.getInt("idProdukt"),
                        rs.getString("nazev"),
                        rs.getDouble("cena"),
                        rs.getInt("Kategorie_idKategorie"));
                produkty.add(produkt);
            }
            return produkty;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean vytvoritProdukt(Produkt produkt) {
        String query = "INSERT INTO produkt (nazev, cena, Kategorie_idKategorie) VALUES (?, ?, ?)";

        ArrayList<Param<?>> parametry = new ArrayList<>();
        parametry.add(new Param<>(1, produkt.getNazev(), "s"));
        parametry.add(new Param<>(2, produkt.getCena(), "d"));
        parametry.add(new Param<>(3, produkt.getidKategorie(), "i"));

        try (ResultSet rs = sql.execUpdate(query, parametry)) {
            if (rs != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateProdukt(Produkt produkt) {
        String query = "UPDATE produkt SET nazev=?, cena=?, Kategorie_idKategorie=? WHERE idProdukt = ?";

        ArrayList<Param<?>> parametry = new ArrayList<>();
        parametry.add(new Param<>(1, produkt.getNazev(), "s"));
        parametry.add(new Param<>(2, produkt.getCena(), "d"));
        parametry.add(new Param<>(3, produkt.getidKategorie(), "i"));
        parametry.add(new Param<>(4, produkt.getId(), "i"));

        try (ResultSet rs = sql.execUpdate(query, parametry)) {
            if (rs != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public ArrayList<Kategorie> nacistKategorie() {

        String query = "SELECT idKategorie, nazev FROM kategorie";

        try (ResultSet rs = sql.execQuery(query, new ArrayList<Param<?>>())) {

            ArrayList<Kategorie> kategorieList = new ArrayList<>();

            while (rs.next()) {
                Kategorie kategorie = new Kategorie(
                        rs.getInt("idKategorie"),
                        rs.getString("nazev"));
                kategorieList.add(kategorie);
            }
            return kategorieList;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean vytvoritKategorii(Kategorie kategorie) {
        String query = "INSERT INTO kategorie (nazev) VALUES (?)";

        ArrayList<Param<?>> parametry = new ArrayList<>();
        parametry.add(new Param<>(1, kategorie.getNazev(), "s"));

        try (ResultSet rs = sql.execUpdate(query, parametry)) {
            if (rs != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Map<Integer, ArrayList<ProduktObjednavka>> nacistObjednavkyPodleStavu(int stav) {
        String query = "SELECT o.idObjednavka, o.stav, p.Produkt_idProdukt, p.cena, p.pocet FROM objednavky as o join produkt_objednavka as p on o.idObjednavka = p.Objednavky_idObjednavky WHERE o.stav = ?";

        ArrayList<Param<?>> parametry = new ArrayList<>();
        parametry.add(new Param<>(1, stav, "i"));

        Map<Integer, ArrayList<ProduktObjednavka>> objednavky = new HashMap<>();
        
        ArrayList<Produkt> produkty = nacistProdukty();

        ArrayList<ProduktObjednavka> proObj = new ArrayList<>();
        int predchoziIdObjednavka = -1;

        try (ResultSet rs = sql.execQuery(query, parametry)) {
            while (rs.next()) {
                int idObjednavka = rs.getInt("idObjednavka");

                if (idObjednavka != predchoziIdObjednavka) {
                    if (predchoziIdObjednavka != -1) {
                        objednavky.put(predchoziIdObjednavka, proObj);
                    }
                    proObj = new ArrayList<>();
                }else{
                    int idProdukt = rs.getInt("Produkt_idProdukt");
                    ProduktObjednavka produkt2 = null;
    
                    for (Produkt produkt : produkty) {
                        if (produkt.getId() == idProdukt) {
                            
                            double cena = rs.getDouble("cena");
                            int pocet = rs.getInt("pocet");
        
                            produkt2 = new ProduktObjednavka(idProdukt, produkt.getNazev(), cena, produkt.getidKategorie(), pocet);
                            break;
                        }
                        
                    }
                    proObj.add(produkt2);
                }
                predchoziIdObjednavka = idObjednavka;

                if (predchoziIdObjednavka != -1 && !proObj.isEmpty()) {
                    objednavky.put(predchoziIdObjednavka, proObj);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return objednavky;
    }

    public boolean zmenitStavObjednavky(int id, int stav){
        String query = "UPDATE objednavky SET stav=? WHERE idObjednavka = ?";

        ArrayList<Param<?>> parametry = new ArrayList<>();
        parametry.add(new Param<>(1, stav, "i"));
        parametry.add(new Param<>(2, id, "i"));

        try (ResultSet rs = sql.execUpdate(query, parametry)) {
            if (rs != null) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void vytvoritObjednavku(Objednavka objednavka) {
        String objednavkaQuery = "INSERT INTO objednavky () VALUES ()";
        String produktObjednavkaQuery = "INSERT INTO produkt_objednavka (Produkt_idProdukt, Objednavky_idObjednavky, cena, pocet) VALUES (?, ?, ?, ?)";

        int idObjednavky = -1;

        ArrayList<Produkt> produkty = this.nacistProdukty();

        try {
            ResultSet objednavkaResult = sql.execUpdate(objednavkaQuery, new ArrayList<Param<?>>());

            if (objednavkaResult.next()) {
                idObjednavky = objednavkaResult.getInt(1);
            } else {
                throw new SQLException("Nepodařilo se získat id poslední objednávky.");
            }

            ArrayList<Integer> ulozeno = new ArrayList<>();

            for (Map.Entry<Integer, Integer> entry : objednavka.getProdukty().entrySet()) {
                int idProdukt = entry.getKey();
                int pocet = entry.getValue();

                if (ulozeno.contains(idProdukt)) {
                    continue;
                }

                ArrayList<Param<?>> params = new ArrayList<>();
                params.add(new Param<>(1, idProdukt, "i"));
                params.add(new Param<>(2, idObjednavky, "i"));
                
                Produkt prrr = null;
                for (Produkt prod : produkty) {
                    if (idProdukt == prod.getId()) {
                        prrr = prod;
                        break;
                    }
                }
                params.add(new Param<>(3, prrr.getCena(), "d"));
                params.add(new Param<>(4, pocet, "i"));

                sql.execUpdate(produktObjednavkaQuery, params);

                ulozeno.add(idProdukt);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
