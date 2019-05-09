package cn.jianchengwang.playass.core.mvc.route.process;

import cn.jianchengwang.playass.core.mvc.context.param.ParamMap;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Timestamp;

public class BeanProcess {

    public static <T> T toBean(ParamMap paramMap, Class<T> type) throws Exception {

        PropertyDescriptor[] props = propertyDescriptors(type);

        return createBean(paramMap, type, props);
    }

    public static <T> T createBean(ParamMap paramMap, Class<T> type, PropertyDescriptor[] props) throws Exception {

        T bean = type.newInstance();

        for (int i = 0; i < props.length; i++) {

            if(paramMap.containsKey(props[i].getName())) {
                Object value = paramMap.get(props[i].getName());
                callSetter(bean, props[i], value);
            }
        }

        return bean;
    }

    private static PropertyDescriptor[] propertyDescriptors(Class<?> c)
            throws Exception {
        // Introspector caches BeanInfo classes for better performance
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(c);

        } catch (IntrospectionException e) {
            throw new Exception(
                    "Bean introspection failed: " + e.getMessage());
        }

        return beanInfo.getPropertyDescriptors();
    }

    private static boolean isCompatibleType(Object value, Class<?> type) {
        // Do object check first, then primitives
        if (value == null || type.isInstance(value)) {
            return true;

        } else if (type.equals(Integer.TYPE) && value instanceof Integer) {
            return true;

        } else if (type.equals(Long.TYPE) && value instanceof Long) {
            return true;

        } else if (type.equals(Double.TYPE) && value instanceof Double) {
            return true;

        } else if (type.equals(Float.TYPE) && value instanceof Float) {
            return true;

        } else if (type.equals(Short.TYPE) && value instanceof Short) {
            return true;

        } else if (type.equals(Byte.TYPE) && value instanceof Byte) {
            return true;

        } else if (type.equals(Character.TYPE) && value instanceof Character) {
            return true;

        } else if (type.equals(Boolean.TYPE) && value instanceof Boolean) {
            return true;

        }
        return false;

    }

    private static void callSetter(Object target, PropertyDescriptor prop, Object value)
            throws Exception {

        Method setter = prop.getWriteMethod();

        if (setter == null) {
            return;
        }

        Class<?>[] params = setter.getParameterTypes();
        try {
            final String targetType = params[0].getName();

            // convert types for some popular ones
            if (value instanceof java.util.Date) {
                if ("java.sql.Date".equals(targetType)) {
                    value = new java.sql.Date(((java.util.Date) value).getTime());
                } else
                if ("java.sql.Time".equals(targetType)) {
                    value = new java.sql.Time(((java.util.Date) value).getTime());
                } else
                if ("java.sql.Timestamp".equals(targetType)) {
                    Timestamp tsValue = (Timestamp) value;
                    int nanos = tsValue.getNanos();
                    value = new java.sql.Timestamp(tsValue.getTime());
                    ((Timestamp) value).setNanos(nanos);
                }
            } else {
                if (value instanceof String) {
                    if(params[0].isEnum()) {
                        value = Enum.valueOf(params[0].asSubclass(Enum.class), (String) value);
                    }
                } else {
                    String fsType =params[0].getSimpleName();
                    Class<?> class1 = Class.forName(targetType);
                    Method method = class1.getMethod("parse" + fixParse(fsType),String.class);
                    if (method != null) {
                        Object rec = method.invoke(null, value);
                        value = rec;
                    }
                }
            }


            // Don't call setter if the value object isn't the right type
            if (isCompatibleType(value, params[0])) {
                setter.invoke(target, new Object[]{value});
            } else {
                throw new SQLException(
                        "Cannot set " + prop.getName() + ": incompatible types, cannot convert "
                                + value.getClass().getName() + " to " + params[0].getName());
                // value cannot be null here because isCompatibleType allows null
            }

        } catch (IllegalArgumentException e) {
            throw new SQLException(
                    "Cannot set " + prop.getName() + ": " + e.getMessage());

        } catch (IllegalAccessException e) {
            throw new SQLException(
                    "Cannot set " + prop.getName() + ": " + e.getMessage());

        } catch (InvocationTargetException e) {
            throw new SQLException(
                    "Cannot set " + prop.getName() + ": " + e.getMessage());
        }
    }

    private static String fixParse(String fsType) {
        switch (fsType) {
            case "Integer":
                return "Int";
            default:
                return fsType;
        }
    }

}
