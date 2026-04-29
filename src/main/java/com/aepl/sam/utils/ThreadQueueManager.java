package com.aepl.sam.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ThreadQueueManager - Controls the number of concurrent threads executing
 * tests.
 * 
 * This manager ensures that only a defined number of threads execute
 * concurrently.
 * It uses a BlockingQueue to manage thread slots. When all slots are occupied,
 * new threads wait until a slot becomes available.
 * 
 * Thread-Safe: Yes (uses BlockingQueue which is thread-safe)
 * Singleton: Yes (double-checked locking pattern)
 * 
 * Usage:
 * ThreadQueueManager.acquireSlot(); // Wait for available slot
 * try {
 * // Execute test
 * } finally {
 * ThreadQueueManager.releaseSlot(); // Release slot for next thread
 * }
 */
public class ThreadQueueManager {

    private static final Logger logger = LogManager.getLogger(ThreadQueueManager.class);
    private static ThreadQueueManager instance;
    private static final Object lock = new Object();

    private final BlockingQueue<Integer> threadSlotQueue;
    private final int maxConcurrentThreads;
    private final long acquisitionTimeoutSeconds;

    // Configuration constants
    private static final int DEFAULT_MAX_THREADS = 4;
    private static final long DEFAULT_TIMEOUT_SECONDS = 60;

    /**
     * Private constructor to prevent direct instantiation
     */
    private ThreadQueueManager(int maxThreads, long timeoutSeconds) {
        this.maxConcurrentThreads = maxThreads;
        this.acquisitionTimeoutSeconds = timeoutSeconds;
        this.threadSlotQueue = new LinkedBlockingQueue<>(maxThreads);

        // Initialize queue with available slots
        for (int i = 0; i < maxThreads; i++) {
            threadSlotQueue.offer(i);
        }

        logger.info("ThreadQueueManager initialized with max concurrent threads: {} and timeout: {}s",
                maxThreads, timeoutSeconds);
    }

    /**
     * Get singleton instance with default configuration
     * Default: 4 concurrent threads, 60 second timeout
     */
    public static ThreadQueueManager getInstance() {
        return getInstance(DEFAULT_MAX_THREADS, DEFAULT_TIMEOUT_SECONDS);
    }

    /**
     * Get singleton instance with custom configuration
     */
    public static ThreadQueueManager getInstance(int maxThreads, long timeoutSeconds) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new ThreadQueueManager(maxThreads, timeoutSeconds);
                }
            }
        }
        return instance;
    }

    /**
     * Acquire a thread slot. Blocks until a slot is available.
     * Must be called at the beginning of test execution.
     * 
     * @return slot number for logging/debugging
     * @throws InterruptedException if thread is interrupted while waiting
     */
    public int acquireSlot() throws InterruptedException {
        String threadName = Thread.currentThread().getName();
        long threadId = Thread.currentThread().getId();

        logger.debug("Thread [{}] (ID: {}) requesting a slot. Available slots: {}",
                threadName, threadId, threadSlotQueue.size());

        Integer slotNumber;
        try {
            // Wait with timeout to prevent indefinite blocking
            slotNumber = threadSlotQueue.poll(acquisitionTimeoutSeconds, TimeUnit.SECONDS);

            if (slotNumber == null) {
                String errorMsg = String.format(
                        "Thread [%s] (ID: %d) exceeded timeout of %d seconds waiting for thread slot",
                        threadName, threadId, acquisitionTimeoutSeconds);
                logger.error(errorMsg);
                throw new InterruptedException(errorMsg);
            }

            logger.info("Thread [{}] (ID: {}) acquired slot #{}. Queue size: {}",
                    threadName, threadId, slotNumber, threadSlotQueue.size());

            // Store slot number in ThreadLocal for later release
            ThreadLocalSlotNumber.set(slotNumber);
            return slotNumber;

        } catch (InterruptedException e) {
            String errorMsg = String.format(
                    "Thread [%s] (ID: %d) interrupted while waiting for slot",
                    threadName, threadId);
            logger.error(errorMsg, e);
            throw new InterruptedException(errorMsg);
        }
    }

    /**
     * Release the thread slot. Must be called in finally block to ensure cleanup.
     * Should be called at the end of test execution.
     */
    public void releaseSlot() {
        String threadName = Thread.currentThread().getName();
        long threadId = Thread.currentThread().getId();

        Integer slotNumber = ThreadLocalSlotNumber.get();

        if (slotNumber == null) {
            logger.warn("Thread [{}] (ID: {}) attempted to release slot but no slot was acquired",
                    threadName, threadId);
            return;
        }

        try {
            threadSlotQueue.put(slotNumber);
            ThreadLocalSlotNumber.remove();

            logger.info("Thread [{}] (ID: {}) released slot #{}. Queue size: {}",
                    threadName, threadId, slotNumber, threadSlotQueue.size());

        } catch (InterruptedException e) {
            logger.error("Thread [{}] (ID: {}) interrupted while releasing slot: {}",
                    threadName, threadId, e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Get the number of available thread slots
     */
    public int getAvailableSlots() {
        return threadSlotQueue.size();
    }

    /**
     * Get the maximum number of concurrent threads allowed
     */
    public int getMaxConcurrentThreads() {
        return maxConcurrentThreads;
    }

    /**
     * Get the number of currently executing threads (approximate)
     */
    public int getActiveThreads() {
        return maxConcurrentThreads - threadSlotQueue.size();
    }

    /**
     * Get queue statistics for monitoring
     */
    public String getQueueStats() {
        return String.format(
                "[ThreadQueue] Active: %d/%d, Available Slots: %d",
                getActiveThreads(), maxConcurrentThreads, getAvailableSlots());
    }

    /**
     * ThreadLocal storage for slot numbers per thread
     * This allows us to track which slot each thread acquired
     */
    private static class ThreadLocalSlotNumber {
        private static final ThreadLocal<Integer> slotLocal = new ThreadLocal<>();

        static void set(Integer slot) {
            slotLocal.set(slot);
        }

        static Integer get() {
            return slotLocal.get();
        }

        static void remove() {
            slotLocal.remove();
        }
    }

    /**
     * Reset singleton instance (useful for testing)
     * WARNING: Only use in test cleanup, not in production
     */
    protected static void reset() {
        synchronized (lock) {
            instance = null;
        }
    }
}
