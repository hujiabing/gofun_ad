package com.gvsoft.gofun_ad.tread;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池封装。注意：确保不再需要后，应调用 {@link #shutdown()}，释放线程资源。
 */
public class ThreadPoolUtil {


    private ThreadPoolExecutor mExecutor;

    /**
     * 默认实例
     */
    private volatile static ThreadPoolUtil sDefaultInstance;

    /**
     * 默认实例的线程个数
     */
    private static final int DEFAULT_POOL_SIZE = 3;
    /**
     * 最大线程数
     */
    private static final int MAX_POOL_SIZE = 8;
    /**
     * 线程存活时间
     */
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int CAPACITY = 10;
    /**
     * 默认实例的名称
     */
    private static final String DEFAULT_POOL_NAME = "gofun_ad";

    /**
     * 构造方法。
     *
     * @param poolName 线程池名称
     * @param poolSize 线程池大小，即并发线程数
     */
    private ThreadPoolUtil(String poolName, int poolSize) {
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(10);
        mExecutor = new ThreadPoolExecutor(poolSize, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS, queue, new GofunThreadFactory(
                poolName));

    }


    public void execute(Runnable runnable) {
        if (runnable != null) {
            mExecutor.execute(runnable);
        }
    }

    /**
     * 结束线程池，不再接受新的调度请求，释放线程资源。
     */
    public void shutdown() {
        mExecutor.shutdown();
    }

    public void shutdownNow() {
        mExecutor.shutdownNow();
    }

    public void removeRunnable(Runnable runnable) {
        mExecutor.remove(runnable);
    }

    /**
     * 获取默认实例。当不需要单独创建线程池时，应使用默认实例。
     *
     * @return 默认实例
     */
    public static ThreadPoolUtil defaultInstance() {
        if (sDefaultInstance == null) {
            synchronized (ThreadPoolUtil.class) {
                if (sDefaultInstance == null) {
                    sDefaultInstance = new ThreadPoolUtil(DEFAULT_POOL_NAME, DEFAULT_POOL_SIZE);
                }
            }
        }
        return sDefaultInstance;
    }

    public ThreadPoolExecutor getExecutor() {
        return mExecutor;
    }

    private static class GofunThreadFactory implements ThreadFactory {
        private String mPoolName;
        private AtomicInteger mCount;

        public GofunThreadFactory(String poolName) {
            this.mPoolName = poolName;
            mCount = new AtomicInteger();
        }

        @Override
        public Thread newThread(Runnable r) {
            String name = mPoolName + "-pool-thread-" + mCount.getAndIncrement();
            return new Thread(r, name);
        }
    }

}