package cn.jianchengwang.playass.core.mvc.route;

import cn.jianchengwang.playass.core.kit.StrKit;
import cn.jianchengwang.playass.core.mvc.annotation.Path;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Route {

    private String uri;

    private Path path;
    private Class clazz;
    private ActionConfig actionConfig;

    private Map<String, String> pathParams = new HashMap<>(8);

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

//        if()

        uriSb.append(path.suffix());

    }

    public Route(String uri) {
        this.uri = uri;
    }
}
