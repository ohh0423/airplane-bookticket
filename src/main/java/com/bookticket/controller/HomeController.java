package com.bookticket.controller;

import com.bookticket.pojo.Line;
import com.bookticket.pojo.Notice;
import com.bookticket.pojo.Trips;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bookticket.service.LineService;
import com.bookticket.service.NoticeService;
import com.bookticket.service.TripsService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.InvalidSessionException;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 控制首页相关
 */
@Controller
//@RequestMapping("/")
public class HomeController {

    //Logger实例
    public static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private int pageSize = 8;//表示每页展示的数据
    @Autowired
    private TripsService tripsService;
    @Autowired
    private LineService lineService;
    @Autowired
    private NoticeService noticeService;

    /**
     * 跳转到首页
     *
     * @return java.lang.String 页面逻辑名
     */


    //获取全部班次
    @GetMapping("/")
    public String getAllTrips(Model model, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        logger.info("--------------------------------获取全部班次列表-------------------------------");

        Session session = SecurityUtils.getSubject().getSession();
        //清除搜索结果
        session.removeAttribute("pageInfo");
        //为了程序的严谨性，判断非空：
        if (pageNum == null || pageNum <= 0) {
            pageNum = 1;   //设置默认当前页
        }
// 设置每页显示的条数
        int pageSize = 7;
        //获取页面的分页信息
        PageInfo<Trips> pageInfo = null;
        try {
            pageInfo = (PageInfo<Trips>) session.getAttribute("pageInfo");
        } catch (InvalidSessionException e) {
            e.printStackTrace();
        }

        List<Trips> allTrips = null;
        if (pageInfo == null || pageInfo.getPageNum() != pageNum) {
            // 如果页面信息不存在或当前页与请求的页数不一致，则重新查询数据库获取对应页的数据
            //1.引入分页插件,pageNum是第几页，pageSize是每页显示多少条,默认查询总数count
            PageHelper.startPage(pageNum, pageSize);
            //2.紧跟的查询就是一个分页查询-必须紧跟.后面的其他查询不会被分页，除非再次调用PageHelper.startPage
            allTrips = tripsService.getAllTrips();
            if (allTrips != null && !allTrips.isEmpty()) {
                allTrips.forEach(trips -> {
                    Line line = lineService.getById(trips.getTrips_line_id()); // 获取线路信息
                    if (line != null) {
                        trips.setTrips_start_station_name(line.line_start_station_name());
                        trips.setTrips_end_station_name(line.line_end_station_name());
                    }
                });
                try {
                    //3.使用PageInfo包装查询后的结果
                    pageInfo = new PageInfo<>(allTrips, pageSize);
                    model.addAttribute("allTrips", allTrips);
                    model.addAttribute("pageInfo", pageInfo);
                    session.setAttribute("pageInfo", pageInfo);
                    // 清理 ThreadLocal 存储的分页参数，保证线程安全
                    PageHelper.clearPage();

                } finally {
                    logger.info("------------------------清理 ThreadLocal 存储的分页参数,保证线程安全-------------------------");
                    PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
                }
            }
        } else {
            //如果页面信息已经存在，则直接返回已有的页面信息
            allTrips = pageInfo.getList(); // 从pageInfo中获取allTrips
            model.addAttribute("allTrips", allTrips);
            model.addAttribute("pageInfo", pageInfo);
        }

        return "default/index";
    }


    /**
     * 分页操作获取信息
     *
     * @param model 给页面传递参数
     * @return java.lang.String
     */

