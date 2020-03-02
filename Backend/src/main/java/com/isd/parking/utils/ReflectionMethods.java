package com.isd.parking.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReflectionMethods {

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

    /**
     * Get the method name for a depth in call stack. <br />
     * Utility function
     *
     * @param depth depth in the call stack (0 means current method, 1 means call method, ...)
     * @return method name
     */
    public static String getMethodName(final int depth) {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();

        //System. out.println(ste[ste.length-depth].getClassName()+"#"+ste[ste.length-depth].getMethodName());
        // return ste[ste.length - depth].getMethodName();  //Wrong, fails for depth = 0
        return ste[ste.length - 1 - depth].getMethodName(); //Thank you Tom Tresansky
    }
}

