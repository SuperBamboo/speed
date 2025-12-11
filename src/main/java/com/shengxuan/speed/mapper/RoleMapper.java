package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.Role;
import com.shengxuan.speed.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RoleMapper {

    @Select("select * from role where name = #{name}")
    User findByName(String name);

    @Select("select * from role where id = #{id}")
    Role findById(Integer id);

    @Insert("insert into role (name) values (#{name})")
    void add(User user);
}
