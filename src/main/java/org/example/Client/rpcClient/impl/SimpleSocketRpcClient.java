package org.example.Client.rpcClient.impl;

import lombok.AllArgsConstructor;
import org.example.Client.rpcClient.RpcClient;
import org.example.Common.rpc.RPCReqeust;
import org.example.Common.rpc.RPCResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

@AllArgsConstructor
public class SimpleSocketRpcClient implements RpcClient {
    String host;

    int port;

    @Override
    public RPCResponse sendRequest(RPCReqeust rpcReqeust) {
        try {
            Socket socket = new Socket(host, port);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

            oos.writeObject(rpcReqeust);

            oos.flush();

            Object object = ois.readObject();

            return (RPCResponse) object;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
