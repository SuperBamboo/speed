package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.Logger;
import com.shengxuan.speed.entity.PushAlarm;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class LoggerSqlProvider {

    public String selectByLogger(@Param("logger") Logger logger){
        return new SQL(){{
            SELECT("*");
            FROM("logger");
            if(logger.getUsername() != null && !logger.getUsername().isEmpty() && !"全部".equals(logger.getUsername())){
                WHERE("username = #{logger.username}");
            }
            if(logger.getDesc1() != null && !logger.getDesc1().isEmpty()){
                WHERE("desc1 like concat('%', #{logger.desc1},'%')");
            }
            if(logger.getDate() != null && !logger.getDate().isEmpty()){
                WHERE(" date >= #{logger.date}");
            }
            ORDER_BY("id desc");
        }}.toString();
    }
}
