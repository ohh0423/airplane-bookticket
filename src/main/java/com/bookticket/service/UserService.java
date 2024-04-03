package com.bookticket.service;

import com.bookticket.pojo.User;
import org.springframework.stereotype.Service;
/**
 * User的服务器类
 */

@Service
public interface UserService {

    /**
     * 通过登陆名查找用户，不存在则返回null
     * @param user_login_name
     * @return com.bookticket.dao.User
     */
    public User selecOneByname(String user_login_name);

    /**
     * 新增一个用户
     * @param user 用户
     * @return boolean
     */
    public boolean addOne(User user);

    /**
     * 修改一个用户的信息,并将用户存储到session中
     *
     * @param user 用户
     * @return int
     */
    public int update(User user);

    
}
