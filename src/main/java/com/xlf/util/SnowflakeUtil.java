package com.xlf.util;

public class SnowflakeUtil {
    private final static long EPOCH = 1656604800000L;

    /**
     * 0L
     **/
    private final static long MIN_POSITIVE_LONG = 0L;

    /**
     * bit info
     */
    private final static long CENTER_ID_BITS = 5L;
    private final static long WORKER_ID_BITS = 5L;
    private final static long SEQUENCE_BITS = 12L;

    /**
     * max value
     */
    private final static long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private final static long MAX_CENTER_ID = ~(-1L << CENTER_ID_BITS);
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);


    private final static long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    /**
     * shift info
     **/
    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;
    private final static long CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    private final static long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + CENTER_ID_BITS;

    private static long centerId;
    private static long workerId;
    private static long sequence = 0L;
    private static long lastTimestamp = -1L;

    public static void init(long workerId, long centerId) {
        if (workerId <= MAX_WORKER_ID && workerId >= MIN_POSITIVE_LONG) {
            if (centerId <= MAX_CENTER_ID && centerId >= MIN_POSITIVE_LONG) {
                SnowflakeUtil.workerId = workerId;
                SnowflakeUtil.centerId = centerId;
            } else {
                throw new IllegalArgumentException(String.format("dataCenter Id can't be greater than %d or less than 0", MAX_CENTER_ID));
            }
        } else {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
    }

    /**
     * next id
     *
     * @return snowflakeId
     */
    public static synchronized long nextId() {
        long timestamp = genTime();

        // Clock moved backwards
        if (timestamp < lastTimestamp) {
            throw new IllegalStateException(String.format("Clock moved backwards. Refusing to generate id for %d ms", lastTimestamp - timestamp));
        }

        // same time
        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1L) & SEQUENCE_MASK;
            // max sequence, tilNextMillis
            if (sequence == 0L) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // not same time
            sequence = 0L;
        }

        lastTimestamp = timestamp;
        // timestamp | centerId | workerId | sequence
        return (timestamp - EPOCH) << TIMESTAMP_LEFT_SHIFT | centerId << CENTER_ID_SHIFT | workerId << WORKER_ID_SHIFT | sequence;
    }


    private static long tilNextMillis(long lastTimestamp) {
        long timestamp = genTime();
        while (timestamp <= lastTimestamp) {
            timestamp = genTime();
        }
        return timestamp;
    }


    private static long genTime() {
        return System.currentTimeMillis();
    }
}