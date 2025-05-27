package org.example.Server.ratelimit.impl;

import org.example.Server.ratelimit.RateLimit;

public class TokenBucketRateLimitImpl implements RateLimit {
    //  令牌产生速度 （ms）
    private static int RATE;

    //  桶容量
    private static int CAPACITY;

    //  当前桶容量
    private volatile int curCapacity;

    //  上次请求的时间戳
    private volatile long timeStamp = System.currentTimeMillis();

    public TokenBucketRateLimitImpl(int rate, int capacity) {
        this.RATE = rate;
        this.CAPACITY = capacity;
    }


    /**
     *  获取访问许可
     * @return
     */
    @Override
    public synchronized boolean getToken() {
        //  若当前还有令牌，直接扣减并返回true
        if (curCapacity > 0) {
            curCapacity--;
            return true;
        }
        //  当前时间戳
        long now = System.currentTimeMillis();

        if (now - timeStamp >= RATE) {
            if ((now - timeStamp) / RATE >= 2) {
                curCapacity += (int) ((now - timeStamp) / RATE - 1);
            }
            if (curCapacity > CAPACITY) {
                curCapacity = CAPACITY;
            }
            //  更新时间戳
            timeStamp = now;

            return true;
        }

        return false;

    }
}
