package org.example.Common.serializer.myCode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.AllArgsConstructor;
import org.example.Common.rpc.MessageType;
import org.example.Common.rpc.RPCReqeust;
import org.example.Common.rpc.RPCResponse;
import org.example.Common.serializer.mySerializer.Serializer;

@AllArgsConstructor
public class MyEncoder extends MessageToByteEncoder {
    private Serializer serializer;

    /**
     * @param ctx
     * @param msg
     * @param out
     * @throws Exception
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        // 输出对象类，便于调试
        System.out.println(msg.getClass());

        if (msg instanceof RPCReqeust) {
            out.writeShort(MessageType.REQUEST.getCode());
        } else if (msg instanceof RPCResponse) {
            out.writeShort(MessageType.RESPONSE.getCode());
        }

        out.writeShort(serializer.getType());

        byte[] bytes = serializer.serialize(msg);

        out.writeInt(bytes.length);
        out.writeBytes(bytes);


    }
}
