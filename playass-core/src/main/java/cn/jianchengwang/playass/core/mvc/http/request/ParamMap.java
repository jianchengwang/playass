package cn.jianchengwang.playass.core.mvc.http.request;

import java.util.HashMap;
import java.util.Map;

public class ParamMap extends HashMap<String,Object> implements IParam {
    @Override
    public String getString(String param) {
        return (String) super.get(param);
    }

    @Override
    public Integer getInteger(String param) {
        return Integer.parseInt(getString(param));
    }

    @Override
    public Long getLong(String param) {
        return Long.parseLong(getString(param));
    }

    @Override
    public Double getDouble(String param) {

        return Double.parseDouble(getString(param));
    }

    @Override
    public Float getFloat(String param) {
        return Float.parseFloat(getString(param));
    }

    @Override
    public Boolean getBoolean(String param) {
        return Boolean.getBoolean(getString(param));
    }

    public void mergePathParam(Map<String, Object> pathParamMap) {

        if(pathParamMap != null) {

            pathParamMap.forEach((k,v) -> {
                this.put(k, v);
            });

        }

    }
}
