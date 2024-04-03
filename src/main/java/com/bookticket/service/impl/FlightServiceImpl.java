package com.bookticket.service.impl;

import com.bookticket.mapper.FlightMapper;
import com.bookticket.pojo.Flight;
import com.bookticket.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 航班服务类的实现类
 */
@Service
public class FlightServiceImpl implements FlightService {
    @Autowired
    private FlightMapper flightMapper;


    @Override
    public Flight getOneById(int flight_id) {
        Flight flight = flightMapper.selectById(flight_id);
        return flight;
    }
}
