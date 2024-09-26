package com.tuan.assignment2.util;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class ReflectionUtils {
    public Object getFieldValue(Object object, Field field) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot retrive value of field: " + field.getName(), e);
        }
    }

    public void setFieldValue(Object object, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot set value of field: " + field.getName(), e);
        }
    }
}
