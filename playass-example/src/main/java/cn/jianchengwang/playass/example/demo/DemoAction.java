package cn.jianchengwang.playass.example.demo;


import cn.jianchengwang.playass.core.mvc.annotation.Path;
import cn.jianchengwang.playass.core.mvc.annotation.action.Action;
import cn.jianchengwang.playass.core.mvc.context.WebContext;
import cn.jianchengwang.playass.core.mvc.context.wrapper.Rq;

@Path("demo")
public class DemoAction {

    @Action
    public void test(String name, Integer age) throws Exception {

        System.out.println("demo action execute .....");

        Rq rq = WebContext.me().getRq();

        User user = new User(name, age);

        System.out.println("name: " + user.getName());
        System.out.println("age: " + user.getAge());

        WebContext.me().getRp().json(user);

        System.out.println("demo action execute done .....");

    }

    @Action
    public void test1(User user) throws Exception {

        System.out.println("demo action execute .....");

        Rq rq = WebContext.me().getRq();

        System.out.println("name: " + user.getName());
        System.out.println("age: " + user.getAge());

        WebContext.me().getRp().json(user);

        System.out.println("demo action execute done .....");

    }

    @Action("test/{id}")
    public void testPath(Integer id) throws Exception {
        System.out.println("demo action testPath execute .....");
        WebContext.me().getRp().json(new User(id));
        System.out.println("demo action testPath execute done .....");
    }
}
