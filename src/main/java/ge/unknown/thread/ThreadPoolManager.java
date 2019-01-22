package ge.unknown.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * This class handles com.ownabit.corelib.thread pooling system. It relies on two ThreadPoolExecutor arrays, which poolers number is generated using config.
 * <p>
 * Those arrays hold following pools :
 * </p>
 * <ul>
 * <li>Scheduled pool keeps a track about incoming, future events.</li>
 * <li>Instant pool handles short-life events.</li>
 * </ul>
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "thread")
public class ThreadPoolManager {

    private static int SCHEDULED_THREAD_POOL_COUNT;
    private static int THREADS_PER_SCHEDULED_THREAD_POOL;
    private static int INSTANT_THREAD_POOL_COUNT;
    private static int THREADS_PER_INSTANT_THREAD_POOL;

    @Value("-1")
    public void setScheduledThreadPoolCount(int scheduledThreadPoolCount) {
        SCHEDULED_THREAD_POOL_COUNT = scheduledThreadPoolCount;
    }

    @Value("2")
    public void setThreadsPerScheduledThreadPool(int threadsPerScheduledThreadPool) {
        THREADS_PER_SCHEDULED_THREAD_POOL = threadsPerScheduledThreadPool;
    }

    @Value("-1")
    public void setInstantThreadPoolCount(int instantThreadPoolCount) {
        INSTANT_THREAD_POOL_COUNT = instantThreadPoolCount;
    }

    @Value("2")
    public void setThreadsPerInstantThreadPool(int threadsPerInstantThreadPool) {
        THREADS_PER_INSTANT_THREAD_POOL = threadsPerInstantThreadPool;
    }

    private static final long MAX_DELAY = TimeUnit.NANOSECONDS.toMillis(Long.MAX_VALUE - System.nanoTime()) / 2;

    private static int _threadPoolRandomizer;

    protected static ScheduledThreadPoolExecutor[] _scheduledPools;
    protected static ThreadPoolExecutor[] _instantPools;

    @PostConstruct
    public static void init() {
        // Feed scheduled pool.
        int poolCount = SCHEDULED_THREAD_POOL_COUNT;
        if (poolCount == -1) {
            poolCount = Runtime.getRuntime().availableProcessors();
        }

        _scheduledPools = new ScheduledThreadPoolExecutor[poolCount];
        for (int i = 0; i < poolCount; i++) {
            _scheduledPools[i] = new ScheduledThreadPoolExecutor(THREADS_PER_SCHEDULED_THREAD_POOL);
        }

        // Feed instant pool.
        poolCount = INSTANT_THREAD_POOL_COUNT;
        if (poolCount == -1) {
            poolCount = Runtime.getRuntime().availableProcessors();
        }

        _instantPools = new ThreadPoolExecutor[poolCount];
        for (int i = 0; i < poolCount; i++) {
            _instantPools[i] = new ThreadPoolExecutor(THREADS_PER_INSTANT_THREAD_POOL, THREADS_PER_INSTANT_THREAD_POOL, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(100000));
        }

        // Prestart core threads.
        for (ScheduledThreadPoolExecutor threadPool : _scheduledPools) {
            threadPool.prestartAllCoreThreads();
        }

        for (ThreadPoolExecutor threadPool : _instantPools) {
            threadPool.prestartAllCoreThreads();
        }

        // Launch purge task.
        scheduleAtFixedRate(ThreadPoolManager::purge, 600000, 600000);

        log.info("ThreadPoolManager: Initialized " + getPoolSize(_instantPools) + "/" + getMaximumPoolSize(_instantPools) + " instant com.ownabit.corelib.thread(s).");
        log.info("ThreadPoolManager: Initialized " + getPoolSize(_scheduledPools) + "/" + getMaximumPoolSize(_scheduledPools) + " scheduled com.ownabit.corelib.thread(s).");
    }

    public static void purge() {
        for (ScheduledThreadPoolExecutor threadPool1 : _scheduledPools) {
            threadPool1.purge();
        }
        for (ThreadPoolExecutor threadPool2 : _instantPools) {
            threadPool2.purge();
        }
    }

