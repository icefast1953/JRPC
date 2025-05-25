package org.example.Client;

import org.example.Client.proxy.ClientProxy;
import org.example.Common.pojo.User;
import org.example.Common.service.UserService;

public class TestClient {
    public static void main(String[] args) throws InterruptedException {
        ClientProxy proxy = new ClientProxy();
        UserService userService = proxy.getProxy(UserService.class);
        User byUserId = userService.getUserByUserId(1);
        System.out.println(byUserId);

        User user = User.builder()
                .id(100)
                .sex(true)
                .userName("wxx")
                .build();
        Integer i = userService.insertUserId(user);
        System.out.println(i);

    }
}
