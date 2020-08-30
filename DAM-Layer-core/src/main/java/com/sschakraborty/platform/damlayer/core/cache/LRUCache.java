package com.sschakraborty.platform.damlayer.core.cache;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of an in-memory LRU cache
 */
public class LRUCache<K, T> implements Cache<K, T> {
    private final Map<K, Node<K, T>> internalMap = new HashMap<>();
    private int maxSize = 500;
    private Node<K, T> headNode = null, tailNode = null;

    public LRUCache(int maxSize) {
        if (maxSize >= 2 && maxSize <= 100000) {
            this.maxSize = maxSize;
        }
    }

    @Override
    public void put(K key, T data) {
        if (exists(key)) {
            final Node<K, T> node = internalMap.get(key);
            node.setData(data);
            processNodeAccess(node);
        } else {
            final Node<K, T> node = new Node<>(key, data);
            internalMap.put(key, node);
            if (internalMap.size() > maxSize) {
                final K keyToRemove = removeLastNode();
                if (keyToRemove != null) {
                    internalMap.remove(keyToRemove);
                }
            }
            processNodeCreate(node);
        }
    }

    @Override
    public T get(K key) {
        if (exists(key)) {
            final Node<K, T> node = internalMap.get(key);
            processNodeAccess(node);
            return node.getData();
        }
        return null;
    }

    @Override
    public void invalidate(K key) {
        if (exists(key)) {
            final Node<K, T> node = internalMap.get(key);
            processNodeDelete(node);
            internalMap.remove(key);
        }
    }

    @Override
    public boolean exists(K key) {
        return internalMap.containsKey(key);
    }

    @Override
    public int size() {
        return internalMap.size();
    }

    /**
     * Caution - Only for test use.
     * Never use it for production.
     */
    void debugDump() {
        PrintWriter printWriter = new PrintWriter(System.out);
        Node<K, T> debugNode = headNode;
        printWriter.printf("Cache size: %d -> ", size());
        while (debugNode != tailNode.getNextNode()) {
            printWriter.print(debugNode.getKey() + " - " + debugNode.getData());
            printWriter.print(debugNode != tailNode ? " | " : "");
            debugNode = debugNode.getNextNode();
        }
        printWriter.println();
        printWriter.flush();
    }

    private void processNodeAccess(Node<K, T> node) {
        if (node.isLastNode()) {
            node.getPreviousNode().setNextNode(null);
            node.setPreviousNode(null);
            node.setNextNode(headNode);
        } else if (node.isMiddleNode()) {
            node.getPreviousNode().setNextNode(node.getNextNode());
            node.getNextNode().setPreviousNode(node.getPreviousNode());
            node.setNextNode(headNode);
            node.setPreviousNode(null);
        }
        if (headNode != null && headNode != node) {
            headNode.setPreviousNode(node);
        }
        headNode = node;
    }

    private void processNodeDelete(Node<K, T> node) {
        if (node.isFirstNode() && headNode == node) {
            headNode = headNode.getNextNode();
            node.getNextNode().setPreviousNode(null);
            node.setNextNode(null);
        } else if (node.isMiddleNode()) {
            node.getPreviousNode().setNextNode(node.getNextNode());
            node.getNextNode().setPreviousNode(node.getPreviousNode());
            node.setNextNode(null);
            node.setPreviousNode(null);
        } else if (node.isLastNode() && tailNode == node) {
            tailNode = tailNode.getPreviousNode();
            node.getPreviousNode().setNextNode(null);
            node.setPreviousNode(null);
        } else {
            headNode = tailNode = null;
        }
    }

    private void processNodeCreate(Node<K, T> node) {
        if (node.isNewNode()) {
            node.setNextNode(headNode);
            if (headNode != null) {
                headNode.setPreviousNode(node);
            } else {
                tailNode = node;
            }
            headNode = node;
        }
    }

    private K removeLastNode() {
        K key = null;
        if (tailNode.isLastNode()) {
            final Node<K, T> previousNode = tailNode.getPreviousNode();
            key = tailNode.getKey();
            previousNode.setNextNode(null);
            tailNode.setPreviousNode(null);
            tailNode = previousNode;
        }
        return key;
    }

    private static class Node<K, T> {
        private final K key;
        private T data;
        private Node<K, T> previousNode;
        private Node<K, T> nextNode;

        private Node(K key, T data) {
            this.key = key;
            this.data = data;
            previousNode = null;
            nextNode = null;
        }

        public K getKey() {
            return key;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Node<K, T> getPreviousNode() {
            return previousNode;
        }

        public void setPreviousNode(Node<K, T> previousNode) {
            this.previousNode = previousNode;
        }

        public Node<K, T> getNextNode() {
            return nextNode;
        }

        public void setNextNode(Node<K, T> nextNode) {
            this.nextNode = nextNode;
        }

        public boolean isFirstNode() {
            return previousNode == null && nextNode != null;
        }

        public boolean isLastNode() {
            return previousNode != null && nextNode == null;
        }

        public boolean isMiddleNode() {
            return previousNode != null && nextNode != null;
        }

        public boolean isNewNode() {
            return previousNode == null && nextNode == null;
        }
    }
}