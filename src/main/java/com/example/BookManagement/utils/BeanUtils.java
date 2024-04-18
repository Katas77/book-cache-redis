package com.example.BookManagement.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;

@UtilityClass
public class BeanUtils {
    @SneakyThrows
    public void copyNonNullProperties(Object source, Object destination) {
        Class<?> clazz = source.getClass();
        Field[] filelds = clazz.getDeclaredFields();
        for (Field field : filelds) {
            field.setAccessible(true);
            Object value = field.get(source);
            if (value != null) {
                field.set(destination, value);
            }
        }
    }
}
