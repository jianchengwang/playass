package cn.jianchengwang.playass.core.mvc.route.meta;

import cn.jianchengwang.playass.core.kit.StrKit;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class RouteNode {

    private String path;
    private int deep;
    private boolean isParam;

    private Route routeInfo;
    private Map<String, RouteNode> pathNodeMap;

    public RouteNode() {
    }

    public RouteNode(String path, int deep) {
        this.path = path;
        this.deep = deep;

        if(path.startsWith("{") && path.endsWith("}")) {
            this.isParam = true;
        }
    }

    public void build(String uri, Route routeInfo) {

        String[] paths = uri.split("/");

        RouteNode routeNode = this;
        for(int i=0; i<paths.length; i++) {
            routeNode = routeNode.build(paths[i], i, i==paths.length-1, routeInfo);
        }
    }

    private RouteNode build(String path, int deep, boolean lastPath, Route routeInfo) {

        path = path.trim();
        if(this.pathNodeMap == null) {
            this.pathNodeMap = new LinkedHashMap<>();
        }

        if(this.pathNodeMap.containsKey(path)) {

            if(lastPath) {
                this.pathNodeMap.get(path).routeInfo = routeInfo;
            }

            return this.pathNodeMap.get(path);

        } else {

            RouteNode child = new RouteNode(path, deep);

            this.pathNodeMap.put(path, child);

            return child;

        }
    }

    public Route match(String uri) {

        String[] paths = uri.split("/");

        RouteNode routeNode = this;
        Map<String, Object> paramMap = new LinkedHashMap<>();

        for(int i=0; i<paths.length; i++) {
            if(routeNode != null) {
                routeNode = routeNode.match(paths[i], i==paths.length-1, paramMap);
            } else {
                break;
            }
        }

        if(routeNode == null || routeNode.routeInfo == null) return null;

        if(paramMap.size() > 0) routeNode.routeInfo.setPathParamMap(paramMap);

        return routeNode.routeInfo;
    }

    private RouteNode match(final String path, boolean lastPath, final Map pathParamMap) {

        if(this.pathNodeMap.containsKey(path)) {
            return this.pathNodeMap.get(path);
        }

        final RouteNode[] routeNodes = new RouteNode[1];

        this.pathNodeMap.forEach((k, v) -> {
            if(v.isParam) {
                pathParamMap.put(StrKit.removeHeadTailChar(k), path);
                routeNodes[0] = v;
            }
        });
        return routeNodes[0];
    }

}
