package com.sschakraborty.platform.damlayer.transformation;

import com.sschakraborty.platform.damlayer.transformation.converter.Converter;
import com.sschakraborty.platform.damlayer.transformation.converter.OneToManyConverter;
import com.sschakraborty.platform.damlayer.transformation.converter.OneToOneConverter;
import com.sschakraborty.platform.damlayer.transformation.entry.Entry;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransformerImpl implements Transformer {
    private static final String ONE_TO_MANY_CONVERTER_WAS_NOT_FOUND = "One to many converter was not found!";
    private static final String ONE_TO_ONE_CONVERTER_WAS_NOT_FOUND = "One to one converter was not found!";

    private final List<Entry> entryList;

    public TransformerImpl() {
        this.entryList = new ArrayList<>(50);
    }

    public <S, D> void registerConverter(final Entry<S, D> entry) {
        this.entryList.add(entry);
    }

    @Override
    public <S> boolean isOneToOne(final S sourceObject) {
        final Optional<Converter> converterMono = getConverter(sourceObject);
        return converterMono.map(Converter::isOneToOne).orElse(false);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S, D> List<D> transformOneToMany(final S sourceObject) throws Exception {
        final Optional<Converter> converterMono = getConverter(sourceObject);
        if (converterMono.isPresent()) {
            final Converter converter = converterMono.get();
            if (!converter.isOneToOne() && converter instanceof OneToManyConverter) {
                return ((OneToManyConverter<S, D>) converter).convert(sourceObject);
            } else {
                throw new Exception(ONE_TO_MANY_CONVERTER_WAS_NOT_FOUND);
            }
        } else {
            throw new Exception(ONE_TO_MANY_CONVERTER_WAS_NOT_FOUND);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S, D> D transformOneToOne(final S sourceObject) throws Exception {
        final Optional<Converter> converterMono = getConverter(sourceObject);
        if (converterMono.isPresent()) {
            final Converter converter = converterMono.get();
            if (converter.isOneToOne() && converter instanceof OneToOneConverter) {
                return ((OneToOneConverter<S, D>) converter).convert(sourceObject);
            } else {
                throw new Exception(ONE_TO_ONE_CONVERTER_WAS_NOT_FOUND);
            }
        } else {
            throw new Exception(ONE_TO_ONE_CONVERTER_WAS_NOT_FOUND);
        }
    }

    @SuppressWarnings("unchecked")
    private <S> Optional<Converter> getConverter(final S sourceObject) {
        return this.entryList.stream().filter(entry -> {
            return entry.getSourceClass().isAssignableFrom(sourceObject.getClass());
        }).map(Entry::getConverter).findFirst();
    }
}