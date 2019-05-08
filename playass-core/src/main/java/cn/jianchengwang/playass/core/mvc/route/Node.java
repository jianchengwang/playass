package cn.jianchengwang.playass.core.mvc.route;

import java.util.LinkedList;
import java.util.List;

public class Node {

    private String path;
    private int deep;
    private boolean isParam;
    private List<Node> childNodeList;

    public Node() {
    }

    public Node(String path, int deep) {
        this.path = path;
        this.deep = deep;

        if(path.startsWith("{") && path.endsWith("}")) {
            this.isParam = true;
        }
    }

    public void matchUri(String uri) {

        String[] paths = uri.split("/");

        for(int i=0; i<paths.length; i++) {

            matchPath(paths[i], i);

        }
    }

    public void matchPath(String path, int deep) {

        path = path.trim();
        if(this.path.equals(path)) {
            return;
        } else {
            if(this.childNodeList == null) this.childNodeList = new LinkedList<>();

            Node child = new Node(path, deep);
            this.childNodeList.add(child);
        }
    }
}
