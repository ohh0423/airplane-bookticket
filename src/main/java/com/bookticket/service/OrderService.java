package com.bookticket.service;

import com.bookticket.pojo.Orders;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 提供航班相关操作的接口方法
 */
@Service
public interface OrderService {
    /**
     *
     *
     * @param order 创建订单，同时该班次的对应的机票数量也要减一
     * @return int
     */

    public int saveOne(Orders order);

    /**
     * 通过用户id得到该用户的订单列表
     * @param user_id 用户编号
     * @return java.util.List<com.bookticket.dao.Orders>
     */

    public List<Orders> getOrderListByUser(int user_id);

    /**
     * 进行退票操作，并且相应班次的相应坐席的机票数量要+1
     * @param order_id 订单编号
     * @return int
     */

    public int refundTicket(int order_id);

    /**
     * 根据订单编号获取订单信息
     * @param order_id 订单编号
     * @return com.bookticket.dao.Orders
     */

    public Orders getOneById(int order_id);

    /**
     * 进行改签操作（在事务中）
     * 对原有的订单状态修改为已改签，对选择的班次生成相应的订单，订单大部分信息由原来的订单信息里获得
     * 原订单相应班次的相应坐席票数+1 ，新订单对应的班次的对应坐席机票数量-1
     * @param order_id 订单编号，需要改签的订单
     * @param trips_id 班次编号，选择改签的班次
     * @return int
     */

    public int changing_order(int order_id,int trips_id);
}
