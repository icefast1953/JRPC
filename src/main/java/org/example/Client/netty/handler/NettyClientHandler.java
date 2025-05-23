package org.example.Client.netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;
import org.example.Common.rpc.RPCResponse;

import java.lang.reflect.InvocationHandler;

public class NettyClientHandler extends SimpleChannelInboundHandler<RPCResponse> {
    // 该方法用于读取服务端返回的数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RPCResponse response) throws Exception {

        System.out.println("Client received: " + response.toString());

        AttributeKey<RPCResponse> key = AttributeKey.valueOf("RPCResponse");

        ctx.channel().attr(key).set(response);

        System.out.println("放置成功！" + response.toString());

        ctx.channel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // 异常处理
        cause.printStackTrace();
        System.out.println("出现异常" + cause.getMessage());
        ctx.close();
    }


}
