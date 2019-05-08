package cn.jianchengwang.playass.core.mvc.route;

import cn.jianchengwang.playass.core.kit.PlaceHolderKit;
import cn.jianchengwang.playass.core.kit.StrKit;
import cn.jianchengwang.playass.core.mvc.annotation.Path;
import lombok.Data;

import java.util.*;

@Data
public class Route {

    private String uri;

    private Path path;
    private Class clazz;
    private ActionConfig actionConfig;

    private boolean havePathParam;
    private Set<String> pathParams = new LinkedHashSet<>();

    public Route() {
    }

    public Route(Path path, Class clazz, ActionConfig actionConfig) {
        this.path = path;
        this.clazz = clazz;
        this.actionConfig = actionConfig;

        StringBuilder uriSb = new StringBuilder(path.value()).append("/");

        if(actionConfig.getValue().length() > 0) {
            uriSb.append(actionConfig.getValue());
        } else {
            uriSb.append(StrKit.upperCase(actionConfig.getExecuteMethod().getName()));
        }

        pathParams = PlaceHolderKit.findPlaceHolderKeys(uriSb.toString());
        if(pathParams != null && pathParams.size() > 0) {
            havePathParam = true;
        }

        uriSb.append(path.suffix());

        this.uri = uriSb.toString();

    }

    public Route(String uri) {
        this.uri = uri;
    }
}
