package cn.jianchengwang.playass.core.context.wrapper;

import cn.jianchengwang.playass.core.context.param.IParam;
import cn.jianchengwang.playass.core.context.param.ParamMap;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import lombok.Data;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

@Data
public class Rq implements IParam {

    private FullHttpRequest fullReq;
    private ParamMap paramMap;

    public Rq(FullHttpRequest fullReq) throws Exception {
        this.fullReq = fullReq;

        buildParamMap();
    }

    private void buildParamMap() throws Exception {

        HttpMethod method = fullReq.method();

        ParamMap paramMap = new ParamMap();

        if (HttpMethod.GET == method) {
            // 是GET请求
            QueryStringDecoder decoder = new QueryStringDecoder(fullReq.uri());
            decoder.parameters().entrySet().forEach( entry -> {
                // entry.getValue()是一个List, 只取第一个元素
                paramMap.put(entry.getKey(), entry.getValue().get(0));
            });
        } else if (HttpMethod.POST == method) {
            // 是POST请求
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(fullReq);
            decoder.offer(fullReq);

            List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();

            for (InterfaceHttpData parm : parmList) {

                Attribute data = (Attribute) parm;
                paramMap.put(data.getName(), data.getValue());
            }

        } else {
            // 不支持其它方法
            throw new Exception(""); // 这是个自定义的异常, 可删掉这一行
        }

        this.paramMap = paramMap;
    }

    private <T> T buildClassInstance(Class<T> clazz) throws Exception {

        T instance = clazz.newInstance();

        Field[] fields = clazz.getDeclaredFields();

        for(Field field: fields) {

            Class fieldClass = field.getDeclaringClass();
            String fieldName = field.getName();

            if(paramMap.containsKey(fieldName)) {

                Method method;
                try {
                    method = clazz.getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), fieldClass);
                } catch (Exception e) {
                    continue;
                }

                if(fieldClass == String.class) {
                    method.invoke(instance, paramMap.getString(fieldName));
                } else if(fieldClass == Boolean.class) {
                    method.invoke(instance, paramMap.getBoolean(fieldName));
                } else if(fieldClass == Integer.class) {
                    method.invoke(instance, paramMap.getInteger(fieldName));
                } else if(fieldClass == Long.class) {
                    method.invoke(instance, paramMap.getLong(fieldName));
                } else if(fieldClass == Double.class) {
                    method.invoke(instance, paramMap.getDouble(fieldName));
                } else if(fieldClass == Float.class) {
                    method.invoke(instance, paramMap.getFloat(fieldName));
                } else {
                    method.invoke(instance, paramMap.get(fieldName));
                }

            }
        }

        return instance;

    }

    @Override
    public String getString(String param) {
        return this.paramMap.getString(param);
    }

    @Override
    public Integer getInteger(String param) {
        return this.paramMap.getInteger(param);
    }

    @Override
    public Long getLong(String param) {
        return this.paramMap.getLong(param);
    }

    @Override
    public Double getDouble(String param) {
        return this.paramMap.getDouble(param);
    }

    @Override
    public Float getFloat(String param) {
        return this.paramMap.getFloat(param);
    }

    @Override
    public Boolean getBoolean(String param) {
        return this.paramMap.getBoolean(param);
    }
}
