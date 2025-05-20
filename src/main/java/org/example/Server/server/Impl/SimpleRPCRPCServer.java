package org.example.Server.server.Impl;

import lombok.AllArgsConstructor;
import org.example.Server.provider.ServiceProvider;
import org.example.Server.server.RpcServer;
import org.example.Server.server.work.WorkThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@AllArgsConstructor
public class SimpleRPCRPCServer implements RpcServer {
    private ServiceProvider serviceProvider;

    @Override
    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("服务器启动~~~");

            while (true) {
                // 如果没有连接，会阻塞在这里
                Socket socket = serverSocket.accept();

                new Thread(new WorkThread(socket, serviceProvider)).start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        // 这里留给之后的自定义逻辑
    }
}
