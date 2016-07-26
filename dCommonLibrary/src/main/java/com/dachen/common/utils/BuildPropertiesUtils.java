package com.dachen.common.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

/**
 * @author gaozhuo
 * @date 2016/4/11
 *
 */
class BuildPropertiesUtils {

    private static final Properties properties;

    static {
        properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean containsKey(final Object key) {
        return properties.containsKey(key);
    }

    public static boolean containsValue(final Object value) {
        return properties.containsValue(value);
    }

    public static Set<Entry<Object, Object>> entrySet() {
        return properties.entrySet();
    }

    public static String getProperty(final String name) {
        return properties.getProperty(name);
    }

    public static String getProperty(final String name, final String defaultValue) {
        return properties.getProperty(name, defaultValue);
    }

    public static boolean isEmpty() {
        return properties.isEmpty();
    }

    public static Enumeration<Object> keys() {
        return properties.keys();
    }

    public static Set<Object> keySet() {
        return properties.keySet();
    }

    public static int size() {
        return properties.size();
    }

    public static Collection<Object> values() {
        return properties.values();
    }


}
