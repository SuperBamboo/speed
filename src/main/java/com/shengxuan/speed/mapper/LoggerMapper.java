package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.Logger;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface LoggerMapper {

    @Select("select * from logger")
    List<Logger> findAll();

    @SelectProvider(type = LoggerSqlProvider.class,method = "selectByLogger")
    List<Logger> findByConditionAndPage(@Param("logger") Logger logger);

    @Insert("insert into logger(username,date,time,desc1) " +
            "values(#{username},#{date},#{time},#{desc1})")
    void add(Logger logger);

    @Delete("delete from device where device_id = #{deviceId}")
    void del(String time);

}
