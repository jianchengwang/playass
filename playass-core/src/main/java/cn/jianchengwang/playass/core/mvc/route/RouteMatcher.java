package cn.jianchengwang.playass.core.mvc.route;

import cn.jianchengwang.playass.core.mvc.http.HttpMethod;
import cn.jianchengwang.playass.core.mvc.route.meta.Route;
import cn.jianchengwang.playass.core.mvc.route.meta.RouteNode;

import java.util.LinkedHashMap;
import java.util.Map;

public class RouteMatcher {

    public static final Map<String, Route> ROUTE_MAP = new LinkedHashMap<>();

    public static final RouteNode ROUTE_ROOT = new RouteNode("/", 0);

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
