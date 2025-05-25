package org.example.Common.serializer.myCode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.example.Common.rpc.MessageType;
import org.example.Common.serializer.mySerializer.JsonSerializer;
import org.example.Common.serializer.mySerializer.ObjectSerializer;
import org.example.Common.serializer.mySerializer.Serializer;

import java.util.List;

public class MyDecoder extends ByteToMessageDecoder {
    /**
     * @param ctx
     * @param byteBuf
     * @param list
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 读取消息类型
        short messageType = byteBuf.readShort();
        // 判断消息类型
        if (messageType != MessageType.REQUEST.getCode() && messageType != MessageType.RESPONSE.getCode()) {
            throw new Exception("暂不支持此类型");
        }
        // 读取解析器类型
        short serializerType = byteBuf.readShort();
        // 通过静态工厂方法获取解析器的具体对象
        Serializer serializer = Serializer.getSerializerByCode(serializerType);

        if (serializer == null) {
            throw new Exception("暂不支持此类序列化器");
        }
        // 获取对象的长度，用于创建数组来获取对象
        int byteLength = byteBuf.readInt();
        byte[] bytes = new byte[byteLength];
        // 通过把数据写入数组来获取对象
        byteBuf.readBytes(bytes);
        // 反序列化
        Object object = serializer.deserialize(bytes, messageType);
        // 在结果列表中添加对象
        list.add(object);


    }
}
