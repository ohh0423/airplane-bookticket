package com.bookticket.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "notice")
public class Notice implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer notice_id;
    private Date create_time;
    private String notice_name;
    private String text;


}
