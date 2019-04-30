package cn.jianchengwang.playass.core.context;

import cn.jianchengwang.playass.core.context.wrapper.Rp;
import cn.jianchengwang.playass.core.context.wrapper.Rq;
import lombok.Data;

@Data
public class WebContext {

    private static final ThreadLocal<WebContext> CONTEXT = new ThreadLocal<>();

    private Rq rq;
    private Rp rp;

    public WebContext() {
    }

    public static void init(Rq rq, Rp rp) {
        WebContext context = new WebContext();
        context.rq = rq;
        context.rp = rp;
        CONTEXT.set(context);
    }

    public static WebContext me() {
        return CONTEXT.get();
    }

    public static void remove() {
        CONTEXT.remove();
    }




}
