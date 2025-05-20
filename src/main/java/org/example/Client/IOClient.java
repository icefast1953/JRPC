package org.example.Client;

import org.example.Common.rpc.RPCReqeust;
import org.example.Common.rpc.RPCResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class IOClient {

    public static RPCResponse sendReqeust(String host, int port, RPCReqeust rpcReqeust) {
        try {
            //套接字连接
            Socket socket = new Socket(host, port);
            // 对象输出字节流（用于序列化）
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            // 对象输入字节流（用于反序列化）
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            // 写入网络io
            oos.writeObject(rpcReqeust);
            // 刷盘，确保完全写入
            oos.flush();
            // 读取网络io
            RPCResponse rpcResponse = (RPCResponse) ois.readObject();

            return rpcResponse;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
