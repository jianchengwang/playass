package cn.jianchengwang.playass.example.demo;


import cn.jianchengwang.playass.core.bootstrap.InitBootstrap;
import cn.jianchengwang.playass.core.netty.HttpServer;

public class App {

    public static void main(String[] args) throws Exception {

        new HttpServer(8989)
                .boot(new InitBootstrap("cn.jianchengwang.todo.lib.netty.todo.web.demo"))
                .start();
    }
}
