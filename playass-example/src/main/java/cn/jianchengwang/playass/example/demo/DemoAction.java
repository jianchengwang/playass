package cn.jianchengwang.playass.example.demo;


import cn.jianchengwang.playass.core.mvc.action.IWorkAction;
import cn.jianchengwang.playass.core.mvc.annotation.action.Action;
import cn.jianchengwang.playass.core.mvc.context.WebContext;
import cn.jianchengwang.playass.core.mvc.context.wrapper.Rq;

@Action("demo")
public class DemoAction implements IWorkAction {

    @Override
    public void execute(WebContext context) throws Exception {

        System.out.println("demo action execute .....");

        Rq rq = context.getRq();

        String name = rq.getString("name");
        Integer age = rq.getInteger("age");
        User user = new User(name, age);

        System.out.println("name: " + user.getName());
        System.out.println("age: " + user.getAge());

        WebContext.me().getRp().json(user);

        System.out.println("demo action execute done .....");

    }
}
