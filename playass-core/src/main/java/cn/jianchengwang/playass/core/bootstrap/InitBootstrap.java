package cn.jianchengwang.playass.core.bootstrap;


import cn.jianchengwang.playass.core.Const;
import cn.jianchengwang.playass.core.annotation.Action;
import cn.jianchengwang.playass.core.kit.PagKit;
import cn.jianchengwang.playass.core.route.Route;
import lombok.Data;

import java.util.Set;

@Data
public class InitBootstrap {

    String pkgName;
    public InitBootstrap(String pkgName) {
        this.pkgName = pkgName;
    }

    public void init() {

        Set<String> clazzNameSet = PagKit.findClasses(pkgName);
        clazzNameSet.forEach(clazzName -> {

            Class clazz = null;
            try {
                clazz = Class.forName(clazzName);

                if(clazz.isAnnotationPresent(Action.class)) {

                    Action action = (Action) clazz.getAnnotation(Action.class);
                    String uri = action.value().length()==0?clazz.getSimpleName():action.value();
                    Const.ROUTE_MAP.put(uri, new Route(uri, clazz, null));
                }

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        });

    }
}
