package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.Port;
import com.shengxuan.speed.entity.UserRegion;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserRegionMapper {
    @Select("select * from user_region where uid = #{uid}")
    @Results({
            @Result(property ="uid",column = "uid"),
            @Result(property ="rid",column = "rid")
    })
    List<UserRegion> findByUserId(Integer uid);

    @Insert("insert into user_region(uid,rid) values(#{uid},#{rid})")
    void add(@Param("uid") Integer uid,@Param("rid") Integer rid);

    @Delete("delete from user_region where uid = #{uid}")
    void delByUserId(Integer uid);

    @Delete("delete from user_region where rid = #{rid}")
    void delByRid(Integer rid);

}
