package org.example.Common.service;

import org.example.Common.pojo.User;

public interface UserService {
    /**
     * 根据id查询用户
     * @param id
     * @return
     */
     User getUserByUserId(Integer id);

     Integer insertUserId(User user);
}
