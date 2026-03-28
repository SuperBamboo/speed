package com.shengxuan.speed.util;

import com.shengxuan.speed.entity.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class LoggerUtils {
    public static Logger getLoggerByDesc(String desc){
        Logger logger = new Logger();
        //1.设置用户名
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication!=null && authentication.getPrincipal() instanceof UserDetails){
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            logger.setUsername(userDetails.getUsername());
        }else {
            logger.setUsername("未知用户");
        }
        logger.setDesc1(desc);
        String dateNotHaveTime = DateFormat.getDateNotHaveTime();
        logger.setDate(dateNotHaveTime);
        //获取详细时间
        String date = DateFormat.dateNowFormat();
        logger.setTime(date);
        return logger;
    }
}
