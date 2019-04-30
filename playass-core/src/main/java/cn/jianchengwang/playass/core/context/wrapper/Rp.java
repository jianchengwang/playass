package cn.jianchengwang.playass.core.context.wrapper;

import cn.jianchengwang.playass.core.Const;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import lombok.Data;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

@Data
public class Rp {

    private FullHttpResponse raw;

    public Rp() {

    }

    public void error(String errorMsg) {
        text(errorMsg);
    }

    public void text(String text) {

        buildRaw(text.getBytes());

        raw.headers().set(CONTENT_TYPE, "text/plain");
        raw.headers().setInt(CONTENT_LENGTH, raw.content().readableBytes());

        raw.headers().set(CONNECTION, CLOSE);
    }

    public void html(String path, boolean tplRender) {

        if(tplRender) {

        } else {

            buildRaw(path.getBytes());

            raw.headers().set(CONTENT_TYPE, "text/html");
            raw.headers().setInt(CONTENT_LENGTH, raw.content().readableBytes());

            raw.headers().set(CONNECTION, CLOSE);
        }

    }

    public void json(Object object) throws Exception {

        String jsonString = Const.MAPPER.writeValueAsString(object); // 对象转为json字符串

        buildRaw(jsonString.getBytes());

        raw.headers().set(CONTENT_TYPE, "application/json");
        raw.headers().setInt(CONTENT_LENGTH, raw.content().readableBytes());

        raw.headers().set(CONNECTION, CLOSE);

    }

    private void buildRaw(byte[] bytes) {
        raw = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(bytes));
    }
}
