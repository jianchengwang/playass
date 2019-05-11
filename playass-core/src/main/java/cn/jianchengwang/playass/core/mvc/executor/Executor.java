package cn.jianchengwang.playass.core.mvc.executor;

import io.netty.util.concurrent.Promise;

import java.util.concurrent.Future;

public interface Executor<T> {

    T execute(Object... request);

    Future<T> asyncExecute(Promise<T> promise, Object... request);

}
