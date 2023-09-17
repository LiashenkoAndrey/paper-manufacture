package com.paper.util;

import com.github.dozermapper.core.CustomConverter;

import java.util.Map;

public class MapConverter implements CustomConverter {
    @Override
    public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue, Class<?> destinationClass, Class<?> sourceClass) {
        if (sourceFieldValue instanceof Map<?,?>) {
            return  sourceFieldValue;
        } else return existingDestinationFieldValue;
    }
}
