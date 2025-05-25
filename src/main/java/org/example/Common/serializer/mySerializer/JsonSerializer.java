package org.example.Common.serializer.mySerializer;



import com.alibaba.fastjson.JSONObject;
import org.example.Common.rpc.RPCReqeust;
import org.example.Common.rpc.RPCResponse;


public class JsonSerializer implements Serializer {
    /**
     * 把对象序列化为字节数组
     *
     * @param obj
     * @return
     */
    @Override
    public byte[] serialize(Object obj) {
        byte[] bytes = JSONObject.toJSONBytes(obj);
        return bytes;
    }

    /**
     * 将一个字节数组反序列化为对象
     *
     * @param bytes
     * @param messageType // 0 表示 rpcRequest， 1 表示rpcResponse
     * @return
     */
    @Override
    public Object deserialize(byte[] bytes, int messageType) {
        Object obj = null;
        switch (messageType) {
            case 0:
                RPCReqeust rpcReqeust = JSONObject.parseObject(bytes, RPCReqeust.class);
                Object[] objects = new Object[rpcReqeust.getParams().length];
                for (int i = 0; i < rpcReqeust.getParams().length; i++) {

                    Class<?> type = rpcReqeust.getParamsType()[i];

                    if (!type.isAssignableFrom(rpcReqeust.getParams()[i].getClass())) {
                        objects[i] = JSONObject.toJavaObject((JSONObject) rpcReqeust.getParams()[i], rpcReqeust.getParamsType()[i]);
                    } else {
                        objects[i] = rpcReqeust.getParams()[i];
                    }
                }
                rpcReqeust.setParams(objects);
                obj = rpcReqeust;
                break;

                case 1:
                    RPCResponse rpcResponse = JSONObject.parseObject(bytes, RPCResponse.class);
                    Class<?> dataType = rpcResponse.getDataType();
                    if (!dataType.isAssignableFrom(rpcResponse.getData().getClass())) {
                        rpcResponse.setData(JSONObject.toJavaObject((JSONObject) rpcResponse.getData(), dataType));
                    }
                    obj = rpcResponse;
                    break;

                    default:
                        System.out.println("暂时不接受这种消息！");
                        break;

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
        return 1;
    }
}
