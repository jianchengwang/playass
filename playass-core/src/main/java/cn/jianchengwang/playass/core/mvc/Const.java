package cn.jianchengwang.playass.core.mvc;


import cn.jianchengwang.playass.core.kit.PlayAssKit;
import cn.jianchengwang.playass.core.kit.StrKit;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface Const {

    public static final ObjectMapper MAPPER = new ObjectMapper();

    int          DEFAULT_SERVER_PORT        = 9000;
    String       DEFAULT_SERVER_ADDRESS     = "0.0.0.0";
    String       LOCAL_IP_ADDRESS           = "127.0.0.1";
    String       VERSION                    = "2.0.15.ALPHA";
    String       WEB_JARS                   = "/webjars/";
    String       CLASSPATH                  = PlayAssKit.getCurrentClassPath();
    String       CONTENT_TYPE_HTML          = "text/html; charset=UTF-8";
    String       CONTENT_TYPE_JSON          = "application/json; charset=UTF-8";
    String       CONTENT_TYPE_TEXT          = "text/plain; charset=UTF-8";
    String       HTTP_DATE_FORMAT           = "EEE, dd MMM yyyy HH:mm:ss zzz";
    String       INTERNAL_SERVER_ERROR_HTML = "<center><h1>500 Internal Server Error</h1><hr/></center>";
    String       DEFAULT_THREAD_NAME        = "_(:3」∠)_";
    List<String> PLUGIN_PACKAGE_NAME        = new ArrayList<>(Collections.singletonList("cn.jianchengwang.playass.plugin"));
    List<String> DEFAULT_STATICS            = new ArrayList<>(
            Arrays.asList("/favicon.ico", "/robots.txt", "/static", "/upload", "/webjars/"));

    @Deprecated
    String PROP_NAME0 = "classpath:app.properties";
    String PROP_NAME  = "classpath:application.properties";

    // Env key
    String ENV_KEY_DEV_MODE                  = "app.devMode";
    String ENV_KEY_APP_NAME                  = "app.name";
    String ENV_KEY_APP_ENV                   = "app.env";
    String ENV_KEY_APP_THREAD_NAME           = "app.thread-name";
    String ENV_KEY_APP_WATCH_ENV             = "app.watch-env";
    String ENV_KEY_BANNER_PATH               = "app.banner-path";
    String ENV_KEY_TASK_THREAD_COUNT         = "app.task.thread-count";
    String ENV_KEY_CONTEXT_PATH              = "app.context-path";
    String ENV_KEY_GZIP_ENABLE               = "http.gzip.enable";
    String ENV_KEY_CORS_ENABLE               = "http.cors.enable";
    String ENV_KEY_SESSION_KEY               = "http.session.key";
    String ENV_KEY_SESSION_TIMEOUT           = "http.session.timeout";
    String ENV_KEY_HTTP_CACHE_TIMEOUT        = "http.cache.timeout";
    String ENV_KEY_HTTP_REQUEST_COST         = "http.request.cost";
    String ENV_KEY_PAGE_404                  = "mvc.view.404";
    String ENV_KEY_PAGE_500                  = "mvc.view.500";
    String ENV_KEY_STATIC_DIRS               = "mvc.statics";
    String ENV_KEY_STATIC_LIST               = "mvc.statics.show-list";
    String ENV_KEY_TEMPLATE_PATH             = "mvc.template.path";
    String ENV_KEY_SERVER_ADDRESS            = "server.address";
    String ENV_KEY_SERVER_PORT               = "server.port";
    String ENV_KEY_PERFORMANCE               = "server.performance";
    String ENV_KEY_SSL                       = "server.ssl.enable";
    String ENV_KEY_SSL_CERT                  = "server.ssl.cert-path";
    String ENE_KEY_SSL_PRIVATE_KEY           = "server.ssl.private-key-path";
    String ENE_KEY_SSL_PRIVATE_KEY_PASS      = "server.ssl.private-key-pass";
    String ENC_KEY_NETTY_ACCEPT_THREAD_COUNT = "server.netty.accept-thread-count";
    String ENV_KEY_NETTY_IO_THREAD_COUNT     = "server.netty.io-thread-count";

    String ENV_KEY_BOOT_CONF        = "boot_conf";
    String ENV_KEY_AUTO_REFRESH_DIR = "app.auto.refresh.dir";

    // netty default config
    int DEFAULT_ACCEPT_THREAD_COUNT = 1;
    int DEFAULT_IO_THREAD_COUNT     = 0;

    /**
     * A request processing ends the elapsed time, in milliseconds.
     */
    String REQUEST_COST_TIME = "costTime";

    String NEW_LINE = "\r\n";

    int    BANNER_PADDING = 60;
    String BANNER_TEXT    = NEW_LINE +
            StrKit.padLeft("__, _,   _, __, __,", BANNER_PADDING) + NEW_LINE +
            StrKit.padLeft("|_) |   /_\\ | \\ |_", BANNER_PADDING - 1) + NEW_LINE +
            StrKit.padLeft("|_) | , | | |_/ |", BANNER_PADDING - 2) + NEW_LINE +
            StrKit.padLeft("~   ~~~ ~ ~ ~   ~~~", BANNER_PADDING);


}
