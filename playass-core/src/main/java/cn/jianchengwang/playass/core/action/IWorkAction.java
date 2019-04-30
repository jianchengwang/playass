package cn.jianchengwang.playass.core.action;


import cn.jianchengwang.playass.core.context.WebContext;

public interface IWorkAction {
    void execute(WebContext context) throws Exception;
}
