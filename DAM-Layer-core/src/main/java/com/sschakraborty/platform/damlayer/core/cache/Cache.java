package com.sschakraborty.platform.damlayer.core.cache;

public interface Cache<K, T> {
    void put(K key, T data);

    T get(K key);

    void invalidate(K key);

    boolean exists(K key);

    int size();
}