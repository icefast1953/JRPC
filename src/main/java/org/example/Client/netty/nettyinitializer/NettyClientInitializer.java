package org.example.Client.netty.nettyinitializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.example.Client.netty.handler.NettyClientHandler;
import org.example.Common.serializer.myCode.MyDecoder;
import org.example.Common.serializer.myCode.MyEncoder;
import org.example.Common.serializer.mySerializer.JsonSerializer;

public class NettyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        socketChannel.pipeline()
                .addLast(new MyDecoder())
                .addLast(new MyEncoder(new JsonSerializer()))
                .addLast(new NettyClientHandler());
    }
}
