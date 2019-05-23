package cn.jianchengwang.playass.core.netty.handler;

import cn.jianchengwang.playass.core.mvc.Const;
import cn.jianchengwang.playass.core.mvc.WebContext;
import cn.jianchengwang.playass.core.mvc.handler.RequestHandler;
import cn.jianchengwang.playass.core.mvc.http.HttpMethod;
import cn.jianchengwang.playass.core.mvc.http.response.HttpResp;
import cn.jianchengwang.playass.core.netty.HttpConst;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;
import io.netty.util.internal.SystemPropertyUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.*;

@Slf4j
public class StaticFileHandler implements RequestHandler {

    public static final String HTTP_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";
    public static final String HTTP_DATE_GMT_TIMEZONE = "GMT";
    public static final int HTTP_CACHE_SECONDS = 60;

    private static final String STYLE = "body{background:#fff;margin:0;padding:30px;-webkit-font-smoothing:antialiased;font-family:Menlo,Consolas,monospace}main{max-width:920px}header{display:flex;justify-content:space-between}#toggle{display:none;cursor:pointer}#toggle:before{display:inline-block;content:url(\"data:image/svg+xml; utf8, <svg height='24px' version='1.1' viewBox='0 0 24 24' width='24px' xmlns='http://www.w3.org/2000/svg'><g fill='none' fill-rule='evenodd' stroke='none' stroke-width='1'><g transform='translate(-431.000000, -479.000000)'><g transform='translate(215.000000, 119.000000)'/><path d='M432,480 L432,486 L438,486 L438,480 L432,480 Z M440,480 L440,486 L446,486 L446,480 L440,480 Z M448,480 L448,486 L454,486 L454,480 L448,480 Z M449,481 L449,485 L453,485 L453,481 L449,481 Z M441,481 L441,485 L445,485 L445,481 L441,481 Z M433,481 L433,485 L437,485 L437,481 L433,481 Z M432,488 L432,494 L438,494 L438,488 L432,488 Z M440,488 L440,494 L446,494 L446,488 L440,488 Z M448,488 L448,494 L454,494 L454,488 L448,488 Z M449,489 L449,493 L453,493 L453,489 L449,489 Z M441,489 L441,493 L445,493 L445,489 L441,489 Z M433,489 L433,493 L437,493 L437,489 L433,489 Z M432,496 L432,502 L438,502 L438,496 L432,496 Z M440,496 L440,502 L446,502 L446,496 L440,496 Z M448,496 L448,502 L454,502 L454,496 L448,496 Z M449,497 L449,501 L453,501 L453,497 L449,497 Z M441,497 L441,501 L445,501 L445,497 L441,497 Z M433,497 L433,501 L437,501 L437,497 L433,497 Z' fill='#000000'/></g></g></svg>\")}#toggle.single-column:before{content:url(\"data:image/svg+xml; utf8, <svg height='24px' viewBox='0 0 24 24' width='24px' xmlns='http://www.w3.org/2000/svg'><g fill='none' fill-rule='evenodd' id='miu' stroke='none' stroke-width='1'><g transform='translate(-359.000000, -479.000000)'><g transform='translate(215.000000, 119.000000)'/><path d='M360.577138,485 C360.258394,485 360,485.221932 360,485.5 C360,485.776142 360.262396,486 360.577138,486 L381.422862,486 C381.741606,486 382,485.778068 382,485.5 C382,485.223858 381.737604,485 381.422862,485 L360.577138,485 L360.577138,485 Z M360.577138,490 C360.258394,490 360,490.221932 360,490.5 C360,490.776142 360.262396,491 360.577138,491 L381.422862,491 C381.741606,491 382,490.778068 382,490.5 C382,490.223858 381.737604,490 381.422862,490 L360.577138,490 L360.577138,490 Z M360.577138,495 C360.258394,495 360,495.221932 360,495.5 C360,495.776142 360.262396,496 360.577138,496 L381.422862,496 C381.741606,496 382,495.778068 382,495.5 C382,495.223858 381.737604,495 381.422862,495 L360.577138,495 L360.577138,495 Z' fill='#000000'/></g></g></svg>\")}a{color:#1A00F2;text-decoration:none}h1{font-size:18px;font-weight:500;margin-top:0;color:#000;font-family:-apple-system,Helvetica;display:flex}h1 a{color:inherit;font-weight:700;border-bottom:1px dashed transparent}h1 a::after{content:'/'}h1 a:hover{color:#7d7d7d}h1 i{font-style:normal}ul{margin:0;padding:20px 0 0 0}ul.single-column{flex-direction:column}ul li{list-style:none;padding:10px 0;font-size:14px;display:flex;justify-content:space-between}ul li i{color:#9B9B9B;font-size:11px;display:block;font-style:normal;white-space:nowrap;padding-left:15px}ul a{color:#1A00F2;white-space:nowrap;overflow:hidden;display:block;text-overflow:ellipsis}ul a::before{content:url(\"data:image/svg+xml; utf8, <svg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 64 64'><g fill='transparent' stroke='currentColor' stroke-miterlimit='10'><path stroke-width='4' d='M50.46 56H13.54V8h22.31a4.38 4.38 0 0 1 3.1 1.28l10.23 10.24a4.38 4.38 0 0 1 1.28 3.1z'/><path stroke-width='2' d='M35.29 8.31v14.72h14.06'/></g></svg>\");display:inline-block;vertical-align:middle;margin-right:10px}ul a:hover{color:#000}ul a[class=''] + i{display:none}ul a[class='']::before{content:url(\"data:image/svg+xml; utf8, <svg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 64 64'><path fill='transparent' stroke='currentColor' stroke-width='4' stroke-miterlimit='10' d='M56 53.71H8.17L8 21.06a2.13 2.13 0 0 1 2.13-2.13h2.33l2.13-4.28A4.78 4.78 0 0 1 18.87 12h9.65a4.78 4.78 0 0 1 4.28 2.65l2.13 4.28h17.36a3.55 3.55 0 0 1 3.55 3.55z'/></svg>\")}ul a[class='gif']::before,ul a[class='jpg']::before,ul a[class='png']::before,ul a[class='svg']::before{content:url(\"data:image/svg+xml; utf8, <svg width='16' height='16' viewBox='0 0 80 80' xmlns='http://www.w3.org/2000/svg' fill='none' stroke='currentColor' stroke-width='5' stroke-linecap='round' stroke-linejoin='round'><rect x='6' y='6' width='68' height='68' rx='5' ry='5'/><circle cx='24' cy='24' r='8'/><path d='M73 49L59 34 37 52M53 72L27 42 7 58'/></svg>\");width:16px}@media (min-width:768px){#toggle{display:inline-block}ul{display:flex;flex-wrap:wrap}ul li{width:230px;padding-right:20px}ul.single-column li{width:auto}}@media (min-width:992px){body{padding:45px}h1{font-size:15px}ul li{font-size:13px;box-sizing:border-box;justify-content:flex-start}ul li:hover i{opacity:1}ul li i{font-size:10px;opacity:0;margin-left:10px;margin-top:3px;padding-left:0}}";
    private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[^-._]?[^<>&\"]*");

