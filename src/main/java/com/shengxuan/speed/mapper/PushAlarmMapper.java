package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.PushAlarm;
import com.shengxuan.speed.entity.pojo.ServerDevice;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PushAlarmMapper {
    @Insert("insert into pushalarm(alarm_no,device_id,server_id,device_name,of_region_name,of_subregion_name,device_model,supplier,longitude,dimension,occur_date,occur_time,alarm_type,alarm_desc,is_check) values(#{alarmNo},#{deviceId},#{serverId},#{deviceName},#{ofRegionName},#{ofSubregionName},#{deviceModel},#{supplier},#{longitude},#{dimension},#{occurDate},#{occurTime},#{alarmType},#{alarmDesc},#{isCheck})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    void add(PushAlarm pushAlarm);

    /**
     * 报警信息已读(单条)
     */
    @Update("update pushalarm set is_check = 'true' where id = #{id}")
    void updateCheck(@Param("id") Integer id);

    /**
     *设置所有的报警信息已读
     */
    @Update("update pushalarm set is_check = 'true' ")
    void updateCheckAll();

    @Select("select * from pushalarm")
    @Results({
            @Result(property ="id",column = "id"),
            @Result(property ="alarmNo",column = "alarm_no"),
            @Result(property ="deviceId",column = "device_id"),
            @Result(property ="serverId",column = "server_id"),
            @Result(property ="deviceName",column = "device_name"),
            @Result(property ="ofRegionName",column = "of_region_name"),
            @Result(property ="ofSubregionName",column = "of_subregion_name"),
            @Result(property ="deviceModel",column = "device_model"),
            @Result(property ="longitude",column = "longitude"),
            @Result(property ="dimension",column = "dimension"),
            @Result(property ="occurDate",column = "occur_date"),
            @Result(property ="occurTime",column = "occur_time"),
            @Result(property ="alarmType",column = "alarm_type"),
            @Result(property ="alarmDesc",column = "alarm_desc"),
            @Result(property ="isCheck",column = "is_check")
    })
    List<PushAlarm> findAll();

    @Select("select * from pushalarm where device_id = #{deviceId} and server_id = #{serverId}")
    @Results({
            @Result(property ="id",column = "id"),
            @Result(property ="alarmNo",column = "alarm_no"),
            @Result(property ="deviceId",column = "device_id"),
            @Result(property ="serverId",column = "server_id"),
            @Result(property ="deviceName",column = "device_name"),
            @Result(property ="ofRegionName",column = "of_region_name"),
            @Result(property ="ofSubregionName",column = "of_subregion_name"),
            @Result(property ="deviceModel",column = "device_model"),
            @Result(property ="longitude",column = "longitude"),
            @Result(property ="dimension",column = "dimension"),
            @Result(property ="occurDate",column = "occur_date"),
            @Result(property ="occurTime",column = "occur_time"),
            @Result(property ="alarmType",column = "alarm_type"),
            @Result(property ="alarmDesc",column = "alarm_desc"),
            @Result(property ="isCheck",column = "is_check")
    })
    List<PushAlarm> findByDeviceId(@Param("deviceId") String deviceId,@Param("serverId")int serverId);

    /*@SelectProvider(type = PushAlarmSqlProvider.class,method = "selectByPushAlarm")
    @Results({
            @Result(property ="id",column = "id"),
            @Result(property ="alarmNo",column = "alarm_no"),
            @Result(property ="deviceId",column = "device_id"),
            @Result(property ="serverId",column = "server_id"),
            @Result(property ="deviceName",column = "device_name"),
            @Result(property ="ofRegionName",column = "of_region_name"),
            @Result(property ="ofSubregionName",column = "of_subregion_name"),
            @Result(property ="deviceModel",column = "device_model"),
            @Result(property ="longitude",column = "longitude"),
            @Result(property ="dimension",column = "dimension"),
            @Result(property ="occurDate",column = "occur_date"),
            @Result(property ="occurTime",column = "occur_time"),
            @Result(property ="alarmType",column = "alarm_type"),
            @Result(property ="alarmDesc",column = "alarm_desc"),
            @Result(property ="isCheck",column = "is_check")
    })
    List<PushAlarm> findByConditionAndPage(@Param("pushAlarm") PushAlarm pushAlarm,@Param("deviceIds")List<String> deviceIds);*/

    @SelectProvider(type = PushAlarmSqlProvider.class,method = "selectByPushAlarm1")
    @Results({
            @Result(property ="id",column = "id"),
            @Result(property ="alarmNo",column = "alarm_no"),
            @Result(property ="deviceId",column = "device_id"),
            @Result(property ="serverId",column = "server_id"),
            @Result(property ="deviceName",column = "device_name"),
            @Result(property ="ofRegionName",column = "of_region_name"),
            @Result(property ="ofSubregionName",column = "of_subregion_name"),
            @Result(property ="deviceModel",column = "device_model"),
            @Result(property ="longitude",column = "longitude"),
            @Result(property ="dimension",column = "dimension"),
            @Result(property ="occurDate",column = "occur_date"),
            @Result(property ="occurTime",column = "occur_time"),
            @Result(property ="alarmType",column = "alarm_type"),
            @Result(property ="alarmDesc",column = "alarm_desc"),
            @Result(property ="isCheck",column = "is_check")
    })
    List<PushAlarm> findByConditionAndPage1(@Param("pushAlarm") PushAlarm pushAlarm,@Param("serverDeviceList")List<ServerDevice> serverDeviceList);

    @Select("select * from pushalarm where device_id = #{deviceId} and server_id =#{serverId} and is_check = 'false' ")
    List<PushAlarm> findByDeviceIdAndChecked(@Param("deviceId") String deviceId,@Param("serverId") int serverId);

    @Select("select * from pushalarm order by id desc limit 10")
    @Results({
            @Result(property ="id",column = "id"),
            @Result(property ="alarmNo",column = "alarm_no"),
            @Result(property ="deviceId",column = "device_id"),
            @Result(property ="deviceName",column = "device_name"),
            @Result(property ="ofRegionName",column = "of_region_name"),
            @Result(property ="ofSubregionName",column = "of_subregion_name"),
            @Result(property ="deviceModel",column = "device_model"),
            @Result(property ="longitude",column = "longitude"),
            @Result(property ="dimension",column = "dimension"),
            @Result(property ="occurDate",column = "occur_date"),
            @Result(property ="occurTime",column = "occur_time"),
            @Result(property ="alarmType",column = "alarm_type"),
            @Result(property ="alarmDesc",column = "alarm_desc"),
            @Result(property ="isCheck",column = "is_check")
    })
    List<PushAlarm> findNewAlarm10Size();

    /*@Select({
            "<script>",
            "SELECT * FROM pushalarm",
            "WHERE device_id IN",
            "<foreach collection='deviceIds' item='type' open='(' separator=',' close=')'>",
            "#{type}",
            "</foreach>",
            "ORDER BY id DESC", // 按照 id 排序
            "LIMIT 10",    // 只返回前 10 条数据
            "</script>"
    })
    List<PushAlarm> findNewAlarm10SizeByRegion(@Param("deviceIds") List<String> deviceIds);*/

    @Select({
            "<script>",
            "SELECT * FROM pushalarm",
            "<where>",
            "<if test='serverDeviceList != null and serverDeviceList.size() > 0'>",
            "AND (",
            "<foreach collection='serverDeviceList' item='device' separator=' OR '>",
            "(device_id = #{device.deviceId} AND server_id = #{device.serverId})",
            "</foreach>",
            ")",
            "</if>",
            "<if test='serverDeviceList == null or serverDeviceList.size() == 0'>",
            "AND 1=2",  // 如果没有设备，返回空结果
            "</if>",
            "</where>",
            "ORDER BY id DESC",
            "LIMIT 10",
            "</script>"
    })
    List<PushAlarm> findNewAlarm10SizeByRegion(@Param("serverDeviceList") List<ServerDevice> serverDeviceList);

    /**
     * 删除某个日期之前的数据
     * @param occurDate
     */
    @Delete("delete from pushalarm where occur_date < #{occurDate}")
    void deleteDateAgo(@Param("occurDate") String occurDate);

    @Delete("delete from pushalarm where server_id = #{serverId}")
    void delByServerId(int serverId);
}
