package org.example.Server.server.work;

import lombok.AllArgsConstructor;
import org.example.Common.rpc.RPCReqeust;
import org.example.Common.rpc.RPCResponse;
import org.example.Server.provider.ServiceProvider;
import org.example.Server.server.RpcServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

@AllArgsConstructor
public class WorkThread implements Runnable {
    Socket socket;

    ServiceProvider serviceProvider;

    @Override
    public void run() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            // 读取请求
            RPCReqeust rpcReqeust = (RPCReqeust) ois.readObject();

            RPCResponse response = getResponse(rpcReqeust);
            oos.writeObject(response);
            oos.flush();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // 得到请求，处理并返回响应
    private RPCResponse getResponse(RPCReqeust rpcReqeust) {
        // 获得服务对象
        Object service = serviceProvider.getService(rpcReqeust.getInterfaceName());

        Method method = null;

        try {
            // 获取方法名
            String methodName = rpcReqeust.getMethodName();

            method = service.getClass().getMethod(methodName, rpcReqeust.getParamsType());

            method.setAccessible(true);

            Object data = method.invoke(service, rpcReqeust.getParams());

            return RPCResponse.success(data);

            //找不到请求的方法，方法无法访问，执行异常
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.out.println("方法执行错误");
            RPCResponse.failure();
        }

        return RPCResponse.failure();

    }
}
