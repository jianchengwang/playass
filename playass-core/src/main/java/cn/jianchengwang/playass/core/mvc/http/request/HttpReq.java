package cn.jianchengwang.playass.core.mvc.http.request;

import cn.jianchengwang.playass.core.mvc.route.RouteMatcher;
import cn.jianchengwang.playass.core.mvc.route.meta.Route;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;

import lombok.Data;

import java.util.List;

@Data
public class HttpReq implements IParam {

    private String uri;
    private String remoteAddress;

    private FullHttpRequest nettyFullReq;
    private ParamMap paramMap;

    public HttpReq(FullHttpRequest nettyFullReq, String remoteAddress) {

        this(nettyFullReq);
        this.remoteAddress = remoteAddress.substring(1);
    }

    public HttpReq(FullHttpRequest nettyFullReq)  {

        this.nettyFullReq = nettyFullReq;
        buildParamMap();

        String uri = this.nettyFullReq.uri();
        if(uri.contains("?")) {
            uri = uri.substring(0, uri.indexOf("?"));
        }

        this.uri = uri;

    }

    private void buildParamMap() {

        HttpMethod method = nettyFullReq.method();

        ParamMap paramMap = new ParamMap();

        try {
            if (HttpMethod.GET == method) {
                // 是GET请求
                QueryStringDecoder decoder = new QueryStringDecoder(nettyFullReq.uri());
                decoder.parameters().entrySet().forEach( entry -> {
                    // entry.getValue()是一个List, 只取第一个元素
                    paramMap.put(entry.getKey(), entry.getValue().get(0));
                });
            } else if (HttpMethod.POST == method) {
                // 是POST请求
                HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(nettyFullReq);
                decoder.offer(nettyFullReq);

                List<InterfaceHttpData> parmList = decoder.getBodyHttpDatas();

                for (InterfaceHttpData parm : parmList) {

                    Attribute data = (Attribute) parm;
                    paramMap.put(data.getName(), data.getValue());
                }

            } else {
                // 不支持其它方法
                throw new RuntimeException(""); // 这是个自定义的异常, 可删掉这一行
            }

            this.paramMap = paramMap;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

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
