package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.Server;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ServerMapper {
    @Select("select * from server")
    List<Server> findAll();

    @Select("select * from server where id = #{id}")
    @Results({
            @Result(property ="serverName",column = "server_name")
    })
    Server findById(int id);

    @Insert("insert into server(server_name,ip,port,username,password,flag) " +
            "values(#{serverName},#{ip},#{port},#{username},#{password},#{flag})")
    void add(Server server);

    @Delete("delete from server where id = #{id}")
    void delById(int id);

    @Select("select * from server where server_name = #{serverName}")
    Server findByServerName(String serverName);

    @Update("update server set flag = #{flag} where id = #{id}")
    void updateFlagById(@Param("id") int id, @Param("flag") boolean flag);
}
