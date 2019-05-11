package cn.jianchengwang.playass.core.kit;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReflectKit {

    public final static Map<String, Class> CACHE_CLASS_MAP = new LinkedHashMap<>();

    public static Class getClass(String className) throws Exception {
        if(!CACHE_CLASS_MAP.containsKey(className)) {
            CACHE_CLASS_MAP.put(className, Class.forName(className));
        }

        return CACHE_CLASS_MAP.get(className);
    }

    public static void invokeSetMethod(Class clazz, Field field, Object instance, Object value) throws Exception {

        String methodName = "set" + StrKit.upperCase(field.getName());

        Method method = clazz.getMethod(methodName, field.getType());

        switch (field.getType().getName()) {
            case "java.lang.String":
                method.invoke(instance, (String) value);
                break;
            case "java.lang.Integer":
                method.invoke(instance, Integer.valueOf(value.toString()));
                break;
            case "java.lang.Long":
                method.invoke(instance, Long.valueOf(value.toString()));
                break;
            case "java.lang.Double":
                method.invoke(instance, Double.valueOf(value.toString()));
                break;
            case "java.lang.Float":
                method.invoke(instance, Float.valueOf(value.toString()));
                break;
            case "java.lang.Boolean":
                method.invoke(instance, Boolean.valueOf(value.toString()));
                break;
            default:
                method.invoke(instance, value);
                break;

        }
    }

    public static Object invokeGetMethod(Class clazz, Field field, Object instance) throws Exception {

        String methodName = "set" + StrKit.upperCase(field.getName());

        Method method = clazz.getMethod(methodName, null);

        return method.invoke(instance, null);
    }
}
