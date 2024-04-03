package com.bookticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bookticket.mapper.LineMapper;
import com.bookticket.pojo.Line;
import com.bookticket.service.LineService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 */
@Service
public class LineServiceImpl implements LineService {
    @Autowired
    private LineMapper lineMapper;

    @Override
    public Line getOne(String station_start_name, String station_end_name) {
        Map<String, Object> map = new HashMap<>();
        map.put("line_start_station_name", station_start_name);
        map.put("line_end_station_name", station_end_name);
        Line line = lineMapper.selectOne(new QueryWrapper<Line>().allEq(map));
        return line;
    }

    @Override
    public Line getById(int line_id) {
        Line line = lineMapper.selectById(line_id);
        return line;
    }
}