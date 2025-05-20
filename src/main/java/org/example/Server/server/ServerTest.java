package org.example.Server.server;

import org.example.Common.service.UserService;
import org.example.Common.service.serviceImpl.UserServiceImpl;
import org.example.Server.provider.ServiceProvider;
import org.example.Server.server.Impl.SimpleRPCRPCServer;

public class ServerTest {

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();

        ServiceProvider serviceProvider = new ServiceProvider();

        serviceProvider.provideServiceInterface(userService);

        RpcServer rpcServer = new SimpleRPCRPCServer(serviceProvider);

        rpcServer.start(9999);
    }
}