    @GetMapping("/getTrips")
    public String home(Model model, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {

        logger.info("--------------------------------获取班次列表-------------------------------");
        Session session = SecurityUtils.getSubject().getSession();
        //清除搜索结果
        session.removeAttribute("pageInfo");
        // 为了程序的严谨性，判断非空：
        if (pageNum == null || pageNum <= 0) {
            pageNum = 1;   // 设置默认当前页
        }
// 设置每页显示的条数
        int pageSize = 7;
        // 获取页面的分页信息
        PageInfo<Trips> pageInfo = (PageInfo<Trips>) session.getAttribute("pageInfo");
        if (pageInfo == null || pageInfo.getList().isEmpty()) {
            // 处理 pageInfo 为 null 或空的情况
            // 可以返回错误消息或重定向到其他页面
            return "default/search"; // 或者根据实际需求处理错误逻辑
        }

        // 获取 pageInfo 列表中的第一个班次
        Trips aTrip = pageInfo.getList().get(0);
        int line_id = aTrip.getTrips_line_id();
        Date date = (Date) session.getAttribute("date");
        String start_station = aTrip.getTrips_start_station_name();
        String end_station = aTrip.getTrips_end_station_name();
        List<Trips> someTrips;

        // 1. 引入分页插件，pageNum 是第几页，pageSize 是每页显示多少条，默认查询总数 count
        PageHelper.startPage(pageNum, pageSize);
        // 2. 紧跟的查询就是一个分页查询-必须紧跟.后面的其他查询不会被分页，除非再次调用 PageHelper.startPage
        someTrips = tripsService.getSomeTrips(line_id, date);// 只含本页的数据

        // 这里省略了 someTrips 是否为空的判断

        someTrips.forEach(trips -> {
            trips.setTrips_start_station_name(start_station);
            trips.setTrips_end_station_name(end_station);
        });

        try {
            // 3. 使用 PageInfo 包装查询后的结果
            PageInfo<Trips> pageInfo2 = new PageInfo<>(someTrips, pageSize);
            model.addAttribute("pageInfo", pageInfo2);
            session.setAttribute("date", date);
        } finally {
            logger.info("------------------------清理 ThreadLocal 存储的分页参数，保证线程安全-------------------------");
            PageHelper.clearPage(); // 清理 ThreadLocal 存储的分页参数，保证线程安全
        }

        return "default/search";
    }

    /**
     * 根据用户选择的条件获取特定的班次
     *
     * @param start_station 起始站点名
     * @param end_station   到达站点名
     * @param datestr       用户选择的日期
     * @param model         存放参数
     * @return java.lang.String
     */

    @PostMapping("/getTrips")
    public String getTrips(@RequestParam("StartStation") String start_station,
                           @RequestParam("EndStation") String end_station,
                           @RequestParam("date") String datestr, Model model) throws ParseException {

        logger.info("-------------------------------------获取班次列表----------------------------");
        Session session = SecurityUtils.getSubject().getSession();
        List<Trips> someTrips;
        if (datestr != null && datestr.length() != 0) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date date = format.parse(datestr);
            Line line = lineService.getOne(start_station, end_station);
            int line_id;
            //分页
            int pageNum = 1;//表示第几页
            if (line != null) {
                line_id = line.getLine_id();
                //1.引入分页插件,pageNum是第几页，pageSize是每页显示多少条,默认查询总数count
                PageHelper.startPage(pageNum, pageSize);
                //2.紧跟的查询就是一个分页查询-必须紧跟.后面的其他查询不会被分页，除非再次调用PageHelper.startPage

                someTrips = tripsService.getSomeTrips(line_id, date);//只含本页的数据
                if (someTrips != null && someTrips.size() != 0) {
                    someTrips.forEach(trips -> {
                        trips.setTrips_start_station_name(start_station);
                        trips.setTrips_end_station_name(end_station);
                    });
                    try {
                        //3.使用PageInfo包装查询后的结果
                        PageInfo<Trips> pageInfo = new PageInfo<>(someTrips, pageSize);
                        model.addAttribute("pageInfo", pageInfo);
                        session.setAttribute("date", date);
                        session.setAttribute("pageInfo", pageInfo);
                    } finally {
                        logger.info("-------------------------清理 ThreadLocal 存储的分页参数,保证线程安全-------------------------");
                        PageHelper.clearPage(); //清理 ThreadLocal 存储的分页参数,保证线程安全
                    }
                }

            }
        }
        return "default/search";
    }

    /**
     * 跳转到确认订单页面，页面需包括班次的一些信息
     *
     * @param trips_id 班次编号
     * @param model    给页面传递参数
     * @return java.lang.String
     */

    @GetMapping("/user/confirm_order/{trips_id}")
    public String toConfirmOrder(@PathVariable("trips_id") Integer trips_id, Model model) {

        logger.info("---------------------------跳转到确认订单页面，所订的班次号为" + trips_id + "-----------------------------");
        Trips trips = tripsService.getOneById(trips_id);
        if (trips != null) {
            int line_id = trips.getTrips_line_id();
            Line line = lineService.getById(line_id);
            if (line != null) {
                String start_station = line.getLine_start_station_name();
                String end_station = line.getLine_end_station_name();
                trips.setTrips_start_station_name(start_station);
                trips.setTrips_end_station_name(end_station);
            }
        }
        model.addAttribute("trips", trips);
        return "default/confirm_order";
    }
    //公告

    @GetMapping("/notice")
    public String getAllNotices(Model model) {
        List<Notice> noticeList = noticeService.getAllNotices();
        model.addAttribute("noticeList", noticeList);
        return "user/notice"; // 返回Thymeleaf模板页面的名称
    }
    //公告详情
    @GetMapping("/tochecknotice/{noticeId}")
    public String goToCheckNotice(@PathVariable("noticeId") int noticeId, Model model) {
        // 根据 noticeId 获取相应的公告详情信息
        Notice notice = noticeService.getNoticeById(noticeId);

        // 将公告详情信息添加到模型中，以便在详情页面中使用
        model.addAttribute("notice", notice);

        // 返回到详情页面的视图名称，根据实际情况修改
        return "user/notice_detail"; // 假设你的详情页面视图名称为 checkNotice
    }

}