    /**
     * Schedules a one-shot action that becomes enabled after a delay. The pool is chosen based on pools activity.
     *
     * @param r     : the task to execute.
     * @param delay : the time from now to delay execution.
     * @return a ScheduledFuture representing pending completion of the task and whose get() method will return null upon completion.
     */
    public static ScheduledFuture<?> schedule(Runnable r, long delay) {
        try {
            return getPool(_scheduledPools).schedule(new TaskWrapper(r), validate(delay), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Schedules a periodic action that becomes enabled after a delay. The pool is chosen based on pools activity.
     *
     * @param r      : the task to execute.
     * @param delay  : the time from now to delay execution.
     * @param period : the period between successive executions.
     * @return a ScheduledFuture representing pending completion of the task and whose get() method will throw an exception upon cancellation.
     */
    public static ScheduledFuture<?> scheduleAtFixedRate(Runnable r, long delay, long period) {
        try {
            return getPool(_scheduledPools).scheduleAtFixedRate(new TaskWrapper(r), validate(delay), validate(period), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Executes the given task sometime in the future.
     *
     * @param r : the task to execute.
     */
    public static void execute(Runnable r) {
        try {
            getPool(_instantPools).execute(new TaskWrapper(r));
        } catch (Exception e) {
        }
    }

    public static String[] getStats() {
        List<String> stats = new ArrayList<>();
        for (int i = 0; i < _scheduledPools.length; i++) {
            final ScheduledThreadPoolExecutor threadPool = _scheduledPools[i];
            stats.add("Scheduled pool #" + i + ":");
            stats.add(" |- ActiveCount: ...... " + threadPool.getActiveCount());
            stats.add(" |- CorePoolSize: ..... " + threadPool.getCorePoolSize());
            stats.add(" |- PoolSize: ......... " + threadPool.getPoolSize());
            stats.add(" |- LargestPoolSize: .. " + threadPool.getLargestPoolSize());
            stats.add(" |- MaximumPoolSize: .. " + threadPool.getMaximumPoolSize());
            stats.add(" |- CompletedTaskCount: " + threadPool.getCompletedTaskCount());
            stats.add(" |- QueuedTaskCount: .. " + threadPool.getQueue().size());
            stats.add(" |- TaskCount: ........ " + threadPool.getTaskCount());
            stats.add(" | -------");
        }
        for (int i = 0; i < _instantPools.length; i++) {
            final ThreadPoolExecutor threadPool = _instantPools[i];
            stats.add("Scheduled pool #" + i + ":");
            stats.add(" |- ActiveCount: ...... " + threadPool.getActiveCount());
            stats.add(" |- CorePoolSize: ..... " + threadPool.getCorePoolSize());
            stats.add(" |- PoolSize: ......... " + threadPool.getPoolSize());
            stats.add(" |- LargestPoolSize: .. " + threadPool.getLargestPoolSize());
            stats.add(" |- MaximumPoolSize: .. " + threadPool.getMaximumPoolSize());
            stats.add(" |- CompletedTaskCount: " + threadPool.getCompletedTaskCount());
            stats.add(" |- QueuedTaskCount: .. " + threadPool.getQueue().size());
            stats.add(" |- TaskCount: ........ " + threadPool.getTaskCount());
            stats.add(" | -------");
        }
        return stats.toArray(new String[stats.size()]);
    }

    /**
     * Shutdown com.ownabit.corelib.thread pooling system correctly. Send different informations.
     */
    @PreDestroy
    public static void shutdown() {
        try {
            log.info("ThreadPoolManager: Shutting down.");

            for (ScheduledThreadPoolExecutor threadPool : _scheduledPools) {
                threadPool.shutdownNow();
            }

            for (ThreadPoolExecutor threadPool : _instantPools) {
                threadPool.shutdownNow();
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * @param <T>         : The pool type.
     * @param threadPools : The pool array to check.
     * @return the less fed pool.
     */
    private static <T> T getPool(T[] threadPools) {
        return threadPools[_threadPoolRandomizer++ % threadPools.length];
    }

    /**
     * @param delay : The delay to validate.
     * @return a secured value, from 0 to MAX_DELAY.
     */
    private static long validate(long delay) {
        return Math.max(0, Math.min(MAX_DELAY, delay));
    }

    /**
     * @param threadPools : The pool array to check.
     * @return the overall actual pools size.
     */
    private static long getPoolSize(ThreadPoolExecutor[] threadPools) {
        long result = 0;

        for (ThreadPoolExecutor threadPool : threadPools) {
            result += threadPool.getPoolSize();
        }

        return result;
    }

    /**
     * @param threadPools : The pool array to check.
     * @return the overall maximum pools size.
     */
    private static long getMaximumPoolSize(ThreadPoolExecutor[] threadPools) {
        long result = 0;

        for (ThreadPoolExecutor threadPool : threadPools) {
            result += threadPool.getMaximumPoolSize();
        }

        return result;
    }

    public static final class TaskWrapper implements Runnable {
        private final Runnable _runnable;

        public TaskWrapper(Runnable runnable) {
            _runnable = runnable;
        }

        @Override
        public void run() {
            try {
                _runnable.run();
            } catch (RuntimeException e) {
                log.warn("Exception in " + _runnable.getClass().getName() + " execution: " + e.getMessage());
            }
        }
    }
}


