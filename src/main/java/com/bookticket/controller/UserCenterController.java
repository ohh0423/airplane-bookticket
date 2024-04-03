package com.bookticket.controller;

import com.bookticket.pojo.Message;
import com.bookticket.pojo.Orders;
import com.bookticket.pojo.User;
import com.bookticket.service.MailService;
import com.bookticket.service.MessageService;
import com.bookticket.service.OrderService;
import com.bookticket.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * 控制用户中心
 *
 */
@Controller
@RequestMapping("/user")
public class UserCenterController {
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MessageService messageService;

    //修改密码所需的验证码
    public static final String UPDATE_VEFICATION_CODE="update_vefication_code";

    public static final Logger logger= LoggerFactory.getLogger(UserCenterController.class);
    /**
     * 随机生成六位验证码，并发送到指定邮箱
     * @param email 邮箱号
     * @param attributes 给跳转页面传递参数
     * @return java.lang.String
     */
    @RequestMapping("/getUpdateCheckCode")
    @ResponseBody
    public String getUpdateCheckCode(@RequestParam String email,RedirectAttributes attributes) {

        logger.info("---------------------邮箱号为"+email+"正在获取修改密码的验证码----------------------");
        if(!StringUtils.hasText(email)) {
            attributes.addFlashAttribute("message","验证码为空，请输入!!!");
            return "redirect:/user/register";
        }
        String checkCode = String.valueOf(new Random().nextInt(899999) + 100000);
        String Subject="机票预订系统验证码";
        String Text="你的验证码为：" + checkCode;
        String To=email;
        mailService.sendSimpleMail(To,Subject,Text);
        SecurityUtils.getSubject().getSession().setAttribute(UPDATE_VEFICATION_CODE,checkCode);//保存验证码以便验证
        logger.info("-------------------修改密码验证码已经发送给"+email+"，验证码为"+checkCode+"--------------------");
        return "请查看邮箱收到的验证码！！！！";
    }
    /**
     * 进入个人中心页面
     *
     * @return java.lang.String
     */

    @GetMapping("/ToUserCenter")
    public String toUserCenter() {
        return "user/user_center";
    }
    /**
     * 进入个人信息页面
     *
     * @return java.lang.String
     */

    @GetMapping("/ToUserInfo")
    public String toUserInfo() {

        return "user/user_info";
    }

    /**
     * 进入完善资料页面
     *
     * @return java.lang.String
     */

    @GetMapping("/ToUpdateInfo")
    public String toUpdateInfo() {

        return "user/edit_info";

    }

    /**
     * 进入更改页面
     *
     * @return java.lang.String
     */

    @GetMapping("/ToUpdatePwd")
    public String updateUserPwd(Model model) {

        return "user/edit_pwd";
    }

//留言功能
@GetMapping("/messageList")
public String getMessageList(Model model, HttpSession session) {
    // 获取当前用户的登录名
    String userLoginName = (String) session.getAttribute("username");

    // 获取所有消息
    List<Message> messageList = messageService.getMessageList();

    // 筛选出与当前用户登录名匹配的消息
    List<Message> filteredMessageList = messageList.stream()
            .filter(message -> {
                String loginName = message.getUserLoginName();
                return loginName != null && loginName.equals(userLoginName);
            })
            .collect(Collectors.toList());

    model.addAttribute("messageList", filteredMessageList);
    return "user/message";
}

    // 添加留言功能
    @PostMapping("/addMessage")
    public String addMessage(@RequestParam("message") String content, HttpSession session) {
        // 获取当前用户的登录名
        String userLoginName = (String) session.getAttribute("username");

        // 创建留言对象并设置相关信息
        Message message = new Message();
        message.setMessage(content);
        message.setUser_Login_Name(userLoginName);

        // 手动设置message_create_time为当前时间
        message.setMessage_create_time(new Date());
        // 调用服务层方法保存留言
        messageService.addMessage(message);

        // 重定向到留言列表页面
        return "redirect:/user/messageList";
    }

    /**
     * 跳转到用户的订单表
     *
     * @param model 页面参数
     * @return java.lang.String
     */

    @GetMapping("/ToOrderList")
    public String toOrderList( Model model) {

        // 获取 subject 认证主体
        Subject currentUser = SecurityUtils.getSubject() ;
        Session session = currentUser.getSession() ;
        User user=(User)session.getAttribute("user");
        int user_id=user.getUser_id();

        List<Orders> orderList = orderService.getOrderListByUser(user_id);
        model.addAttribute("orderList",orderList);
        return "user/order_list";
    }

    /**
     * 更改密码
     * @param check_code 用户输入的验证码
     * @param pasword 用户输入的新密码
     * @param attributes 给页面传递参数
     * @return java.lang.String
     */

    @PostMapping("/UpdatePwd")
    public String updatePwd(@RequestParam("verification_code") String check_code,
                            @RequestParam("password") String pasword,
                            RedirectAttributes attributes) {

        logger.info("-------------进行密码修改，用户输入的密码为"+pasword+"输入的验证码为"+check_code+"----------------");
        // 获取 subject 认证主体
        Subject currentUser = SecurityUtils.getSubject() ;
        Session session = currentUser.getSession() ;

        String vefication_code = (String) session.getAttribute(UPDATE_VEFICATION_CODE);     //获取验证码
        String message = "";
        String success_message = "";
        if (!StringUtils.hasText(vefication_code) || !vefication_code.equals(check_code))
            message = "验证码错误！！！";
        else if (!StringUtils.hasText(pasword))
            message = "密码为空，请输入！！！";
        else {
            User user =(User)session.getAttribute("user");                       //获取用户
            //更改密码
            String salt = new SecureRandomNumberGenerator().nextBytes().toHex();    //随机生成盐值
            Md5Hash md5Hash = new Md5Hash(pasword, salt, 3);
            user.setUser_password(md5Hash.toHex());
            user.setUser_salt(salt);
            userService.update(user);
            success_message = "修改成功";
        }
        //带参数返回原页面
        if (!message.equals("")) {
            logger.info("--------------------密码修改失败，原因为："+message+"-----------------------");
            attributes.addFlashAttribute("message", message);
        }

        if (!success_message.equals("")) {
            logger.info("---------------------密码修改成功！---------------------");
            attributes.addFlashAttribute("success_message", success_message);

        }
        return "redirect:/user/ToUpdatePwd";
    }

    /**
     * 完善个人资料
     *
     * @param identity_num 身份证号
     * @param phone 手机号
     * @param birth 生日
     * @param sex 性别
     * @param model 给跳转页面传递参数
     * @return java.lang.String
     */

    @PostMapping("/UpdateInfo")
    public String updateInfo(@RequestParam("identity_num") String identity_num,
                             @RequestParam("phone") String phone,
                             @RequestParam("birth") String birth,
                             @RequestParam("sex") String sex ,Model model) throws ParseException {

        logger.info("-----------完善个人信息，身份证号为"+identity_num+",手机号为"+phone+",生日为"+birth+",性别为"+sex+"-------------");
        // 获取 subject 认证主体
        Subject currentUser = SecurityUtils.getSubject() ;
        Session session = currentUser.getSession() ;

        User user =(User) session.getAttribute("user");
        if(StringUtils.hasText(birth)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(birth);
            user.setUser_birth(date);
        }
        user.setUser_identity_num(identity_num);
        user.setUser_phone(phone);
        user.setUser_sex(sex);
        userService.update(user);
        model.addAttribute("success_message", "修改成功！！！！");
        logger.info("--------------------成功完善个人资料-------------------");
        return "/user/edit_info";

    }


}
