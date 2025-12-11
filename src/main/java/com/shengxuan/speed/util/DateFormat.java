package com.shengxuan.speed.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class DateFormat {

    /**
     * 获取当前时间
     * @return  yyyy-MM-dd HH:mm:ss
     */
    public static String dateNowFormat(){
        Date currentTime = new Date();
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentTime);
    }


    /**
     * 获取yyyyMMdd格式的日期
     * @return
     */
    public static String getDateNotHaveTime(){
        LocalDate now = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String format = now.format(formatter);
        return format;
    }

    /**
     * 获取当前时间10分钟前的字符串
     * @return  yyyy-MM-dd HH:mm:ss
     */
    public static String getTimeBefore10Minutes(){
        LocalDateTime tenMinBefore = LocalDateTime.now().minusMinutes(10);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return tenMinBefore.format(formatter);
    }

    public static boolean is10(String timeStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date inputDate = sdf.parse(timeStr);
            Date currentDate = new Date();
            long a = Math.abs(currentDate.getTime() - inputDate.getTime());
            long b = a/ (60* 1000);
            return b>10;
        }catch (ParseException e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 时间日期格式转换
     * @param dateString yyyyMMddHHmmss
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String format(String dateString){
        Date date = null;
        String now = "";
        try {
            date = new SimpleDateFormat("yyyyMMddHHmmss").parse(dateString);
            now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
       return now;
    }

    /**
     * 计算天数
     * @param dayStr
     * @return
     */
    public static int countDay(String dayStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date1 = new Date();
        Date date2 = null;
        try {
            date2 = sdf.parse(dayStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long days = (date2.getTime() - date1.getTime()) / (24*3600*1000);
        long yushu = (date2.getTime() - date1.getTime()) % (24*3600*1000);

        System.out.println("days:" + days + ",yushu:" + yushu);

        //规整方法1
        date1.setHours(0);
        date1.setMinutes(0);
        date1.setSeconds(0);
        long days2 = (date2.getTime() - date1.getTime()) / (24*3600*1000);
        long yushu2 = (date2.getTime() - date1.getTime()) % (24*3600*1000);

        System.out.println("days2:" + days2 + ",yushu2:" + yushu2);

        //规整方法2
        String sdate1 = sdf.format(date1);
        try {
            date1 = sdf.parse(sdate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long days3 = (date2.getTime() - date1.getTime()) / (24*3600*1000);
        long yushu3 = (date2.getTime() - date1.getTime()) % (24*3600*1000);

        System.out.println("days3:" + days3 + ",yushu3:" + yushu3);

        return (int)days;
    }

    public static void main(String[] args) {

    }

    /**
     * 是否在24小时内
     * @param dateStr
     * @return
     */
    public static boolean is24h(String dateStr){
        boolean flag = false;
        Date dNow = new Date();   //当前时间
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
        dBefore = calendar.getTime();   //得到前一天的时间
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss"); //设置时间格式
        Date date3 = null;
        try {
            date3 = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(dBefore.getTime()<=date3.getTime() && dNow.getTime()>=date3.getTime()){
            //System.out.println("date3在date1和date2日期范围内！");
            flag = true;
        }
        return flag;
    }

    /**
     * 获取 Seq 后六位是随机数
     * @return
     */
    public static String getSeq(){
        Date now = new Date();
        SimpleDateFormat f = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = f.format(now);
        Random random = new Random();
        int num = random.nextInt(1000000);
        String randomStr = String.format("%06d",num);
        return dateStr + randomStr;
    }
}
