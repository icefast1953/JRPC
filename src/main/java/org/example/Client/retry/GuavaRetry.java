package org.example.Client.retry;

import com.github.rholder.retry.*;
import org.example.Client.rpcClient.RpcClient;
import org.example.Common.rpc.RPCReqeust;
import org.example.Common.rpc.RPCResponse;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class GuavaRetry {

    private RpcClient rpcClient;

    public RPCResponse sendServiceWithRetry(RPCReqeust rpcReqeust, RpcClient rpcClient) {
        this.rpcClient = rpcClient;

        Retryer<RPCResponse> retryer = RetryerBuilder.<RPCResponse>newBuilder()
                //  如果异常，则重试
                .retryIfException()
                //  如果状态码等于500，重试
                .retryIfResult(response -> response.getCode() == 500)
                //  2秒后超时重试
                .withWaitStrategy(WaitStrategies.exponentialWait(2, TimeUnit.SECONDS))
                //  最多重试3次
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                //  增加监听器
                .withRetryListener(new RetryListener() {
                    /**
                     * @param attempt
                     * @param <V>
                     */
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        System.out.println("重试已被调用 " + attempt.getAttemptNumber() + " 次！");
                    }
                })
                .build();

        try {
            RPCResponse response = retryer.call(() -> rpcClient.sendRequest(rpcReqeust));
            return response;
        } catch (ExecutionException | RetryException e) {
            e.printStackTrace();
        }

        return RPCResponse.failure();
    }
}
