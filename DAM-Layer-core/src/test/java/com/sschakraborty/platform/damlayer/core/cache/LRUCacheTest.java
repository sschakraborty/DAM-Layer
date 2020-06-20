package com.sschakraborty.platform.damlayer.core.cache;

import org.junit.Test;

public class LRUCacheTest {
    private final LRUCache<String, Integer> lruCache = new LRUCache<>(3);

    @Test
    public void testInsertFirstCase() {
        lruCache.put("A", 1);
        lruCache.debugDump();
        lruCache.put("B", 2);
        lruCache.debugDump();
        lruCache.put("C", 3);
        lruCache.debugDump();
        lruCache.put("D", 4);
        lruCache.debugDump();
        lruCache.put("E", 5);
        lruCache.debugDump();
    }

    @Test
    public void testInsertSecondCase() {
        lruCache.put("A", 1);
        lruCache.debugDump();
        lruCache.put("B", 2);
        lruCache.debugDump();
        lruCache.put("C", 3);
        lruCache.debugDump();
        lruCache.get("B");
        lruCache.debugDump();
        lruCache.get("C");
        lruCache.debugDump();
    }

    @Test
    public void testDeleteFirstCase() {
        lruCache.put("A", 1);
        lruCache.debugDump();
        lruCache.put("B", 2);
        lruCache.debugDump();
        lruCache.put("C", 3);
        lruCache.debugDump();
        lruCache.invalidate("B");
        lruCache.debugDump();
        lruCache.put("C", 6);
        lruCache.debugDump();
        lruCache.put("D", 7);
        lruCache.debugDump();
        lruCache.invalidate("A");
        lruCache.debugDump();
        lruCache.put("E", 12);
        lruCache.debugDump();
    }

}