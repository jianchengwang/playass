package cn.jianchengwang.playass.core.mvc.route.meta;

import cn.jianchengwang.playass.core.kit.PlaceHolderKit;
import cn.jianchengwang.playass.core.mvc.annotation.Path;
import cn.jianchengwang.playass.core.mvc.route.RouteMatcher;
import lombok.Data;

import java.util.*;

@Data
public class Route {

    private String uri;

    private Path path;
    private Class clazz;
    private RouteMethod routeMethod;

    private boolean havePathParam;
    private Set<String> pathParams = new LinkedHashSet<>();
    private Map<String, Object> pathParamMap = new LinkedHashMap<>();

    public Route() {
    }

    public Route(Path path, Class clazz, RouteMethod routeMethod) {
        this.path = path;
        this.clazz = clazz;
        this.routeMethod = routeMethod;

        String pathStr = path.value();
        if(!pathStr.startsWith("/")) pathStr = "/" + pathStr;
        StringBuilder uriSb = new StringBuilder(pathStr).append("/");

        if(routeMethod.getValue().length() > 0) {
            uriSb.append(routeMethod.getValue());
        } else {
            uriSb.append(routeMethod.getExecuteMethod().getName().substring(0,1).toLowerCase() +
                    routeMethod.getExecuteMethod().getName().substring(1));
        }

        pathParams = PlaceHolderKit.findPlaceHolderKeys(uriSb.toString());
        if(pathParams != null && pathParams.size() > 0) {
            havePathParam = true;
        }

        uriSb.append(path.suffix());

        this.uri = uriSb.toString();

        if(!havePathParam) {
            RouteMatcher.ROUTE_MAP.put(this.uri, this);
        }

        RouteMatcher.ROUTE_ROOT.build(this.uri, this);
    }
}
