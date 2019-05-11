package cn.jianchengwang.playass.core.netty.handler;

import cn.jianchengwang.playass.core.mvc.WebContext;
import cn.jianchengwang.playass.core.mvc.handler.RequestHandler;
import cn.jianchengwang.playass.core.mvc.http.request.HttpReq;
import cn.jianchengwang.playass.core.mvc.http.request.ParamMap;
import cn.jianchengwang.playass.core.mvc.http.response.HttpResp;
import cn.jianchengwang.playass.core.mvc.route.meta.Route;
import cn.jianchengwang.playass.core.mvc.route.meta.RouteMethod;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;

import javax.xml.ws.Response;
import java.lang.reflect.Method;
import java.util.Map;

public class RouteMethodHandler implements RequestHandler {

    @Override
    public void handle(WebContext webContext) throws Exception {

        Route route = webContext.getRoute();
        HttpReq httpReq = webContext.getHttpReq();

        ParamMap paramMap = httpReq.getParamMap();
        paramMap.mergePathParam(route.getPathParamMap());

        RouteMethod routeMethod = route.getRouteMethod();

        Method executeMethod = routeMethod.getExecuteMethod();
        Object returnParam = executeMethod.invoke(route.getClazz().newInstance(), routeMethod.getFieldList(paramMap));

        if (returnParam != null) {
            webContext.getHttpResp().json(returnParam);
        }

    }

    public FullHttpResponse handleResponse(HttpReq httpReq, HttpResp httpResp, ChannelHandlerContext context) {
        return httpResp.getRaw();
    }
}