package com.tun.assignment2.util;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class ReflectionUtils {

    // Lấy giá trị của trường dựa trên tên
    // Lấy giá trị của field bằng reflection
    public static Object getFieldValue(Object object, Field field) {
        try {
            field.setAccessible(true);
            return field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Không thể truy xuất giá trị của field: " + field.getName(), e);
        }
    }

    // Thiết lập giá trị cho field bằng reflection
    public static void setFieldValue(Object object, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Không thể gán giá trị cho field: " + field.getName(), e);
        }
    }

    // Tìm tất cả các field có annotation CsvColumn
    public static Field[] getAnnotatedFields(Class<?> clazz) {
        return clazz.getDeclaredFields();
    }
}

