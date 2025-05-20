package org.example.Common.service.serviceImpl;

import org.example.Common.pojo.User;
import org.example.Common.service.UserService;

import java.util.Random;
import java.util.UUID;

public class UserServiceImpl implements UserService {

    @Override
    public User getUserByUserId(Integer id) {
        System.out.println("客户查询了id为 " + id + " 的用户");
        Random random = new Random();
        User user = User.builder()
                .userName(UUID.randomUUID().toString())
                .sex(random.nextBoolean())
                .id(id)
                .build();
        return user;
    }

    @Override
    public Integer insertUserId(User user) {
        System.out.println("成功插入用户" + user.getUserName());
        return user.getId();
    }
}
