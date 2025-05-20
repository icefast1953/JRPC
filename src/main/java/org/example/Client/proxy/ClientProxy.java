package org.example.Client.proxy;

import lombok.AllArgsConstructor;
import org.example.Client.IOClient;
import org.example.Common.rpc.RPCReqeust;
import org.example.Common.rpc.RPCResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@AllArgsConstructor
public class ClientProxy implements InvocationHandler {
    private String host;

    private int port;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RPCReqeust rpcReqeust = RPCReqeust.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .params(args)
                .paramsType(method.getParameterTypes())
                .build();
        RPCResponse rpcResponse = IOClient.sendReqeust(host, port, rpcReqeust);

        return rpcResponse.getData();
    }


    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, this);
    }
}
