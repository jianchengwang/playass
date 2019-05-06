package cn.jianchengwang.playass.core.mvc.action;


import cn.jianchengwang.playass.core.mvc.context.WebContext;

public interface IWorkAction {
    void execute(WebContext context) throws Exception;
}
