package cn.jianchengwang.playass.core.mvc.route;

import cn.jianchengwang.playass.core.mvc.context.H;
import lombok.Data;

import java.lang.reflect.Method;

@Data
public class ActionConfig {
    String value;
    H.Method[] allowHttpMethods;

    Method executeMethod;

    public ActionConfig(String value, H.Method[] allowHttpMethods, Method executeMethod) {
        this.value = value;
        this.allowHttpMethods = allowHttpMethods;
        this.executeMethod = executeMethod;
    }
}
