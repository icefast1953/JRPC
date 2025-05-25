package org.example.Server.netty.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import lombok.AllArgsConstructor;
import org.example.Common.serializer.myCode.MyDecoder;
import org.example.Common.serializer.myCode.MyEncoder;
import org.example.Common.serializer.mySerializer.ObjectSerializer;
import org.example.Server.netty.handler.NettyServerHandler;
import org.example.Server.provider.ServiceProvider;

@AllArgsConstructor
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {
    private ServiceProvider serviceProvider;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline()
                .addLast(new MyDecoder())
                .addLast(new MyEncoder(new ObjectSerializer()))
                .addLast(new NettyServerHandler(serviceProvider));

    }
}
