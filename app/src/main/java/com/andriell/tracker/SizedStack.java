package com.andriell.tracker;

import java.util.Stack;

public class SizedStack<T> extends Stack<T> {
    private int maxSize;

    public SizedStack(int size) {
        super();
        this.maxSize = size;
    }

    @Override
    public T push(T object) {
        while (this.size() >= maxSize) {
            this.remove(0);
        }
        return super.push(object);
    }

    @Override
    public synchronized String toString() {
        StringBuilder builder = new StringBuilder();
        for (T s :this) {
            builder.append(s);
            builder.append("\n");
        }
        return builder.toString();
    }
}
