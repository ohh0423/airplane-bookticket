package com.bookticket.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bookticket.service.LineService;
import com.bookticket.service.OrderService;
import com.bookticket.service.TripsService;
import com.bookticket.mapper.OrderMapper;
import com.bookticket.mapper.TripsMapper;
import com.bookticket.pojo.Line;
import com.bookticket.pojo.Orders;
import com.bookticket.pojo.Trips;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Date;
import java.util.List;

/**
 * 订单服务的实现类
 *
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private TripsMapper tripsMapper;
    @Autowired
    private LineService lineService;
    @Autowired
    private TripsService tripsService;

    // 移除Redis相关的操作
    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int saveOne(Orders order) {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setName("订票操作！");
        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(transactionDefinition);

        // 生成订单
        int count = orderMapper.insert(order);
        // 减少机票数量
        int trips_id = order.getOrder_trips_id();
        int order_seat_level = order.getOrder_seat_level();
        if (order_seat_level == 4)
            count += tripsService.decrease_business_class_seatnum(trips_id);
        else
            count += tripsService.decrease_economy_class_seatnum(trips_id);
        if (count == 2) {
            // 存入缓存
            // 此处原有的Redis缓存操作已被移除
        } else {
            transactionManager.rollback(status); // 进行回滚
        }

        return count;
    }

    @Override
    public List<Orders> getOrderListByUser(int user_id) {
        // 直接从数据库中取出数据
        List<Orders> ordersList = orderMapper.selectList(
                new QueryWrapper<Orders>().eq("order_user_id", user_id).orderByDesc("order_create_time"));

        if (ordersList != null && ordersList.size() > 0) {
            ordersList.forEach(order -> {
                int trips_id = order.getOrder_trips_id();
                Trips trips = tripsMapper.getOneByIdForOrder(trips_id);  //已逻辑删除的班次也会获取到
                order.setOrder_flight_name(trips.getTrips_flight_name()); //获得航班名

                Date date = trips.getTrips_start_time();
                if (date.getTime() <= new Date().getTime()) {
                    order.setOrder_status(1);   //设置已起飞
                    orderMapper.updateById(order);
                }
                order.setStart_date(date);  //设置起飞日期

                int line_id = trips.getTrips_line_id();
                Line line = lineService.getById(line_id);
                order.setStart_city(line.getLine_start_station_name()); //设置出发和达到城市
                order.setEnd_city(line.getLine_end_station_name());
            });
        }

        return ordersList;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int refundTicket(int order_id) {
        DefaultTransactionDefinition transactionDefinition = new DefaultTransactionDefinition();
        transactionDefinition.setName("退票操作！");
        transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(transactionDefinition);

        // 移除Redis缓存操作
        Orders order = getOneById(order_id);
        // 移除Redis缓存操作
        order.setOrder_status(2);
        int count = orderMapper.updateById(order);

        int trips_id = order.getOrder_trips_id();
        int seat_level = order.getOrder_seat_level();
        if (seat_level == 4)
            count += tripsService.increase_business_class_seatnum(trips_id);
        else
            count += tripsService.increase_economy_class_seatnum(trips_id);
        if (count != 2)
            transactionManager.rollback(status);

        return count;
    }

    @Override
    public Orders getOneById(int order_id) {
        // 移除Redis缓存操作
        return orderMapper.selectById(order_id);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public int changing_order(int order_id, int trips_id) {

        try {
            // 获取旧订单信息
            Orders old_order = getOneById(order_id);
            int seat_level = old_order.getOrder_seat_level();

            // 修改旧订单状态为已改签
            old_order.setOrder_status(3);
            orderMapper.updateById(old_order);

            // 增加原班次座位数量
            if (seat_level == 4)
                tripsService.increase_business_class_seatnum(old_order.getOrder_trips_id());
            else
                tripsService.increase_economy_class_seatnum(old_order.getOrder_trips_id());

            // 获取新班次信息
            Trips new_trips = tripsService.getOneById(trips_id);
            Float new_trips_price;
            // 根据座位等级设置新班次价格并减少座位数量
            if (seat_level == 4) {
                new_trips_price = new_trips.getBusiness_class_seat_price();
                tripsService.decrease_business_class_seatnum(trips_id);
            } else {
                new_trips_price = new_trips.getEconomy_class_seat_price();
                tripsService.decrease_economy_class_seatnum(trips_id);
            }

            // 创建新订单
            Orders new_order = new Orders();
            BeanUtils.copyProperties(old_order, new_order);
            new_order.setOrder_id(null);
            new_order.setOrder_trips_id(trips_id);
            new_order.setOrder_status(0);
            new_order.setOrder_create_time(null);
            new_order.setOrder_update_time(null);
            new_order.setOrder_price(new_trips_price);
            orderMapper.insert(new_order);

            return 1;
        } catch (Exception e) {
            // 捕获异常并抛出新的异常
            throw new RuntimeException("改签操作失败", e);
        }
    }
}