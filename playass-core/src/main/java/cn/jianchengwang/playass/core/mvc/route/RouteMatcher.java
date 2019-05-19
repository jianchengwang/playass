package cn.jianchengwang.playass.core.mvc.route;

import cn.jianchengwang.playass.core.ioc.BeanContainer;
import cn.jianchengwang.playass.core.mvc.annotation.Path;
import cn.jianchengwang.playass.core.mvc.annotation.action.*;
import cn.jianchengwang.playass.core.mvc.http.HttpMethod;
import cn.jianchengwang.playass.core.mvc.route.meta.Route;
import cn.jianchengwang.playass.core.mvc.route.meta.RouteMethod;
import cn.jianchengwang.playass.core.mvc.route.meta.RouteNode;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public class RouteMatcher {

    public static final Map<String, Route> ROUTE_MAP = new LinkedHashMap<>();

    public static final RouteNode ROUTE_ROOT = new RouteNode("/", 0);

    public static void loadRoutes() {

        BeanContainer beanContainer = BeanContainer.getInstance();

        beanContainer.getClasses().stream()
                .filter(clazz -> clazz.isAnnotationPresent(Path.class))
                .forEach(clazz -> {
                    Path path = (Path) clazz.getAnnotation(Path.class);

                    Method[] methods = clazz.getMethods();
                    for(Method method: methods) {

                        Route routeInfo = new Route();
                        Boolean isRoute = true;

                        if(method.isAnnotationPresent(Action.class)) {
                            Action action = (Action) method.getAnnotation(Action.class);
                            routeInfo = new Route(path, clazz, new RouteMethod(action.value(), action.method(), method));
                        }
                        else if(method.isAnnotationPresent(GetAction.class)) {
                            GetAction action = (GetAction) method.getAnnotation(GetAction.class);
                            routeInfo = new Route(path, clazz, new RouteMethod(action.value(), action.method(), method));
                        } else if(method.isAnnotationPresent(PostAction.class)) {
                            PostAction action = (PostAction) method.getAnnotation(PostAction.class);
                            routeInfo = new Route(path, clazz, new RouteMethod(action.value(), action.method(), method));
                        } else if(method.isAnnotationPresent(PutAction.class)) {
                            PutAction action = (PutAction) method.getAnnotation(PutAction.class);
                            routeInfo = new Route(path, clazz, new RouteMethod(action.value(), action.method(), method));
                        } else if(method.isAnnotationPresent(DeleteAction.class)) {
                            DeleteAction action = (DeleteAction) method.getAnnotation(DeleteAction.class);
                            routeInfo = new Route(path, clazz, new RouteMethod(action.value(), action.method(), method));
                        } else {
                            isRoute = false;
                        }

                        if(isRoute) {
                            RouteMatcher.add(routeInfo.getUri(), routeInfo);
                        }

                    }
                });
    }

    public static void add(String uri, Route routeInfo) {
        ROUTE_ROOT.build(uri, routeInfo);
    }

    public static Route match(String method, String uri) {

        Route route = null;

        if(ROUTE_MAP.containsKey(uri)) {
            route = ROUTE_MAP.get(uri);
        } else {
            route = ROUTE_ROOT.match(uri);
        }

        if(route!=null) {

            HttpMethod[] allowHttpMethods = route.getRouteMethod().getAllowHttpMethods();
            for(HttpMethod allowHttpMethod: allowHttpMethods) {
                if(allowHttpMethod == HttpMethod.ALL ||
                    allowHttpMethod.name().equalsIgnoreCase(method)) {
                    return route;
                }
            }
        }

        return null;
    }
}
