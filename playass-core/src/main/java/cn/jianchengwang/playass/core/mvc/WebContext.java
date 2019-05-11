package cn.jianchengwang.playass.core.mvc;


import cn.jianchengwang.playass.core.mvc.http.request.HttpReq;
import cn.jianchengwang.playass.core.mvc.http.response.HttpResp;
import cn.jianchengwang.playass.core.mvc.route.meta.Route;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

@Data
public class WebContext {

    private static final ThreadLocal<WebContext> CONTEXT = new ThreadLocal<>();

    private ChannelHandlerContext ctx;
    private HttpReq httpReq;
    private HttpResp httpResp;

    private Route route;

    public WebContext() {
    }

    public static WebContext create(HttpReq httpRequest, HttpResp httpResp, ChannelHandlerContext ctx) {
        WebContext context = new WebContext();
        context.httpReq = httpRequest;
        context.httpResp = httpResp;
        context.ctx = ctx;
        return context;
    }

    public static WebContext me() {
        return CONTEXT.get();
    }

    public static void set(WebContext context) {
        CONTEXT.set(context);
    }

    public static void remove() {
        CONTEXT.remove();
    }

}
