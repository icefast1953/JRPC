package org.example.Server.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.AllArgsConstructor;
import org.example.Common.rpc.RPCReqeust;
import org.example.Common.rpc.RPCResponse;
import org.example.Server.provider.ServiceProvider;
import org.example.Server.ratelimit.RateLimit;
import org.example.Server.ratelimit.provider.RateLimitProvider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@AllArgsConstructor
public class NettyServerHandler extends SimpleChannelInboundHandler<RPCReqeust> {
    private ServiceProvider serviceProvider;

    // 用于读取客户端传输过来的请求
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCReqeust rpcReqeust) throws Exception {
        RPCResponse response = getResponse(rpcReqeust);

        ctx.writeAndFlush(response);

        ctx.close();
    }


    // 处理异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }



    // 得到请求，处理并返回响应
    private RPCResponse getResponse(RPCReqeust rpcReqeust) {
        System.out.println("收到请求" + rpcReqeust);

        String interfaceName = rpcReqeust.getInterfaceName();

//        RateLimit rateLimit = serviceProvider.getRateLimitProvider().getRateLimit(interfaceName);
//
//        if (!rateLimit.getToken()) {
//            System.out.println("服务限流！！");
//            return RPCResponse.failure();
//        }

        // 获得服务对象
        Object service = serviceProvider.getService(interfaceName);

        Method method = null;

        try {
            // 获取方法名
            String methodName = rpcReqeust.getMethodName();

            method = service.getClass().getMethod(methodName, rpcReqeust.getParamsType());

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
