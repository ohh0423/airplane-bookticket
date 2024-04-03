package com.bookticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bookticket.mapper.TripsMapper;
import com.bookticket.pojo.Line;
import com.bookticket.pojo.Orders;
import com.bookticket.pojo.Trips;
import com.bookticket.service.LineService;
import com.bookticket.service.OrderService;
import com.bookticket.service.TripsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 线路接口的实现类
 *
 */
@Service
public class TripsServiceImpl implements TripsService {
    @Autowired
    private TripsMapper tripsMapper;
    @Autowired
    private OrderService orderService;
    @Autowired
    private LineService lineService;

    @Override
    public List<Trips> getAllTrips() {
        return tripsMapper.selectList(new QueryWrapper<>());
    }

    @Override
    public List<Trips> getSomeTrips(int line_id, Date start_date) {
        if (start_date == null) {
            // 处理start_date为null的情况，例如抛出异常或返回空列表
            return Collections.emptyList(); // 或者其他适当的处理方式
        }
        Date end_date = new Date(start_date.getTime() + 3600 * 24 * 1000);
        return tripsMapper.selectList(new QueryWrapper<Trips>()
                .eq("trips_line_id", line_id).between("trips_start_time", new Date(), end_date)
                .and(i -> i.ge("business_class_seat_num", 1).or().ge("economy_class_seat_num", 1))
        );
    }

    @Override
    public Trips getOneById(int trips_id) {
        return tripsMapper.selectById(trips_id);
    }

    @Override
    public int decrease_business_class_seatnum(int trips_id) {
        return tripsMapper.decrease_business_class_seat(trips_id);
    }

    @Override
    public int decrease_economy_class_seatnum(int trips_id) {
        return tripsMapper.decrease_economy_class_seat(trips_id);
    }

    @Override
    public int increase_business_class_seatnum(int trips_id) {
        return tripsMapper.increase_business_class_seat(trips_id);
    }

    @Override
    public int increase_economy_class_seatnum(int trips_id) {
        return tripsMapper.increase_economy_class_seat(trips_id);
    }

    @Override
    public List<Trips> getChangingTrips(int order_id) {
        Orders order = orderService.getOneById(order_id);
        int seat_level = order.getOrder_seat_level();
        Integer trips_id = order.getOrder_trips_id();
        Trips trips = getOneById(trips_id);
        int line_id = trips.getTrips_line_id();
        Line line = lineService.getById(line_id);
        String start_station_name = line.getLine_start_station_name();
        String end_station_name = line.getLine_end_station_name();
        List<Trips> result;

        if (seat_level == 4)
            result = tripsMapper.selectList(new QueryWrapper<Trips>().eq("trips_line_id", line_id).ne("trips_id", trips_id).
                    gt("business_class_seat_num", 0).gt("trips_start_time", new Date()));
        else
            result = tripsMapper.selectList(new QueryWrapper<Trips>().eq("trips_line_id", line_id).ne("trips_id", trips_id).
                    gt("economy_class_seat_num", 0).gt("trips_start_time", new Date()));

        result.forEach(res -> {
            res.setTrips_start_station_name(start_station_name);
            res.setTrips_end_station_name(end_station_name);
        });

        return result;
    }

    @Override
    public int deleteEveryDay() {
        return tripsMapper.delete(new QueryWrapper<Trips>().eq("trips_delete_flag", 0).
                lt("trips_start_time", new Date()));
    }
}