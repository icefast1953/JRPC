package org.example.Client.netty.nettyinitializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.example.Client.netty.handler.NettyClientHandler;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        socketChannel.pipeline()
                .addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                .addLast(new LengthFieldPrepender(4))
                .addLast(new ObjectEncoder())
                .addLast(new ObjectDecoder(new ClassResolver() {
                    @Override
                    public Class<?> resolve(String s) throws ClassNotFoundException {
                        return Class.forName(s);
                    }
                }))
                .addLast(new NettyClientHandler());
    }
}
