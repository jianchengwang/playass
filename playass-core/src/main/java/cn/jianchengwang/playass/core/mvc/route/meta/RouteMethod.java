package cn.jianchengwang.playass.core.mvc.route.meta;

import cn.jianchengwang.playass.core.kit.ReflectKit;
import cn.jianchengwang.playass.core.mvc.annotation.Param;
import cn.jianchengwang.playass.core.mvc.http.HttpMethod;
import cn.jianchengwang.playass.core.mvc.WebContext;
import cn.jianchengwang.playass.core.mvc.http.request.HttpReq;
import cn.jianchengwang.playass.core.mvc.http.request.ParamMap;
import cn.jianchengwang.playass.core.mvc.http.response.HttpResp;
import cn.jianchengwang.playass.core.mvc.route.process.BeanProcess;
import lombok.Data;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
public class RouteMethod {
    String value;
    HttpMethod[] allowHttpMethods;

    Method executeMethod;

    Map<String, Parameter> parameterMap = new LinkedHashMap<>();

    public RouteMethod(String value, HttpMethod[] allowHttpMethods, Method executeMethod) {
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

            boolean isParam = false;
            Param param = v.getAnnotation(Param.class);
            if(param != null) {
                k = param.value();
                isParam = true;
            }

            String parameterTypeName = v.getParameterizedType().getTypeName();

            if(paramMap.containsKey(k)) {

                switch (parameterTypeName) {
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

                if(!parameterTypeName.startsWith("java.lang.")) {

                    try {
                        Class clazz = ReflectKit.getClass(parameterTypeName);

                        if(clazz == HttpReq.class) {
                            fieldList.add(WebContext.me().getHttpReq());
                        } else if(clazz == HttpResp.class) {
                            fieldList.add(WebContext.me().getHttpResp());
                        } else {
                            Object instance = BeanProcess.toBean(paramMap, clazz);
                            fieldList.add(instance);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    if(isParam) {
                        if(param.notBlank() || param.notNull()) {
                            System.out.println("参数异常");
                        }
                    }
                    fieldList.add(null);
                }

            }
        });

        return fieldList.toArray();

    }
}
