package org.example.Common.serializer.mySerializer;

// 用于自己特定的序列化
public interface Serializer {
    /**
     * 把对象序列化为字节数组
     * @param obj
     * @return
     */
    byte[] serialize(Object obj);

    /**
     * 将一个字节数组反序列化为对象
     * @param bytes
     * @param messageType
     * @return
     */
    Object deserialize(byte[] bytes, int messageType);

    /**
     * 获取消息类型
     * @return
     */
    int getType();


    /**
     * 静态工厂方法
     * 根据序号取出序列化器， 0 为JAVA自带的， 1 为Json序列化器
     * @param code
     * @return
     */
    static Serializer getSerializerByCode(int code) {
        switch (code) {
            case 0:
                return  new ObjectSerializer();
            case 1:
                return  new JsonSerializer();
            default:
                return null;
        }
    }
}
