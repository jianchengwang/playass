package cn.jianchengwang.playass.core.mvc.handler;

import cn.jianchengwang.playass.core.mvc.WebContext;

@FunctionalInterface
public interface RequestHandler {

    void handle(WebContext webContext) throws Exception;

}