    private FullHttpRequest request;
    private WebContext webContext;

    @Override
    public void handle(WebContext webContext) throws Exception {

        this.webContext = webContext;
        this.request = webContext.getHttpReq().getNettyFullReq();

        ChannelHandlerContext ctx = webContext.getCtx();

        if (!request.decoderResult().isSuccess()) {
            sendError(ctx, BAD_REQUEST);
            return;
        }

        if (!HttpMethod.GET.name().equals(request.method().name())) {
            sendError(ctx, METHOD_NOT_ALLOWED);
            return;
        }

        final boolean keepAlive = HttpUtil.isKeepAlive(request);
        final String uri = request.uri();
        final String path = sanitizeUri(uri);
        if (path == null) {
            this.sendError(ctx, FORBIDDEN);
            return;
        }

        File file = new File(path);
        if (file.isHidden() || !file.exists()) {
            this.sendError(ctx, NOT_FOUND);
            return;
        }

        if (file.isDirectory()) {
            if (uri.endsWith("/")) {
                this.sendListing(ctx, file, uri);
            } else {
                this.sendRedirect(ctx, uri + '/');
            }
            return;
        }

        if (!file.isFile()) {
            sendError(ctx, FORBIDDEN);
            return;
        }

        // Cache Validation
        String ifModifiedSince = request.headers().get(HttpHeaderNames.IF_MODIFIED_SINCE);
        if (ifModifiedSince != null && !ifModifiedSince.isEmpty()) {
            SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.CHINA);
            Date ifModifiedSinceDate = dateFormatter.parse(ifModifiedSince);

            // Only compare up to the second because the datetime format we send to the client
            // does not have milliseconds
            long ifModifiedSinceDateSeconds = ifModifiedSinceDate.getTime() / 1000;
            long fileLastModifiedSeconds = file.lastModified() / 1000;
            if (ifModifiedSinceDateSeconds == fileLastModifiedSeconds) {
                this.sendNotModified(ctx);
                return;
            }
        }

        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException ignore) {
            sendError(ctx, NOT_FOUND);
            return;
        }
        long fileLength = raf.length();

        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
        HttpUtil.setContentLength(response, fileLength);
        setContentTypeHeader(response, file);
        setDateAndCacheHeaders(response, file);

        if (!keepAlive) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        } else if (request.protocolVersion().equals(HTTP_1_0)) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }

        // Write the initial line and the header.
        ctx.write(response);

        // Write the content.
        ChannelFuture sendFileFuture;
        ChannelFuture lastContentFuture;
        if (ctx.pipeline().get(SslHandler.class) == null) {
            sendFileFuture =
                    ctx.write(new DefaultFileRegion(raf.getChannel(), 0, fileLength), ctx.newProgressivePromise());
            // Write the end marker.
            lastContentFuture = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
        } else {
            sendFileFuture =
                    ctx.writeAndFlush(new HttpChunkedInput(new ChunkedFile(raf, 0, fileLength, 8192)),
                            ctx.newProgressivePromise());
            // HttpChunkedInput will write the end marker (LastHttpContent) for us.
            lastContentFuture = sendFileFuture;
        }

        sendFileFuture.addListener(new ChannelProgressiveFutureListener() {
            @Override
            public void operationProgressed(ChannelProgressiveFuture future, long progress, long total) {
                if (total < 0) { // total unknown
                    System.err.println(future.channel() + " Transfer progress: " + progress);
                } else {
                    System.err.println(future.channel() + " Transfer progress: " + progress + " / " + total);
                }
            }

            @Override
            public void operationComplete(ChannelProgressiveFuture future) {
                System.err.println(future.channel() + " Transfer complete.");
            }
        });

        // Decide whether to close the connection or not.
        if (!keepAlive) {
            // Close the connection when the whole content is written out.
            lastContentFuture.addListener(ChannelFutureListener.CLOSE);
        }

    }

    private void sendNotModified(ChannelHandlerContext ctx) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, NOT_MODIFIED);
        setDateHeader(response);

        this.sendAndCleanupConnection(ctx, response);
    }

    private static void setDateHeader(FullHttpResponse response) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));

        Calendar time = new GregorianCalendar();
        response.headers().set(HttpHeaderNames.DATE, dateFormatter.format(time.getTime()));
    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        var response = new DefaultFullHttpResponse(HTTP_1_1, status,
                Unpooled.copiedBuffer("Failure: " + status + "\r\n", CharsetUtil.UTF_8));

        response.headers().set(HttpConst.CONTENT_TYPE, Const.CONTENT_TYPE_TEXT);
        // Close the connection as soon as the error message is sent.
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");
    private static String sanitizeUri(String uri) {
        // Decode the path.
        try {
            uri = URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new Error(e);
        }

        if (uri.isEmpty() || uri.charAt(0) != '/') {
            return null;
        }

        // Convert file separators.
        uri = uri.replace('/', File.separatorChar);

        // Simplistic dumb security check.
        // You will have to do something serious in the production environment.
        if (uri.contains(File.separator + '.') ||
                uri.contains('.' + File.separator) ||
                uri.charAt(0) == '.' || uri.charAt(uri.length() - 1) == '.' ||
                INSECURE_URI.matcher(uri).matches()) {
            return null;
        }

        // Convert to absolute path.
        return SystemPropertyUtil.get("user.dir") + File.separator + uri;
    }

    private void sendListing(ChannelHandlerContext ctx, File dir, String dirPath) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/html; charset=UTF-8");

        StringBuilder buf = new StringBuilder()
                .append("<!DOCTYPE html>\r\n")
                .append("<html><head><meta charset='utf-8' /><title>")
                .append("Listing of: ")
                .append(dirPath)
                .append("</title><style>" + STYLE + "</style></head><body>\r\n")

                .append("<h3>Listing of: ")
                .append(dirPath)
                .append("</h3>\r\n")

                .append("<ul>")
                .append("<li><a href=\"../\">..</a></li>\r\n");

        for (File f: dir.listFiles()) {
            if (f.isHidden() || !f.canRead()) {
                continue;
            }

            String name = f.getName();
            if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
                continue;
            }

            buf.append("<li><a href=\"")
                    .append(name)
                    .append("\">")
                    .append(name)
                    .append("</a></li>\r\n");
        }

        buf.append("</ul></body></html>\r\n");
        ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
        response.content().writeBytes(buffer);
        buffer.release();

        this.sendAndCleanupConnection(ctx, response);
    }

    private void sendRedirect(ChannelHandlerContext ctx, String newUri) {
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
        response.headers().set(HttpHeaderNames.LOCATION, newUri);

        this.sendAndCleanupConnection(ctx, response);
    }

    /**
     * If Keep-Alive is disabled, attaches "Connection: close" header to the response
     * and closes the connection after the response being sent.
     */
    private void sendAndCleanupConnection(ChannelHandlerContext ctx, FullHttpResponse response) {
        final FullHttpRequest request = this.request;
        final boolean keepAlive = HttpUtil.isKeepAlive(request);
        HttpUtil.setContentLength(response, response.content().readableBytes());
        if (!keepAlive) {
            // We're going to close the connection as soon as the response is sent,
            // so we should also make it clear for the client.
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        } else if (request.protocolVersion().equals(HTTP_1_0)) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }

        this.webContext.setHttpResp(new HttpResp(response));
    }

    private static void setDateAndCacheHeaders(HttpResponse response, File fileToCache) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(HTTP_DATE_FORMAT, Locale.US);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(HTTP_DATE_GMT_TIMEZONE));

        // Date header
        Calendar time = new GregorianCalendar();
        response.headers().set(HttpHeaderNames.DATE, dateFormatter.format(time.getTime()));

        // Add cache headers
        time.add(Calendar.SECOND, HTTP_CACHE_SECONDS);
        response.headers().set(HttpHeaderNames.EXPIRES, dateFormatter.format(time.getTime()));
        response.headers().set(HttpHeaderNames.CACHE_CONTROL, "private, max-age=" + HTTP_CACHE_SECONDS);
        response.headers().set(
                HttpHeaderNames.LAST_MODIFIED, dateFormatter.format(new Date(fileToCache.lastModified())));
    }

    private static void setContentTypeHeader(HttpResponse response, File file) {
        MimetypesFileTypeMap mimeTypesMap = new MimetypesFileTypeMap();
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, mimeTypesMap.getContentType(file.getPath()));
    }

}
