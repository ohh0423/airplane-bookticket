package com.bookticket.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "message")
public class Message {
    private Integer message_id;
    private Integer user_id;
    private String user_Login_Name;
    private Date message_create_time;
    private String message;
    private Date reply_time;
    private String reply;

    public String getUserLoginName() {
        return this.user_Login_Name;
    }
}