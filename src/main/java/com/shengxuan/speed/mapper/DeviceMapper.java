package com.shengxuan.speed.mapper;

import com.shengxuan.speed.entity.Device;
import com.shengxuan.speed.entity.RegionDeviceType;
import com.shengxuan.speed.entity.pojo.ServerDevice;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface DeviceMapper {

    @Select("select * from device")
    @Results({
            @Result(property ="deviceId",column = "device_id"),
            @Result(property ="deviceName",column = "device_name"),
            @Result(property ="ofRegionId",column = "of_region_id"),
            @Result(property ="ofSubRegionId",column = "of_subRegion_id"),
            @Result(property ="deviceType",column = "device_type"),
            @Result(property ="deviceModel",column = "device_model"),
            @Result(property ="deviceAttribute",column = "device_attribute"),
            @Result(property ="serviceDate",column = "service_date"),
            @Result(property = "serverId",column = "server_id")
    })
    List<Device> findAll();

    @Select("select * from device where server_id = #{serverId}")
    @Results({
            @Result(property ="deviceId",column = "device_id"),
            @Result(property ="deviceName",column = "device_name"),
            @Result(property ="ofRegionId",column = "of_region_id"),
            @Result(property ="ofSubRegionId",column = "of_subRegion_id"),
            @Result(property ="deviceType",column = "device_type"),
            @Result(property ="deviceModel",column = "device_model"),
            @Result(property ="deviceAttribute",column = "device_attribute"),
            @Result(property ="serviceDate",column = "service_date"),
            @Result(property = "serverId",column = "server_id")
    })
    List<Device> findAllByServerId(int serverId);

    @SelectProvider(type = DeviceSqlProvider.class,method = "selectByDevice")
    @Results({
            @Result(property ="deviceId",column = "device_id"),
            @Result(property ="deviceName",column = "device_name"),
            @Result(property ="ofRegionId",column = "of_region_id"),
            @Result(property ="ofSubRegionId",column = "of_subRegion_id"),
            @Result(property ="deviceType",column = "device_type"),
            @Result(property ="deviceModel",column = "device_model"),
            @Result(property ="deviceAttribute",column = "device_attribute"),
            @Result(property ="serviceDate",column = "service_date"),
            @Result(property = "serverId",column = "server_id")
    })
    List<Device> findByConditionAndPage(@Param("device") Device device,List<RegionDeviceType> regionDeviceTypeList);

//    /**
//     * 查询所有权限下的设备id集合按照升序
//     * @param regionDeviceTypeList
//     * @return 设备id集合
//     */
//    @SelectProvider(type = DeviceSqlProvider.class,method = "selectDeviceIdsByDevice")
//    List<String> findByRegionDeviceType(List<RegionDeviceType> regionDeviceTypeList);

    /**
     * 查询所有权限下的设备id集合按照升序
     * @param regionDeviceTypeList
     * @return ServerDevice 某个服务器下的某个设备
     */
    @SelectProvider(type = DeviceSqlProvider.class,method = "selectServerDeviceIdsByDevice")
    @Results({
            @Result(property ="serverId",column = "server_id"),
            @Result(property ="deviceId",column = "device_id")
    })
    List<ServerDevice> findByServerRegionDeviceType(List<RegionDeviceType> regionDeviceTypeList);

    @Select("SELECT DISTINCT server_id,of_region_id,of_subregion_id,device_type FROM device WHERE server_id = #{serverId} ORDER BY of_subregion_id;")
    @Results({
            @Result(property ="region",column = "of_region_id"),
            @Result(property ="subregion",column = "of_subregion_id"),
            @Result(property ="deviceType",column = "device_type")
    })
    List<RegionDeviceType> findAllRegionAndDeviceType(int serverId);

    @Select("select * from device where device_id = #{deviceId} and server_id = #{serverId}")
    @Results({
            @Result(property ="deviceId",column = "device_id"),
            @Result(property ="deviceName",column = "device_name"),
            @Result(property ="ofRegionId",column = "of_region_id"),
            @Result(property ="ofSubRegionId",column = "of_subRegion_id"),
            @Result(property ="deviceType",column = "device_type"),
            @Result(property ="deviceModel",column = "device_model"),
            @Result(property ="deviceAttribute",column = "device_attribute"),
            @Result(property ="serviceDate",column = "service_date"),
            @Result(property = "serverId",column = "server_id")
    })
    Device findByDeviceId(@Param("deviceId") String deviceId,@Param("serverId")int serverId);

    @Select("select * from device where device_id = #{deviceId} and server_id = #{serverId}")
    @Results({
            @Result(property ="deviceId",column = "device_id"),
            @Result(property ="deviceName",column = "device_name"),
            @Result(property ="ofRegionId",column = "of_region_id"),
            @Result(property ="ofSubRegionId",column = "of_subRegion_id"),
            @Result(property ="deviceType",column = "device_type"),
            @Result(property ="deviceModel",column = "device_model"),
            @Result(property ="deviceAttribute",column = "device_attribute"),
            @Result(property ="serviceDate",column = "service_date"),
            @Result(property = "serverId",column = "server_id")
    })
    Device findByDeviceIdAndServerId(@Param("deviceId") String deviceId,@Param("serverId") int serverId);

    @Select("SELECT DISTINCT of_region_id FROM device")
    List<String> findAllRegion();

    @Select("select DISTINCT device_type from device ORDER BY device_type ASC")
    List<String> findAllDeviceType();

    @Select("SELECT DISTINCT of_subRegion_id FROM device WHERE of_region_id = #{regionName}")
    List<String> findAllSubRegionByRegion(String regionName);

    @Insert("insert into device(device_id,device_name,of_region_id,of_subRegion_id,device_type,device_model,device_attribute,supplier,service_date,longitude,dimension,flag,server_id) " +
            "values(#{deviceId},#{deviceName},#{ofRegionId},#{ofSubRegionId},#{deviceType},#{deviceModel},#{deviceAttribute},#{supplier},#{serviceDate},#{longitude},#{dimension},#{flag},#{serverId})")
    void add(Device device);

    @Delete("delete from device where device_id = #{deviceId} and server_id = #{serverId}")
    void del(@Param("deviceId") String deviceId,@Param("serverId") int serverId);

    @Update("update device set longitude = #{longitude},dimension = #{dimension} where device_id = #{deviceId} and server_id =#{serverId}")
    void updateLatLng(Device device);

    @Select("select * from device where of_region_id = #{ofRegionId} and of_subRegion_id = #{ofSubRegionId}")
    List<Device> findByRegionAndSubRegion(@Param("ofRegionId") String ofRegionId, @Param("ofSubRegionId") String ofSubRegionId);

    /*@Select({
            "<script>",
            "SELECT * FROM device",
            "WHERE of_region_id = #{ofRegionId}",
            "AND of_subregion_id = #{ofSubRegionId}",
            "AND device_type IN",
            "<foreach collection='deviceTypeList' item='type' open='(' separator=',' close=')'>",
            "#{type}",
            "</foreach>",
            "</script>"
    })
    List<Device> findByRegionAndSubRegionAndDeviceTypeList(@Param("ofRegionId") String ofRegionId, @Param("ofSubRegionId") String ofSubRegionId,@Param("deviceTypeList")List<String> deviceTypeList);*/

    @Select({
            "<script>",
            "SELECT * FROM device",
            "WHERE server_id = #{serverId}",
            "AND of_region_id = #{ofRegionId}",
            "AND of_subregion_id = #{ofSubRegionId}",
            "AND device_type IN",
            "<foreach collection='deviceTypeList' item='type' open='(' separator=',' close=')'>",
            "#{type}",
            "</foreach>",
            "</script>"
    })
    List<Device> findByServerAndRegionAndSubRegionAndDeviceTypeList(@Param("serverId")int serverId,@Param("ofRegionId") String ofRegionId, @Param("ofSubRegionId") String ofSubRegionId,@Param("deviceTypeList")List<String> deviceTypeList);

    @Delete("delete from device where server_id = #{serverId}")
    void delByServerId(int serverId);
}
