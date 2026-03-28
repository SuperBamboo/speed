package com.shengxuan.speed.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author memory
 * @desc
 * @date 2017/8/18
 */
public class BytesUtil {

    /**
     * 16进制字符串转二进制字符串（8位） (生成8位二进制字符串)
     * @param hexStr
     * @return
     */
    public static String hexToBinary(String hexStr){
        String result = "";
        try{
            int i = Integer.parseInt(hexStr, 16);
            result = Integer.toBinaryString(i);
            while (result.length()<8){
                result = "0"+result;
            }
        }catch (Exception e){

        }

        return result;
    }

    /**
     * (8位)2进制字符串转换为16进制字符串
     * @param binaryStr
     * @return
     */
    public static String binaryToHex(String binaryStr){
        String result ="";
        if(binaryStr!=null&&binaryStr.length()>0){
            String hexStr = Integer.toHexString(Integer.parseInt(binaryStr, 2));
            if(hexStr.length()<2){
                result = "0"+hexStr;
            }else {
                result = hexStr;
            }
        }
        return result;
    }


    /**
     * 10进制字符串转换为16进制字符串
     * @param decStr
     * @return
     */
    public static String decToHex(String decStr){
        String result ="";
        String hexStr = Integer.toHexString(Integer.parseInt(decStr));
        if(hexStr.length()<2){
            result = "0"+hexStr;
        }else {
            result = hexStr;
        }
        return result;
    }


    /**
     * 16进制转换为10进制字符串
     * @param hexStr
     * @return
     */
    public static String hexToDec(String hexStr){
        return Integer.parseInt(hexStr,16)+"";
    }

    /**
     * 判断ip字符串是否合法
     * 不合法返回null
     * 合法返回ip16进制集合【0-3】
     * @param ipStr
     * @return
     */
    public static List<String> getTrueIPList(String ipStr){
        if(ipStr == null || ipStr.equals("") || ipStr.equals("null") || ipStr.length()<9){
            //字符串错误
            return null;
        }
        if (!ipStr.matches("^([0-9]{1,3}\\.){3}[0-9]{1,3}$")) {
            return null;
        }

        // 将IP地址分割成四个部分
        String[] parts = ipStr.split("\\.");

        // 验证每个部分是否在0到255之间
        for (String part : parts) {
            int num = Integer.parseInt(part);
            if (num < 0 || num > 255) {
                return null;
            }
        }

        List<String> data = new ArrayList<>();
        for (String part : parts) {
            data.add(decToHex(part));
        }

        return data;
    }

    /**
     * 判断port是否合法
     * 合法返回正常数据
     * 不合法返回-1
     * @param portStr
     * @return
     */
    public static List<String> getTruePort(String portStr){
        if(portStr == null || portStr.equals("") || portStr.equals("null")){
            //字符串错误
            return null;
        }
        int port = Integer.parseInt(portStr);
        if(port<0 || port>65535){
            return null;
        }
        int a = port%256;
        int b = port/256;
        List<String> result = new ArrayList<>();
        result.add(decToHex(a+""));
        result.add(decToHex(b+""));
        return result;
    }

    /**
     * 小于15的数字字符串转换为1位16进制字符串
     * @param decStr
     * @return
     */
    public static String decTo1Hex(String decStr){
        String result ="";
        result = Integer.toHexString(Integer.parseInt(decStr));
        return result;
    }

    /**
     * (4位)2进制字符串转换为16进制字符串(1位)
     * @param binaryStr
     * @return
     */
    public static String binary4ToHex(String binaryStr){
        String result ="";
        if(binaryStr!=null&&binaryStr.length()>0){
            result = Integer.toHexString(Integer.parseInt(binaryStr, 2));
        }
        return result;
    }

    public static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }

    public static String[] asciiToHex(String asciiStr) {
        String[] hexArray = new String[asciiStr.length()];
        for (int i = 0; i < asciiStr.length(); i++) {
            char ch = asciiStr.charAt(i);
            hexArray[i] = String.format("%02x", (int) ch);
        }

        return hexArray;
    }

    /**
     * 判断用户输入电压是否合法（不大于25）
     * @param input
     * @return
     */
    public static boolean isValidInput(String input) {
        // 正则表达式匹配 0-25 的整数，或不大于 25 的两位小数
        String regex = "^(25(\\.0{0,2})?|2[0-4](\\.[0-9]{1,2})?|1?[0-9](\\.[0-9]{1,2})?)$";

        // 匹配正则表达式
        return input.matches(regex);
    }

    /**
     * 更具dec 获取 3位 2进制 字符串
     * @param dec 范围 【0，7】
     * @return
     */
    public static String getBinary3ByDec(int dec){
        if(dec == 0){
            return "000";
        } else if (dec == 1) {
            return "001";
        } else if (dec == 2) {
            return "010";
        } else if (dec == 3) {
            return "011";
        }else if (dec == 4){
            return "100";
        }else if (dec == 5){
            return "101";
        } else if (dec == 6) {
            return "110";
        }else {
            return "111";
        }
    }

    public static String to6BitBinary(int num) {
        if (num < 0 || num > 63) {
            //throw new IllegalArgumentException("输入值必须介于 0 和 63 之间");
            return "000000";
        }
        String binaryStr = Integer.toBinaryString(num);
        // 如果不足 6 位，前面补零
        while (binaryStr.length() < 6) {
            binaryStr = "0" + binaryStr;
        }
        return binaryStr;
    }

}