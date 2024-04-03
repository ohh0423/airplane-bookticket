package com.bookticket.service;

import com.bookticket.pojo.Notice;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface NoticeService {

        List<Notice> getAllNotices();


        Notice getNoticeById(int noticeId);
}
