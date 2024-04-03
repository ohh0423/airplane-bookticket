package com.bookticket.config;

import com.bookticket.common.DbRealm111;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro配置类
 *
 */
@Configuration
public class shiroConfig {

    /**
     * 注入 Shiro 过滤器
     * @return org.apache.shiro.spring.web.ShiroFilterFactoryBean
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter() {
        // 定义 shiroFactoryBean
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        // 设置自定义的 securityManager
        shiroFilter.setSecurityManager(mySecurityManager());
        //设置默认登录的 URL，身份认证失败会访问该 URL
        shiroFilter.setLoginUrl("/login");
        // 设置成功之后要跳转的链接
        shiroFilter.setSuccessUrl("/");
        // LinkedHashMap 是有序的，进行顺序拦截器配置
        Map<String,String> filterChainMap = new LinkedHashMap<>();
        // 配置可以匿名访问的地址，可以根据实际情况自己添加，放行一些静态资源等，anon 表示放行
        filterChainMap.put("/css/**", "anon");
        filterChainMap.put("/images/**", "anon");
        filterChainMap.put("/js/**", "anon");
        filterChainMap.put("/", "anon");
        filterChainMap.put("/getTrips", "anon");
        filterChainMap.put("/loginValidateCode", "anon");
        filterChainMap.put("/getCheckCode", "anon");
        // 登录 URL 放行
        filterChainMap.put("/login", "anon");
        // 注册 URL 放行
        filterChainMap.put("/register", "anon");
        // 以“/user” 开头的用户需要身份认证，authc 表示要进行身份认证
        filterChainMap.put("/user/**", "authc");
        // 配置 logout 过滤器
        filterChainMap.put("/logout", "logout");

        shiroFilter.setFilterChainDefinitionMap(filterChainMap);
        return shiroFilter;
    }


    /**
     * 注入安全管理器
     * @return java.lang.SecurityManager
     */
    @Bean
    public DefaultWebSecurityManager mySecurityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myRealm());
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }
    /**
     *
     * 注入自定义的Realm
     * @return com.bookticket.common.DbRealm
     */
    @Bean
    public DbRealm111 myRealm() {
        DbRealm111 dbRealm = new DbRealm111();
        dbRealm.setCredentialsMatcher(hashedCredentialsMatcher());      //设置加密
        return dbRealm;
    }

    /**
     * 会话管理器
     * @return org.apache.shiro.web.session.mgt.DefaultWebSessionManager
     */
    @Bean
    public DefaultWebSessionManager sessionManager(){
        DefaultWebSessionManager sessionManager=new DefaultWebSessionManager();
        sessionManager.setGlobalSessionTimeout(3600000);
        sessionManager.setDeleteInvalidSessions(true);
        return sessionManager;
    }

    /**
     * 凭证匹配器
     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了）
     * HashedCredentialsMatcher说明：
     * 用户传入的token先经过shiroRealm的doGetAuthenticationInfo方法
     * 此时token中的密码为明文。
     * 再经由HashedCredentialsMatcher加密password与查询用户的结果password做对比。
     * @return org.apache.shiro.authc.credential.HashedCredentialsMatcher
     */

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");            //设置加密算法
        matcher.setHashIterations(3);                   //散列的次数，比如散列两次，相当于 MD5(MD5(""));
        return matcher;
    }
}
