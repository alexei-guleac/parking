package com.isd.parking.utilities;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Utility class
 * Contains methods with class access using Java Reflection API
 */
@Slf4j
public class ReflectionMethods {

    /**
     * Sets target object private property value by given property name
     *
     * @param bean  - target object
     * @param name  - target field name
     * @param value - desired field value
     */
    public static void setPropertyValue(Object bean, String name, Object value) {
        try {
            PropertyUtils.setSimpleProperty(bean, name, value);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets target object private property value by given property name
     *
     * @param bean - target object
     * @param name - target field name
     * @return estimated field value
     */
    public static Object getPropertyValue(Object bean, String name) {
        Object value = null;
        try {
            value = PropertyUtils.getSimpleProperty(bean, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Returns String representation of target object private property
     *
     * @param bean - target object
     * @param name - target field name
     * @return target object field value
     */
    public static String getStringPropertyValue(Object bean, String name) {
        String value = null;
        Object objValue = null;

        try {
            if (getPropertyType(bean, name) == String.class) {
                value = (String) PropertyUtils.getSimpleProperty(bean, name);
            } else {
                objValue = PropertyUtils.getSimpleProperty(bean, name);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return value != null ? value : objValue != null ? objValue.toString() : null;
    }

    /**
     * Returns target object property type
     *
     * @param bean - target object
     * @param name - target field name
     * @return target object property type
     */
    public static Class<?> getPropertyType(Object bean, String name) {
        Class<?> type = null;
        try {
            type = PropertyUtils.getPropertyType(bean, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return type;
    }

    /**
     * Get the method name for a depth in call stack. <br />
     * Utility function
     *
     * @param depth depth in the call stack (0 means current method, 1 means call method, ...)
     *              But depending on the number of installed plugins and application settings,
     *              the depth may vary, must be selected empirically.
     *              For current application configuration depth is 3.
     * @return target current executed method name
     */
    static String getMethodName(final int depth) {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        return ste[depth].getMethodName();
    }

    /**
     * Returns String array of all target object field names plus their values using Java Reflection API
     * Sample FieldName=FieldValue
     *
     * @return String array of all target object field names plus their values
     */
    public String[] getFields() {
        Class<?> componentClass = getClass();
        Field[] fields = componentClass.getFields();
        List<String> lines = new ArrayList<>(fields.length);

        Arrays.stream(fields).forEach(field -> {
            field.setAccessible(true);
            try {
                lines.add(field.getName() + " = " + field.get(this));
            } catch (final IllegalAccessException e) {
                lines.add(field.getName() + " > " + e.getClass().getSimpleName());
            }
        });
        String[] fieldsArray = new String[lines.size()];
        return lines.toArray(fieldsArray);
    }

    /**
     * Returns String array of target object private field names using Java Reflection API
     *
     * @param componentClass - target object class
     * @return String array of target object private field names
     */
    public List<String> getFieldsNames(Class<?> componentClass) {
        List<String> privateFields = new ArrayList<>();
        Field[] allFields = componentClass.getDeclaredFields();

        for (Field field : allFields) {
            if (Modifier.isPrivate(field.getModifiers())) {
                privateFields.add(field.getName());
            }
        }

        return privateFields;
    }
}

