package org.example.Server.server.Impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.AllArgsConstructor;
import org.example.Server.netty.initializer.NettyServerInitializer;
import org.example.Server.provider.ServiceProvider;
import org.example.Server.server.RpcServer;

@AllArgsConstructor
public class NettyRPCServer implements RpcServer {

    private ServiceProvider serviceProvider;


    @Override
    public void start(int port) {


        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        System.out.println("netty服务器启动");
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new NettyServerInitializer(serviceProvider));
            ChannelFuture channelFuture = bootstrap.bind(port).sync();

            channelFuture.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

    @Override
    public void stop() {
        // 等待自定义停止逻辑的实现
    }
}
