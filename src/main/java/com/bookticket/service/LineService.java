package com.bookticket.service;

import com.bookticket.pojo.Line;
import org.springframework.stereotype.Service;

/**
 * 提供线路操作相关接口方法
 */
@Service
public interface LineService {

    /**
     * 通过起始站点和到达站点找到对应的线路信息
     *
     * @param station_start_name 起始站点名
     * @param station_end_name 到达站点名
     * @return combookticket.dao.Line
     */

    public Line getOne(String station_start_name,String station_end_name);
    /**
     * 通过id号查找
     * @param line_id 线路编号
     * @return com.bookticket.dao.Line
     */

    public Line getById(int line_id);
}
