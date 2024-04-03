package com.bookticket.pojo;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName user
 */
@Data
@TableName(value = "user")
public class User implements Serializable {
    /**
     * 编号
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer user_id;

    /**
     * 登陆名
     *
     */
    private String user_login_name;

    /**
     * 密码（md5存储+盐）
     *
     */
    private String user_password;

    /**
     * 盐
     *
     */
    private String user_salt;

    /**
     * 性别
     *
     */
    private String user_sex;

    /**
     * 联系电话
     *
     */
    private String user_phone;

    /**
     * 邮箱
     *
     */
    private String user_email;

    /**
     * 生日
     *
     */
    private Date user_birth;

    /**
     * 身份证号
     *
     */
    private String user_identity_num;

}