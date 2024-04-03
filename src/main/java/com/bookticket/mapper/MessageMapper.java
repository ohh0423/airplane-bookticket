package com.bookticket.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bookticket.pojo.Message;

import org.apache.ibatis.annotations.Mapper;



@Mapper
public interface MessageMapper extends BaseMapper<Message> {


}