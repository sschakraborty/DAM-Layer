package com.sschakraborty.platform.damlayer.transformation;

import com.sschakraborty.platform.damlayer.transformation.entry.Entry;

import java.util.List;

public interface Transformer {
    <S> boolean isOneToOne(final S sourceObject);

    <S, D> List<D> transformOneToMany(final S sourceObject) throws Exception;

    <S, D> D transformOneToOne(final S sourceObject) throws Exception;

    List<Entry> getAllEntries();
}