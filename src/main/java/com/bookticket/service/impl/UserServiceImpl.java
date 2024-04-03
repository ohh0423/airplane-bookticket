package com.bookticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bookticket.mapper.UserMapper;
import com.bookticket.pojo.User;
import com.bookticket.service.UserService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * userService的实现类
 *
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public User selecOneByname(String user_login_name) {
        return userMapper.selectOne(new QueryWrapper<User>()
                .eq(true, "user_login_name", user_login_name));

    }
    @Override
    public boolean addOne(User user) {
        return userMapper.insert(user) != 0;
    }

    @Override
    public int update(User user)  {
        if (userMapper.updateById(user) != 0) {
            // 获取 subject 认证主体
            Subject currentUser = SecurityUtils.getSubject();
            Session session = currentUser.getSession();
            session.setAttribute("user", user);

            return 1;
        }
        return 0;
    }
}
