package cn.jianchengwang.playass.core.bootstrap;


import cn.jianchengwang.playass.core.Const;
import cn.jianchengwang.playass.core.mvc.annotation.Path;
import cn.jianchengwang.playass.core.mvc.annotation.action.*;
import cn.jianchengwang.playass.core.kit.PagKit;
import cn.jianchengwang.playass.core.mvc.route.Handler;
import cn.jianchengwang.playass.core.mvc.route.Route;
import cn.jianchengwang.playass.core.mvc.route.RouteInfo;
import lombok.Data;

import java.lang.reflect.Method;
import java.util.Set;

@Data
public class InitBootstrap {

    private String pkgName;
    public InitBootstrap(String pkgName) {
        this.pkgName = pkgName;
    }

    public void init() {

        Set<String> clazzNameSet = PagKit.findClasses(pkgName);
        clazzNameSet.forEach(clazzName -> {

            Class clazz = null;
            try {
                clazz = Class.forName(clazzName);

                if(clazz.isAnnotationPresent(Path.class)) {

                    Path path = (Path) clazz.getAnnotation(Path.class);

                    Method[] methods = clazz.getMethods();
                    for(Method method: methods) {

                        RouteInfo routeInfo = new RouteInfo();
                        Boolean isRoute = true;

                        if(method.isAnnotationPresent(Action.class)) {
                            Action action = (Action) method.getAnnotation(Action.class);
                            routeInfo = new RouteInfo(path, clazz, new Handler(action.value(), action.method(), method));
                        }
                        else if(method.isAnnotationPresent(GetAction.class)) {
                            GetAction action = (GetAction) method.getAnnotation(GetAction.class);
                            routeInfo = new RouteInfo(path, clazz, new Handler(action.value(), action.method(), method));
                        } else if(method.isAnnotationPresent(PostAction.class)) {
                            PostAction action = (PostAction) method.getAnnotation(PostAction.class);
                            routeInfo = new RouteInfo(path, clazz, new Handler(action.value(), action.method(), method));
                        } else if(method.isAnnotationPresent(PutAction.class)) {
                            PutAction action = (PutAction) method.getAnnotation(PutAction.class);
                            routeInfo = new RouteInfo(path, clazz, new Handler(action.value(), action.method(), method));
                        } else if(method.isAnnotationPresent(DeleteAction.class)) {
                            DeleteAction action = (DeleteAction) method.getAnnotation(DeleteAction.class);
                            routeInfo = new RouteInfo(path, clazz, new Handler(action.value(), action.method(), method));
                        } else {
                            isRoute = false;
                        }

                        if(isRoute) {
                            Route.add(routeInfo.getUri(), routeInfo);
                        }

                    }
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        });

    }
}
