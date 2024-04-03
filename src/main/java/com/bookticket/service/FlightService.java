package com.bookticket.service;

import com.bookticket.pojo.Flight;
import org.springframework.stereotype.Service;

/**
 * 提供航班相关操作的接口方法
 *
 */

@Service
public interface FlightService {
    /**
     *
     * 通过航班id得到航班对象
     *
     */

    public Flight getOneById(int flight_id);

}
