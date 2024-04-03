package com.bookticket.service;

import com.bookticket.pojo.Trips;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 提供班次操作相关接口方法
 */
@Service
public interface TripsService {

    //获取全部班次
    List<Trips> getAllTrips();
    /**
     * 根据线路id和出发日期获得该班次
     * @param line_id 线路id
     * @param start_date 出发日期
     * @return java.util.List<com.bookticket.dao.Trips>
     */
    List<Trips> getSomeTrips(int line_id, Date start_date);

    /**
     * 通过班次编号查找获取班次信息(忽略已逻辑删除的行)
     * @param trips_id 班次编号
     * @return com.bookticket.dao.Trips
     */

    Trips getOneById(int trips_id);

    /**
     * 减少该班次商务舱的机票数量，考虑超卖的问题
     * @param trips_id 班次编号
     * @return int
     */
    int decrease_business_class_seatnum(int trips_id);
    /**
     * 减少该班次经济舱的数量，考虑超卖的问题
     * @param trips_id 班次编号
     * @return int
     */
    int decrease_economy_class_seatnum(int trips_id);

    /**
     * 用于退票改签操作使原班次的商务舱座位数量+1
     *
     * @param trips_id 班次编号
     * @return int
     */
    int increase_business_class_seatnum(int trips_id);

    /**
     * 用于退票改签操作使原班次的经济舱座位数量+1
     *
     * @param trips_id 班次编号
     * @return int
     */
    int increase_economy_class_seatnum(int trips_id);

    /**
     * 查找特定线路编号和坐席级别相应座位数量大于0，且起飞时间大于现在的班次，用于改签选择
     *
     * @param order_id 订单编号
     * @return java.util.List<com.bookticket.dao.Trips>
     */
    List<Trips> getChangingTrips(int order_id);

    /**
     *
     * 对超过起飞日期的班次进行逻辑删除
     * @return int
     */
    int deleteEveryDay();
}
