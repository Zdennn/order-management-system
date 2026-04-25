package cz.common.Singletons;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cz.common.model.Objednavka;

public final class SingleObjednavka implements Iterable<Objednavka> {
    private static SingleObjednavka objednavkaIns;
    private final ConcurrentHashMap<Integer, Objednavka> objednavkaMap;

    private SingleObjednavka() {
        objednavkaMap = new ConcurrentHashMap<>();
    }

    public static SingleObjednavka getSingleObjednavka() {
        if (objednavkaIns == null) {
            objednavkaIns = new SingleObjednavka();
        }
        return objednavkaIns;
    }

    public void put(int key, Objednavka objednavka) {
        objednavkaMap.put(key, objednavka);
    }

    @Override
    public Iterator<Objednavka> iterator() {
        return new SingleObjednavkaIterator();
    }

    private class SingleObjednavkaIterator implements Iterator<Objednavka> {
        private final Iterator<Map.Entry<Integer, Objednavka>> iterator;

        private SingleObjednavkaIterator() {
            this.iterator = objednavkaMap.entrySet().iterator();
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Objednavka next() {
            if (hasNext()) {
                return iterator.next().getValue();
            }
            throw new UnsupportedOperationException("No more elements to iterate.");
        }
    }
}