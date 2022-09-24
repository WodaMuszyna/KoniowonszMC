package xyz.wodamuszyna.koniowonsz.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.TreeMap;

public class ConfigUtils {
    public static void deserialize(Map<String, Object> m, Object o) {
        for (int i=0; i < o.getClass().getFields().length; i++) {
            Field f = o.getClass().getFields()[i];
            if (Modifier.isPublic(f.getModifiers()) && !Modifier.isStatic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers()) && m.containsKey(f.getName())) {
                try {
                    f.set(o, m.get(f.getName()));
                }
                catch (IllegalArgumentException|IllegalAccessException|NullPointerException ex2) {
                    if (!(ex2 instanceof NullPointerException)) {
                        ex2.printStackTrace();
                    }
                }
            }
        }
    }

    public static Map<String, Object> serialize(Object o) {
        TreeMap<String, Object> m = new TreeMap<String, Object>();
        for (int i = 0; i < o.getClass().getFields().length; i++) {
            Field f = o.getClass().getFields()[i];
            if (Modifier.isPublic(f.getModifiers()) && !Modifier.isStatic(f.getModifiers()) && !Modifier.isFinal(f.getModifiers())) {
                try {
                    if (f.getType() == String.class) {
                        m.put(f.getName(), f.get(o));
                    } else {

                        m.put(f.getName(), f.get(o));
                    }

                } catch (IllegalArgumentException|IllegalAccessException|NullPointerException ex2) {
                    if (!(ex2 instanceof NullPointerException)) {
                        ex2.printStackTrace();
                    }
                }
            }
        }
        return m;
    }
}
