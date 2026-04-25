package cz.common.Singletons;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import cz.common.db.SqlMethods;
import cz.common.model.Produkt;

public final class SingleProdukty implements Iterable<Produkt> {
    private static SingleProdukty produktyIns;
    private static SqlMethods sqlMethods;
    private final ConcurrentHashMap<Integer, Produkt> produktyMap;

    private SingleProdukty(){
        produktyMap = new ConcurrentHashMap<>();
    }

    public static SingleProdukty getSingleProdukty(){
        if (produktyIns == null){
            produktyIns = new SingleProdukty();
            produktyIns.nacitData();
        }
        return produktyIns;
    }

    private void nacitData(){
        sqlMethods.nacistProdukty();
    }

    public void put(int key, Produkt produkt){
        produktyMap.put(key, produkt);
    }

    public Produkt getProdukt(int idProdukt){
        return produktyMap.get(idProdukt);
    }

    @Override
    public Iterator<Produkt> iterator() {
        return new SingleProduktIterator();
    }
    private class SingleProduktIterator implements Iterator<Produkt> {
        private final Iterator<Map.Entry<Integer, Produkt>> iterator;

        private SingleProduktIterator() {
            this.iterator = produktyMap.entrySet().iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Produkt next() {
            if (hasNext()) {
                return iterator.next().getValue();
            }
            throw new UnsupportedOperationException("No more elements to iterate.");
        }
    }
}
