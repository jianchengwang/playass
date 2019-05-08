package cn.jianchengwang.playass.core.mvc.route;

import cn.jianchengwang.playass.core.kit.PlaceHolderKit;
import cn.jianchengwang.playass.core.kit.StrKit;
import cn.jianchengwang.playass.core.mvc.annotation.Path;
import lombok.Data;

import java.util.*;

@Data
public class RouteInfo {

    private String uri;

    private Path path;
    private Class clazz;
    private Handler handler;

    private boolean havePathParam;
    private Set<String> pathParams = new LinkedHashSet<>();
    private Map<String, Object> pathParamMap = new LinkedHashMap<>();

    public RouteInfo() {
    }

    public RouteInfo(Path path, Class clazz, Handler handler) {
        this.path = path;
        this.clazz = clazz;
        this.handler = handler;

        StringBuilder uriSb = new StringBuilder(path.value()).append("/");

        if(handler.getValue().length() > 0) {
            uriSb.append(handler.getValue());
        } else {
            uriSb.append(handler.getExecuteMethod().getName().substring(0,1).toLowerCase() +
                    handler.getExecuteMethod().getName().substring(1));
        }

        pathParams = PlaceHolderKit.findPlaceHolderKeys(uriSb.toString());
        if(pathParams != null && pathParams.size() > 0) {
            havePathParam = true;
        }

        uriSb.append(path.suffix());

        this.uri = uriSb.toString();

        if(!havePathParam) {
            Route.ROUTE_MAP.put(this.uri, this);
        }

        Route.ROUTE_ROOT.build(this.uri, this);
    }
}
