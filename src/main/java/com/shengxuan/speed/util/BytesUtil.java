package com.shengxuan.speed.util;

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
}