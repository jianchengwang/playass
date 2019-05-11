package cn.jianchengwang.playass.core.netty.handler;

import cn.jianchengwang.playass.core.mvc.WebContext;
import cn.jianchengwang.playass.core.mvc.handler.RequestHandler;

public class StaticFileHandler implements RequestHandler {


    @Override
    public void handle(WebContext webContext) throws Exception {

        System.out.println("handler static file");

    }
}
