package cn.jianchengwang.playass.core.netty;

import cn.jianchengwang.playass.core.mvc.context.WebContext;
import cn.jianchengwang.playass.core.mvc.context.wrapper.Rp;
import cn.jianchengwang.playass.core.mvc.context.wrapper.Rq;
import cn.jianchengwang.playass.core.mvc.route.Route;
import cn.jianchengwang.playass.core.mvc.route.RouteInfo;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private static final byte[] SUCCESS = { 'S', 'U', 'C', 'C', 'E', 'S', 'S' };
    private static final byte[] NOTFOUND = { 'N', 'O', 'T', 'F', 'O', 'U', 'N', 'D' };

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {

        byte[] responseMsg = SUCCESS;

        System.out.println("uri:" + msg.uri());
        System.out.println("method:" + msg.method().name());
        System.out.println("headers:");msg.headers().forEach(h -> System.out.println(h));

        WebContext.init(
            new Rq(msg),
            new Rp()
        );

        String uri = msg.uri();
        if(uri.indexOf(".do") != -1) {
            uri = uri.substring(0, msg.uri().indexOf(".do"));

            if(uri.indexOf("/") == 0) {
                uri = uri.substring(1, uri.length());
            }

            RouteInfo routeInfo = Route.match(uri);
            if(routeInfo != null) {
                System.out.println("clazz:" + routeInfo.getClazz().getSimpleName());
                System.out.println("handler method:" + routeInfo.getHandler().getExecuteMethod().getName());
                System.out.println("pathParams:");

                routeInfo.getPathParamMap().forEach((k, v) -> {
                    System.out.println(k + ":" + v);
                });

                routeInfo.getHandler().getExecuteMethod().invoke(routeInfo.getClazz().newInstance(), 1);
            } else {
                WebContext.me().getRp().error("NOTFOUND");
            }

        } else {
            WebContext.me().getRp().error("NOTFOUND");
        }

        ChannelFuture f = ctx.writeAndFlush(WebContext.me().getRp().getRaw());
        f.addListener(ChannelFutureListener.CLOSE);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
