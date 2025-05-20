package org.example.Server.server.Impl;

import org.example.Server.provider.ServiceProvider;
import org.example.Server.server.RpcServer;
import org.example.Server.server.work.WorkThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class ThreadPoolRPCServer implements RpcServer {
    // 本地线程池
    private ThreadPoolExecutor executor;
    // 本地服务提供器
    private ServiceProvider serviceProvider;

    ThreadPoolRPCServer(ServiceProvider serviceProvider) {
        this.serviceProvider = serviceProvider;
        executor = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
                1000, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100));
    }

    ThreadPoolRPCServer(ServiceProvider serviceProvider, int corePoolSize,
                        int maximumPoolSize,
                        long keepAliveTime,
                        TimeUnit unit,
                        BlockingQueue<Runnable> workQueue) {
        this.serviceProvider = serviceProvider;
        this.executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }


    @Override
    public void start(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            // 服务器启动
            System.out.println("服务器启动~~~");
            while (true) {
                Socket socket = serverSocket.accept();

                executor.execute(new WorkThread(socket, serviceProvider));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {

    }
}
