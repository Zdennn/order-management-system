package cz.common.Singletons;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cz.common.db.SqlMethods;
import cz.common.model.Kategorie;

public final class SingleKategorie implements Iterable<Kategorie> {
    private static SingleKategorie kategorieIns;
    private static SqlMethods sqlMethods;
    private final ConcurrentHashMap<Integer, Kategorie> kategorieMap;

    private SingleKategorie() {
        kategorieMap = new ConcurrentHashMap<>();
    }

    public static SingleKategorie getSingleKategorie() {
        if (kategorieIns == null) {
            kategorieIns = new SingleKategorie();
            kategorieIns.nacitData();
        }
        return kategorieIns;
    }

    private void nacitData() {
        sqlMethods.nacistKategorie();
    }

    public void put(int key, Kategorie kategorie) {
        kategorieMap.put(key, kategorie);
    }

    @Override
    public Iterator<Kategorie> iterator() {
        return new SingleKategorieIterator();
    }

    private class SingleKategorieIterator implements Iterator<Kategorie> {
        private final Iterator<Map.Entry<Integer, Kategorie>> iterator;

        private SingleKategorieIterator() {
            this.iterator = kategorieMap.entrySet().iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Kategorie next() {
            if (hasNext()) {
                return iterator.next().getValue();
            }
            throw new UnsupportedOperationException("No more elements to iterate.");
        }
    }
}