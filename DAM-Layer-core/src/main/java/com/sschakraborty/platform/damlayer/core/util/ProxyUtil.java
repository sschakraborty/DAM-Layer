package com.sschakraborty.platform.damlayer.core.util;

import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/*
 *  Snippet taken from https://candrews.integralblue.com/2009/03/hibernate-deep-deproxy/
 *  by Craig Andrews
 */
public class ProxyUtil {
    private ProxyUtil() {
    }

    public static <T> T recursiveUnproxy(final Object maybeProxy) throws ClassCastException {
        if (maybeProxy == null) return null;
        return recursiveUnproxy(maybeProxy, new HashSet<>());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <T> T recursiveUnproxy(final Object maybeProxy, final HashSet<Object> visitedSet) throws ClassCastException {
        if (maybeProxy == null) return null;
        Hibernate.initialize(maybeProxy);

        Class clazz;
        if (maybeProxy instanceof HibernateProxy) {
            final HibernateProxy proxy = (HibernateProxy) maybeProxy;
            final LazyInitializer lazyInitializer = proxy.getHibernateLazyInitializer();
            clazz = lazyInitializer.getImplementation().getClass();
        } else {
            clazz = maybeProxy.getClass();
        }

        final T returnObject = (T) recursiveUnproxy(maybeProxy, clazz);
        if (visitedSet.contains(returnObject)) return returnObject;
        visitedSet.add(returnObject);

        for (final PropertyDescriptor property : PropertyUtils.getPropertyDescriptors(returnObject)) {
            try {
                final String propertyName = property.getName();

                if (property.getWriteMethod() != null) {
                    Object value = PropertyUtils.getProperty(returnObject, propertyName);
                    boolean needToSetProperty = false;

                    if (value instanceof HibernateProxy) {
                        value = recursiveUnproxy(value, visitedSet);
                        needToSetProperty = true;
                    }

                    if (value instanceof Object[]) {
                        Object[] valueArray = (Object[]) value;
                        Object[] result = (Object[]) Array.newInstance(value.getClass(), valueArray.length);
                        for (int i = 0; i < valueArray.length; i++) {
                            result[i] = recursiveUnproxy(valueArray[i], visitedSet);
                        }
                        value = result;
                        needToSetProperty = true;
                    }

                    if (value instanceof Set) {
                        Set valueSet = (Set) value;
                        Set result = new HashSet();
                        for (Object o : valueSet) {
                            result.add(recursiveUnproxy(o, visitedSet));
                        }
                        value = result;
                        needToSetProperty = true;
                    }

                    if (value instanceof Map) {
                        Map valueMap = (Map) value;
                        Map result = new HashMap();
                        for (Object key : valueMap.keySet()) {
                            result.put(recursiveUnproxy(key, visitedSet), recursiveUnproxy(valueMap.get(key), visitedSet));
                        }
                        value = result;
                        needToSetProperty = true;
                    }

                    if (value instanceof List) {
                        List valueList = (List) value;
                        List result = new ArrayList(valueList.size());
                        for (Object listObject : valueList) {
                            result.add(recursiveUnproxy(listObject, visitedSet));
                        }
                        value = result;
                        needToSetProperty = true;
                    }

                    if (needToSetProperty) PropertyUtils.setProperty(returnObject, propertyName, value);
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                // TODO: Log exception if required
            }
        }
        return returnObject;
    }

    private static <T> T recursiveUnproxy(Object maybeProxy, Class<T> baseClass) throws ClassCastException {
        if (maybeProxy == null) return null;
        if (maybeProxy instanceof HibernateProxy) {
            return baseClass.cast(((HibernateProxy) maybeProxy).getHibernateLazyInitializer().getImplementation());
        } else {
            return baseClass.cast(maybeProxy);
        }
    }
}