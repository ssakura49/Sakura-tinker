package com.ssakura49.sakuratinker.utils.java;

import java.util.LinkedList;

public class FixedSizeQueue<T> extends LinkedList<T> {
    private final int maxSize;

    public FixedSizeQueue(int maxSize) {
        this.maxSize = maxSize;
    }

    @Override
    public boolean add(T t) {
        if (size() >= maxSize) {
            removeFirst();
        }
        return super.add(t);
    }
}