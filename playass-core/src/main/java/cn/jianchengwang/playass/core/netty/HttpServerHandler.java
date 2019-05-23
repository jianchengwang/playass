package cn.jianchengwang.playass.core.netty;

import cn.jianchengwang.playass.core.mvc.Const;
import cn.jianchengwang.playass.core.mvc.WebContext;
import cn.jianchengwang.playass.core.mvc.http.request.HttpReq;
import cn.jianchengwang.playass.core.mvc.http.response.HttpResp;
import cn.jianchengwang.playass.core.mvc.route.RouteMatcher;
import cn.jianchengwang.playass.core.mvc.route.meta.Route;
import cn.jianchengwang.playass.core.netty.handler.RouteMethodHandler;
import cn.jianchengwang.playass.core.netty.handler.StaticFileHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final Set<String> notStaticUri = new HashSet<>(32);

    private final StaticFileHandler staticFileHandler = new StaticFileHandler();
    private final RouteMethodHandler routeHandler = new RouteMethodHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {

        HttpReq httpReq = new HttpReq(fullHttpRequest);

        CompletableFuture<HttpReq> future = CompletableFuture.completedFuture(httpReq);

        Executor executor = ctx.executor();

        future.thenApplyAsync(req -> buildWebContext(ctx, fullHttpRequest), executor)
                .thenApplyAsync(this::executeLogic, executor)
                .thenApplyAsync(this::buildResponse, executor)
                .thenAcceptAsync(msg -> writeResponse(ctx, future, msg), ctx.channel().eventLoop());

    }

    private WebContext buildWebContext(ChannelHandlerContext ctx,
                                       FullHttpRequest fullHttpReq) {

        String remoteAddress = ctx.channel().remoteAddress().toString();
        return WebContext.create(new HttpReq(fullHttpReq, remoteAddress), new HttpResp(), ctx);
    }

    private WebContext executeLogic(WebContext webContext) {
        try {
            WebContext.set(webContext);
            HttpReq httpReq = webContext.getHttpReq();
            String method = httpReq.getNettyFullReq().method().name();
            String uri = httpReq.getUri();

            if(isStaticFile(method, uri)) {
                staticFileHandler.handle(webContext);
            } else {
                Route route = RouteMatcher.match(method, uri);
                if (null != route) {
                    webContext.setRoute(route);
                } else {
                    throw new Exception(uri);
                }
                routeHandler.handle(webContext);
            }

            return webContext;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    private FullHttpResponse buildResponse(WebContext webContext) {
        WebContext.set(webContext);
        return routeHandler.handleResponse(
                webContext.getHttpReq(), webContext.getHttpResp(),
                webContext.getCtx()
        );
    }

    private void writeResponse(ChannelHandlerContext ctx, CompletableFuture<HttpReq> future, FullHttpResponse msg) {
        ctx.writeAndFlush(msg);
        future.complete(null);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private boolean isStaticFile(String method, String uri) {
        if (HttpMethod.POST.name().equals(method)) {
            return false;
        }

        Optional<String> result = Const.DEFAULT_STATICS.stream()
                .filter(s -> s.equals(uri) || uri.startsWith(s)).findFirst();

        if (!result.isPresent()) {
            notStaticUri.add(uri);
            return false;
        }
        return true;
    }
}
