package cn.jianchengwang.playass.core.bootstrap;


import cn.jianchengwang.playass.core.ioc.BeanContainer;
import cn.jianchengwang.playass.core.ioc.Ioc;
import cn.jianchengwang.playass.core.mvc.route.RouteMatcher;
import lombok.Data;

@Data
public class InitBootstrap {

    private String basePackage;
    private BeanContainer beanContainer;
    private Ioc ioc;

    public InitBootstrap(String basePackage) {

        this.basePackage = basePackage;

    }

    public void init() {


        this.beanContainer = BeanContainer.getInstance();
        beanContainer.loadBeans(basePackage);

        RouteMatcher.loadRoutes();

        ioc = new Ioc();
        ioc.doIoc();

    }
}
