package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Select("select * from user where username = #{username}")
    User findByUsername(@Param("username") String username);

    @Select("select * from user where id = #{id}")
    User findById(@Param("id") Integer id);

    @Insert("insert into user (username,password,role) values (#{username},#{password},#{role})")
    void add(User user);

    @Select("select * from user")
    List<User> findAll();

    @Delete("delete from user where id = #{id}")
    void delById(@Param("id") Integer id);

    @Update("update user set username = #{username},password = #{password},role = #{role} where id = #{id}")
    void update(User user);

    @Update("update user set password = #{password} where username = #{username}")
    void updatePasswordByUsername(@Param("username") String username,@Param("password") String password);
}
