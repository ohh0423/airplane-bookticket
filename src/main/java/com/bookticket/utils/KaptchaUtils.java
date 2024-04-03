package com.bookticket.utils;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

/**
 * 请求验证码工具类
 * 生成带图片等信息的ServletOutputStream，并在Session属性里添加相应的验证码文本信息
 */
@Component
public class KaptchaUtils {
    public static final Logger logger= LoggerFactory.getLogger(KaptchaUtils.class);

    public static void validateCode(HttpServletResponse response, DefaultKaptcha captchaProducer, String validateSessionKey) throws Exception{
        // Set to expire far in the past.
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");

        // return a jpeg
        response.setContentType("image/jpeg");

        // create the text for the image
        String capText = captchaProducer.createText();

        logger.info("---------------------------登陆验证码为："+capText+"------------------------");
        // store the text in the session
        SecurityUtils.getSubject().getSession().setAttribute(validateSessionKey, capText);

        // create the image with the text
        BufferedImage bi = captchaProducer.createImage(capText);

        ServletOutputStream out = response.getOutputStream();

        // write the data out
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
            logger.info("-----------------------输出流已经关闭-----------------------");
        }
    }
}
