package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.Region;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface RegionMapper {
    @Insert("insert into region(region_name) values(#{regionName})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    void add(Region region);
}
