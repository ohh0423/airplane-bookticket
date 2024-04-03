package com.bookticket.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bookticket.pojo.Message;
import com.bookticket.mapper.MessageMapper;
import com.bookticket.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;
    @Override
    public List<Message> getMessageList() {
        return messageMapper.selectList(new QueryWrapper<>());
    }
    @Override
    public void addMessage(Message message) {
        messageMapper.insert(message);
    }

}