package com.shengxuan.speed.util;

public class CheckUtils {

    /**
     * 正则判断是否是合法的IP字符串
     * @param ip
     * @return
     */
    public static boolean isValidIP(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }

        String regex = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return ip.matches(regex);
    }

    /**
     * 端口校验
     * @param port
     * @return
     */
    public static boolean isValidPort(int port) {
        return port >= 0 && port <= 65535;
    }


    /**
     * 字符串非空校验 不为空且不能为空格且不能为 'null'
     * @param str
     * @return
     */
    public static boolean isValidStr(String str) {
        return str != null && !str.trim().isEmpty() && !("").equals(str) && !("null").equalsIgnoreCase(str);
    }
}
