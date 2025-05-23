package org.example.Server;

import org.example.Common.service.UserService;
import org.example.Common.service.serviceImpl.UserServiceImpl;
import org.example.Server.provider.ServiceProvider;
import org.example.Server.server.Impl.NettyRPCServer;
import org.example.Server.server.RpcServer;

public class ServerTest {

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        ServiceProvider serviceProvider = new ServiceProvider("localhost", 9999);

        serviceProvider.provideServiceInterface(userService);

        RpcServer rpcServer = new NettyRPCServer(serviceProvider);

        rpcServer.start(9999);
    }
}
