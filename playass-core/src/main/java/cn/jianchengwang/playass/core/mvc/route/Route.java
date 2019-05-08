package cn.jianchengwang.playass.core.mvc.route;

import java.util.LinkedHashMap;
import java.util.Map;

public class Route {

    public static final Map<String, RouteInfo> ROUTE_MAP = new LinkedHashMap<>();

    public static final RouteNode ROUTE_ROOT = new RouteNode("/", 0);

    public static void build(String uri, RouteInfo routeInfo) {
        ROUTE_ROOT.build(routeInfo.getUri(), routeInfo);
    }

    public static RouteInfo match(String uri) {

        if(ROUTE_MAP.containsKey(uri)) {
            return ROUTE_MAP.get(uri);
        } else {

            return ROUTE_ROOT.match(uri);

        }
    }
}
