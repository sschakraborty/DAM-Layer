package com.sschakraborty.platform.damlayer.core.processor;

import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class CallbackHandlerManager {
    private final CallbackHandler defaultHandler;
    private final Map<Class<?>, LinkedList<CallbackHandler>> handlerMap = new ConcurrentHashMap<>();

    public CallbackHandlerManager(CallbackHandler defaultHandler) {
        if (defaultHandler == null) {
            this.defaultHandler = new CallbackHandler() {
            };
        } else {
            this.defaultHandler = defaultHandler;
        }
    }

    public CallbackHandlerManager() {
        this.defaultHandler = new CallbackHandler() {
        };
    }

    public CallbackHandler getDefaultHandler() {
        return defaultHandler;
    }

    public <T> void registerHandler(Class<T> tClass, CallbackHandler callbackHandler) {
        if (tClass != null && callbackHandler != null) {
            this.handlerMap.computeIfAbsent(tClass, k -> new LinkedList<>());
            this.handlerMap.get(tClass).add(callbackHandler);
        }
    }

    public LinkedList<CallbackHandler> getHandlerFor(Class<?> tClass) {
        Objects.requireNonNull(tClass);
        this.handlerMap.computeIfAbsent(tClass, k -> new LinkedList<>());
        return this.handlerMap.get(tClass);
    }
}