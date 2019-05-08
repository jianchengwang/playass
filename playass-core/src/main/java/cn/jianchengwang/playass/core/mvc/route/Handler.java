package cn.jianchengwang.playass.core.mvc.route;

import cn.jianchengwang.playass.core.kit.ReflectKit;
import cn.jianchengwang.playass.core.mvc.context.H;
import cn.jianchengwang.playass.core.mvc.context.param.ParamMap;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class Handler {
    String value;
    H.Method[] allowHttpMethods;

    Method executeMethod;

    Map<String, Parameter> parameterMap = new LinkedHashMap<>();

    public Handler(String value, H.Method[] allowHttpMethods, Method executeMethod) {
        this.value = value;
        this.allowHttpMethods = allowHttpMethods;
        this.executeMethod = executeMethod;

        Parameter[] parameters = executeMethod.getParameters();
        if(parameters != null) {
            for (Parameter p : parameters) {
                parameterMap.put(p.getName(), p);
            }
        }

    }

    public Object[] getFieldList(ParamMap paramMap) {

        final List<Object> fieldList = new LinkedList<>();
        parameterMap.forEach((k, v) -> {
            if(paramMap.containsKey(k)) {
                System.out.println(v.getParameterizedType().getTypeName());

                switch (v.getParameterizedType().getTypeName()) {
                    case "java.lang.String":
                        fieldList.add(paramMap.getString(k));
                        break;
                    case "java.lang.Integer":
                        fieldList.add(paramMap.getInteger(k));
                        break;
                    case "java.lang.Long":
                        fieldList.add(paramMap.getLong(k));
                        break;
                    case "java.lang.Double":
                        fieldList.add(paramMap.getDouble(k));
                        break;
                    case "java.lang.Float":
                        fieldList.add(paramMap.getFloat(k));
                        break;
                    case "java.lang.Boolean":
                        fieldList.add(paramMap.getBoolean(k));
                        break;

                    default:

                        fieldList.add(paramMap.get(k));

                        break;
                }


            } else {

                try {
                    Class clazz = Class.forName(v.getParameterizedType().getTypeName());
                    Object instance = null;
                    Field[] fields = clazz.getDeclaredFields();
                    for(Field field: fields) {
                        if(paramMap.containsKey(field.getName())) {
                            if(instance == null) instance = clazz.newInstance();
                            ReflectKit.invokeSetMethod(clazz, field, instance, paramMap.get(field.getName()));
                        }
                    }
                    fieldList.add(instance);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        return fieldList.toArray();

    }
}
