package cn.jianchengwang.playass.core.mvc.executor;

import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public abstract class AbstractExecutor<T> implements Executor<T> {

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractExecutor.class);

    private java.util.concurrent.Executor eventExecutor;

    public AbstractExecutor() {
        this(null);
    }

    public AbstractExecutor(java.util.concurrent.Executor eventExecutor) {
        this.eventExecutor = eventExecutor == null ? EventExecutorHolder.eventExecutor : eventExecutor;
    }

    @Override
    public T execute(Object... request) {
        return doExecute(request);
    }

    @Override
    public Future<T> asyncExecute(Promise<T> promise, Object... request) {
        if (promise == null) {
            throw new IllegalArgumentException("promise should not be null");
        }
        // 异步执行
        eventExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    T response = doExecute(request);
                    promise.setSuccess(response);
                } catch (Exception e) {
                    promise.setFailure(e);
                }
            }
        });
        // 返回promise
        return promise;
    }

    /**
     * 执行具体的方法
     *
     * @param request 请求对象
     * @return 返回结果
     */
    public abstract T doExecute(Object... request);

    private static final class EventExecutorHolder {
        private static java.util.concurrent.Executor eventExecutor = new ThreadPoolExecutor(
                10,
                20,
                10,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10));
    }

}
