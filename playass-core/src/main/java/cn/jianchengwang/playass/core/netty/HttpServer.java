package cn.jianchengwang.playass.core.netty;

import cn.jianchengwang.playass.core.bootstrap.InitBootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.Data;

@Data
public class HttpServer {

    private Integer port = 8080;
    private InitBootstrap initBootstrap;

    public HttpServer() {

    }

    public HttpServer(int port) {
        this.port = port;
    }

    public HttpServer boot(InitBootstrap initBootstrap) {

        this.initBootstrap = initBootstrap;

        this.initBootstrap.init();

        return this;
    }

    public void start() throws Exception {

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {

            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpServerInitializer());

            Channel ch = b.bind(port).sync().channel();

            ch.closeFuture().sync();
            System.err.println("Open your web browser and navigate to " + "://127.0.0.1:" + port + '/');

        } finally {

            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

        }

    }

}
