package com.bookticket.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bookticket.pojo.Notice;
import com.bookticket.mapper.NoticeMapper;
import com.bookticket.service.NoticeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class NoticeServiceImpl implements NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;
    @Override
    public List<Notice> getAllNotices() {
        return noticeMapper.selectList(new QueryWrapper<>());
    }
    @Override
    public Notice getNoticeById(int noticeId) {
        return noticeMapper.selectById(noticeId);
    }
    }
