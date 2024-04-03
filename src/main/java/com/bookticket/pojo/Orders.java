package com.bookticket.pojo;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

/**
 * @TableName order
 */
@Data
@TableName(value = "orders")
public class Orders implements Serializable {
    /**
     * 编号
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer order_id;

    /**
     * 用户编号
     *
     */
    private Integer order_user_id;

    /**
     * 班次编号
     *
     */
    private Integer order_trips_id;

    /**
     * 创建时间
     *
     */
    @TableField(fill = FieldFill.INSERT)
    private Date order_create_time;

    /**
     * 修改时间
     *
     */
    @TableField(fill = FieldFill.UPDATE)
    private Date order_update_time;

    /**
     * 订单状态（订单状态（0：创建，1：已起飞，2：退票，3：改签））
     *
     */
    private Integer order_status;

    /**
     * 乘客姓名
     *
     */
    private String order_passenger_name;

    /**
     * 联系人姓名
     *
     */
    private String order_linkman_name;

    /**
     * 联系人手机号
     *
     */
    private String order_linkman_phone;

    /**
     * 乘客身份证号码
     *
     */
    private String order_passenger_identity_num;

    /**
     * 订购的坐席（商务舱/经济舱,分别为1，2）
     *
     */
    private Integer order_seat_level;
    /**
     * 订单金额
     *
     */
    private Float order_price;

    //非数据库字段
    @TableField(exist = false)
    private String order_flight_name;       //航班名称

    @TableField(exist = false)
    private Date start_date;                //起飞日期

    @TableField(exist = false)
    private String start_city;              //出发城市

    @TableField(exist = false)
    private String end_city;                //到达城市
}