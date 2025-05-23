package org.example.Client.rpcClient.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.example.Client.netty.nettyinitializer.NettyClientInitializer;
import org.example.Client.rpcClient.RpcClient;
import org.example.Client.serviceCenter.ServiceCenter;
import org.example.Client.serviceCenter.ZKServiceCenter;
import org.example.Common.rpc.RPCReqeust;
import org.example.Common.rpc.RPCResponse;

import java.net.InetSocketAddress;

public class NettyRpcClient implements RpcClient {

    private ServiceCenter serviceCenter;

    static final private Bootstrap bootstrap;

    static final private EventLoopGroup group;

    public NettyRpcClient() {
        serviceCenter = new ZKServiceCenter();
    }

    static {
        bootstrap = new Bootstrap();
        group = new NioEventLoopGroup();
        bootstrap.group(group);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new NettyClientInitializer());
    }

    @Override
    public RPCResponse sendRequest(RPCReqeust rpcReqeust) {
        try {
            InetSocketAddress inetSocketAddress = serviceCenter.serviceDiscovery(rpcReqeust.getInterfaceName());

            String host = inetSocketAddress.getAddress().getHostAddress();

            int port = inetSocketAddress.getPort();

            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();

            System.out.println("连接成功!");

            Channel channel = channelFuture.channel();
            // 发送数据
            channel.writeAndFlush(rpcReqeust);
            // 阻塞以获取结果
            channel.closeFuture().sync();

            AttributeKey<RPCResponse> key = AttributeKey.valueOf("RPCResponse");

            RPCResponse rpcResponse = channel.attr(key).get();

            System.out.println(rpcResponse);

            return rpcResponse;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
