package cn.jianchengwang.playass.example.demo;


import cn.jianchengwang.playass.core.mvc.annotation.Param;
import cn.jianchengwang.playass.core.mvc.annotation.Path;
import cn.jianchengwang.playass.core.mvc.annotation.action.Action;
import cn.jianchengwang.playass.core.mvc.WebContext;
import cn.jianchengwang.playass.core.mvc.http.request.HttpReq;
import cn.jianchengwang.playass.core.mvc.http.response.HttpResp;

@Path("demo")
public class DemoAction {

    @Action
    public void test(String name, Integer age) throws Exception {

        System.out.println("demo action execute .....");

        User user = new User(name, age);

        System.out.println("name: " + user.getName());
        System.out.println("age: " + user.getAge());

        WebContext.me().getHttpResp().json(user);

        System.out.println("demo action execute done .....");

    }

    @Action
    public User test1(User user) throws Exception {

        System.out.println("demo action execute .....");

        System.out.println("name: " + user.getName());
        System.out.println("age: " + user.getAge());

        System.out.println("demo action execute done .....");

        return user;

    }

    @Action("test/{id}")
    public void testPath(@Param("id") Integer id, String name, Integer age) throws Exception {
        System.out.println("demo action testPath execute .....");
        WebContext.me().getHttpResp().json(new User(id,name,age));
        System.out.println("demo action testPath execute done .....");
    }
}
