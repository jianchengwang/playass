package cn.jianchengwang.playass.core.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;

public class HttpServerInitializer extends ChannelInitializer {
    @Override
    protected void initChannel(Channel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(
            new HttpServerCodec(),
            new HttpServerExpectContinueHandler(),
            new HttpObjectAggregator(65536),
            new HttpServerHandler()
        );

    }
}
