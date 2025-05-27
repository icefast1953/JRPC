package org.example.Client.proxy;

import org.example.Client.circuitBreaker.CircuitBreaker;
import org.example.Client.circuitBreaker.CircuitBreakerProvider;
import org.example.Client.retry.GuavaRetry;
import org.example.Client.rpcClient.RpcClient;
import org.example.Client.rpcClient.impl.NettyRpcClient;
import org.example.Client.rpcClient.impl.SimpleSocketRpcClient;
import org.example.Client.serviceCenter.ServiceCenter;
import org.example.Client.serviceCenter.ZKServiceCenter;
import org.example.Common.rpc.RPCReqeust;
import org.example.Common.rpc.RPCResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


public class ClientProxy implements InvocationHandler {
    //传入参数service接口的class对象，反射封装成一个request

    private ServiceCenter serviceCenter;

    private RpcClient rpcClient;

    private CircuitBreakerProvider circuitBreakerProvider;

    public ClientProxy(String host,int port,int choose) throws InterruptedException {
        serviceCenter = new ZKServiceCenter();
        circuitBreakerProvider = new CircuitBreakerProvider();
        switch (choose){
            case 0:
                rpcClient=new NettyRpcClient();
                break;
            case 1:
                rpcClient=new SimpleSocketRpcClient(host,port);
        }
    }
    public ClientProxy() throws InterruptedException {
        rpcClient=new NettyRpcClient();
        serviceCenter = new ZKServiceCenter();
        circuitBreakerProvider = new CircuitBreakerProvider();
    }
    //jdk动态代理，每一次代理对象调用方法，都会经过此方法增强（反射获取request对象，socket发送到服务端）
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //构建request
        RPCReqeust request=RPCReqeust.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args).paramsType(method.getParameterTypes()).build();
        //  获取熔断器
        CircuitBreaker circuitBreaker = circuitBreakerProvider.getCircuitBreaker(method.getName());
        //  若被熔断
        if (!circuitBreaker.allowRequest()) {
            return null;
        }
        //数据传输
        RPCResponse response = null;
        //  若在白名单内，则可以重试任务
        if (serviceCenter.checkRetry(method.getDeclaringClass().getName())){
            response = new GuavaRetry().sendServiceWithRetry(request, rpcClient);
        } else {
            response = rpcClient.sendRequest(request);
        }

        if (response!=null) {
            circuitBreaker.recordSuccess();
        } else {
            circuitBreaker.recordFailure();
        }

        return response.getData();
    }
    public <T>T getProxy(Class<T> clazz){
        Object o = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
        return (T)o;
    }
}