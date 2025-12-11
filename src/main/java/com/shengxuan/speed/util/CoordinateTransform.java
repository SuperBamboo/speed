package com.shengxuan.speed.util;

import java.util.HashMap;
import java.util.Map;

/**
 * GPS坐标转换工具类（WGS-84转GCJ-02）
 * 适用于高德地图、腾讯地图等国内地图
 */
public class CoordinateTransform {

    // 定义常量
    private static final double PI = 3.1415926535897932384626;
    private static final double A = 6378245.0; // 长半轴
    private static final double EE = 0.00669342162296594323; // 扁率

    /**
     * WGS-84 转 GCJ-02
     * @param wgsLat WGS-84纬度
     * @param wgsLng WGS-84经度
     * @return 包含GCJ-02经纬度的Map
     */
    public static Map<String, Double> wgs84ToGcj02(double wgsLng, double wgsLat) {
        Map<String, Double> result = new HashMap<>();

        if (outOfChina(wgsLng, wgsLat)) {
            result.put("lng", wgsLng);
            result.put("lat", wgsLat);
            return result;
        }

        double[] delta = calculateDelta(wgsLng, wgsLat);
        double gcjLng = wgsLng + delta[0];
        double gcjLat = wgsLat + delta[1];

        result.put("lng", gcjLng);
        result.put("lat", gcjLat);
        return result;
    }

    /**
     * 判断坐标是否在中国境内
     */
    private static boolean outOfChina(double lng, double lat) {
        if (lng < 72.004 || lng > 137.8347) {
            return true;
        }
        if (lat < 0.8293 || lat > 55.8271) {
            return true;
        }
        return false;
    }

    /**
     * 计算偏移量
     */
    private static double[] calculateDelta(double lng, double lat) {
        double[] delta = new double[2];
        double dLat = transformLat(lng - 105.0, lat - 35.0);
        double dLng = transformLng(lng - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * PI;
        double magic = Math.sin(radLat);
        magic = 1 - EE * magic * magic;
        double sqrtMagic = Math.sqrt(magic);

        delta[0] = dLng * 180.0 / (A / sqrtMagic * Math.cos(radLat) * PI);
        delta[1] = dLat * 180.0 / ((A * (1 - EE)) / (magic * sqrtMagic) * PI);

        return delta;
    }

    /**
     * 纬度转换
     */
    private static double transformLat(double x, double y) {
        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y
                + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 经度转换
     */
    private static double transformLng(double x, double y) {
        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x
                + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
    }

    /**
     * 批量转换坐标
     */
    public static double[][] batchTransform(double[][] coordinates) {
        if (coordinates == null || coordinates.length == 0) {
            return new double[0][2];
        }

        double[][] result = new double[coordinates.length][2];
        for (int i = 0; i < coordinates.length; i++) {
            Map<String, Double> transformed = wgs84ToGcj02(coordinates[i][0], coordinates[i][1]);
            result[i][0] = transformed.get("lng");
            result[i][1] = transformed.get("lat");
        }
        return result;
    }
}