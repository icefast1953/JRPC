package org.example.Common.serializer.mySerializer;

import java.io.*;

public class ObjectSerializer implements Serializer {
    /**
     * 把对象序列化为字节数组
     *
     * @param obj
     * @return
     */
    @Override
    public byte[] serialize(Object obj) {
        byte[] bytes = null;

        // 使用字节数组输出流来将字节数组写入字节缓冲区中
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            // 对象流不能单独使用，其作用是把对象转换为字节数组
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            // 将字节缓冲区的内容存入bytes中
            bytes = bos.toByteArray();
            oos.close();
            bos.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return bytes;

    }

    /**
     * 将一个字节数组反序列化为对象
     *
     * @param bytes
     * @param messageType
     * @return
     */
    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        // 此处不需要管messageType
        Object obj = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return obj;
    }

    /**
     * 获取消息类型
     *
     * @return
     */
    @Override
    public int getType() {
        return 0;
    }
}