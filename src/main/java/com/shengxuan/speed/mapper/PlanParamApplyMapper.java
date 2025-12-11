package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.PlanParamApply;
import com.shengxuan.speed.entity.WarningTone;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PlanParamApplyMapper {
    @Select("select * from plan_param_apply where device_id = #{deviceId}")
    List<PlanParamApply> findByDeviceId(String deviceId);

    @Insert("insert into plan_param_apply(device_id,server_id,plan_no,plan_param_no,plan_param_value,device_plan_mode_apply_id) " +
            "values(#{deviceId},#{serverId},#{planNo},#{planParamNo},#{planParamValue},#{devicePlanModeApplyId})")
    void add(PlanParamApply planParamApply);

    @Delete("delete from plan_param_apply where device_id = #{deviceId}")
    void delByDeviceId(String deviceId);

    @Delete("delete from plan_param_apply where server_id = #{serverId}")
    void delByServerId(int serverId);
}
