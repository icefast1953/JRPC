package org.example.Server.ratelimit;

public interface RateLimit {

    /**
     * 获取访问许可
     * @return
     */
    boolean getToken();
}
