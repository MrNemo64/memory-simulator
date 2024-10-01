package me.nemo_64.a2.so.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class SortedArrayList<E> extends ArrayList<E> {

    private static final long serialVersionUID = 980449613565418619L;

    private final Comparator<E> comp;

    public SortedArrayList(Comparator<E> comp) {
        this.comp = comp;
    }

    @Override
    public E set(int index, E element) {
        E e = super.set(index, element);
        sort();
        return e;
    }

    @Override
    public boolean add(E e) {
        boolean b = super.add(e);
        sort();
        return b;
    }

    @Override
    public void add(int index, E element) {
        super.add(index, element);
        sort();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean b = super.addAll(c);
        sort();
        return b;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean b = super.addAll(c);
        sort();
        return b;
    }

    private void sort() {
        super.sort(comp);
    }

    @Override
    public void sort(Comparator<? super E> c) {
        throw new UnsupportedOperationException("Cannot call sort on an instance of a sorted list");
    }

}