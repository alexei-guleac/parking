package com.isd.parking.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.isd.parking.utils.ColorConsoleOutput.methodMsgStatic;


@Slf4j
public class ReflectionMethods {

    public static void setPropertyValue(Object bean, String name, Object value) {
        try {
            PropertyUtils.setSimpleProperty(bean, name, value);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static Object getPropertyValue(Object bean, String name) {
        Object value = null;
        try {
            value = PropertyUtils.getSimpleProperty(bean, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String getStringPropertyValue(Object bean, String name) {
        String value = null;
        Object objValue = null;

        try {
            log.info(methodMsgStatic("getPropertyType(bean, name) == String.class " + (getPropertyType(bean, name) == String.class)));
            log.info(methodMsgStatic("getPropertyType(bean, name)" + getPropertyType(bean, name)));
            if (getPropertyType(bean, name) == String.class) {

                value = (String) PropertyUtils.getSimpleProperty(bean, name);
                log.info(methodMsgStatic("VALUE " + value));
            } else {
                objValue = PropertyUtils.getSimpleProperty(bean, name);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        log.info(methodMsgStatic("VALUE" + value));
        return value != null ? value : objValue != null ? objValue.toString() : null;
    }

    public static Class getPropertyType(Object bean, String name) {
        Class type = null;
        try {
            type = PropertyUtils.getPropertyType(bean, name);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return type;
    }

    public String[] getFields() {
        Class<? extends Object> componentClass = getClass();
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

        System.out.println(lines);
        return lines.toArray(new String[lines.size()]);
    }

    public List<String> getFieldsNames(Class<? extends Object> componentClass) {
        List<String> privateFields = new ArrayList<>();
        Field[] allFields = componentClass.getDeclaredFields();
        for (Field field : allFields) {
            if (Modifier.isPrivate(field.getModifiers())) {
                privateFields.add(field.getName());
                System.out.format("type is %s", field.getType());
            }
        }

        return privateFields;
    }

    /**
     * Get the method name for a depth in call stack. <br />
     * Utility function
     *
     * @param depth depth in the call stack (0 means current method, 1 means call method, ...)
     * @return method name
     */
    public static String getMethodName(final int depth) {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        // print stacktrace elements
        /*for (StackTraceElement s : ste) {
            System.out.println(s);
        }*/

        //System.out.println(ste[ste.length - depth].getClassName() + "#" + ste[ste.length - depth].getMethodName());
        // return ste[ste.length - depth].getMethodName();                  //Wrong, fails for depth = 0
        //return ste[ste.length - 1 - depth].getMethodName();
        return ste[depth].getMethodName();
    }
}

