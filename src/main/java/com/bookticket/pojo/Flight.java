package com.bookticket.pojo;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @TableName flight
 */
@Data
@TableName(value = "flight")
public class Flight implements Serializable {
    /**
     * 编号
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer flight_id;

    /**
     * 航班名称，类似G6340
     *
     */
    private String flight_name;

    /**
     * 航班的最大速度
     *
     */
    private Float flight_speed;

    /**
     * 航班的座位数量
     *
     */
    private Integer flight_seat_num;

}