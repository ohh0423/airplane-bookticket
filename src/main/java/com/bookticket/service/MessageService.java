package com.bookticket.service;

import com.bookticket.pojo.Message;
import org.springframework.stereotype.Service;


import java.util.List;
@Service
public interface MessageService {
    List<Message> getMessageList();
    void addMessage(Message message);
}