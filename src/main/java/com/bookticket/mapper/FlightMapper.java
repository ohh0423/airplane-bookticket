package com.bookticket.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bookticket.pojo.Flight;
import org.apache.ibatis.annotations.Mapper;

/**
 * 继承mybatis-plus的BaseMapper<T>,里面有通用的数据库操作方法可使用
 * 也可自定义操作flight表的接口方法，这里采用Mybatis的注解形式

 */

@Mapper
public interface FlightMapper extends BaseMapper<Flight> {

}