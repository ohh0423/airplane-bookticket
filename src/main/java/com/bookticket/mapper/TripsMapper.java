package com.bookticket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bookticket.pojo.Trips;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 *
 * 继承mybatis-plus的BaseMapper<T>,里面有通用的数据库操作方法可使用
 * 也可自定义操作trips表的接口方法，这里采用Mybatis的注解形式
 */

@Mapper
public interface TripsMapper extends BaseMapper<Trips> {

    /**
     * 乐观锁实现
     * 对应编号的班次的商务舱机票数量-1
     * @param trips_id 班次编号
     * @return int
     */
    @Update("update trips set business_class_seat_num=business_class_seat_num-1 where trips_id=#{trips_id} and business_class_seat_num-1>=0 ")
    int decrease_business_class_seat(int trips_id);

    /**
     * 乐观锁实现
     * 对应编号的班次的经济舱机票数量-1
     * @param trips_id 班次编号
     * @return int
     */
    @Update("update trips set economy_class_seat_num=economy_class_seat_num-1 where trips_id=#{trips_id} and economy_class_seat_num-1>=0 ")
    int decrease_economy_class_seat(int trips_id);

    /**
     * 乐观锁实现
     * 对应编号的班次的商务舱机票数量+1
     * @param trips_id 班次编号
     * @return int
     */
    @Update("update trips set business_class_seat_num=business_class_seat_num+1 where trips_id=#{trips_id}  ")
    int increase_business_class_seat(int trips_id);

    /**
     * 乐观锁实现
     * 对应编号的班次的经济舱机票数量+1
     * @param trips_id 班次编号
     * @return int
     */
    @Update("update trips set economy_class_seat_num=economy_class_seat_num+1 where trips_id=#{trips_id}  ")
    int increase_economy_class_seat(int trips_id);

    /**
     * 通过编号查找班次，无论它是否已经被逻辑删除，用于用户查询自己的订单时订单里的班次
     *
     * @param trips_id 班次编号
     * @return com.bookticket.dao.Trips
     */
    @Select("select * from trips where trips_id=#{trips_id}")
    Trips getOneByIdForOrder(int trips_id);
}