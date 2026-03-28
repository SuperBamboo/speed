package com.shengxuan.speed.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shengxuan.speed.entity.pojo.ResultQuery;
import com.shengxuan.speed.entity.pojo.jnch.JSDSys;
import com.shengxuan.speed.entity.pojo.jnch.SpeedSys;
import com.shengxuan.speed.entity.pojo.jnch.jsd.*;
import com.shengxuan.speed.entity.pojo.jnch.speed.*;

import java.text.DecimalFormat;
import java.util.*;

public class ParseJNCHUtils {
    public static String parseJNCH(int type,String parameters){
        String result = null;
        if(type == 0){
            SpeedSys speedSys = new SpeedSys();
            List<List<String>> splitStringToMySys = splitStringToMySys(parameters);
            List<String> dataList1 = splitStringToMySys.get(0);
            List<String> dataList2 = splitStringToMySys.get(1);
            List<String> dataList3 = splitStringToMySys.get(2);
            List<String> dataList4 = splitStringToMySys.get(3);
            SpeedWebNetSys speedWebNetSys = new SpeedWebNetSys();
            //移动运营商 时区
            if("00".equals(BytesUtil.hexToBinary(dataList1.get(10)).charAt(1) +""+BytesUtil.hexToBinary(dataList1.get(10)).charAt(2)+"")){
                speedWebNetSys.setYdyys(0);
            }else if("01".equals(BytesUtil.hexToBinary(dataList1.get(10)).charAt(1) +""+BytesUtil.hexToBinary(dataList1.get(10)).charAt(2)+"")){
                speedWebNetSys.setYdyys(1);
            }else if("10".equals(BytesUtil.hexToBinary(dataList1.get(10)).charAt(1) +""+BytesUtil.hexToBinary(dataList1.get(10)).charAt(2)+"")){
                speedWebNetSys.setYdyys(2);
            }else if("11".equals(BytesUtil.hexToBinary(dataList1.get(10)).charAt(1) +""+BytesUtil.hexToBinary(dataList1.get(10)).charAt(2)+"")){
                speedWebNetSys.setYdyys(3);
            }
            speedWebNetSys.setSq(BytesUtil.hexToDec(dataList1.get(10).substring(1)));
            if("0".equals(BytesUtil.hexToBinary(dataList1.get(10)).charAt(3) +"")){
                speedWebNetSys.setDxbp(0);
            }else {
                speedWebNetSys.setDxbp(1);
            }
            //目标ip地址*/
            speedWebNetSys.setMbIP(Integer.parseInt(dataList1.get(11), 16)+"."+Integer.parseInt(dataList1.get(12),16)+"."+Integer.parseInt(dataList1.get(13),16)+"."+Integer.parseInt(dataList1.get(14),16));
            //目标端口
            speedWebNetSys.setMbPort(Integer.parseInt(dataList1.get(16),16) * 256 + Integer.parseInt(dataList1.get(15),16));
            //连机方式/主从机 等待修改
            if("1".equals(BytesUtil.hexToBinary(dataList1.get(17)).charAt(0) +"")){
                speedWebNetSys.setXtfjsx(0);
            }else {
                if("00".equals(BytesUtil.hexToBinary(dataList1.get(17)).charAt(2) +""+BytesUtil.hexToBinary(dataList1.get(17)).charAt(3)+"")){
                    speedWebNetSys.setXtfjsx(1);
                }else if("01".equals(BytesUtil.hexToBinary(dataList1.get(17)).charAt(2) +""+BytesUtil.hexToBinary(dataList1.get(17)).charAt(3)+"")){
                    speedWebNetSys.setXtfjsx(2);
                }else {
                    speedWebNetSys.setXtfjsx(3);
                }
            }
            //联机方式
            if("0".equals(BytesUtil.hexToBinary(dataList1.get(17)).charAt(7) +"")){
                speedWebNetSys.setLjfs(0);
            }else {
                speedWebNetSys.setLjfs(1);
            }
            //联机间隔
            speedWebNetSys.setLjjg(Integer.parseInt(dataList1.get(19),16) * 256 + Integer.parseInt(dataList1.get(18),16));
            //调试目标ip
            speedWebNetSys.setTsmbIP(Integer.parseInt(dataList1.get(20), 16)+"."+Integer.parseInt(dataList1.get(21),16)+"."+Integer.parseInt(dataList1.get(22),16)+"."+Integer.parseInt(dataList1.get(23),16));
            //调式port
            speedWebNetSys.setTsmbPort(Integer.parseInt(dataList1.get(25),16) * 256 + Integer.parseInt(dataList1.get(24),16));
            //时间ip
            speedWebNetSys.setTimeIP(Integer.parseInt(dataList1.get(26), 16)+"."+Integer.parseInt(dataList1.get(27),16)+"."+Integer.parseInt(dataList1.get(28),16)+"."+Integer.parseInt(dataList1.get(29),16));
            //时间port
            speedWebNetSys.setTimePort(Integer.parseInt(dataList1.get(31),16) * 256 + Integer.parseInt(dataList1.get(30),16));
            //本机物联网地址地址
            int deviceAddress = Integer.parseInt(dataList1.get(33), 16) * 256 + Integer.parseInt(dataList1.get(32),16);
            speedWebNetSys.setNum(deviceAddress);
            //发送分机信息时不发超速标志
            if("1".equals(BytesUtil.hexToBinary(dataList1.get(38)).charAt(4) +"")){
                speedWebNetSys.setFlag1(true);
            }else {
                speedWebNetSys.setFlag1(false);
            }
            //蓝牙关闭标志
            if("1".equals(BytesUtil.hexToBinary(dataList1.get(38)).charAt(2) +"")){
                //表示蓝牙常开
                speedWebNetSys.setFlag2(false);
            }else {
                speedWebNetSys.setFlag2(true);
            }
            //loar信道
            speedWebNetSys.setWxxd(Integer.parseInt(dataList1.get(40),16) - 1);
            //loar速率
            speedWebNetSys.setWxsl(Integer.parseInt(dataList1.get(41),16) - 1);
            speedSys.setSpeedWebNetSys(speedWebNetSys);
            //-----------------------网络参数解析完毕-----------------------------------



            SpeedAndTJSys speedAndTJSys = new SpeedAndTJSys();
            if("0".equals(BytesUtil.hexToBinary(dataList1.get(38)).charAt(5)+"")){  //测速选项
                speedAndTJSys.setCswx(false);
            }else {
                speedAndTJSys.setCswx(true);
            }
            int a = Integer.parseInt(dataList1.get(39),16) / 10;
            String b = Integer.parseInt(dataList1.get(39),16) % 10 == 0? "": "."+Integer.parseInt(dataList1.get(39),16) % 10;
            speedAndTJSys.setYxsdsj(a+b);   //有效速度时间

            String binary44 = BytesUtil.hexToBinary(dataList1.get(44));
            String begin44 = binary44.substring(0, 2);  //方向选择
            String end44 = binary44.substring(4);
            String content44 = binary44.substring(2,4);
            if(content44.equals("00")){
                speedAndTJSys.setLdtxxy(0);
            } else if (content44.equals("01")) {
                speedAndTJSys.setLdtxxy(1);
            } else if (content44.equals("10")) {
                speedAndTJSys.setLdtxxy(2);
            } else if (content44.equals("11")) {
                speedAndTJSys.setLdtxxy(3);
            }
            if(begin44.equals("00")){
                speedAndTJSys.setSdxx(0);
            }else if(begin44.equals("01")){
                speedAndTJSys.setSdxx(1);
            }else {
                speedAndTJSys.setSdxx(2);
            }
            speedAndTJSys.setYxsdyz(Integer.parseInt(end44,2)+"");  //有效速度阈值

            speedAndTJSys.setCsyz(Integer.parseInt(dataList1.get(45),16)+ "");        //超速阈值

            speedAndTJSys.setCsyzZQ1(Integer.parseInt(dataList1.get(46),16) + "");  //周期一超速阈值
            speedAndTJSys.setDsyzZQ1(Integer.parseInt(dataList1.get(47),16) + "");   //周期一低速阈值

            if("00".equals(dataList1.get(48))){     //周期一统计时间
                speedAndTJSys.setTjzqZQ1(0);
            }else if("01".equals(dataList1.get(48))){
                speedAndTJSys.setTjzqZQ1(1);
            }else if("02".equals(dataList1.get(48))){
                speedAndTJSys.setTjzqZQ1(2);
            }else if("03".equals(dataList1.get(48))){
                speedAndTJSys.setTjzqZQ1(3);
            }else if("04".equals(dataList1.get(48))){
                speedAndTJSys.setTjzqZQ1(4);
            }else if("05".equals(dataList1.get(48))){
                speedAndTJSys.setTjzqZQ1(5);
            }else if("06".equals(dataList1.get(48))){
                speedAndTJSys.setTjzqZQ1(6);
            }else if("07".equals(dataList1.get(48))){
                speedAndTJSys.setTjzqZQ1(7);
            }

            speedAndTJSys.setZdclsZQ1(Integer.parseInt(dataList1.get(49),16) + "");  //周期一最低车辆数

            if("00".equals(dataList1.get(50))){      //周期二统计时间
                speedAndTJSys.setTjzqZQ2(0);
            }else if("01".equals(dataList1.get(50))){
                speedAndTJSys.setTjzqZQ2(1);
            }else if("02".equals(dataList1.get(50))){
                speedAndTJSys.setTjzqZQ2(2);
            }else if("03".equals(dataList1.get(50))){
                speedAndTJSys.setTjzqZQ2(3);
            }else if("04".equals(dataList1.get(50))){
                speedAndTJSys.setTjzqZQ2(4);
            }else if("05".equals(dataList1.get(50))){
                speedAndTJSys.setTjzqZQ2(5);
            }else if("06".equals(dataList1.get(50))){
                speedAndTJSys.setTjzqZQ2(6);
            }else if("07".equals(dataList1.get(50))){
                speedAndTJSys.setTjzqZQ2(7);
            }

            speedAndTJSys.setZdclsZQ2(Integer.parseInt(dataList1.get(51),16) + "");  //周期二最低车辆数
            speedSys.setSpeedAndTJSys(speedAndTJSys);

            //-------------------------速度及统计参数解析完毕-----------------------------------


            SpeedPowerSys speedPowerSys = new SpeedPowerSys();
            speedPowerSys.setXdcdymdz(Integer.parseInt(dataList1.get(35),16) * 256 + Integer.parseInt(dataList1.get(34),16) +"");
            speedPowerSys.setTyndymdz(Integer.parseInt(dataList1.get(37),16) * 256 + Integer.parseInt(dataList1.get(36),16) +"");

            String binary38 = BytesUtil.hexToBinary(dataList1.get(38));
            String substring = binary38.substring(binary38.length() - 2, binary38.length());
            String str38 = binary38.substring(1, 6);
            speedPowerSys.setStr38(str38);
            if("00".equals(substring)){
                speedPowerSys.setDylx(0);
            }else if("01".equals(substring)){
                speedPowerSys.setDylx(1);
            }else if("10".equals(substring)){
                speedPowerSys.setDylx(2);
            }else {
                speedPowerSys.setDylx(3);
            }
            if(Integer.parseInt(dataList1.get(38),16)>=128){
                speedPowerSys.setCdkz(1);
            }else {
                speedPowerSys.setCdkz(0);
            }
            speedSys.setSpeedPowerSys(speedPowerSys);

            //--------------------------------电源参数解析完毕------------------------------


            SpeedVoiceSys speedVoiceSys = new SpeedVoiceSys();
            if("0".equals(BytesUtil.hexToBinary(dataList1.get(38)).charAt(1)+"")){  //语音选择
                speedVoiceSys.setYyxz(0);
            }else {
                speedVoiceSys.setYyxz(1);
            }

            if("0".equals(BytesUtil.hexToBinary(dataList1.get(38)).charAt(3)+"")){  //播放选择
                speedVoiceSys.setBfxz(0);
            }else {
                speedVoiceSys.setBfxz(1);
            }

            speedVoiceSys.setJnsd1HourStart(Integer.parseInt(dataList1.get(52),16) + "");    //节能时段
            speedVoiceSys.setJnsd1MinStart(Integer.parseInt(dataList1.get(53),16) + "");
            speedVoiceSys.setJnsd1HourEnd(Integer.parseInt(dataList1.get(54),16) + "");
            speedVoiceSys.setJnsd1MinEnd(Integer.parseInt(dataList1.get(55),16) + "");
            speedVoiceSys.setJnsd2HourStart(Integer.parseInt(dataList1.get(56),16) + "");
            speedVoiceSys.setJnsd2MinStart(Integer.parseInt(dataList1.get(57),16) + "");
            speedVoiceSys.setJnsd2HourEnd(Integer.parseInt(dataList1.get(58),16) + "");
            speedVoiceSys.setJnsd2MinEnd(Integer.parseInt(dataList1.get(59),16) + "");

            speedVoiceSys.setTsyjcd0(Integer.parseInt(dataList1.get(60).substring(0, 1), 16)+"");      //主机语音参数
            speedVoiceSys.setTsyj0(Integer.parseInt(dataList1.get(60).substring(1), 16));
            speedVoiceSys.setJgyjcd0(Integer.parseInt(dataList1.get(61).substring(0, 1), 16) + "");
            speedVoiceSys.setJgyj0(Integer.parseInt(dataList1.get(61).substring(1), 16));

            speedVoiceSys.setTsyjcd1(Integer.parseInt(dataList1.get(62).substring(0, 1), 16)+"");      //一号分机语音参数
            speedVoiceSys.setTsyj1(Integer.parseInt(dataList1.get(62).substring(1), 16));
            speedVoiceSys.setJgyjcd1(Integer.parseInt(dataList1.get(63).substring(0, 1), 16) + "");
            speedVoiceSys.setJgyj1(Integer.parseInt(dataList1.get(63).substring(1), 16));

            speedVoiceSys.setTsyjcd2(Integer.parseInt(dataList1.get(64).substring(0, 1), 16)+"");      //二号分机语音参数
            speedVoiceSys.setTsyj2(Integer.parseInt(dataList1.get(64).substring(1), 16));
            speedVoiceSys.setJgyjcd2(Integer.parseInt(dataList1.get(65).substring(0, 1), 16) + "");
            speedVoiceSys.setJgyj2(Integer.parseInt(dataList1.get(65).substring(1), 16));

            speedVoiceSys.setTsyjcd3(Integer.parseInt(dataList1.get(66).substring(0, 1), 16)+"");      //三号分机语音参数
            speedVoiceSys.setTsyj3(Integer.parseInt(dataList1.get(66).substring(1), 16));
            speedVoiceSys.setJgyjcd3(Integer.parseInt(dataList1.get(67).substring(0, 1), 16) + "");
            speedVoiceSys.setJgyj3(Integer.parseInt(dataList1.get(67).substring(1), 16));

            speedVoiceSys.setYjyyyl(Integer.parseInt(dataList1.get(68).substring(0,1),16));  //语音音量大小参数
            speedVoiceSys.setBtyyyl(Integer.parseInt(dataList1.get(68).substring(1),16));
            speedSys.setSpeedVoiceSys(speedVoiceSys);

            //----------------------------------语音提示参数解析完毕-------------------------------

            SpeedPoseSys speedPoseSys = new SpeedPoseSys();
            String binary42 = BytesUtil.hexToBinary(dataList1.get(42));
            String substringPose = binary42.substring(binary42.length() - 2, binary42.length());
            String substring1 = binary42.substring(4, 6);
            String substring2 = binary42.substring(0, 1);
            String str42 = binary42.substring(1, 4);
            speedPoseSys.setStr42(str42);
            if("00".equals(substringPose)){
                speedPoseSys.setCzz(0);
            }else if("01".equals(substringPose)){
                speedPoseSys.setCzz(1);
            }else if("10".equals(substringPose)){
                speedPoseSys.setCzz(2);
            }else {
                speedPoseSys.setCzz(3);
            }
            if("00".equals(substring1)){
                speedPoseSys.setLmd(0);
            }else if("01".equals(substring1)){
                speedPoseSys.setLmd(1);
            }else if("10".equals(substring1)){
                speedPoseSys.setLmd(2);
            }else {
                speedPoseSys.setLmd(2);
            }
            if("0".equals(substring2)){
                speedPoseSys.setFx(0);
            }else {
                speedPoseSys.setFx(1);
            }
            //倾斜角度阈值
            speedPoseSys.setQxjyz(Integer.parseInt(dataList1.get(43), 16)+"");
            speedSys.setSpeedPoseSys(speedPoseSys);

            List<SpeedDisPlayFA> disPlayFAList = new ArrayList<>();
            for (int index = 1; index <= 8; index++) {
                SpeedDisPlayFA speedDisPlayFA = new SpeedDisPlayFA();
                int n =  42+ (index - 1) * 4;
                if("00".equals(BytesUtil.hexToBinary(dataList2.get(n)).charAt(0)+""+BytesUtil.hexToBinary(dataList2.get(n)).charAt(1)+"")){
                    speedDisPlayFA.setXsys1(0);
                }else if("01".equals(BytesUtil.hexToBinary(dataList2.get(n)).charAt(0)+""+BytesUtil.hexToBinary(dataList2.get(n)).charAt(1)+"")){
                    speedDisPlayFA.setXsys1(1);
                }else if("10".equals(BytesUtil.hexToBinary(dataList2.get(n)).charAt(0)+""+BytesUtil.hexToBinary(dataList2.get(n)).charAt(1)+"")){
                    speedDisPlayFA.setXsys1(2);
                }else {
                    speedDisPlayFA.setXsys1(3);
                }
                if("1".equals(BytesUtil.hexToBinary(dataList2.get(n)).charAt(2)+"")){
                    speedDisPlayFA.setCrjssd1(true);
                }else {
                    speedDisPlayFA.setCrjssd1(false);
                }

                speedDisPlayFA.setZxz1(Integer.parseInt(dataList2.get(n).substring(1), 16));
                speedDisPlayFA.setTlsj1(Integer.parseInt(dataList2.get(n+1),16)+"");

                if("00".equals(BytesUtil.hexToBinary(dataList2.get(n+2)).charAt(0)+""+BytesUtil.hexToBinary(dataList2.get(n+2)).charAt(1)+"")){
                    speedDisPlayFA.setXsys2(0);
                }else if("01".equals(BytesUtil.hexToBinary(dataList2.get(n+2)).charAt(0)+""+BytesUtil.hexToBinary(dataList2.get(n+2)).charAt(1)+"")){
                    speedDisPlayFA.setXsys2(1);
                }else if("10".equals(BytesUtil.hexToBinary(dataList2.get(n+2)).charAt(0)+""+BytesUtil.hexToBinary(dataList2.get(n+2)).charAt(1)+"")){
                    speedDisPlayFA.setXsys2(2);
                }else {
                    speedDisPlayFA.setXsys2(3);
                }
                if("1".equals(BytesUtil.hexToBinary(dataList2.get(n+2)).charAt(2)+"")){
                    speedDisPlayFA.setCrjssd2(true);
                }else {
                    speedDisPlayFA.setCrjssd2(false);
                }

                speedDisPlayFA.setZxz2(Integer.parseInt(dataList2.get(n+2).substring(1), 16));
                speedDisPlayFA.setTlsj2(Integer.parseInt(dataList2.get(n+3),16)+"");
                disPlayFAList.add(speedDisPlayFA);
            }
            speedSys.setDisPlayFAList(disPlayFAList);

            speedSys.setParameter(parameters);


            ObjectMapper objectMapper = new ObjectMapper();
            try {
                result = objectMapper.writeValueAsString(speedSys);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else if (type == 1) {

        } else if (type == 2) {
            //警闪灯解析
            JSDSys jsdSys = new JSDSys();
            List<List<String>> splitStringToMySys = splitStringToJSDSys(parameters);
            List<String> dataList1 = splitStringToMySys.get(0);
            List<String> dataList2 = splitStringToMySys.get(1);
            List<String> dataList3 = splitStringToMySys.get(2);
            List<String> dataList4 = splitStringToMySys.get(3);
            List<String> dataList5 = splitStringToMySys.get(4);
            List<String> dataList6 = splitStringToMySys.get(5);
            List<String> dataList7 = splitStringToMySys.get(6);
            List<String> dataList8 = splitStringToMySys.get(7);
            List<String> dataList9 = splitStringToMySys.get(8);
            List<String> dataList10 = splitStringToMySys.get(9);
            List<String> dataList11 = splitStringToMySys.get(10);
            List<String> dataList12 = splitStringToMySys.get(11);
            List<String> dataList13 = splitStringToMySys.get(12);
            List<String> dataList14 = splitStringToMySys.get(13);
            List<String> dataList15 = splitStringToMySys.get(14);

            JSDWorkTypeSys jsdWorkTypeSys = new JSDWorkTypeSys();
            //工作方式
            int workType  = Integer.parseInt(dataList1.get(46),16);
            jsdWorkTypeSys.setGzfs(workType);
            //输出状态 闪灯状态
            String scztBinary = BytesUtil.hexToBinary(dataList1.get(47));
            String otherString1 = scztBinary.substring(0,2);
            jsdWorkTypeSys.setOtherString1(otherString1);
            String sdztBinary = BytesUtil.hexToBinary(dataList1.get(48));
            String otherString2 = sdztBinary.substring(0,2);
            jsdWorkTypeSys.setOtherString2(otherString2);
            List<Boolean> scztList = new ArrayList<>();
            List<Boolean> sdztList = new ArrayList<>();
            for (int i = 7; i > 1 ; i--) {
                if("1".equals(scztBinary.charAt(i)+"")){
                    scztList.add(true);
                }else {
                    scztList.add(false);
                }
                if("1".equals(sdztBinary.charAt(i)+"")){
                    sdztList.add(true);
                }else {
                    sdztList.add(false);
                }
            }
            jsdWorkTypeSys.setSczt(scztList);
            jsdWorkTypeSys.setSdzt(sdztList);
            //起始阶段号
            int qsjd = Integer.parseInt(dataList1.get(49), 16);
            jsdWorkTypeSys.setQsjd((qsjd+1)+"");

            //阶段数量
            int jdsl = Integer.parseInt(dataList1.get(50),16);
            jsdWorkTypeSys.setJdsl(jdsl+"");

            //同步参数
            int tbcs = Integer.parseInt(dataList1.get(51), 16);
            jsdWorkTypeSys.setTbcs(tbcs+"");

            //同步周期
            int tbzq = Integer.parseInt(dataList1.get(52), 16);
            jsdWorkTypeSys.setTbzq(tbzq+"");

            //提示语句
            int tsyj = Integer.parseInt(dataList1.get(53), 16);
            if(tsyj == 0){
                jsdWorkTypeSys.setTsyj(0);
            }else {
                /*for (int i = 0; i < warningToneList.size(); i++) {
                    if(warningToneList.get(i).getWarningToneNo().equals(tsyj+"")){
                        m_spinner_tsyj.setSelectedIndex(i+1);
                    }
                }*/
                jsdWorkTypeSys.setTsyj(tsyj); //--------此处对应前端不能直接使用需要结合warningToneList解析使用
            }

            //提示间隔
            int tsjg = Integer.parseInt(dataList1.get(55), 16);
            jsdWorkTypeSys.setTsjg(tsjg+"");
            jsdSys.setJsdWorkTypeSys(jsdWorkTypeSys);


            //----------------自定义闪灯编辑----------------------------
            List<JSDZDYSDSys> jsdzdysdSysList = new ArrayList<>();
            for (int i = 0; i < 64; i++) {
                JSDZDYSDSys jsdzdysdSys = new JSDZDYSDSys();
                List<String> dataList;
                if (i<=15){
                    dataList = dataList5;
                } else if (i<=31) {
                    dataList = dataList6;
                }else if(i <=47){
                    dataList = dataList7;
                }else {
                    dataList = dataList8;
                }

                int n = i % 16;

                String binary1_2 = BytesUtil.hexToBinary(dataList.get(10 + (n * 4)));
                jsdzdysdSys.setSc1(Integer.parseInt(binary1_2.substring(5), 2));
                if("1".equals(binary1_2.charAt(4)+"")){
                    jsdzdysdSys.setSd1(true);
                }else {
                    jsdzdysdSys.setSd1(false);
                }
                jsdzdysdSys.setSc2(Integer.parseInt(binary1_2.substring(1,4), 2));
                if("1".equals(binary1_2.charAt(0)+"")){
                    jsdzdysdSys.setSd2(true);
                }else {
                    jsdzdysdSys.setSd2(false);
                }


                String binary3_4 = BytesUtil.hexToBinary(dataList.get(11 + (n * 4)));
                jsdzdysdSys.setSc3(Integer.parseInt(binary3_4.substring(5), 2));
                if("1".equals(binary3_4.charAt(4)+"")){
                    jsdzdysdSys.setSd3(true);
                }else {
                    jsdzdysdSys.setSd3(false);
                }
                jsdzdysdSys.setSc4(Integer.parseInt(binary3_4.substring(1,4), 2));
                if("1".equals(binary3_4.charAt(0)+"")){
                    jsdzdysdSys.setSd4(true);
                }else {
                    jsdzdysdSys.setSd4(false);
                }


                String binary5_6 = BytesUtil.hexToBinary(dataList.get(12 + (n * 4)));
                jsdzdysdSys.setSc5(Integer.parseInt(binary5_6.substring(5), 2));
                if("1".equals(binary5_6.charAt(4)+"")){
                    jsdzdysdSys.setSd5(true);
                }else {
                    jsdzdysdSys.setSd5(false);
                }
                jsdzdysdSys.setSc6(Integer.parseInt(binary5_6.substring(1,4), 2));
                if("1".equals(binary5_6.charAt(0)+"")){
                    jsdzdysdSys.setSd6(true);
                }else {
                    jsdzdysdSys.setSd6(false);
                }


                int bc = Integer.parseInt(dataList.get(13 + n * 4), 16);
                jsdzdysdSys.setBc(bc+"");

                jsdzdysdSysList.add(jsdzdysdSys);
            }
            jsdSys.setJsdzdysdSysList(jsdzdysdSysList);


            //---------------------------时段方案设置------------------------
            for (int index = 0; index < 2; index++) {
                List<JSDSDFASys> jsdsdfaSysList = new ArrayList<>();
                List<String> dataListA;
                List<String> dataListB;
                if (index == 0){
                    dataListA = dataList9;
                    dataListB = dataList10;
                } else {
                    dataListA = dataList11;
                    dataListB = dataList12;
                }
                //结束时判断

                for (int i = 0; i < 5; i++) {
                    JSDSDFASys jsdsdfaSys = new JSDSDFASys();
                    int endHour = Integer.parseInt(dataListA.get(10 + (i * 12)), 16);
                    int endMin = Integer.parseInt(dataListA.get(11 + (i * 12)), 16);
                    jsdsdfaSys.setEndHour(endHour+"");
                    jsdsdfaSys.setEndMin(endMin+"");
                    jsdsdfaSys.setGzfs(Integer.parseInt(dataListA.get(12 + (i * 12)), 16));
                    String sd_scztBinary = BytesUtil.hexToBinary(dataListA.get(13 + (i * 12)));
                    String sd_scsdztBinary = BytesUtil.hexToBinary(dataListA.get(14 + (i * 12)));
                    List<Boolean> sd_scztList = new ArrayList<>();
                    List<Boolean> sd_sdztList = new ArrayList<>();
                    for (int j = 7; j > 1 ; j--) {
                        if("1".equals(sd_scztBinary.charAt(j)+"")){
                            sd_scztList.add(true);
                        }else {
                            sd_scztList.add(false);
                        }
                        if("1".equals(sd_scsdztBinary.charAt(j)+"")){
                            sd_sdztList.add(true);
                        }else {
                            sd_sdztList.add(false);
                        }
                    }
                    jsdsdfaSys.setScztList(sd_scztList);
                    jsdsdfaSys.setSdztList(sd_sdztList);


                    jsdsdfaSys.setQsjd((Integer.parseInt(dataListA.get(15 + (i * 12)), 16)+1)+"");
                    jsdsdfaSys.setJdsl(Integer.parseInt(dataListA.get(16 + (i * 12)), 16)+"");
                    jsdsdfaSys.setTbyc(Integer.parseInt(dataListA.get(17 + (i * 12)), 16)+"");
                    jsdsdfaSys.setTbzq(Integer.parseInt(dataListA.get(18 + (i * 12)), 16)+"");
                    int bfyj = Integer.parseInt(dataListA.get(19 + (i * 12)), 16);    //同步周期
                    if(bfyj == 0){
                        jsdsdfaSys.setBfyj(0);
                    }else {
                        jsdsdfaSys.setBfyj(Integer.parseInt(dataListA.get(19 + (i * 12)), 16));
                    }
                    jsdsdfaSys.setBbjg(Integer.parseInt(dataListA.get(20 + (i * 12)), 16)+"");
                    String cfgzBinary = BytesUtil.hexToBinary(dataListA.get(21 + (i * 12)));
                    if("1".equals(cfgzBinary.charAt(7)+"")){
                        jsdsdfaSys.setTycfgz(1);
                    }else {
                        jsdsdfaSys.setTycfgz(0);
                    }
                    if("1".equals(cfgzBinary.charAt(6)+"")){
                        jsdsdfaSys.setTycfgz1(1);
                    }else {
                        jsdsdfaSys.setTycfgz1(0);
                    }
                    jsdsdfaSysList.add(jsdsdfaSys);
                }

                //第六时段单独解析
                JSDSDFASys jsdsdfaSys6 = new JSDSDFASys();
                int endHour6 = Integer.parseInt(dataListA.get(70), 16);
                jsdsdfaSys6.setEndHour(endHour6+"");
                int enMin6 = Integer.parseInt(dataListA.get(71), 16);
                jsdsdfaSys6.setEndMin(enMin6+"");
                jsdsdfaSys6.setGzfs(Integer.parseInt(dataListA.get(72),16));
                String binary73 = BytesUtil.hexToBinary(dataListA.get(73));     //输出状态
                String binary10 = BytesUtil.hexToBinary(dataListB.get(10));     //闪灯状态
                List<Boolean> sd_scztList = new ArrayList<>();
                List<Boolean> sd_sdztList = new ArrayList<>();
                for (int i = 7; i > 1 ; i--) {
                    if("1".equals(binary73.charAt(i)+"")){
                        sd_scztList.add(true);
                    }else {
                        sd_scztList.add(false);
                    }
                    if("1".equals(binary10.charAt(i)+"")){
                        sd_sdztList.add(true);
                    }else {
                        sd_sdztList.add(false);
                    }
                }
                jsdsdfaSys6.setScztList(sd_scztList);
                jsdsdfaSys6.setSdztList(sd_sdztList);

                jsdsdfaSys6.setQsjd((Integer.parseInt(dataListB.get(11), 16)+1)+"");    //起始阶段
                jsdsdfaSys6.setJdsl(Integer.parseInt(dataListB.get(12), 16)+"");    //阶段数量
                jsdsdfaSys6.setTbyc(Integer.parseInt(dataListB.get(13), 16)+"");    //同步延迟
                jsdsdfaSys6.setTbzq(Integer.parseInt(dataListB.get(14), 16)+"");    //同步周期
                int bfyj = Integer.parseInt(dataListB.get(15), 16);    //播放语句
                if(bfyj == 0){
                    jsdsdfaSys6.setBfyj(0);
                }else {
                    jsdsdfaSys6.setBfyj(Integer.parseInt(dataListB.get(15), 16));
                }
                jsdsdfaSys6.setBbjg(Integer.parseInt(dataListB.get(16), 16)+"");    //播报间隔
                String cfgzBinary = BytesUtil.hexToBinary(dataListB.get(17));  //触发规则
                if("1".equals(cfgzBinary.charAt(7)+"")){
                    jsdsdfaSys6.setTycfgz(1);
                }else {
                    jsdsdfaSys6.setTycfgz(0);
                }
                if("1".equals(cfgzBinary.charAt(6)+"")){
                    jsdsdfaSys6.setTycfgz1(1);
                }else {
                    jsdsdfaSys6.setTycfgz1(0);
                }
                jsdsdfaSysList.add(jsdsdfaSys6);

                for (int i = 0; i < 4; i++) {
                    JSDSDFASys jsdsdfaSys = new JSDSDFASys();
                    int endHour = Integer.parseInt(dataListB.get(18 + (i * 12)), 16);
                    int endMin = Integer.parseInt(dataListB.get(19 + (i * 12)), 16);
                    jsdsdfaSys.setEndHour(endHour+"");
                    jsdsdfaSys.setEndMin(endMin+"");
                    jsdsdfaSys.setGzfs(Integer.parseInt(dataListB.get(20 + (i * 12)), 16));
                    String scztBinaryB = BytesUtil.hexToBinary(dataListB.get(21 + (i * 12)));
                    String scsdztBinaryB = BytesUtil.hexToBinary(dataListB.get(22 + (i * 12)));
                    List<Boolean> scztListB = new ArrayList<>();
                    List<Boolean> sdztListB = new ArrayList<>();
                    for (int j = 7; j > 1 ; j--) {
                        if("1".equals(scztBinaryB.charAt(j)+"")){
                            scztListB.add(true);
                        }else {
                            scztListB.add(false);
                        }
                        if("1".equals(scsdztBinaryB.charAt(j)+"")){
                            sdztListB.add(true);
                        }else {
                            sdztListB.add(false);
                        }
                    }
                    jsdsdfaSys.setScztList(scztListB);
                    jsdsdfaSys.setSdztList(sdztListB);
                    jsdsdfaSys.setQsjd((Integer.parseInt(dataListB.get(23 + (i * 12)), 16)+1)+"");
                    jsdsdfaSys.setJdsl(Integer.parseInt(dataListB.get(24 + (i * 12)), 16)+"");
                    jsdsdfaSys.setTbyc(Integer.parseInt(dataListB.get(25 + (i * 12)), 16)+"");
                    jsdsdfaSys.setTbzq(Integer.parseInt(dataListB.get(26 + (i * 12)), 16)+"");
                    int bfyjB = Integer.parseInt(dataListB.get(27 + (i * 12)), 16);
                    if(bfyjB == 0){
                        jsdsdfaSys.setBfyj(0);
                    }else {
                        jsdsdfaSys.setBfyj(Integer.parseInt(dataListB.get(27 + (i * 12)), 16));
                    }
                    jsdsdfaSys.setBbjg(Integer.parseInt(dataListB.get(28 + (i * 12)), 16)+"");
                    String cfgzBinaryB = BytesUtil.hexToBinary(dataListB.get(29 + (i * 12)));
                    if("1".equals(cfgzBinaryB.charAt(7)+"")){
                        jsdsdfaSys.setTycfgz(1);
                    }else {
                        jsdsdfaSys.setTycfgz(0);
                    }
                    if("1".equals(cfgzBinaryB.charAt(6)+"")){
                        jsdsdfaSys.setTycfgz1(1);
                    }else {
                        jsdsdfaSys.setTycfgz1(0);
                    }
                    jsdsdfaSysList.add(jsdsdfaSys);
                }

                if(index == 0){
                    jsdSys.setJsdsdfaSysList(jsdsdfaSysList);
                }else {
                    jsdSys.setJsdsdfaSysList1(jsdsdfaSysList);
                }
            }

            //---------------------------特殊日---------------------------
            List<JSDTSR> jsdtsrList = new ArrayList<>();
            //1-21号特殊日解析
            for (int i = 0; i < 21; i++) {
                JSDTSR jsdtsr = new JSDTSR();
                jsdtsr.setIndex(i+1);
                int tsMDec = Integer.parseInt(dataList13.get(10 + (3 * i)), 16);
                int tsDDec = Integer.parseInt(dataList13.get(11 + (3 * i)), 16);
                int tsSDDec = Integer.parseInt(dataList13.get(12 + (3 * i)), 16);
                jsdtsr.setMonth(tsMDec);
                jsdtsr.setDay(tsDDec);
                jsdtsr.setSdfa(tsSDDec);
                jsdtsrList.add(jsdtsr);
            }
            JSDTSR jsdtsr22 = new JSDTSR();
            jsdtsr22.setIndex(22);
            //第22行第一个字节
            int m22Dec = Integer.parseInt(dataList13.get(dataList13.size() - 2), 16);
            jsdtsr22.setMonth(m22Dec);

            int d22Dec = Integer.parseInt(dataList14.get(10), 16);
            jsdtsr22.setDay(d22Dec);
            int sd22Dec = Integer.parseInt(dataList14.get(11), 16);
            jsdtsr22.setSdfa(sd22Dec);
            jsdtsrList.add(jsdtsr22);

            for (int i = 0; i < 18; i++) {
                JSDTSR jsdtsr = new JSDTSR();
                jsdtsr.setIndex(i+23);
                int tsMDec = Integer.parseInt(dataList14.get(12 + (3 * i)), 16);
                int tsDDec = Integer.parseInt(dataList14.get(13 + (3 * i)), 16);
                int tsSDDec = Integer.parseInt(dataList14.get(14 + (3 * i)), 16);
                jsdtsr.setMonth(tsMDec);
                jsdtsr.setDay(tsDDec);
                jsdtsr.setSdfa(tsSDDec);
                jsdtsrList.add(jsdtsr);
            }
            jsdSys.setJsdtsrList(jsdtsrList);

            //------------------------------通道出发规则-----------------------
            List<JSDTDCFGZ> jsdtdcfgzList = new ArrayList<>();
            for (int index = 0; index < 6; index++) {
                JSDTDCFGZ jsdtdcfgz = new JSDTDCFGZ();
                List<Boolean> tjList = new ArrayList<>();
                List<Boolean> cfList = new ArrayList<>();
                List<Boolean> hfList = new ArrayList<>();
                String binary1 = BytesUtil.hexToBinary(dataList2.get(10+8*index));
                String binary2 = BytesUtil.hexToBinary(dataList2.get(11+8*index));
                String binary3 = BytesUtil.hexToBinary(dataList2.get(12+8*index));
                String binary5 = BytesUtil.hexToBinary(dataList2.get(14+8*index));
                for (int i = 7; i > 2 ; i--) {
                    tjList.add("1".equals(binary1.charAt(i) + ""));
                    cfList.add("1".equals(binary2.charAt(i) + ""));
                    hfList.add("1".equals(binary3.charAt(i) + ""));
                }
                jsdtdcfgz.setTjList(tjList);
                jsdtdcfgz.setCfList(cfList);
                jsdtdcfgz.setHfList(hfList);

                String s = binary5.substring(5);
                if(s.charAt(0) == '1'){
                    jsdtdcfgz.setSczt(3);
                }else {
                    if(s.charAt(2) == '0'){
                        jsdtdcfgz.setSczt(0);
                    }else {
                        if(s.charAt(1) == '1'){
                            jsdtdcfgz.setSczt(2);
                        }else {
                            jsdtdcfgz.setSczt(1);
                        }
                    }
                }
                int time1 = Integer.parseInt(dataList2.get(15+8*index), 16);
                int time2 = Integer.parseInt(dataList2.get(16+8*index), 16);
                int time3 = Integer.parseInt(dataList2.get(17+8*index), 16);
                jsdtdcfgz.setZdzxsj((time2*256+time1)+"");
                jsdtdcfgz.setHfycsj(time3+"");
                jsdtdcfgzList.add(jsdtdcfgz);
            }
            jsdSys.setJsdtdcfgzList(jsdtdcfgzList);

            //-----------------------------语音触发参数-----------------------------
            List<JSDVoiceSys> jsdVoiceSysList = new ArrayList<>();
            for (int index = 0; index < 2; index++) {
                JSDVoiceSys jsdVoiceSys = new JSDVoiceSys();
                List<Boolean> tjList = new ArrayList<>();
                List<Boolean> cfList = new ArrayList<>();
                List<Boolean> hfList = new ArrayList<>();
                String binary1 = BytesUtil.hexToBinary(dataList2.get(58+8*index));
                String binary2 = BytesUtil.hexToBinary(dataList2.get(59+8*index));
                String binary3 = BytesUtil.hexToBinary(dataList2.get(60+8*index));

                for (int i = 7; i >= 0 ; i--) {
                    tjList.add("1".equals(binary1.charAt(i) + ""));
                    cfList.add("1".equals(binary2.charAt(i) + ""));
                    hfList.add("1".equals(binary3.charAt(i) + ""));
                }
                jsdVoiceSys.setTjList(tjList);
                jsdVoiceSys.setCfjList(cfList);
                jsdVoiceSys.setHfList(hfList);
                //提示语句
                int voice_tsyj = Integer.parseInt(dataList2.get(62+8*index), 16);
                if(voice_tsyj == 0){
                    jsdVoiceSys.setBfyj(0);
                }else {
                    jsdVoiceSys.setBfyj(Integer.parseInt(dataList2.get(62+8*index), 16));
                }

                int bbjg = Integer.parseInt(dataList2.get(63+8*index), 16);
                int bbcs = Integer.parseInt(dataList2.get(64+8*index), 16);
                jsdVoiceSys.setBbjg(bbjg+"");
                jsdVoiceSys.setBbcs(bbcs+"");
                jsdVoiceSysList.add(jsdVoiceSys);
            }
            jsdSys.setJsdVoiceSysList(jsdVoiceSysList);


            //--------------------------自定义闪灯触发规则------------------------------
            List<JSDZDYSDCFGZ> jsdzdysdcfgzList = new ArrayList<>();
            for (int index = 0; index < 2; index++) {
                JSDZDYSDCFGZ jsdzdysdcfgz = new JSDZDYSDCFGZ();
                List<Boolean> tjList = new ArrayList<>();
                List<Boolean> cfList = new ArrayList<>();
                List<Boolean> hfList = new ArrayList<>();
                String binary1 = BytesUtil.hexToBinary(dataList3.get(10+16*index));
                String binary2 = BytesUtil.hexToBinary(dataList3.get(11+16*index));
                String binary3 = BytesUtil.hexToBinary(dataList3.get(12+16*index));

                for (int i = 7; i > 2 ; i--) {
                    tjList.add("1".equals(binary1.charAt(i) + ""));
                    cfList.add("1".equals(binary2.charAt(i) + ""));
                    hfList.add("1".equals(binary3.charAt(i) + ""));
                }
                jsdzdysdcfgz.setTjList(tjList);
                jsdzdysdcfgz.setCfList(cfList);
                jsdzdysdcfgz.setHfList(hfList);
                //提示语句
                jsdzdysdcfgz.setQsjd(String.valueOf(Integer.parseInt(dataList3.get(14 + 16 * index), 16) + 1));
                jsdzdysdcfgz.setJdsl(String.valueOf(Integer.parseInt(dataList3.get(15 + 16 * index), 16)));
                jsdzdysdcfgz.setTbxwc(String.valueOf(Integer.parseInt(dataList3.get(16+16*index), 16)));
                jsdzdysdcfgz.setTbzq(String.valueOf(Integer.parseInt(dataList3.get(17+16*index), 16)));
                jsdzdysdcfgz.setBfyj(Integer.parseInt(dataList3.get(22+16*index), 16));

                int bbjg = Integer.parseInt(dataList3.get(23+16*index), 16);
                int bbcs = Integer.parseInt(dataList3.get(24+16*index), 16);
                jsdzdysdcfgz.setBbjg(bbjg+"");
                jsdzdysdcfgz.setBbcs(bbcs+"");
                jsdzdysdcfgzList.add(jsdzdysdcfgz);
            }
            jsdSys.setJsdzdysdcfgzList(jsdzdysdcfgzList);

            //---------------------------------网络及通讯参数---------------------------

            JSDWebNetSys jsdWebNetSys = new JSDWebNetSys();
            //移动运营商 时区
            if("00".equals(BytesUtil.hexToBinary(dataList1.get(10)).charAt(1) +""+BytesUtil.hexToBinary(dataList1.get(10)).charAt(2)+"")){
                jsdWebNetSys.setYdyys(0);
            }else if("01".equals(BytesUtil.hexToBinary(dataList1.get(10)).charAt(1) +""+BytesUtil.hexToBinary(dataList1.get(10)).charAt(2)+"")){
                jsdWebNetSys.setYdyys(1);
            }else if("10".equals(BytesUtil.hexToBinary(dataList1.get(10)).charAt(1) +""+BytesUtil.hexToBinary(dataList1.get(10)).charAt(2)+"")){
                jsdWebNetSys.setYdyys(2);
            }else if("11".equals(BytesUtil.hexToBinary(dataList1.get(10)).charAt(1) +""+BytesUtil.hexToBinary(dataList1.get(10)).charAt(2)+"")){
                jsdWebNetSys.setYdyys(3);
            }
            jsdWebNetSys.setSq(BytesUtil.hexToDec(dataList1.get(10).substring(1)));
            if("0".equals(BytesUtil.hexToBinary(dataList1.get(10)).charAt(3) +"")){
                jsdWebNetSys.setDxbq(0);
            }else {
                jsdWebNetSys.setDxbq(1);
            }
            //目标ip地址*/
            jsdWebNetSys.setMbIP(Integer.parseInt(dataList1.get(11), 16)+"."+Integer.parseInt(dataList1.get(12),16)+"."+Integer.parseInt(dataList1.get(13),16)+"."+Integer.parseInt(dataList1.get(14),16));
            //目标端口
            jsdWebNetSys.setMbPort(Integer.parseInt(dataList1.get(16),16) * 256 + Integer.parseInt(dataList1.get(15),16) +"");
            //连机方式/主从机 等待修改
            String substring = BytesUtil.hexToBinary(dataList1.get(17)).substring(1, 7);
            int n = Integer.parseInt(substring, 2);
            jsdWebNetSys.setLdbh((n+1)+"");
            if("1".equals(BytesUtil.hexToBinary(dataList1.get(17)).charAt(0) +"")){
                jsdWebNetSys.setXtfjsx(0);
            }else {
                //如果是分机则是分机地址(几号分机)
                jsdWebNetSys.setXtfjsx(1);
            }
            //联机方式
            if("0".equals(BytesUtil.hexToBinary(dataList1.get(17)).charAt(7) +"")){
                jsdWebNetSys.setLjfs(0);
            }else {
                jsdWebNetSys.setLjfs(1);
            }
            //联机间隔
            jsdWebNetSys.setLjjg(Integer.parseInt(dataList1.get(19),16) * 256 + Integer.parseInt(dataList1.get(18),16) +"");
            //调试目标ip
            jsdWebNetSys.setTsmbIP(Integer.parseInt(dataList1.get(20), 16)+"."+Integer.parseInt(dataList1.get(21),16)+"."+Integer.parseInt(dataList1.get(22),16)+"."+Integer.parseInt(dataList1.get(23),16));
            //调式port
            jsdWebNetSys.setTsmbPort(Integer.parseInt(dataList1.get(25),16) * 256 + Integer.parseInt(dataList1.get(24),16) +"");
            //时间ip
            jsdWebNetSys.setTimeIP(Integer.parseInt(dataList1.get(26), 16)+"."+Integer.parseInt(dataList1.get(27),16)+"."+Integer.parseInt(dataList1.get(28),16)+"."+Integer.parseInt(dataList1.get(29),16));
            //时间port
            jsdWebNetSys.setTimePort(Integer.parseInt(dataList1.get(31),16) * 256 + Integer.parseInt(dataList1.get(30),16) +"");
            //本机物联网地址地址
            int deviceAddress = Integer.parseInt(dataList1.get(33), 16) * 256 + Integer.parseInt(dataList1.get(32),16);
            jsdWebNetSys.setNum(deviceAddress+"");
            //loar信道
            jsdWebNetSys.setWxxd(Integer.parseInt(dataList1.get(40),16) - 1);
            //loar速率
            jsdWebNetSys.setWxsl(Integer.parseInt(dataList1.get(41),16) - 1);
            //APN
            int len = Integer.parseInt(dataList4.get(10),16);
            StringBuilder sb = new StringBuilder();
            for (int i = 11; i < (len+11); i++) {
                sb.append(dataList4.get(i));
            }
            String resultHex = sb.toString();
            String s = BytesUtil.hexToAscii(resultHex);
            jsdWebNetSys.setApn(s);
            jsdSys.setJsdWebNetSys(jsdWebNetSys);


            //-----------------------------------------电源管理参数------------------------------
            JSDPowerSys jsdPowerSys = new JSDPowerSys();
            jsdPowerSys.setXdcdymdz(Integer.parseInt(dataList1.get(35),16) * 256 + Integer.parseInt(dataList1.get(34),16) +"");
            jsdPowerSys.setTyndymdz(Integer.parseInt(dataList1.get(37),16) * 256 + Integer.parseInt(dataList1.get(36),16) +"");

            String binary40 = BytesUtil.hexToBinary(dataList1.get(38));
            String dylxBinnary = binary40.substring(6);
            if("00".equals(dylxBinnary)){
                jsdPowerSys.setDylx(0);
            }else if("01".equals(dylxBinnary)){
               jsdPowerSys.setDylx(1);
            }else if("10".equals(dylxBinnary)){
                jsdPowerSys.setDylx(2);
            }else {
                jsdPowerSys.setDylx(3);
            }
            List<JSDPowerDY> jsdPowerDYList = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                JSDPowerDY jsdPowerDY = new JSDPowerDY();
                int index = 42+(i*8);
                //夜间阀值
                int yjfz = Integer.parseInt(dataList3.get(index+1), 16) * 256 + Integer.parseInt(dataList3.get(index),16);
                double yjfzDouble = (double) yjfz / 100.0;
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                jsdPowerDY.setYjyzdy(decimalFormat.format(yjfzDouble)+"");
                //欠压电压
                int qydy = Integer.parseInt(dataList3.get(index+3), 16) * 256 + Integer.parseInt(dataList3.get(index+2),16);
                double qydyDouble = (double) qydy / 100.0;
                jsdPowerDY.setQydy(decimalFormat.format(qydyDouble)+"");
                //满电电压
                int mddy = Integer.parseInt(dataList3.get(index+5), 16) * 256 + Integer.parseInt(dataList3.get(index+4),16);
                double mddyDouble = (double) mddy / 100.0;
                jsdPowerDY.setMddy(decimalFormat.format(mddyDouble)+"");
                //过充电压
                int gcdy = Integer.parseInt(dataList3.get(index+7), 16) * 256 + Integer.parseInt(dataList3.get(index+6),16);
                double gcdyDouble = (double) gcdy / 100.0;
                jsdPowerDY.setGcdy(decimalFormat.format(gcdyDouble)+"");
                jsdPowerDYList.add(jsdPowerDY);
            }
            jsdPowerSys.setJsdPowerDYList(jsdPowerDYList);
            if(Integer.parseInt(dataList1.get(40),16)>=128){
                jsdPowerSys.setCdkz(1);
            }else {
                jsdPowerSys.setCdkz(0);
            }
            jsdSys.setJsdPowerSys(jsdPowerSys);

            //-------------------------------姿态及传感器参数--------------------------
            JSDPoseSys jsdPoseSys = new JSDPoseSys();
            String binary42 = BytesUtil.hexToBinary(dataList1.get(42));
            String substring_pose = binary42.substring(binary42.length() - 2);    //垂直面
            String substring1 = binary42.substring(4, 6);   //灵敏度
            String substring2 = binary42.substring(0, 1);   //方向
            //str42 = binary42.substring(1, 4);
            jsdPoseSys.setStr42(binary42.substring(1, 4));

            if("00".equals(substring_pose)){
                jsdPoseSys.setCzz(0);
            }else if("01".equals(substring_pose)){
                jsdPoseSys.setCzz(1);
            }else if("10".equals(substring_pose)){
                jsdPoseSys.setCzz(2);
            }else {
                jsdPoseSys.setCzz(2);
            }
            if("00".equals(substring1)){
                jsdPoseSys.setLmd(0);
            }else if("01".equals(substring1)){
                jsdPoseSys.setLmd(1);
            }else if("10".equals(substring1)){
                jsdPoseSys.setLmd(2);
            }else {
                jsdPoseSys.setLmd(2);
            }
            if("0".equals(substring2)){
                jsdPoseSys.setFx(0);
            }else {
                jsdPoseSys.setFx(1);
            }
            //倾斜角度阈值
            jsdPoseSys.setQxjyz(Integer.parseInt(dataList1.get(43), 16)+"");
            //接近距离阀值 单位0.1米
            double v = Double.parseDouble(Integer.parseInt(dataList1.get(44), 16) + "");
            double a = v / 10;
            jsdPoseSys.setJjjlyz(a+"");
            //保持时间
            jsdPoseSys.setYxbcsj(Integer.parseInt(dataList1.get(45), 16)+"");
            //开关有效保持时间
            jsdPoseSys.setKgyxbcsj(Integer.parseInt(dataList1.get(56), 16)+"");

            String binary57 = BytesUtil.hexToBinary(dataList1.get(57));
            String substring0_3 = binary57.substring(binary57.length() - 4);
            String substring4_7 = binary57.substring(0,4);
            jsdPoseSys.setCqjjbjsj(Integer.parseInt(substring0_3,2));
            jsdPoseSys.setKgzlbjsj(Integer.parseInt(substring4_7,2));
            jsdSys.setJsdPoseSys(jsdPoseSys);


            //-----------------------------------其它参数--------------------
            JSDOtherSys jsdOtherSys = new JSDOtherSys();

            String binary38 = BytesUtil.hexToBinary(dataList1.get(38));
            if("00".equals(binary38.substring(2,4))){   //自编程
                jsdOtherSys.setZbcsdsz(0);
            } else if ("01".equals(binary38.substring(2, 4))) {
                jsdOtherSys.setZbcsdsz(1);
            }else {
                jsdOtherSys.setZbcsdsz(2);
            }
            if("00".equals(binary38.substring(4,6))){   //节假日
                jsdOtherSys.setJjrxx(0);
            } else if ("01".equals(binary38.substring(4,6))) {
                jsdOtherSys.setJjrxx(1);
            }else {
                jsdOtherSys.setJjrxx(2);
            }

            String binary39 = BytesUtil.hexToBinary(dataList1.get(39));
            if("0".equals(binary39.charAt(0)+"")){  //黄慢平闪
                jsdOtherSys.setHmms(0);
            }else {
                jsdOtherSys.setHmms(1);
            }
            if("0".equals(binary39.charAt(1)+"")){  //工作时间
                jsdOtherSys.setGzsj(0);
            }else {
                jsdOtherSys.setGzsj(1);
            }
            if("0".equals(binary39.charAt(2)+"")){  //主动上报
                jsdOtherSys.setZdsb(0);
            }else {
                jsdOtherSys.setZdsb(1);
            }
            if("0".equals(binary39.charAt(3)+"")){  //gps
                jsdOtherSys.setGps(0);
            }else {
                jsdOtherSys.setGps(1);
            }
            jsdOtherSys.setStartHour1(Integer.parseInt(dataList1.get(58),16) + "");
            jsdOtherSys.setStartMin1(Integer.parseInt(dataList1.get(59),16) + "");
            jsdOtherSys.setEndHour1(Integer.parseInt(dataList1.get(60),16) + "");
            jsdOtherSys.setEndMin1(Integer.parseInt(dataList1.get(61),16) + "");
            jsdOtherSys.setStartHour2(Integer.parseInt(dataList1.get(62),16) + "");
            jsdOtherSys.setStartMin2(Integer.parseInt(dataList1.get(63),16) + "");
            jsdOtherSys.setEndHour2(Integer.parseInt(dataList1.get(64),16) + "");
            jsdOtherSys.setEndMin2(Integer.parseInt(dataList1.get(65),16) + "");

            jsdOtherSys.setYjyyyl(Integer.parseInt(dataList1.get(54).substring(0,1),16));
            jsdOtherSys.setBtyyyl(Integer.parseInt(dataList1.get(54).substring(1),16));
            jsdSys.setJsdOtherSys(jsdOtherSys);

            jsdSys.setParameter(parameters);


            ObjectMapper objectMapper = new ObjectMapper();
            try {
                result = objectMapper.writeValueAsString(jsdSys);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

        } else if (type == 3) {

        } else if (type == 4) {

        }
        return result;
    }

    /**
     * 测速设备参数合法性校验并组装
     * @return
     */
    public static ResultQuery checkParam0(SpeedSys speedSys){
        ResultQuery resultQuery = new ResultQuery();
        if(speedSys == null || speedSys.getParameter() == null || Objects.equals(speedSys.getParameter(), "")){
            resultQuery.setType(-1);
            resultQuery.setMessage("传入机内参数为空!");
            return resultQuery;
        }
        List<List<String>> splitStringToMySys = splitStringToMySys(speedSys.getParameter());
        List<String> dataList1 = splitStringToMySys.get(0);
        List<String> dataList2 = splitStringToMySys.get(1);
        List<String> dataList3 = splitStringToMySys.get(2);
        List<String> dataList4 = splitStringToMySys.get(3);

        SpeedWebNetSys speedWebNetSys = speedSys.getSpeedWebNetSys();
        String target_ip = speedWebNetSys.getMbIP();
        List<String> trueIPList = BytesUtil.getTrueIPList(target_ip);
        if (trueIPList == null){
            resultQuery.setType(-1);
            resultQuery.setMessage("网络及通讯参数 --> [目标IP] 错误!");
            return resultQuery;
        }
        String target_port = String.valueOf(speedWebNetSys.getMbPort());
        List<String> truePort = BytesUtil.getTruePort(target_port);
        if(truePort == null){
            resultQuery.setType(-1);
            resultQuery.setMessage("网络及通讯参数 --> [目标端口] 错误!");
            return resultQuery;
        }
        String ljjgStr = String.valueOf(speedWebNetSys.getLjjg());
        List<String> ljjg = BytesUtil.getTruePort(ljjgStr);
        if(ljjg == null){
            resultQuery.setType(-1);
            resultQuery.setMessage("网络及通讯参数 --> [联机间隔] 错误!");
            return resultQuery;
        }
        String debug_ip = speedWebNetSys.getTsmbIP();
        List<String> trueDebugIPList = BytesUtil.getTrueIPList(debug_ip);
        if(trueDebugIPList == null){
            resultQuery.setType(-1);
            resultQuery.setMessage("网络及通讯参数 --> [调试目标IP] 错误!");
            return resultQuery;
        }
        String debug_port = String.valueOf(speedWebNetSys.getTsmbPort());
        List<String> trueDebugPort = BytesUtil.getTruePort(debug_port);
        if(trueDebugPort == null){
            resultQuery.setType(-1);
            resultQuery.setMessage("网络及通讯参数 --> [调试端口] 错误!");
            return resultQuery;
        }
        String time_ip = speedWebNetSys.getTimeIP();
        List<String> trueTimeIPList = BytesUtil.getTrueIPList(time_ip);
        if(trueTimeIPList == null){
            resultQuery.setType(-1);
            resultQuery.setMessage("网络及通讯参数 --> [时间服务器IP] 错误!");
            return resultQuery;
        }
        String time_port = String.valueOf(speedWebNetSys.getTimePort());
        List<String> trueTimePort = BytesUtil.getTruePort(time_port);
        if(trueTimePort == null){
            resultQuery.setType(-1);
            resultQuery.setMessage("网络及通讯参数 --> [时间服务器端口] 错误!");
            return resultQuery;
        }
        String device_address = String.valueOf(speedWebNetSys.getNum());
        List<String> trueDeviceAddress = BytesUtil.getTruePort(device_address);
        if(trueDeviceAddress == null){
            resultQuery.setType(-1);
            resultQuery.setMessage("网络及通讯参数 --> [物联网编号] 错误!");
            return resultQuery;
        }
        String sq = speedWebNetSys.getSq();
        if("".equals(sq) || "null".equals(sq)){
            resultQuery.setType(-1);
            resultQuery.setMessage("网络及通讯参数 --> [时区] 不能为空!");
            return resultQuery;
        }
        int sqInt = Integer.parseInt(sq);
        if(sqInt < 0 || sqInt>13){
            resultQuery.setType(-1);
            resultQuery.setMessage("网络及通讯参数 --> [时区] 范围[0,13]");
            return resultQuery;
        }
        //-------------------网络参数校验结束--------------------------------------

        SpeedAndTJSys speedAndTJSys = speedSys.getSpeedAndTJSys();
        String yxsd_timeStr = speedAndTJSys.getYxsdsj();
        if("".equals(yxsd_timeStr) || "null".equals(yxsd_timeStr)){
            resultQuery.setType(-1);
            resultQuery.setMessage("速度及统计参数 --> [有效速度时间] 不能为空");
            return resultQuery;
        }
        double yxsd_timeDouble;
        try{
            yxsd_timeDouble = Double.parseDouble(yxsd_timeStr);
        }catch (Exception e){
            resultQuery.setType(-1);
            resultQuery.setMessage("速度及统计参数 --> [有效速度时间] 错误");
            return resultQuery;
        }
        if(yxsd_timeDouble<0.0 || yxsd_timeDouble>25.5){
            resultQuery.setType(-1);
            resultQuery.setMessage("速度及统计参数 --> [有效速度时间] 范围 0-25.5");
            return resultQuery;
        }
        String yxsd_timeHex = BytesUtil.decToHex(((int)(yxsd_timeDouble*10))+"");   // 有效速度保持时间

        /*-------------------------------------------*/
        String highest_speedStr = speedAndTJSys.getCsyz();
        if("".equals(highest_speedStr) || "null".equals(highest_speedStr)){
            resultQuery.setType(-1);
            resultQuery.setMessage("速度及统计参数 --> [超速阀值] 不能为空");
            return resultQuery;
        }
        int highest_speed = Integer.parseInt(highest_speedStr);
        if(highest_speed<0 || highest_speed>255){
            resultQuery.setType(-1);
            resultQuery.setMessage("速度及统计参数 --> [超速阀值] 范围 0-255");
            return resultQuery;
        }
        String highest_speedHex = BytesUtil.decToHex(highest_speed+"");
        /*---------------------------------------------*/
        String high_speed_cycle1Str = speedAndTJSys.getCsyzZQ1();
        if("".equals(high_speed_cycle1Str) || "null".equals(high_speed_cycle1Str)){
            resultQuery.setType(-1);
            resultQuery.setMessage("速度及统计参数 --> [周期一超速阀值] 不能为空");
            return resultQuery;
        }
        int high_speed_cycle1 = Integer.parseInt(high_speed_cycle1Str);
        if(high_speed_cycle1<0 || high_speed_cycle1>255){
            resultQuery.setType(-1);
            resultQuery.setMessage("速度及统计参数 --> [周期一超速阀值] 范围 0-255");
            return resultQuery;
        }
        String high_speed_cycle1Hex = BytesUtil.decToHex(high_speed_cycle1+"");
        /*------------------------------------------*/
        String low_speed_cycle1Str = speedAndTJSys.getDsyzZQ1();
        if("".equals(low_speed_cycle1Str) || "null".equals(low_speed_cycle1Str)){
            resultQuery.setType(-1);
            resultQuery.setMessage("速度及统计参数 --> [周期一低速阀值] 不能为空");
            return resultQuery;
        }
        int low_speed_cycle1 = Integer.parseInt(low_speed_cycle1Str);
        if(low_speed_cycle1<0 || low_speed_cycle1>255){
            resultQuery.setType(-1);
            resultQuery.setMessage("速度及统计参数 --> [周期一低速阀值] 范围 0-255");
            return resultQuery;
        }
        String low_speed_cycle1Hex = BytesUtil.decToHex(low_speed_cycle1+"");

        /*----------------------------------------------*/
        String car_num_cycle1Str = speedAndTJSys.getZdclsZQ1();
        if("".equals(car_num_cycle1Str) || "null".equals(car_num_cycle1Str)){
            resultQuery.setType(-1);
            resultQuery.setMessage("速度及统计参数 --> [周期一最低车辆] 不能为空");
            return resultQuery;
        }
        int car_num_cycle1 = Integer.parseInt(car_num_cycle1Str);
        if(car_num_cycle1<0 || car_num_cycle1>255){
            resultQuery.setType(-1);
            resultQuery.setMessage("速度及统计参数 --> [周期一最低车辆] 范围 0-255");
            return resultQuery;
        }
        String car_num_cycle1Hex = BytesUtil.decToHex(car_num_cycle1+"");
        /*----------------------------------------------*/
        String car_num_cycle2Str = speedAndTJSys.getZdclsZQ2();
        if("".equals(car_num_cycle2Str) || "null".equals(car_num_cycle2Str)){
            resultQuery.setType(-1);
            resultQuery.setMessage("速度及统计参数 --> [周期二最低车辆] 不能为空");
            return resultQuery;
        }
        int car_num_cycle2 = Integer.parseInt(car_num_cycle2Str);
        if(car_num_cycle2<0 || car_num_cycle2>255){
            resultQuery.setType(-1);
            resultQuery.setMessage("速度及统计参数 --> [周期二最低车辆] 范围 0-255");
            return resultQuery;
        }
        String car_num_cycle2Hex = BytesUtil.decToHex(car_num_cycle2+"");

        String lowest_speedStr = speedAndTJSys.getYxsdyz();
        if("".equals(lowest_speedStr) || "null".equals(lowest_speedStr)){
            resultQuery.setType(-1);
            resultQuery.setMessage("速度及统计参数 --> [有效速度阀值] 不能为空");
            return resultQuery;
        }
        int lowest_speed = Integer.parseInt(lowest_speedStr);
        if(lowest_speed<0 || lowest_speed>15){
            resultQuery.setType(-1);
            resultQuery.setMessage("速度及统计参数 --> [有效速度阀值] 范围 0-15");
            return resultQuery;
        }
        String lowest_speedHex = BytesUtil.decTo1Hex(lowest_speed+"");
        //-----------------速度及统计周期数据校验结束------------------------------------------------------

        SpeedPowerSys speedPowerSys = speedSys.getSpeedPowerSys();
        String xdcdymdzStr = speedPowerSys.getXdcdymdz();
        List<String> xdcdymdz = BytesUtil.getTruePort(xdcdymdzStr);
        if(xdcdymdz == null){
            resultQuery.setType(-1);
            resultQuery.setMessage("电源参数 --> [蓄电池电压满度值] 错误");
            return resultQuery;
        }

        String tyndymdzStr = speedPowerSys.getTyndymdz();
        List<String> tyndymdz = BytesUtil.getTruePort(tyndymdzStr);
        if(tyndymdz == null){
            resultQuery.setType(-1);
            resultQuery.setMessage("电源参数 --> [太阳能电压满度值] 错误");
            return resultQuery;
        }
        //------------------------电源参数校验结束----------------------------------

        SpeedVoiceSys speedVoiceSys = speedSys.getSpeedVoiceSys();
        //--------------主机-------提示-------------
        String zjyjcdStr = speedVoiceSys.getTsyjcd0();
        if("".equals(zjyjcdStr) || "null".equals(zjyjcdStr)){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [主机提示语句长度] 不能为空");
            return resultQuery;
        }
        int zjyjcd = Integer.parseInt(zjyjcdStr);
        if(zjyjcd<0 || zjyjcd>15){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [主机提示语句长度] 范围 0-15");
            return resultQuery;
        }
        String zjyjcdHex = BytesUtil.decTo1Hex(zjyjcd+"");

        //--------------主机--------警告------------
        String zjjgcdStr = speedVoiceSys.getJgyjcd0();
        if("".equals(zjjgcdStr) || "null".equals(zjjgcdStr)){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [主机警告语句长度] 不能为空");
            return resultQuery;
        }
        int zjjgcd = Integer.parseInt(zjjgcdStr);
        if(zjjgcd<0 || zjjgcd>15){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [主机警告语句长度] 范围 0-15");
            return resultQuery;
        }
        String zjjgcdHex = BytesUtil.decTo1Hex(zjjgcd+"");

        //--------------一号分机------提示-----------
        String yjcd1Str = speedVoiceSys.getTsyjcd1();
        if("".equals(yjcd1Str) || "null".equals(yjcd1Str)){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [一号分机提示语句长度] 不能为空");
            return resultQuery;
        }
        int yjcd1 = Integer.parseInt(yjcd1Str);
        if(yjcd1<0 || yjcd1>15){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [一号分机提示语句长度] 范围 0-15");
            return resultQuery;
        }
        String yjcd1Hex = BytesUtil.decTo1Hex(yjcd1+"");

        //-------------一号分机-------警告-----------
        String jgcd1Str = speedVoiceSys.getJgyjcd1();
        if("".equals(jgcd1Str) || "null".equals(jgcd1Str)){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [一号分机警告语句长度] 不能为空");
            return resultQuery;
        }
        int jgcd1 = Integer.parseInt(jgcd1Str);
        if(jgcd1<0 || jgcd1>15){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [一号分机警告语句长度] 范围 0-15");
            return resultQuery;
        }
        String jgcd1Hex = BytesUtil.decTo1Hex(jgcd1+"");

        //-------------二号分级--------提示----------
        String yjcd2Str = speedVoiceSys.getTsyjcd2();
        if("".equals(yjcd2Str) || "null".equals(yjcd2Str)){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [二号分机提示语句长度] 不能为空");
            return resultQuery;
        }
        int yjcd2 = Integer.parseInt(yjcd2Str);
        if(yjcd2<0 || yjcd2>15){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [二号分机提示语句长度] 范围 0-15");
            return resultQuery;
        }
        String yjcd2Hex = BytesUtil.decTo1Hex(yjcd2+"");

        //------------二号分级--------警告-----------
        String jgcd2Str = speedVoiceSys.getJgyjcd2();
        if("".equals(jgcd2Str) || "null".equals(jgcd2Str)){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [二号分机警告语句长度] 不能为空");
            return resultQuery;
        }
        int jgcd2 = Integer.parseInt(jgcd2Str);
        if(jgcd2<0 || jgcd2>15){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [二号分机警告语句长度] 范围 0-15");
            return resultQuery;
        }
        String jgcd2Hex = BytesUtil.decTo1Hex(jgcd2+"");

        //-----------三号分级--------提示----------
        String yjcd3Str = speedVoiceSys.getTsyjcd3();
        if("".equals(yjcd3Str) || "null".equals(yjcd3Str)){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [三号分机提示语句长度] 不能为空");
            return resultQuery;
        }
        int yjcd3 = Integer.parseInt(yjcd3Str);
        if(yjcd3<0 || yjcd3>15){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [三号分机提示语句长度] 范围 0-15");
            return resultQuery;
        }
        String yjcd3Hex = BytesUtil.decTo1Hex(yjcd3+"");

        //------------三号分级--------警告---------
        String jgcd3Str = speedVoiceSys.getJgyjcd3();
        if("".equals(jgcd3Str) || "null".equals(jgcd3Str)){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [三号分机警告语句长度] 不能为空");
            return resultQuery;
        }
        int jgcd3 = Integer.parseInt(jgcd3Str);
        if(jgcd3<0 || jgcd3>15){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [三号分机警告语句长度] 范围 0-15");
            return resultQuery;
        }
        String jgcd3Hex = BytesUtil.decTo1Hex(jgcd3+"");

        //-------时段1---------------
        String start_hour1Str = speedVoiceSys.getJnsd1HourStart();
        if("".equals(start_hour1Str) || "null".equals(start_hour1Str)){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [时段一起始时] 不能为空");
            return resultQuery;
        }
        int start_hour1 = Integer.parseInt(start_hour1Str);
        if(start_hour1<0 || start_hour1>24){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [时段一起始时] 范围 0-23");
            return resultQuery;
        }
        String start_hour1Hex = BytesUtil.decToHex(start_hour1 + "");

        String start_min1Str = speedVoiceSys.getJnsd1MinStart();
        if("".equals(start_min1Str) || "null".equals(start_min1Str)){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [时段一起始分] 不能为空");
            return resultQuery;
        }
        int start_min1 = Integer.parseInt(start_min1Str);
        if(start_min1<0 || start_min1>59){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [时段一起始分] 范围 0-59");
            return resultQuery;
        }
        String start_min1Hex = BytesUtil.decToHex(start_min1 + "");

        String end_hour1Str = speedVoiceSys.getJnsd1HourEnd();
        if("".equals(end_hour1Str) || "null".equals(end_hour1Str)){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [时段一结束时] 不能为空");
            return resultQuery;
        }
        int end_hour1 = Integer.parseInt(end_hour1Str);
        if(end_hour1<0 || end_hour1>23){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [时段一结束时] 范围 0-23");
            return resultQuery;
        }
        String end_hour1Hex = BytesUtil.decToHex(end_hour1 + "");

        String end_min1Str = speedVoiceSys.getJnsd1MinEnd();
        if("".equals(end_min1Str) || "null".equals(end_min1Str)){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [时段一结束分] 不能为空");
            return resultQuery;
        }
        int end_min1 = Integer.parseInt(end_min1Str);
        if(end_min1<0 || end_min1>59){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [时段一结束分] 范围 0-59");
            return resultQuery;
        }
        String end_min1Hex = BytesUtil.decToHex(end_min1 + "");

        //-----------时段二--------------
        String start_hour2Str = speedVoiceSys.getJnsd2HourStart();
        if("".equals(start_hour2Str) || "null".equals(start_hour2Str)){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [时段二起始时] 不能为空");
            return resultQuery;
        }
        int start_hour2 = Integer.parseInt(start_hour2Str);
        if(start_hour2<0 || start_hour2>24){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [时段二起始时] 范围 0-23");
            return resultQuery;
        }
        String start_hour2Hex = BytesUtil.decToHex(start_hour2 + "");

        String start_min2Str = speedVoiceSys.getJnsd2MinStart();
        if("".equals(start_min2Str) || "null".equals(start_min2Str)){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [时段二起始分] 不能为空");
            return resultQuery;
        }
        int start_min2 = Integer.parseInt(start_min2Str);
        if(start_min2<0 || start_min2>59){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [时段二起始分] 范围 0-59");
            return resultQuery;
        }
        String start_min2Hex = BytesUtil.decToHex(start_min2 + "");

        String end_hour2Str = speedVoiceSys.getJnsd2HourEnd();
        if("".equals(end_hour2Str) || "null".equals(end_hour2Str)){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [时段二结束时] 不能为空");
            return resultQuery;
        }
        int end_hour2 = Integer.parseInt(end_hour2Str);
        if(end_hour2<0 || end_hour2>23){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [时段二结束时] 范围 0-23");
            return resultQuery;
        }
        String end_hour2Hex = BytesUtil.decToHex(end_hour2 + "");

        String end_min2Str = speedVoiceSys.getJnsd2MinEnd();
        if("".equals(end_min2Str) || "null".equals(end_min2Str)){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [时段二结束分] 不能为空");
            return resultQuery;
        }
        int end_min2 = Integer.parseInt(end_min2Str);
        if(end_min2<0 || end_min2>59){
            resultQuery.setType(-1);
            resultQuery.setMessage("语音参数 --> [时段二结束分] 范围 0-59");
            return resultQuery;
        }
        //-------------------------------语音参数校验完毕-----------------------------------

        SpeedPoseSys speedPoseSys = speedSys.getSpeedPoseSys();
        String qxjdfz = speedPoseSys.getQxjyz();
        if("".equals(qxjdfz) || "null".equals(qxjdfz)){
            resultQuery.setType(-1);
            resultQuery.setMessage("姿态参数 --> [倾斜角度阈值] 不能为空");
            return resultQuery;
        }
        int sqInt_4 = Integer.parseInt(qxjdfz);
        if(sqInt_4 < 0 || sqInt_4>90){
            resultQuery.setType(-1);
            resultQuery.setMessage("姿态参数 --> [倾斜角度阈值] 范围 0-90");
            return resultQuery;
        }
        //-----------------------------姿态参数校验完毕-------------------------------

        List<SpeedDisPlayFA> disPlayFAList = speedSys.getDisPlayFAList();
        List<String> nameList = Arrays.asList("主机正常显示方案","主机超速选择方案","一号分机正常显示方案","一号分机超速显示方案","二号分机正常显示方案","二号分机超速显示方案","三号分机正常显示方案","三号分机超速显示方案");
        List<String> time_stop1HexList = new ArrayList<>();
        List<String> time_stop2HexList = new ArrayList<>();
        for (int i = 0; i < disPlayFAList.size(); i++) {
            SpeedDisPlayFA speedDisPlayFA = disPlayFAList.get(i);
            String time_stop1 = speedDisPlayFA.getTlsj1();
            if("".equals(time_stop1) || "null".equals(time_stop1)){
                resultQuery.setType(-1);
                resultQuery.setMessage("语音参数 "+nameList.get(i)+" --> [显示一停留时间] 不能为空");
                return resultQuery;
            }
            int time_stop1Int = Integer.parseInt(time_stop1);
            if(time_stop1Int < 1 || time_stop1Int>255){
                resultQuery.setType(-1);
                resultQuery.setMessage("语音参数 "+nameList.get(i)+" --> [显示一停留时间] 范围 [1-255]");
                return resultQuery;
            }

            String time_stop1Hex = BytesUtil.decToHex(time_stop1Int + "");
            time_stop1HexList.add(time_stop1Hex);

            String time_stop2 = speedDisPlayFA.getTlsj2();
            if("".equals(time_stop2) || "null".equals(time_stop2)){
                resultQuery.setType(-1);
                resultQuery.setMessage("语音参数 "+nameList.get(i)+" --> [显示二停留时间] 不能为空");
                return resultQuery;
            }
            int time_stop2Int = Integer.parseInt(time_stop2);
            if(time_stop2Int < 0 || time_stop2Int>255){
                resultQuery.setType(-1);
                resultQuery.setMessage("语音参数 "+nameList.get(i)+" --> [显示二停留时间] 范围 [0-255]");
                return resultQuery;
            }
            String time_stop2Hex = BytesUtil.decToHex(time_stop2Int + "");
            time_stop2HexList.add(time_stop2Hex);
        }


        //到此合法性校验结束 开始数据组装----------------------------------------------------------------------

        //时区
        String sqHex = BytesUtil.decTo1Hex(sqInt + "");
        int selectedIndex = speedWebNetSys.getYdyys();     //运营商
        String data10Binary = Integer.parseInt(dataList1.get(10),16) >= 128 ? "1":"0";
        if (selectedIndex == 0) {
            data10Binary = data10Binary+"00";
        } else if (selectedIndex == 1) {
            data10Binary = data10Binary+"01";
        } else if (selectedIndex == 2) {
            data10Binary = data10Binary+"10";
        }else {
            data10Binary = data10Binary+"11";
        }
        int m_spinner_dxSelectedIndex = speedWebNetSys.getDxbp();
        if(m_spinner_dxSelectedIndex == 0){
            data10Binary = data10Binary + "0";
        }else {
            data10Binary = data10Binary + "1";
        }
        String data10Hex = BytesUtil.binary4ToHex(data10Binary) + sqHex;
        dataList1.set(10,data10Hex);
        dataList1.set(11,trueIPList.get(0));     //目标ip
        dataList1.set(12,trueIPList.get(1));
        dataList1.set(13,trueIPList.get(2));
        dataList1.set(14,trueIPList.get(3));

        dataList1.set(15,truePort.get(0));       //目标port
        dataList1.set(16,truePort.get(1));

        StringBuilder sb17 = new StringBuilder(BytesUtil.hexToBinary(dataList1.get(17)));

        int spinner_zcjSelectedIndex = speedWebNetSys.getXtfjsx();
        if(spinner_zcjSelectedIndex == 0){
            sb17.setCharAt(0,'1');
        }else {
            sb17.setCharAt(0,'0');
            if(spinner_zcjSelectedIndex == 1){
                sb17.setCharAt(2,'0');
                sb17.setCharAt(3,'0');
            }else if(spinner_zcjSelectedIndex == 2){
                sb17.setCharAt(2,'0');
                sb17.setCharAt(3,'1');
            }else {
                sb17.setCharAt(2,'1');
                sb17.setCharAt(3,'0');
            }
        }
        int spinner_connect_typeSelectedIndex = speedWebNetSys.getLjfs();
        if(spinner_connect_typeSelectedIndex == 0){
            sb17.setCharAt(7,'0');
        }else {
            sb17.setCharAt(7,'1');
        }
        dataList1.set(17,BytesUtil.binaryToHex(sb17.toString()));   //网络联机参数

        dataList1.set(18,ljjg.get(0));       //联机间隔
        dataList1.set(19,ljjg.get(1));

        dataList1.set(20,trueDebugIPList.get(0));        //调试ip
        dataList1.set(21,trueDebugIPList.get(1));
        dataList1.set(22,trueDebugIPList.get(2));
        dataList1.set(23,trueDebugIPList.get(3));

        dataList1.set(24,trueDebugPort.get(0));      //调试port
        dataList1.set(25,trueDebugPort.get(1));

        dataList1.set(26,trueTimeIPList.get(0));     //时间ip
        dataList1.set(27,trueTimeIPList.get(1));
        dataList1.set(28,trueTimeIPList.get(2));
        dataList1.set(29,trueTimeIPList.get(3));

        dataList1.set(30,trueTimePort.get(0));       //时间port
        dataList1.set(31,trueTimePort.get(1));

        dataList1.set(32,trueDeviceAddress.get(0));
        dataList1.set(33,trueDeviceAddress.get(1));

        StringBuilder sb38 = new StringBuilder(BytesUtil.hexToBinary(dataList1.get(38)));
        if(speedWebNetSys.isFlag1()){
            sb38.setCharAt(4,'1');
        }else {
            sb38.setCharAt(4,'0');
        }
        if(speedWebNetSys.isFlag2()){
            sb38.setCharAt(2,'0');
        }else {
            sb38.setCharAt(2,'1');
        }
        dataList1.set(38,BytesUtil.binaryToHex(sb38.toString()));   //特殊标志

        int spinner_wxxdSelectedIndex = speedWebNetSys.getWxxd();
        dataList1.set(40,BytesUtil.decToHex((spinner_wxxdSelectedIndex+1)+""));     //lora信道

        int spinner_wxslSelectedIndex = speedWebNetSys.getWxsl();
        dataList1.set(41,BytesUtil.decToHex((spinner_wxslSelectedIndex+1)+""));     //lora速率

        //------------------网络及通讯参数数据组装结束---------------------------------------

        StringBuilder sb38_1 = new StringBuilder(BytesUtil.hexToBinary(dataList1.get(38)));
        if(speedAndTJSys.isCswx()){
            sb38_1.setCharAt(5,'1');
        }else {
            sb38_1.setCharAt(5,'0');
        }
        dataList1.set(38,BytesUtil.binaryToHex(sb38_1.toString()));

        dataList1.set(39,yxsd_timeHex); //有效速度保持时间

        String bin44 = "";

        int selectedIndex_1 = speedAndTJSys.getSdxx();        //测速标志
        if(selectedIndex_1 == 0){
            bin44 = bin44+ "00";
        }else if(selectedIndex_1 == 1){
            bin44 = bin44 + "01";
        }else {
            bin44 = bin44 + "10";
        }
        int selectedIndex1_1 = speedAndTJSys.getLdtxxy();
        if(selectedIndex1_1 == 0){
            bin44 = bin44 + "00";
        } else if (selectedIndex1_1 == 1) {
            bin44 = bin44 + "01";
        } else if (selectedIndex1_1 == 2) {
            bin44 = bin44 + "10";
        } else if (selectedIndex1_1 == 3) {
            bin44 = bin44 + "11";
        }

        dataList1.set(44,BytesUtil.binary4ToHex(bin44)+lowest_speedHex);

        dataList1.set(45,highest_speedHex);
        dataList1.set(46,high_speed_cycle1Hex);
        dataList1.set(47,low_speed_cycle1Hex);

        if(speedAndTJSys.getTjzqZQ1()==0){
            dataList1.set(48,"00");
        }else if(speedAndTJSys.getTjzqZQ1()==1){
            dataList1.set(48,"01");
        }else if(speedAndTJSys.getTjzqZQ1()==2){
            dataList1.set(48,"02");
        }else if(speedAndTJSys.getTjzqZQ1()==3){
            dataList1.set(48,"03");
        }else if(speedAndTJSys.getTjzqZQ1()==4){
            dataList1.set(48,"04");
        }else if(speedAndTJSys.getTjzqZQ1()==5){
            dataList1.set(48,"05");
        }else if(speedAndTJSys.getTjzqZQ1()==6){
            dataList1.set(48,"06");
        }else if(speedAndTJSys.getTjzqZQ1()==7){
            dataList1.set(48,"07");
        }

        dataList1.set(49,car_num_cycle1Hex);

        if(speedAndTJSys.getTjzqZQ2()==0){
            dataList1.set(50,"00");
        }else if(speedAndTJSys.getTjzqZQ2()==1){
            dataList1.set(50,"01");
        }else if(speedAndTJSys.getTjzqZQ2()==2){
            dataList1.set(50,"02");
        }else if(speedAndTJSys.getTjzqZQ2()==3){
            dataList1.set(50,"03");
        }else if(speedAndTJSys.getTjzqZQ2()==4){
            dataList1.set(50,"04");
        }else if(speedAndTJSys.getTjzqZQ2()==5){
            dataList1.set(50,"05");
        }else if(speedAndTJSys.getTjzqZQ2()==6){
            dataList1.set(50,"06");
        }else if(speedAndTJSys.getTjzqZQ2()==7){
            dataList1.set(50,"07");
        }

        dataList1.set(51,car_num_cycle2Hex);
        //----------------------速度及统计周期数据组装完毕--------------------------

        dataList1.set(34,xdcdymdz.get(0));
        dataList1.set(35,xdcdymdz.get(1));
        dataList1.set(36,tyndymdz.get(0));
        dataList1.set(37,tyndymdz.get(1));

        StringBuilder sb38_2 = new StringBuilder(BytesUtil.hexToBinary(dataList1.get(38)));
        //String str38 = speedPowerSys.getStr38();
        if(speedPowerSys.getDylx() == 0){
            //str38 = str38 + "00";
            sb38_2.setCharAt(6,'0');
            sb38_2.setCharAt(7,'0');
        }else if(speedPowerSys.getDylx() == 1){
            //str38 = str38 + "01";
            sb38_2.setCharAt(6,'0');
            sb38_2.setCharAt(7,'1');
        }else if(speedPowerSys.getDylx() == 2){
            //str38 = str38 + "10";
            sb38_2.setCharAt(6,'1');
            sb38_2.setCharAt(7,'0');
        }else {
            //str38 = str38 + "11";
            sb38_2.setCharAt(6,'1');
            sb38_2.setCharAt(7,'1');
        }
        if(speedPowerSys.getCdkz() == 0){
            //str38 = "0" + str38;
            sb38_2.setCharAt(0,'0');
        }else {
            //str38 = "1" + str38;
            sb38_2.setCharAt(0,'1');
        }
        dataList1.set(38,BytesUtil.binaryToHex(sb38_2.toString()));
        //------------------------------电源参数组装完毕-----------------------


        String end_min2Hex = BytesUtil.decToHex(end_min2 + "");
        StringBuilder sb38_3 = new StringBuilder(BytesUtil.hexToBinary(dataList1.get(38)));
        int spinner_genderSelectedIndex = speedVoiceSys.getYyxz();
        if(spinner_genderSelectedIndex == 0){
            sb38_3.setCharAt(1,'0');
        }else {
            sb38_3.setCharAt(1,'1');
        }
        int spinner_bfxzSelectedIndex = speedVoiceSys.getBfxz();
        if(spinner_bfxzSelectedIndex == 0){
            sb38_3.setCharAt(3,'0');
        }else {
            sb38_3.setCharAt(3,'1');
        }
        dataList1.set(38,BytesUtil.binaryToHex(sb38_3.toString()));

        dataList1.set(52,start_hour1Hex);
        dataList1.set(53,start_min1Hex);
        dataList1.set(54,end_hour1Hex);
        dataList1.set(55,end_min1Hex);

        dataList1.set(56,start_hour2Hex);
        dataList1.set(57,start_min2Hex);
        dataList1.set(58,end_hour2Hex);
        dataList1.set(59,end_min2Hex);

        dataList1.set(60,zjyjcdHex+BytesUtil.decTo1Hex(speedVoiceSys.getTsyj0()+""));
        dataList1.set(61,zjjgcdHex+BytesUtil.decTo1Hex(speedVoiceSys.getJgyj0()+""));

        dataList1.set(62,yjcd1Hex+BytesUtil.decTo1Hex(speedVoiceSys.getTsyj1()+""));
        dataList1.set(63,jgcd1Hex+BytesUtil.decTo1Hex(speedVoiceSys.getJgyj1()+""));

        dataList1.set(64,yjcd2Hex+BytesUtil.decTo1Hex(speedVoiceSys.getTsyj2()+""));
        dataList1.set(65,jgcd2Hex+BytesUtil.decTo1Hex(speedVoiceSys.getJgyj2()+""));

        dataList1.set(66,yjcd3Hex+BytesUtil.decTo1Hex(speedVoiceSys.getTsyj3()+""));
        dataList1.set(67,jgcd3Hex+BytesUtil.decTo1Hex(speedVoiceSys.getJgyj3()+""));

        dataList1.set(68,BytesUtil.decTo1Hex(speedVoiceSys.getYjyyyl()+"") + BytesUtil.decTo1Hex(speedVoiceSys.getBtyyyl()+""));
        //----------------------------------语音参数组装完毕--------------------------------------------------------

        //时区
        String str42 = speedPoseSys.getStr42();
        String qxjdfzHex = BytesUtil.decToHex(sqInt_4 + "");
        if(speedPoseSys.getFx() == 0){
            str42 = "0" + str42;
        }else {
            str42 = "1" + str42;
        }
        if(speedPoseSys.getLmd() == 0){
            str42 = str42 + "00";
        }else if(speedPoseSys.getLmd() == 1){
            str42 = str42 + "01";
        }else {
            str42 = str42 + "10";
        }

        if(speedPoseSys.getCzz() == 0){
            str42 = str42 + "00";
        }else if(speedPoseSys.getCzz() == 1){
            str42 = str42 + "01";
        }else {
            str42 = str42 + "10";
        }
        dataList1.set(42,BytesUtil.binaryToHex(str42));
        dataList1.set(43,qxjdfzHex);
        //-----------------------------------------姿态参数组装完毕---------------------------

        for (int i = 0; i < disPlayFAList.size(); i++) {
            SpeedDisPlayFA speedDisPlayFA = disPlayFAList.get(i);
            int n = 42 + (i * 4);

            String n1Binary = "";

            if(speedDisPlayFA.getXsys1() == 0){
                n1Binary = "00";
            }else if(speedDisPlayFA.getXsys1() == 1){
                n1Binary = "01";
            }else if(speedDisPlayFA.getXsys1() == 2){
                n1Binary = "10";
            }else {
                n1Binary = "11";
            }
            if(speedDisPlayFA.isCrjssd1()){
                n1Binary = n1Binary + "1" + (BytesUtil.hexToBinary(dataList2.get(n)).charAt(3) +"");
            }else {
                n1Binary = n1Binary + "0" + (BytesUtil.hexToBinary(dataList2.get(n)).charAt(3) +"");
            }
            String n1Hex = BytesUtil.binary4ToHex(n1Binary) + BytesUtil.decTo1Hex(speedDisPlayFA.getZxz1()+"");
            dataList2.set(n,n1Hex);
            dataList2.set(n+1,time_stop1HexList.get(i));

            if("00".equals(time_stop2HexList.get(i))){
                //只要有一个条件满足就判定不需要第二帧
                dataList2.set(n+2,"00");
                dataList2.set(n+3,"00");
            }else {
                String n3Binary = "";

                if(speedDisPlayFA.getXsys2() == 0){
                    n3Binary = "00";
                }else if(speedDisPlayFA.getXsys2() == 1){
                    n3Binary = "01";
                }else if(speedDisPlayFA.getXsys2() == 2){
                    n3Binary = "10";
                } else {
                    n3Binary = "11";
                }
                if(speedDisPlayFA.isCrjssd2()){
                    n3Binary = n3Binary + "1" + (BytesUtil.hexToBinary(dataList2.get(n+2)).charAt(3) +"");
                }else {
                    n3Binary = n3Binary + "0" + (BytesUtil.hexToBinary(dataList2.get(n+2)).charAt(3) +"");
                }
                String n3Hex = BytesUtil.binary4ToHex(n3Binary) + BytesUtil.decTo1Hex((speedDisPlayFA.getZxz2())+"");
                dataList2.set(n+2,n3Hex);
                dataList2.set(n+3,time_stop2HexList.get(i));
            }
        }

        //---------------------------显示方案组装完毕-----------------------------

        resultQuery.setType(1);
        //组装测速机内参数
        StringBuilder sb = new StringBuilder();
        if (dataList1.size() > 73 && dataList2.size() > 73 && dataList3.size() > 73 && dataList4.size() > 73) {
            for (int i = 0; i < 64; i++) {
                sb.append(dataList1.get(10 + i));
            }
            for (int i = 0; i < 64; i++) {
                sb.append(dataList2.get(10 + i));
            }
            for (int i = 0; i < 64; i++) {
                sb.append(dataList3.get(10 + i));
            }
            for (int i = 0; i < 64; i++) {
                sb.append(dataList4.get(10 + i));
            }
            String otherString = speedSys.getParameter().substring(512);
            sb.append(otherString);
        }
        resultQuery.setMessage(sb.toString());

        return resultQuery;
    }

    public static ResultQuery checkParam2(JSDSys jsdSys){
        ResultQuery resultQuery = new ResultQuery();
        if(jsdSys == null || jsdSys.getParameter() == null || Objects.equals(jsdSys.getParameter(), "")) {
            resultQuery.setType(-1);
            resultQuery.setMessage("传入机内参数为空!");
            return resultQuery;
        }
            List<List<String>> splitStringToJSDSys = splitStringToJSDSys(jsdSys.getParameter());
            List<String> dataList1 = splitStringToJSDSys.get(0);
            List<String> dataList2 = splitStringToJSDSys.get(1);
            List<String> dataList3 = splitStringToJSDSys.get(2);
            List<String> dataList4 = splitStringToJSDSys.get(3);
            List<String> dataList5 = splitStringToJSDSys.get(4);
            List<String> dataList6 = splitStringToJSDSys.get(5);
            List<String> dataList7 = splitStringToJSDSys.get(6);
            List<String> dataList8 = splitStringToJSDSys.get(7);
            List<String> dataList9 = splitStringToJSDSys.get(8);
            List<String> dataList10 = splitStringToJSDSys.get(9);
            List<String> dataList11 = splitStringToJSDSys.get(10);
            List<String> dataList12 = splitStringToJSDSys.get(11);
            List<String> dataList13 = splitStringToJSDSys.get(12);
            List<String> dataList14 = splitStringToJSDSys.get(13);
            List<String> dataList15 = splitStringToJSDSys.get(14);

            JSDWebNetSys jsdWebNetSys = jsdSys.getJsdWebNetSys();
            String target_ip = jsdWebNetSys.getMbIP();
            List<String> trueIPList = BytesUtil.getTrueIPList(target_ip);
            if (trueIPList == null) {
                resultQuery.setType(-1);
                resultQuery.setMessage("网络及通讯参数 --> [目标IP] 错误!");
                return resultQuery;
            }
            String target_port = jsdWebNetSys.getMbPort();
            List<String> truePort = BytesUtil.getTruePort(target_port);
            if (truePort == null) {
                resultQuery.setType(-1);
                resultQuery.setMessage("网络及通讯参数 --> [目标端口] 错误!");
                return resultQuery;
            }
            String ljjgStr = jsdWebNetSys.getLjjg();
            List<String> ljjg = BytesUtil.getTruePort(ljjgStr);
            if (ljjg == null) {
                resultQuery.setType(-1);
                resultQuery.setMessage("网络及通讯参数 --> [联机间隔] 错误!");
                return resultQuery;
            }
            String debug_ip = jsdWebNetSys.getTsmbIP();
            List<String> trueDebugIPList = BytesUtil.getTrueIPList(debug_ip);
            if (trueDebugIPList == null) {
                resultQuery.setType(-1);
                resultQuery.setMessage("网络及通讯参数 --> [调试目标IP] 错误!");
                return resultQuery;
            }
            String debug_port = jsdWebNetSys.getTsmbPort();
            List<String> trueDebugPort = BytesUtil.getTruePort(debug_port);
            if (trueDebugPort == null) {
                resultQuery.setType(-1);
                resultQuery.setMessage("网络及通讯参数 --> [调试端口] 错误!");
                return resultQuery;
            }
            String time_ip = jsdWebNetSys.getTimeIP();
            List<String> trueTimeIPList = BytesUtil.getTrueIPList(time_ip);
            if (trueTimeIPList == null) {
                resultQuery.setType(-1);
                resultQuery.setMessage("网络及通讯参数 --> [时间服务器IP] 错误!");
                return resultQuery;
            }
            String time_port = jsdWebNetSys.getTimePort();
            List<String> trueTimePort = BytesUtil.getTruePort(time_port);
            if (trueTimePort == null) {
                resultQuery.setType(-1);
                resultQuery.setMessage("网络及通讯参数 --> [时间服务器端口] 错误!");
                return resultQuery;
            }
            String device_address = jsdWebNetSys.getNum();
            List<String> trueDeviceAddress = BytesUtil.getTruePort(device_address);
            if (trueDeviceAddress == null) {
                resultQuery.setType(-1);
                resultQuery.setMessage("网络及通讯参数 --> [物联网编号] 错误!");
                return resultQuery;
            }
            String sq = jsdWebNetSys.getSq();
            if ("".equals(sq) || "null".equals(sq)) {
                resultQuery.setType(-1);
                resultQuery.setMessage("网络及通讯参数 --> [时区不能为空]");
                return resultQuery;
            }
            int sqInt = Integer.parseInt(sq);
            if (sqInt < 0 || sqInt > 13) {
                resultQuery.setType(-1);
                resultQuery.setMessage("网络及通讯参数 --> [时区] 范围 [0,13]");
                return resultQuery;
            }
            //时区
            String sqHex = BytesUtil.decTo1Hex(sqInt + "");

            //路段编号
            String ldbh = jsdWebNetSys.getLdbh();
            if ("".equals(ldbh) || "null".equals(ldbh)) {
                resultQuery.setType(-1);
                resultQuery.setMessage("网络及通讯参数 --> [路段编号不能为空] ");
                return resultQuery;
            }
            int ldbhInt = Integer.parseInt(ldbh);
            if (ldbhInt <= 0 || ldbhInt > 64) {
                resultQuery.setType(-1);
                resultQuery.setMessage("网络及通讯参数 --> [路段编号] 范围[1,64]");
                return resultQuery;
            }

            //-------------------网络参数校验结束--------------------------------------

            //-----------------------------其他参数校验开始---------------
            JSDOtherSys jsdOtherSys = jsdSys.getJsdOtherSys();
            String start_hour1Str = jsdOtherSys.getStartHour1();
            if ("".equals(start_hour1Str) || "null".equals(start_hour1Str)) {
                resultQuery.setType(-1);
                resultQuery.setMessage("其它参数 --> [时段一起始时不能为空] ");
                return resultQuery;
            }
            int start_hour1 = Integer.parseInt(start_hour1Str);
            if (start_hour1 < 0 || start_hour1 > 24) {
                resultQuery.setType(-1);
                resultQuery.setMessage("其它参数 --> [时段一起始时] 范围 0-23");
                return resultQuery;
            }
            String start_hour1Hex = BytesUtil.decToHex(start_hour1 + "");

            String start_min1Str = jsdOtherSys.getStartMin1();
            if ("".equals(start_min1Str) || "null".equals(start_min1Str)) {
                resultQuery.setType(-1);
                resultQuery.setMessage("其它参数 --> [时段一起始分不能为空] ");
                return resultQuery;
            }
            int start_min1 = Integer.parseInt(start_min1Str);
            if (start_min1 < 0 || start_min1 > 59) {
                resultQuery.setType(-1);
                resultQuery.setMessage("其它参数 --> [时段一起始分] 范围 0-59");
                return resultQuery;
            }
            String start_min1Hex = BytesUtil.decToHex(start_min1 + "");

            String end_hour1Str = jsdOtherSys.getEndHour1();
            if ("".equals(end_hour1Str) || "null".equals(end_hour1Str)) {
                resultQuery.setType(-1);
                resultQuery.setMessage("其它参数 --> [时段一结束时不能为空] ");
                return resultQuery;
            }
            int end_hour1 = Integer.parseInt(end_hour1Str);
            if (end_hour1 < 0 || end_hour1 > 23) {
                resultQuery.setType(-1);
                resultQuery.setMessage("其它参数 --> [时段一结束时] 范围 0-23");
                return resultQuery;
            }
            String end_hour1Hex = BytesUtil.decToHex(end_hour1 + "");

            String end_min1Str = jsdOtherSys.getEndMin1();
            if ("".equals(end_min1Str) || "null".equals(end_min1Str)) {
                resultQuery.setType(-1);
                resultQuery.setMessage("其它参数 --> [时段一结束分不能为空] ");
                return resultQuery;
            }
            int end_min1 = Integer.parseInt(end_min1Str);
            if (end_min1 < 0 || end_min1 > 59) {
                resultQuery.setType(-1);
                resultQuery.setMessage("其它参数 --> [时段一起始分] 范围 0-59");
                return resultQuery;
            }
            String end_min1Hex = BytesUtil.decToHex(end_min1 + "");

            //-----------时段二--------------
            String start_hour2Str = jsdOtherSys.getStartHour2();
            if ("".equals(start_hour2Str) || "null".equals(start_hour2Str)) {
                resultQuery.setType(-1);
                resultQuery.setMessage("其它参数 --> [时段二起始时不能为空] ");
                return resultQuery;
            }
            int start_hour2 = Integer.parseInt(start_hour2Str);
            if (start_hour2 < 0 || start_hour2 > 24) {
                resultQuery.setType(-1);
                resultQuery.setMessage("其它参数 --> [时段二起始时] 范围 0-23");
                return resultQuery;
            }
            String start_hour2Hex = BytesUtil.decToHex(start_hour2 + "");

            String start_min2Str = jsdOtherSys.getStartMin2();
            if ("".equals(start_min2Str) || "null".equals(start_min2Str)) {
                resultQuery.setType(-1);
                resultQuery.setMessage("其它参数 --> [时段二起始分不能为空] ");
                return resultQuery;
            }
            int start_min2 = Integer.parseInt(start_min2Str);
            if (start_min2 < 0 || start_min2 > 59) {
                resultQuery.setType(-1);
                resultQuery.setMessage("其它参数 --> [时段二起始分] 范围 0-59");
                return resultQuery;
            }
            String start_min2Hex = BytesUtil.decToHex(start_min2 + "");

            String end_hour2Str = jsdOtherSys.getEndHour2();
            if ("".equals(end_hour2Str) || "null".equals(end_hour2Str)) {
                resultQuery.setType(-1);
                resultQuery.setMessage("其它参数 --> [时段二结束时不能为空] ");
                return resultQuery;
            }
            int end_hour2 = Integer.parseInt(end_hour2Str);
            if (end_hour2 < 0 || end_hour2 > 23) {
                resultQuery.setType(-1);
                resultQuery.setMessage("其它参数 --> [时段二结束时] 范围 0-23");
                return resultQuery;
            }
            String end_hour2Hex = BytesUtil.decToHex(end_hour2 + "");

            String end_min2Str = jsdOtherSys.getEndMin2();
            if ("".equals(end_min2Str) || "null".equals(end_min2Str)) {
                resultQuery.setType(-1);
                resultQuery.setMessage("其它参数 --> [时段二结束分不能为空] ");
                return resultQuery;
            }
            int end_min2 = Integer.parseInt(end_min2Str);
            if (end_min2 < 0 || end_min2 > 59) {
                resultQuery.setType(-1);
                resultQuery.setMessage("其它参数 --> [时段二结束分] 范围 0-59");
                return resultQuery;
            }
            String end_min2Hex = BytesUtil.decToHex(end_min2 + "");

            //---------------------------------其它参数校验结束----------------------------------

            //---------------------------------姿态参数校验开始-------------------------------------
            JSDPoseSys jsdPoseSys = jsdSys.getJsdPoseSys();
            String qxjdfz = jsdPoseSys.getQxjyz();
            if ("".equals(qxjdfz) || "null".equals(qxjdfz)) {
                resultQuery.setType(-1);
                resultQuery.setMessage("姿态及传感器参数 --> [倾斜角度阈值不能为空] ");
                return resultQuery;
            }
            int qxjdfzInt = Integer.parseInt(qxjdfz);
            if (sqInt < 0 || sqInt > 90) {
                resultQuery.setType(-1);
                resultQuery.setMessage("姿态及传感器参数 --> [倾斜角度阈值] 范围 [0,90]");
                return resultQuery;
            }

            String jjjlfz = jsdPoseSys.getJjjlyz();
            if ("".equals(jjjlfz) || "null".equals(jjjlfz)) {
                resultQuery.setType(-1);
                resultQuery.setMessage("姿态及传感器参数 --> [接近距离阀值不能为空] ");
                return resultQuery;
            }
            double jjjlfzDouble = 0.0;
            try {
                jjjlfzDouble = Double.parseDouble(jjjlfz);
                if (jjjlfzDouble < 0 || jjjlfzDouble > 25.0) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("姿态及传感器参数 --> [接近距离阀值] 范围 [0,25]");
                    return resultQuery;
                }
            } catch (Exception e) {
                resultQuery.setType(-1);
                resultQuery.setMessage("姿态及传感器参数 --> [接近距离阀值] 范围 [0,25]");
                return resultQuery;
            }

            String yxbcsj = jsdPoseSys.getYxbcsj();
            if ("".equals(yxbcsj) || "null".equals(yxbcsj)) {
                resultQuery.setType(-1);
                resultQuery.setMessage("姿态及传感器参数 --> [有效保持时间不能为空] ");
                return resultQuery;
            }
            int yxbcsjInt = Integer.parseInt(yxbcsj);
            if (yxbcsjInt < 0 || yxbcsjInt > 255) {
                resultQuery.setType(-1);
                resultQuery.setMessage("姿态及传感器参数 --> [有效保持时间] 范围 [0,255]");
                return resultQuery;
            }

            String kgyxbcsj = jsdPoseSys.getKgyxbcsj();
            if ("".equals(kgyxbcsj) || "null".equals(kgyxbcsj)) {
                resultQuery.setType(-1);
                resultQuery.setMessage("姿态及传感器参数 --> [开关有效保持时间不能为空] ");
                return resultQuery;
            }
            int kgyxbcsjInt = Integer.parseInt(kgyxbcsj);
            if (kgyxbcsjInt < 0 || kgyxbcsjInt > 255) {
                resultQuery.setType(-1);
                resultQuery.setMessage("姿态及传感器参数 --> [开关有效保持时间] 范围 [0,255]");
                return resultQuery;
            }
            //------------------------姿态及传感器参数校验结束----------------------------

            //------------------------电源参数校验开始---------------------------------
            JSDPowerSys jsdPowerSys = jsdSys.getJsdPowerSys();
            List<JSDPowerDY> jsdPowerDYList = jsdPowerSys.getJsdPowerDYList();
            List<String> yjyzdyStrList = new ArrayList<>();
            List<String> qydyStrList = new ArrayList<>();
            List<String> mddyStrList = new ArrayList<>();
            List<String> gcdyStrList = new ArrayList<>();
            for (JSDPowerDY jsdPowerDY : jsdPowerDYList) {
                String yjyzdyStr = jsdPowerDY.getYjyzdy();
                if (!BytesUtil.isValidInput(yjyzdyStr)) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("电源管理参数 --> [夜间阀值电压不合法] ");
                    return resultQuery;
                }
                yjyzdyStrList.add(yjyzdyStr);
                String qydyStr = jsdPowerDY.getQydy();
                if (!BytesUtil.isValidInput(qydyStr)) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("电源管理参数 --> [欠压电压不合法] ");
                    return resultQuery;
                }
                qydyStrList.add(qydyStr);
                String mddyStr = jsdPowerDY.getMddy();
                if (!BytesUtil.isValidInput(mddyStr)) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("电源管理参数 --> [满电电压不合法] ");
                    return resultQuery;
                }
                mddyStrList.add(mddyStr);
                String gcdyStr = jsdPowerDY.getGcdy();
                if (!BytesUtil.isValidInput(gcdyStr)) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("电源管理参数 --> [过充电压不合法] ");
                    return resultQuery;
                }
                gcdyStrList.add(gcdyStr);
            }

            String xdcdymdzStr = jsdPowerSys.getXdcdymdz();
            List<String> xdcdymdz = BytesUtil.getTruePort(xdcdymdzStr);
            if (xdcdymdz == null) {
                resultQuery.setType(-1);
                resultQuery.setMessage("电源管理参数 --> [蓄电池电压满度值错误] ");
                return resultQuery;
            }

            String tyndymdzStr = jsdPowerSys.getTyndymdz();
            List<String> tyndymdz = BytesUtil.getTruePort(tyndymdzStr);
            if (tyndymdz == null) {
                resultQuery.setType(-1);
                resultQuery.setMessage("电源管理参数 --> [太阳能电压满度值错误] ");
                return resultQuery;
            }
            //----------------------------电源参数校验完毕--------------------------

            //------------------------------时段方案校验开始-------------------------
            List<JSDSDFASys> jsdsdfaSysList = jsdSys.getJsdsdfaSysList();
            //必须要有一行23.59
            boolean isHave2359 = false;
            List<Integer> qsjdList = new ArrayList<>();
            List<String> jdslStrList = new ArrayList<>();
            List<String> tbycStrList = new ArrayList<>();
            List<String> tbzqStrList = new ArrayList<>();
            List<String> bbjgStrList = new ArrayList<>();
            for (int i = 0; i < jsdsdfaSysList.size(); i++) {
                if ("".equals(jsdsdfaSysList.get(i).getEndHour())) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("工作日时段方案设置 --> [时段 " + (i + 1) + " 结束时不能为空] ");
                    return resultQuery;
                }
                if ("".equals(jsdsdfaSysList.get(i).getEndMin())) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("工作日时段方案设置 --> [时段 " + (i + 1) + " 结束分不能为空] ");
                    return resultQuery;
                }
                if ("23".equals(jsdsdfaSysList.get(i).getEndHour()) && "59".equals(jsdsdfaSysList.get(i).getEndMin())) {
                    isHave2359 = true;
                }

                String qsjdStr = jsdsdfaSysList.get(i).getQsjd();
                if ("".equals(qsjdStr)) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("工作日时段方案设置 --> [时段 " + (i + 1) + " 起始阶段不能为空] ");
                    return resultQuery;
                } else {
                    int qsjd = Integer.parseInt(qsjdStr, 10);

                    if (qsjd > 63 || qsjd == 0) {
                        resultQuery.setType(-1);
                        resultQuery.setMessage("工作日时段方案设置 --> [时段 " + (i + 1) + " 起始阶段] 范围 [1,63] ");
                        return resultQuery;
                    }
                }

                String jdslStr = jsdsdfaSysList.get(i).getJdsl();
                if ("".equals(jdslStr)) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("工作日时段方案设置 --> [时段 " + (i + 1) + " 阶段数量不能为空] ");
                    return resultQuery;
                } else {
                    int jdsl = Integer.parseInt(jdslStr, 10);
                    if (jdsl > 64 || jdsl == 0) {
                        resultQuery.setType(-1);
                        resultQuery.setMessage("工作日时段方案设置 --> [时段 " + (i + 1) + " 阶段数量] 范围 [1,64] ");
                        return resultQuery;
                    }
                }
                jdslStrList.add(jdslStr);

                int qsjd = Integer.parseInt(qsjdStr);
                int jdsl = Integer.parseInt(jdslStr);
                if ((qsjd + jdsl) > 64) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("工作日时段方案设置 --> [时段 " + (i + 1) + " 起始阶段和阶段数量之和不能大于64] ");
                    return resultQuery;
                } else {
                    qsjd = qsjd - 1;
                    qsjdList.add(qsjd);
                }

                String tbcsStr = jsdsdfaSysList.get(i).getTbyc();
                if ("".equals(tbcsStr)) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("工作日时段方案设置 --> [时段 " + (i + 1) + " 同步延迟不能为空] ");
                    return resultQuery;
                } else {
                    int tbcs = Integer.parseInt(tbcsStr, 10);
                    if (tbcs > 255) {
                        resultQuery.setType(-1);
                        resultQuery.setMessage("工作日时段方案设置 --> [时段 " + (i + 1) + " 同步延迟] 范围 [0,255]");
                        return resultQuery;
                    }
                }
                tbycStrList.add(tbcsStr);

                String tbzqStr = jsdsdfaSysList.get(i).getTbzq();
                if ("".equals(tbzqStr)) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("工作日时段方案设置 --> [时段 " + (i + 1) + " 同步周期不能为空] ");
                    return resultQuery;
                } else {
                    int tbzq = Integer.parseInt(tbzqStr, 10);
                    if (tbzq > 255) {
                        resultQuery.setType(-1);
                        resultQuery.setMessage("工作日时段方案设置 --> [时段 " + (i + 1) + " 同步周期] 范围 [0,255]");
                        return resultQuery;
                    }
                }
                tbzqStrList.add(tbzqStr);

                String tsjgStr = jsdsdfaSysList.get(i).getBbjg();
                if ("".equals(tsjgStr)) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("工作日时段方案设置 --> [时段 " + (i + 1) + " 播报间隔不能为空] ");
                    return resultQuery;
                } else {
                    int tsjg = Integer.parseInt(tsjgStr, 10);
                    if (tsjg > 25 || tsjg < 2) {
                        resultQuery.setType(-1);
                        resultQuery.setMessage("工作日时段方案设置 --> [时段 " + (i + 1) + " 播报间隔] 范围 [2,25]");
                        return resultQuery;
                    }
                }
                bbjgStrList.add(tsjgStr);

                if (i == 9) {
                    //还需校验最后一行的结束时，分是否合法
                    int endHourDec = Integer.parseInt(jsdsdfaSysList.get(i).getEndHour(), 10);
                    if (endHourDec > 23) {
                        resultQuery.setType(-1);
                        resultQuery.setMessage("工作日时段方案设置 --> [时段 " + (i + 1) + " 结束时不合法] 范围 [0,23]");
                        return resultQuery;
                    }
                    int endMinDec = Integer.parseInt(jsdsdfaSysList.get(i).getEndMin(), 10);
                    if (endMinDec > 59) {
                        resultQuery.setType(-1);
                        resultQuery.setMessage("工作日时段方案设置 --> [时段 " + (i + 1) + " 结束分不合法] 范围 [0,59]");
                        return resultQuery;
                    }
                }
            }

            if (isHave2359 == false) {
                resultQuery.setType(-1);
                resultQuery.setMessage("工作日时段方案设置 --> [必须要有结束时段 23:59] ");
                return resultQuery;
            }

            //必须要有一行23.59
            List<JSDSDFASys> jsdsdfaSysList1 = jsdSys.getJsdsdfaSysList1();
            boolean isHave2359_1 = false;
            List<Integer> qsjdList1 = new ArrayList<>();
            List<String> jdslStrList1 = new ArrayList<>();
            List<String> tbycStrList1 = new ArrayList<>();
            List<String> tbzqStrList1 = new ArrayList<>();
            List<String> bbjgStrList1 = new ArrayList<>();
            for (int i = 0; i < jsdsdfaSysList1.size(); i++) {
                if ("".equals(jsdsdfaSysList1.get(i).getEndHour())) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("节假日时段方案设置 --> [时段" + (i + 1) + "结束时不能为空] ");
                    return resultQuery;
                }
                if ("".equals(jsdsdfaSysList1.get(i).getEndMin())) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("节假日时段方案设置 --> [时段" + (i + 1) + "结束分不能为空] ");
                    return resultQuery;
                }
                if ("23".equals(jsdsdfaSysList1.get(i).getEndHour()) && "59".equals(jsdsdfaSysList1.get(i).getEndMin())) {
                    isHave2359_1 = true;
                }
                String qsjdStr = jsdsdfaSysList1.get(i).getQsjd();
                if ("".equals(qsjdStr)) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("节假日时段方案设置 --> [时段 " + (i + 1) + " 起始阶段不能为空] ");
                    return resultQuery;
                } else {
                    int qsjd = Integer.parseInt(qsjdStr, 10);
                    if (qsjd > 63 || qsjd == 0) {
                        resultQuery.setType(-1);
                        resultQuery.setMessage("节假日时段方案设置 --> [时段 " + (i + 1) + " 起始阶段] 范围 [1,63] ");
                        return resultQuery;
                    }

                }


                String jdslStr = jsdsdfaSysList1.get(i).getJdsl();
                if ("".equals(jdslStr)) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("节假日时段方案设置 --> [时段 " + (i + 1) + " 阶段数量不能为空] ");
                    return resultQuery;
                } else {
                    int jdsl = Integer.parseInt(jdslStr, 10);
                    if (jdsl > 64 || jdsl == 0) {
                        resultQuery.setType(-1);
                        resultQuery.setMessage("节假日时段方案设置 --> [时段 " + (i + 1) + " 阶段数量] 范围 [1,64] ");
                        return resultQuery;
                    }
                }
                jdslStrList1.add(jdslStr);

                int qsjd = Integer.parseInt(qsjdStr);
                int jdsl = Integer.parseInt(jdslStr);
                if ((qsjd + jdsl) > 64) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("节假日时段方案设置 --> [时段 " + (i + 1) + " 起始阶段和阶段数量之和不能大于64] ");
                    return resultQuery;
                } else {
                    qsjd = qsjd - 1;
                    qsjdList1.add(qsjd);
                }

                String tbcsStr = jsdsdfaSysList1.get(i).getTbyc();
                if ("".equals(tbcsStr)) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("节假日时段方案设置 --> [时段 " + (i + 1) + " 同步延迟不能为空] ");
                    return resultQuery;
                } else {
                    int tbcs = Integer.parseInt(tbcsStr, 10);
                    if (tbcs > 255) {
                        resultQuery.setType(-1);
                        resultQuery.setMessage("节假日日时段方案设置 --> [时段 " + (i + 1) + " 同步延迟] 范围 [0,255]");
                        return resultQuery;
                    }
                }
                tbycStrList1.add(tbcsStr);

                String tbzqStr = jsdsdfaSysList1.get(i).getTbzq();
                if ("".equals(tbzqStr)) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("节假日时段方案设置 --> [时段 " + (i + 1) + " 同步周期不能为空] ");
                    return resultQuery;
                } else {
                    int tbzq = Integer.parseInt(tbzqStr, 10);
                    if (tbzq > 255) {
                        resultQuery.setType(-1);
                        resultQuery.setMessage("节假日日时段方案设置 --> [时段 " + (i + 1) + " 同步周期] 范围 [0,255]");
                        return resultQuery;
                    }
                }
                tbzqStrList1.add(tbzqStr);

                String tsjgStr = jsdsdfaSysList1.get(i).getBbjg();
                if ("".equals(tsjgStr)) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("节假日时段方案设置 --> [时段 " + (i + 1) + " 播报间隔不能为空] ");
                    return resultQuery;
                } else {
                    int tsjg = Integer.parseInt(tsjgStr, 10);
                    if (tsjg > 25 || tsjg < 2) {
                        resultQuery.setType(-1);
                        resultQuery.setMessage("节假日时段方案设置 --> [时段 " + (i + 1) + " 播报间隔] 范围 [2,25]");
                        return resultQuery;
                    }
                }
                bbjgStrList1.add(tsjgStr);

                if (i == 9) {
                    //还需校验最后一行的结束时，分是否合法
                    int endHourDec = Integer.parseInt(jsdsdfaSysList1.get(i).getEndHour(), 10);
                    if (endHourDec > 23) {
                        resultQuery.setType(-1);
                        resultQuery.setMessage("节假日时段方案设置 --> [时段 " + (i + 1) + " 结束时不合法] 范围 [0,23]");
                        return resultQuery;
                    }
                    int endMinDec = Integer.parseInt(jsdsdfaSysList1.get(i).getEndMin(), 10);
                    if (endMinDec > 59) {
                        resultQuery.setType(-1);
                        resultQuery.setMessage("节假日时段方案设置 --> [时段 " + (i + 1) + " 结束分不合法] 范围 [0,59]");
                        return resultQuery;
                    }
                }
            }

            if (isHave2359_1 == false) {
                resultQuery.setType(-1);
                resultQuery.setMessage("节假日时段方案设置 --> [必须要有结束时段 23:59] ");
                return resultQuery;
            }

            //------------------------------时段方案校验结束---------------------------------

            //-----------------------------通道出发规则校验开始-----------------------------
            List<JSDTDCFGZ> jsdtdcfgzList = jsdSys.getJsdtdcfgzList();
            List<Integer> zdzxsjList = new ArrayList<>();
            List<Integer> hfycsjList = new ArrayList<>();
            for (int i = 0; i < jsdtdcfgzList.size(); i++) {
                JSDTDCFGZ jsdtdcfgz = jsdtdcfgzList.get(i);
                String zdzxsjStr = jsdtdcfgz.getZdzxsj();
                if ("".equals(zdzxsjStr) || "null".equals(zdzxsjStr)) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("通道触发规则 --> [ " + (i + 1) + " 号通道 最大执行时间不能为空] ");
                    return resultQuery;
                }
                int zdzxsj = Integer.parseInt(zdzxsjStr);
                if (zdzxsj < 0 || zdzxsj > 65535) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("通道触发规则 --> [ " + (i + 1) + " 号通道 最大执行时间] 范围 0-65535");
                    return resultQuery;
                }
                zdzxsjList.add(zdzxsj);

                String hfycsjStr = jsdtdcfgz.getHfycsj();
                if ("".equals(hfycsjStr) || "null".equals(hfycsjStr)) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("通道触发规则 --> [ " + (i + 1) + " 号通道 恢复延迟时间不能为空] ");
                    return resultQuery;
                }
                int hfycsj = Integer.parseInt(hfycsjStr);
                if (hfycsj < 0 || hfycsj > 255) {
                    resultQuery.setType(-1);
                    resultQuery.setMessage("通道触发规则 --> [ " + (i + 1) + " 号通道 恢复延迟时间] 范围 0-255");
                    return resultQuery;
                }
                hfycsjList.add(hfycsj);
            }
            //-------------------------------通道出发规则校验结束----------------------------------------------

            //-------------------------------特殊日校验开始------------------------------------------
            List<JSDTSR> jsdtsrList = jsdSys.getJsdtsrList();
            //------------------------------特殊日校验结束------------------------------------


            //-------------------------------工作方式校验开始--------------------------------------
            JSDWorkTypeSys jsdWorkTypeSys = jsdSys.getJsdWorkTypeSys();
            String qsjdStr = jsdWorkTypeSys.getQsjd();
            if("".equals(qsjdStr)){
                resultQuery.setType(-1);
                resultQuery.setMessage("工作方式选择 --> [起始阶段不能为空] ");
                return resultQuery;
            }else {
                int qsjd = Integer.parseInt(qsjdStr, 10);
                if(qsjd>63 || qsjd == 0){
                    resultQuery.setType(-1);
                    resultQuery.setMessage("工作方式选择 --> [起始阶段] 范围 [1,63] ");
                    return resultQuery;
                }
            }

            String jdslStr = jsdWorkTypeSys.getJdsl();
            if("".equals(jdslStr)){
                resultQuery.setType(-1);
                resultQuery.setMessage("工作方式选择 --> [阶段数量不能为空] ");
                return resultQuery;
            }else {
                int jdsl = Integer.parseInt(jdslStr, 10);
                if(jdsl>64 || jdsl == 0){
                    resultQuery.setType(-1);
                    resultQuery.setMessage("工作方式选择 --> [阶段数量] 范围 [1,64] ");
                    return resultQuery;
                }
            }

            int qsjd = Integer.parseInt(qsjdStr);
            int jdsl = Integer.parseInt(jdslStr);
            if((qsjd+jdsl)>64){
                resultQuery.setType(-1);
                resultQuery.setMessage("工作方式选择 --> [起始阶段和阶段数量之和不能大于64] ");
                return resultQuery;
            }else {
                qsjd = qsjd-1;
            }

            String tbcsStr = jsdWorkTypeSys.getTbcs();
            if("".equals(tbcsStr)){
                resultQuery.setType(-1);
                resultQuery.setMessage("工作方式选择 --> [同步参数不能为空] ");
                return resultQuery;
            }else {
                int tbcs = Integer.parseInt(tbcsStr, 10);
                if(tbcs>255){
                    resultQuery.setType(-1);
                    resultQuery.setMessage("工作方式选择 --> [同步参数] 范围 [0,255] ");
                    return resultQuery;
                }
            }

            String tbzqStr = jsdWorkTypeSys.getTbzq();
            if("".equals(tbzqStr)){
                resultQuery.setType(-1);
                resultQuery.setMessage("工作方式选择 --> [同步周期不能为空] ");
                return resultQuery;
            }else {
                int tbzq = Integer.parseInt(tbzqStr, 10);
                if(tbzq>255){
                    resultQuery.setType(-1);
                    resultQuery.setMessage("工作方式选择 --> [同步周期] 范围 [0,255]");
                    return resultQuery;
                }
            }

            String tsjgStr = jsdWorkTypeSys.getTsjg();
            if("".equals(tsjgStr)){
                resultQuery.setType(-1);
                resultQuery.setMessage("工作方式选择 --> [提示间隔不能为空] ");
                return resultQuery;
            }else {
                int tsjg = Integer.parseInt(tsjgStr, 10);
                if(tsjg>25 || tsjg<2){
                    resultQuery.setType(-1);
                    resultQuery.setMessage("工作方式选择 --> [提示间隔] 范围 [2,25]");
                    return resultQuery;
                }
            }

            //--------------------------------工作方式校验完毕-----------------------------

            //-------------------------------语音触发规则校验开始---------------------------
            List<JSDVoiceSys> jsdVoiceSysList = jsdSys.getJsdVoiceSysList();
            List<Integer> bbjgList = new ArrayList<>();
            List<Integer> bbcsList = new ArrayList<>();
            for (JSDVoiceSys jsdVoiceSys : jsdVoiceSysList) {
                String bbjgStr = jsdVoiceSys.getBbjg();
                if("".equals(bbjgStr) || "null".equals(bbjgStr)){
                    resultQuery.setType(-1);
                    resultQuery.setMessage("语音触发规则 --> [播报间隔不能为空] ");
                    return resultQuery;
                }
                int bbjg = Integer.parseInt(bbjgStr);
                if(bbjg<1 || bbjg>25){
                    resultQuery.setType(-1);
                    resultQuery.setMessage("语音触发规则 --> [播报间隔] 范围 [1,25] ");
                    return resultQuery;
                }
                bbjgList.add(bbjg);

                String bbcsStr = jsdVoiceSys.getBbcs();
                if("".equals(bbcsStr) || "null".equals(bbcsStr)){
                    resultQuery.setType(-1);
                    resultQuery.setMessage("语音触发规则 --> [播报次数不能为空] ");
                    return resultQuery;
                }
                int bbcs = Integer.parseInt(bbcsStr);
                if(bbcs<0 || bbcs>255){
                    resultQuery.setType(-1);
                    resultQuery.setMessage("语音触发规则 --> [播报次数] 范围 [0,255]");
                    return resultQuery;
                }
                bbcsList.add(bbcs);
            }
            //-----------------------------语音触发规则校验结束------------------------------

            //-----------------------------自定义闪灯编辑校验开始-----------------------------
            List<JSDZDYSDSys> jsdzdysdSysList = jsdSys.getJsdzdysdSysList();
            List<Integer> bcList = new ArrayList<>();
            for (int i = 0; i < jsdzdysdSysList.size(); i++) {
                String bcStr = jsdzdysdSysList.get(i).getBc();
                if("".equals(bcStr)){
                    resultQuery.setType(-1);
                    resultQuery.setMessage("自定义闪灯编辑 --> [步长不能为空] ");
                    return resultQuery;
                }
                int bc = Integer.parseInt(bcStr);
                if(bc>255){
                    resultQuery.setType(-1);
                    resultQuery.setMessage("语音触发规则 --> [步长范围] 范围 [0,255]");
                    return resultQuery;
                }
                bcList.add(bc);
            }
            //------------------------------自定义闪灯编辑校验结束--------------------------

            //-----------------------------自定义闪灯触发规则校验开始-------------------------
            List<JSDZDYSDCFGZ> jsdzdysdcfgzList = jsdSys.getJsdzdysdcfgzList();
            List<String> zdysdcf_qsjdStrList = new ArrayList<>();
            List<String> zdysdcf_jdslStrList = new ArrayList<>();
            List<String> zdysdcf_tbzqStrList = new ArrayList<>();
            List<String> zdysdcf_tbxwcStrList = new ArrayList<>();
            List<String> zdysdcf_bbjgStrList = new ArrayList<>();
            List<String> zdysdcf_bbcsStrList = new ArrayList<>();
            for (int i = 0; i < jsdzdysdcfgzList.size(); i++) {
                JSDZDYSDCFGZ jsdzdysdcfgz = jsdzdysdcfgzList.get(i);
                String zdysdcf_qsjdStr = jsdzdysdcfgz.getQsjd();
                if("".equals(zdysdcf_qsjdStr)){
                    resultQuery.setType(-1);
                    resultQuery.setMessage("自定义闪灯触发规则 --> [规则 "+(i+1)+" 起始阶段不能为空] ");
                    return resultQuery;
                }else {
                    int zdysdcf_qsjd = Integer.parseInt(zdysdcf_qsjdStr, 10);
                    if(zdysdcf_qsjd>64){
                        resultQuery.setType(-1);
                        resultQuery.setMessage("自定义闪灯触发规则 --> [规则 "+(i+1)+" 起始阶段] 范围 [1,64]");
                        return resultQuery;
                    }
                }
                zdysdcf_qsjdStrList.add(zdysdcf_qsjdStr);

                String zdysdcf_jdslStr = jsdzdysdcfgz.getJdsl();
                if("".equals(zdysdcf_jdslStr)){
                    resultQuery.setType(-1);
                    resultQuery.setMessage("自定义闪灯触发规则 --> [规则 "+(i+1)+" 阶段数量不能为空] ");
                    return resultQuery;
                }else {
                    int zdysdcf_jdsl = Integer.parseInt(zdysdcf_jdslStr, 10);
                    if(zdysdcf_jdsl>64 || zdysdcf_jdsl == 0){
                        resultQuery.setType(-1);
                        resultQuery.setMessage("自定义闪灯触发规则 --> [规则 "+(i+1)+" 阶段数量] 范围 [1,64]");
                        return resultQuery;
                    }
                }
                zdysdcf_jdslStrList.add(zdysdcf_jdslStr);

                int zdysdcf_qsjd = Integer.parseInt(zdysdcf_qsjdStr);
                int zdysdcf_jdsl = Integer.parseInt(zdysdcf_jdslStr);
                if((zdysdcf_qsjd+zdysdcf_jdsl)>64){
                    resultQuery.setType(-1);
                    resultQuery.setMessage("自定义闪灯触发规则 --> [规则 "+(i+1)+" 起始阶段和阶段数量之和不能大于64] ");
                    return resultQuery;
                }else {
                    zdysdcf_qsjd = zdysdcf_qsjd-1;
                }

                String zdysdcf_tbzqStr = jsdzdysdcfgz.getTbzq();
                if("".equals(zdysdcf_tbzqStr)){
                    resultQuery.setType(-1);
                    resultQuery.setMessage("自定义闪灯触发规则 --> [规则 "+(i+1)+" 同步周期不能为空] ");
                    return resultQuery;
                }else {
                    int zdysdcf_tbcs = Integer.parseInt(zdysdcf_tbzqStr, 10);
                    if(zdysdcf_tbcs>255){
                        resultQuery.setType(-1);
                        resultQuery.setMessage("自定义闪灯触发规则 --> [规则 "+(i+1)+" 同步周期] 范围 [0,255]");
                        return resultQuery;
                    }
                }
                zdysdcf_tbzqStrList.add(zdysdcf_tbzqStr);

                String zdysdcf_tbxwcStr = jsdzdysdcfgz.getTbxwc();
                if("".equals(zdysdcf_tbxwcStr)){
                    resultQuery.setType(-1);
                    resultQuery.setMessage("自定义闪灯触发规则 --> [规则 "+(i+1)+" 同步相位差不能为空] ");
                    return resultQuery;
                }else {
                    int zdysdcf_tbxwc = Integer.parseInt(zdysdcf_tbxwcStr, 10);
                    if(zdysdcf_tbxwc>255){
                        resultQuery.setType(-1);
                        resultQuery.setMessage("自定义闪灯触发规则 --> [规则 "+(i+1)+" 同步相位差] 范围 [0,255]");
                        return resultQuery;
                    }
                }
                zdysdcf_tbxwcStrList.add(zdysdcf_tbxwcStr);

                String zdysdcf_bbjgStr = jsdzdysdcfgz.getBbjg();
                if("".equals(zdysdcf_bbjgStr) || "null".equals(zdysdcf_bbjgStr)){
                    resultQuery.setType(-1);
                    resultQuery.setMessage("自定义闪灯触发规则 --> [规则 "+(i+1)+" 播报间隔不能为空] ");
                    return resultQuery;
                }
                int zdysdcf_bbjg = Integer.parseInt(zdysdcf_bbjgStr);
                if(zdysdcf_bbjg<1 || zdysdcf_bbjg>25){
                    resultQuery.setType(-1);
                    resultQuery.setMessage("自定义闪灯触发规则 --> [规则 "+(i+1)+" 播报间隔] 范围 [1,25]");
                    return resultQuery;
                }
                zdysdcf_bbjgStrList.add(zdysdcf_bbjgStr);

                String zdysdcf_bbcsStr = jsdzdysdcfgz.getBbcs();
                if("".equals(zdysdcf_bbcsStr) || "null".equals(zdysdcf_bbcsStr)){
                    resultQuery.setType(-1);
                    resultQuery.setMessage("自定义闪灯触发规则 --> [规则 "+(i+1)+" 播报次数不能为空] ");
                    return resultQuery;
                }
                int zdysdcf_bbcs = Integer.parseInt(zdysdcf_bbcsStr);
                if(zdysdcf_bbcs<0 || zdysdcf_bbcs>255){
                    resultQuery.setType(-1);
                    resultQuery.setMessage("自定义闪灯触发规则 --> [规则 "+(i+1)+" 播报次数] 范围 [0,255]");
                    return resultQuery;
                }
                zdysdcf_bbcsStrList.add(zdysdcf_bbcsStr);

            }

            //---------------------------自定义闪灯触发规则校验结束--------------------------





            //到此合法性校验结束 开始数据组装-------------------------------------------------------------------------------------

            //-----------------------------网络及通讯参数组装开始--------------------------------------
            int selectedIndex = jsdWebNetSys.getYdyys();     //运营商
            String data10Binary = Integer.parseInt(dataList1.get(10), 16) >= 128 ? "1" : "0";
            if (selectedIndex == 0) {
                data10Binary = data10Binary + "00";
            } else if (selectedIndex == 1) {
                data10Binary = data10Binary + "01";
            } else if (selectedIndex == 2) {
                data10Binary = data10Binary + "10";
            } else {
                data10Binary = data10Binary + "11";
            }
            int m_spinner_dxSelectedIndex = jsdWebNetSys.getDxbq();
            if (m_spinner_dxSelectedIndex == 0) {
                data10Binary = data10Binary + "0";
            } else {
                data10Binary = data10Binary + "1";
            }
            String data10Hex = BytesUtil.binary4ToHex(data10Binary) + sqHex;
            dataList1.set(10, data10Hex);
            dataList1.set(11, trueIPList.get(0));     //目标ip
            dataList1.set(12, trueIPList.get(1));
            dataList1.set(13, trueIPList.get(2));
            dataList1.set(14, trueIPList.get(3));

            dataList1.set(15, truePort.get(0));       //目标port
            dataList1.set(16, truePort.get(1));

            String binary17 = BytesUtil.to6BitBinary(ldbhInt - 1);
            int spinner_zcjSelectedIndex = jsdWebNetSys.getXtfjsx();
            if (spinner_zcjSelectedIndex == 0) {
                //选择了主机
                binary17 = "1" + binary17;
            } else {
                //选择了从机
                binary17 = "0" + binary17;
            }
            int spinner_connect_typeSelectedIndex = jsdWebNetSys.getLjfs();
            if (spinner_connect_typeSelectedIndex == 0) {
                binary17 = binary17 + "0";
            } else {
                binary17 = binary17 + "1";
            }
            dataList1.set(17, BytesUtil.binaryToHex(binary17));   //网络联机参数

            dataList1.set(18, ljjg.get(0));       //联机间隔
            dataList1.set(19, ljjg.get(1));

            dataList1.set(20, trueDebugIPList.get(0));        //调试ip
            dataList1.set(21, trueDebugIPList.get(1));
            dataList1.set(22, trueDebugIPList.get(2));
            dataList1.set(23, trueDebugIPList.get(3));

            dataList1.set(24, trueDebugPort.get(0));      //调试port
            dataList1.set(25, trueDebugPort.get(1));

            dataList1.set(26, trueTimeIPList.get(0));     //时间ip
            dataList1.set(27, trueTimeIPList.get(1));
            dataList1.set(28, trueTimeIPList.get(2));
            dataList1.set(29, trueTimeIPList.get(3));

            dataList1.set(30, trueTimePort.get(0));       //时间port
            dataList1.set(31, trueTimePort.get(1));

            dataList1.set(32, trueDeviceAddress.get(0));  //本机物联网地址
            dataList1.set(33, trueDeviceAddress.get(1));


            int spinner_wxxdSelectedIndex = jsdWebNetSys.getWxxd();
            dataList1.set(40, BytesUtil.decToHex((spinner_wxxdSelectedIndex + 1) + ""));     //lora信道

            int spinner_wxslSelectedIndex = jsdWebNetSys.getWxsl();
            dataList1.set(41, BytesUtil.decToHex((spinner_wxslSelectedIndex + 1) + ""));     //lora速率

            String apnStr = jsdWebNetSys.getApn();
            if ("".equals(apnStr)) {
                dataList4.set(10, "00");
            } else {
                String[] asciiToHex = BytesUtil.asciiToHex(apnStr);
                String data13_10 = BytesUtil.decToHex(asciiToHex.length + "");
                dataList4.set(10, data13_10);
                for (int i = 0; i < asciiToHex.length; i++) {
                    dataList4.set((i + 11), asciiToHex[i]);
                }
            }

            //-----------------------------------网络及通讯参数组装结束---------------------------

            //----------------------------其它参数组装开始-------------------------------

            StringBuilder sb38 = new StringBuilder(BytesUtil.hexToBinary(dataList1.get(38)));
            int zbcsz = jsdOtherSys.getZbcsdsz();
            if (zbcsz == 0) {
                sb38.setCharAt(2, '0');
                sb38.setCharAt(3, '0');
            } else if (zbcsz == 1) {
                sb38.setCharAt(2, '0');
                sb38.setCharAt(3, '1');
            } else {
                sb38.setCharAt(2, '1');
                sb38.setCharAt(3, '0');
            }
            int jjr = jsdOtherSys.getJjrxx();
            if (jjr == 0) {
                sb38.setCharAt(4, '0');
                sb38.setCharAt(5, '0');
            } else if (jjr == 1) {
                sb38.setCharAt(4, '0');
                sb38.setCharAt(5, '1');
            } else {
                sb38.setCharAt(4, '1');
                sb38.setCharAt(5, '0');
            }

            StringBuilder sb39 = new StringBuilder(BytesUtil.hexToBinary(dataList1.get(39)));
            if (jsdOtherSys.getHmms() == 0) {
                sb39.setCharAt(0, '0');
            } else {
                sb39.setCharAt(0, '1');
            }
            if (jsdOtherSys.getGzsj() == 0) {
                sb39.setCharAt(1, '0');
            } else {
                sb39.setCharAt(1, '1');
            }
            if (jsdOtherSys.getZdsb() == 0) {
                sb39.setCharAt(2, '0');
            } else {
                sb39.setCharAt(2, '1');
            }
            if (jsdOtherSys.getGps() == 0) {
                sb39.setCharAt(3, '0');
            } else {
                sb39.setCharAt(3, '1');
            }

            dataList1.set(38, BytesUtil.binaryToHex(sb38.toString()));
            dataList1.set(39, BytesUtil.binaryToHex(sb39.toString()));

            dataList1.set(58, start_hour1Hex);
            dataList1.set(59, start_min1Hex);
            dataList1.set(60, end_hour1Hex);
            dataList1.set(61, end_min1Hex);

            dataList1.set(62, start_hour2Hex);
            dataList1.set(63, start_min2Hex);
            dataList1.set(64, end_hour2Hex);
            dataList1.set(65, end_min2Hex);

            dataList1.set(54, BytesUtil.decTo1Hex(jsdOtherSys.getYjyyyl() + "") + BytesUtil.decTo1Hex(jsdOtherSys.getBtyyyl() + ""));

            //----------------------------------其它参数组装完毕----------------------------------------

            //-------------------------------姿态及传感器参数组装开始----------------------
            String qxjdfzHex = BytesUtil.decToHex(qxjdfzInt + "");
            String str42;
            if (jsdPoseSys.getFx() == 0) {
                str42 = "0" + jsdPoseSys.getStr42();
            } else {
                str42 = "1" + jsdPoseSys.getStr42();
            }
            if (jsdPoseSys.getLmd() == 0) {
                str42 = str42 + "00";
            } else if (jsdPoseSys.getLmd() == 1) {
                str42 = str42 + "01";
            } else {
                str42 = str42 + "10";
            }

            if (jsdPoseSys.getCzz() == 0) {
                str42 = str42 + "00";
            } else if (jsdPoseSys.getCzz() == 1) {
                str42 = str42 + "01";
            } else {
                str42 = str42 + "10";
            }
            dataList1.set(42, BytesUtil.binaryToHex(str42));
            dataList1.set(43, qxjdfzHex);
            int r = (int) (jjjlfzDouble * 10);
            dataList1.set(44, BytesUtil.decToHex(r + ""));
            dataList1.set(45, BytesUtil.decToHex(yxbcsj));
            dataList1.set(56, BytesUtil.decToHex(kgyxbcsj));
            dataList1.set(57, BytesUtil.decTo1Hex(jsdPoseSys.getKgzlbjsj() + "") + BytesUtil.decTo1Hex(jsdPoseSys.getCqjjbjsj() + ""));

            //---------------------------姿态及传感器参数组装完毕--------------------------

            //---------------------------电源管理参数组装开始-----------------------------
            dataList1.set(34, xdcdymdz.get(0));
            dataList1.set(35, xdcdymdz.get(1));
            dataList1.set(36, tyndymdz.get(0));
            dataList1.set(37, tyndymdz.get(1));

            StringBuilder sb40 = new StringBuilder();
            if (jsdPowerSys.getCdkz() == 0) {
                //控制
                sb40.append("0");
            } else {
                //不控制
                sb40.append("1");
            }
            sb40.append(BytesUtil.hexToBinary(dataList1.get(38)).substring(1, 6));
            int dylx = jsdPowerSys.getDylx();
            if (dylx == 0) {
                sb40.append("00");
            } else if (dylx == 1) {
                sb40.append("01");
            } else if (dylx == 2) {
                sb40.append("10");
            } else {
                sb40.append("11");
            }
            for (int i = 0; i < 4; i++) {
                int index = 42 + (8 * i);
                int a = (int) (Double.parseDouble(yjyzdyStrList.get(i)) * 100);
                int b = (int) (Double.parseDouble(qydyStrList.get(i)) * 100);
                int c = (int) (Double.parseDouble(mddyStrList.get(i)) * 100);
                int d = (int) (Double.parseDouble(gcdyStrList.get(i)) * 100);
                List<String> list1 = BytesUtil.getTruePort(a + "");
                List<String> list2 = BytesUtil.getTruePort(b + "");
                List<String> list3 = BytesUtil.getTruePort(c + "");
                List<String> list4 = BytesUtil.getTruePort(d + "");
                dataList3.set(index, list1.get(0));
                dataList3.set(index + 1, list1.get(1));
                dataList3.set(index + 2, list2.get(0));
                dataList3.set(index + 3, list2.get(1));
                dataList3.set(index + 4, list3.get(0));
                dataList3.set(index + 5, list3.get(1));
                dataList3.set(index + 6, list4.get(0));
                dataList3.set(index + 7, list4.get(1));
            }
            dataList1.set(38, BytesUtil.binaryToHex(sb40.toString()));

            //---------------------------------电源管理参数组装完毕----------------------

            //----------------------------------时段方案组装开始-------------------------
            for (int i = 0; i < 10; i++) {      // 工作日组装
                String hex1 = BytesUtil.decToHex(jsdsdfaSysList.get(i).getEndHour());
                String hex2 = BytesUtil.decToHex(jsdsdfaSysList.get(i).getEndMin());
                String hex3 = BytesUtil.decToHex(jsdsdfaSysList.get(i).getGzfs() + "");
                StringBuilder sb4 = new StringBuilder();
                StringBuilder sb5 = new StringBuilder();
                StringBuilder sb12 = new StringBuilder();
                if (i < 5) {
                    String binary4Before = BytesUtil.hexToBinary(dataList9.get(13 + i * 12)).substring(0, 2);
                    String binary5Before = BytesUtil.hexToBinary(dataList9.get(14 + i * 12)).substring(0, 2);
                    sb4.append(binary4Before);
                    sb5.append(binary5Before);
                    String binary12Before = BytesUtil.hexToBinary(dataList9.get(21 + i * 12)).substring(0, 6);
                    sb12.append(binary12Before);
                } else if (i == 5) {
                    String binary4Before = BytesUtil.hexToBinary(dataList9.get(73)).substring(0, 2);
                    String binary5Before = BytesUtil.hexToBinary(dataList10.get(10)).substring(0, 2);
                    sb4.append(binary4Before);
                    sb5.append(binary5Before);
                    String binary12Before = BytesUtil.hexToBinary(dataList10.get(17)).substring(0, 6);
                    sb12.append(binary12Before);
                } else {
                    String binary4Before = BytesUtil.hexToBinary(dataList10.get(21 + (i - 6) * 12)).substring(0, 2);
                    String binary5Before = BytesUtil.hexToBinary(dataList10.get(22 + (i - 6) * 12)).substring(0, 2);
                    sb4.append(binary4Before);
                    sb5.append(binary5Before);
                    String binary12Before = BytesUtil.hexToBinary(dataList10.get(29 + (i - 6) * 12)).substring(0, 2);
                    sb12.append(binary12Before);
                }

                for (int j = 5; j >= 0; j--) {
                    if (jsdsdfaSysList.get(i).getScztList().get(j)) {
                        sb4.append("1");
                    } else {
                        sb4.append("0");
                    }
                    if (jsdsdfaSysList.get(i).getSdztList().get(j)) {
                        sb5.append("1");
                    } else {
                        sb5.append("0");
                    }
                }

                String hex4 = BytesUtil.binaryToHex(sb4.toString());
                String hex5 = BytesUtil.binaryToHex(sb5.toString());
                String hex6 = BytesUtil.decToHex(qsjdList.get(i) + "");
                String hex7 = BytesUtil.decToHex(jdslStrList.get(i));
                String hex8 = BytesUtil.decToHex(tbycStrList.get(i));
                String hex9 = BytesUtil.decToHex(tbzqStrList.get(i));
                String hex10 = BytesUtil.decToHex(jsdsdfaSysList.get(i).getBfyj() + "");
                String hex11 = BytesUtil.decToHex(bbjgStrList.get(i));

                if (jsdsdfaSysList.get(i).getTycfgz1() == 0) {
                    sb12.append("0");
                } else {
                    sb12.append("1");
                }
                if (jsdsdfaSysList.get(i).getTycfgz() == 0) {
                    sb12.append("0");
                } else {
                    sb12.append("1");
                }
                String hex12 = BytesUtil.binaryToHex(sb12.toString());

                //重新组装
                if (i < 5) {
                    dataList9.set(10 + i * 12, hex1);
                    dataList9.set(11 + i * 12, hex2);
                    dataList9.set(12 + i * 12, hex3);
                    dataList9.set(13 + i * 12, hex4);
                    dataList9.set(14 + i * 12, hex5);
                    dataList9.set(15 + i * 12, hex6);
                    dataList9.set(16 + i * 12, hex7);
                    dataList9.set(17 + i * 12, hex8);
                    dataList9.set(18 + i * 12, hex9);
                    dataList9.set(19 + i * 12, hex10);
                    dataList9.set(20 + i * 12, hex11);
                    dataList9.set(21 + i * 12, hex12);
                } else if (i == 5) {
                    dataList9.set(70, hex1);
                    dataList9.set(71, hex2);
                    dataList9.set(72, hex3);
                    dataList9.set(73, hex4);
                    dataList10.set(10, hex5);
                    dataList10.set(11, hex6);
                    dataList10.set(12, hex7);
                    dataList10.set(13, hex8);
                    dataList10.set(14, hex9);
                    dataList10.set(15, hex10);
                    dataList10.set(16, hex11);
                    dataList10.set(17, hex12);
                } else {
                    dataList10.set(18 + (i - 6) * 12, hex1);
                    dataList10.set(19 + (i - 6) * 12, hex2);
                    dataList10.set(20 + (i - 6) * 12, hex3);
                    dataList10.set(21 + (i - 6) * 12, hex4);
                    dataList10.set(22 + (i - 6) * 12, hex5);
                    dataList10.set(23 + (i - 6) * 12, hex6);
                    dataList10.set(24 + (i - 6) * 12, hex7);
                    dataList10.set(25 + (i - 6) * 12, hex8);
                    dataList10.set(26 + (i - 6) * 12, hex9);
                    dataList10.set(27 + (i - 6) * 12, hex10);
                    dataList10.set(28 + (i - 6) * 12, hex11);
                    dataList10.set(29 + (i - 6) * 12, hex12);
                }
            }

            for (int i = 0; i < 10; i++) {      // 节假日组装
                String hex1 = BytesUtil.decToHex(jsdsdfaSysList1.get(i).getEndHour());
                String hex2 = BytesUtil.decToHex(jsdsdfaSysList1.get(i).getEndMin());
                String hex3 = BytesUtil.decToHex(jsdsdfaSysList1.get(i).getGzfs() + "");
                StringBuilder sb4 = new StringBuilder();
                StringBuilder sb5 = new StringBuilder();
                StringBuilder sb12 = new StringBuilder();
                if (i < 5) {
                    String binary4Before = BytesUtil.hexToBinary(dataList11.get(13 + i * 12)).substring(0, 2);
                    String binary5Before = BytesUtil.hexToBinary(dataList11.get(14 + i * 12)).substring(0, 2);
                    sb4.append(binary4Before);
                    sb5.append(binary5Before);
                    String binary12Before = BytesUtil.hexToBinary(dataList11.get(21 + i * 12)).substring(0, 6);
                    sb12.append(binary12Before);
                } else if (i == 5) {
                    String binary4Before = BytesUtil.hexToBinary(dataList11.get(73)).substring(0, 2);
                    String binary5Before = BytesUtil.hexToBinary(dataList12.get(10).substring(0, 2));
                    sb4.append(binary4Before);
                    sb5.append(binary5Before);
                    String binary12Before = BytesUtil.hexToBinary(dataList12.get(17)).substring(0, 6);
                    sb12.append(binary12Before);
                } else {
                    String binary4Before = BytesUtil.hexToBinary(dataList12.get(21 + (i - 6) * 12)).substring(0, 2);
                    String binary5Before = BytesUtil.hexToBinary(dataList12.get(22 + (i - 6) * 12)).substring(0, 2);
                    sb4.append(binary4Before);
                    sb5.append(binary5Before);
                    String binary12Before = BytesUtil.hexToBinary(dataList12.get(29 + (i - 6) * 12)).substring(0, 2);
                    sb12.append(binary12Before);
                }

                for (int j = 5; j >= 0; j--) {
                    if (jsdsdfaSysList1.get(i).getScztList().get(j)) {
                        sb4.append("1");
                    } else {
                        sb4.append("0");
                    }
                    if (jsdsdfaSysList1.get(i).getSdztList().get(j)) {
                        sb5.append("1");
                    } else {
                        sb5.append("0");
                    }
                }

                String hex4 = BytesUtil.binaryToHex(sb4.toString());
                String hex5 = BytesUtil.binaryToHex(sb5.toString());
                String hex6 = BytesUtil.decToHex(qsjdList1.get(i) + "");
                String hex7 = BytesUtil.decToHex(jdslStrList1.get(i));
                String hex8 = BytesUtil.decToHex(tbycStrList1.get(i));
                String hex9 = BytesUtil.decToHex(tbzqStrList1.get(i));
                String hex10 = BytesUtil.decToHex(jsdsdfaSysList1.get(i).getBfyj() + "");
                String hex11 = BytesUtil.decToHex(bbjgStrList1.get(i));

                if (jsdsdfaSysList1.get(i).getTycfgz1() == 0) {
                    sb12.append("0");
                } else {
                    sb12.append("1");
                }
                if (jsdsdfaSysList1.get(i).getTycfgz() == 0) {
                    sb12.append("0");
                } else {
                    sb12.append("1");
                }
                String hex12 = BytesUtil.binaryToHex(sb12.toString());

                //重新组装
                if (i < 5) {
                    dataList11.set(10 + i * 12, hex1);
                    dataList11.set(11 + i * 12, hex2);
                    dataList11.set(12 + i * 12, hex3);
                    dataList11.set(13 + i * 12, hex4);
                    dataList11.set(14 + i * 12, hex5);
                    dataList11.set(15 + i * 12, hex6);
                    dataList11.set(16 + i * 12, hex7);
                    dataList11.set(17 + i * 12, hex8);
                    dataList11.set(18 + i * 12, hex9);
                    dataList11.set(19 + i * 12, hex10);
                    dataList11.set(20 + i * 12, hex11);
                    dataList11.set(21 + i * 12, hex12);
                } else if (i == 5) {
                    dataList11.set(70, hex1);
                    dataList11.set(71, hex2);
                    dataList11.set(72, hex3);
                    dataList11.set(73, hex4);
                    dataList12.set(10, hex5);
                    dataList12.set(11, hex6);
                    dataList12.set(12, hex7);
                    dataList12.set(13, hex8);
                    dataList12.set(14, hex9);
                    dataList12.set(15, hex10);
                    dataList12.set(16, hex11);
                    dataList12.set(17, hex12);
                } else {
                    dataList12.set(18 + (i - 6) * 12, hex1);
                    dataList12.set(19 + (i - 6) * 12, hex2);
                    dataList12.set(20 + (i - 6) * 12, hex3);
                    dataList12.set(21 + (i - 6) * 12, hex4);
                    dataList12.set(22 + (i - 6) * 12, hex5);
                    dataList12.set(23 + (i - 6) * 12, hex6);
                    dataList12.set(24 + (i - 6) * 12, hex7);
                    dataList12.set(25 + (i - 6) * 12, hex8);
                    dataList12.set(26 + (i - 6) * 12, hex9);
                    dataList12.set(27 + (i - 6) * 12, hex10);
                    dataList12.set(28 + (i - 6) * 12, hex11);
                    dataList12.set(29 + (i - 6) * 12, hex12);
                }
            }

            //----------------------------------时段方案组装结束-------------------------

            //----------------------------------通道出发规则组装开始-----------------------
            for (int i = 0; i < 6; i++) {
                StringBuilder sb1 = new StringBuilder(BytesUtil.hexToBinary(dataList2.get(10 + 8 * i)));
                StringBuilder sb2 = new StringBuilder(BytesUtil.hexToBinary(dataList2.get(11 + 8 * i)));
                StringBuilder sb3 = new StringBuilder(BytesUtil.hexToBinary(dataList2.get(12 + 8 * i)));

                for (int j = 4; j >= 0; j--) {
                    if (jsdtdcfgzList.get(i).getTjList().get(j)) {
                        sb1.setCharAt(7 - j, '1');
                    } else {
                        sb1.setCharAt(7 - j, '0');
                    }
                    if (jsdtdcfgzList.get(i).getCfList().get(j)) {
                        sb2.setCharAt(7 - j, '1');
                    } else {
                        sb2.setCharAt(7 - j, '0');
                    }
                    if (jsdtdcfgzList.get(i).getHfList().get(j)) {
                        sb3.setCharAt(7 - j, '1');
                    } else {
                        sb3.setCharAt(7 - j, '0');
                    }

                }

                StringBuilder sb5 = new StringBuilder(BytesUtil.hexToBinary(dataList2.get(14 + 8 * i)));
                if (jsdtdcfgzList.get(i).getSczt() == 0) {
                    sb5.setCharAt(5, '0');
                    sb5.setCharAt(6, '0');
                    sb5.setCharAt(7, '0');
                } else if (jsdtdcfgzList.get(i).getSczt() == 1) {
                    sb5.setCharAt(5, '0');
                    sb5.setCharAt(6, '0');
                    sb5.setCharAt(7, '1');
                } else if (jsdtdcfgzList.get(i).getSczt() == 2) {
                    sb5.setCharAt(5, '0');
                    sb5.setCharAt(6, '1');
                    sb5.setCharAt(7, '1');
                } else if (jsdtdcfgzList.get(i).getSczt() == 3) {
                    sb5.setCharAt(5, '1');
                    sb5.setCharAt(6, '0');
                    sb5.setCharAt(7, '0');
                }
                String hex6 = BytesUtil.decToHex((zdzxsjList.get(i) % 256) + "");
                String hex7 = BytesUtil.decToHex((zdzxsjList.get(i) / 256) + "");
                String hex8 = BytesUtil.decToHex(hfycsjList.get(i) + "");

                dataList2.set(10 + 8 * i, BytesUtil.binaryToHex(sb1.toString()));

                dataList2.set(11 + 8 * i, BytesUtil.binaryToHex(sb2.toString()));

                dataList2.set(12 + 8 * i, BytesUtil.binaryToHex(sb3.toString()));
                dataList2.set(14 + 8 * i, BytesUtil.binaryToHex(sb5.toString()));
                dataList2.set(15 + 8 * i, hex6);
                dataList2.set(16 + 8 * i, hex7);
                dataList2.set(17 + 8 * i, hex8);
            }

            //---------------------------------通道触发规则组装结束----------------------

            //---------------------------------警闪灯特殊日组装--------------------------
            for (int i = 0; i < 21; i++) {
                String hex1 = BytesUtil.decToHex(jsdtsrList.get(i).getMonth()+"");
                String hex2 = BytesUtil.decToHex(jsdtsrList.get(i).getDay()+"");
                String hex3 = BytesUtil.decToHex(jsdtsrList.get(i).getSdfa()+"");
                dataList13.set(10+(3*i),hex1);
                dataList13.set(11+(3*i),hex2);
                dataList13.set(12+(3*i),hex3);
            }
            dataList13.set(dataList13.size()-2,BytesUtil.decToHex(jsdtsrList.get(22).getMonth()+""));
            dataList14.set(10,BytesUtil.decToHex(jsdtsrList.get(22).getDay()+""));
            dataList14.set(11,BytesUtil.decToHex(jsdtsrList.get(22).getSdfa()+""));
            for (int i = 0; i < 18; i++) {
                String hex1 = BytesUtil.decToHex(jsdtsrList.get(i+22).getMonth()+"");
                String hex2 = BytesUtil.decToHex(jsdtsrList.get(i+22).getDay()+"");
                String hex3 = BytesUtil.decToHex(jsdtsrList.get(i+22).getSdfa()+"");
                dataList14.set(12+(3*i),hex1);
                dataList14.set(13+(3*i),hex2);
                dataList14.set(14+(3*i),hex3);
            }

            //---------------------------------特殊日组装完毕-----------------------------

            //---------------------------------工作方式组装开始--------------------------
            String qsjdHex = BytesUtil.decToHex(qsjd+"");
            String jdslHex = BytesUtil.decToHex(jdslStr);
            String tbcsHex = BytesUtil.decToHex(tbcsStr);
            String tbzqHex = BytesUtil.decToHex(tbzqStr);
            String tsjgHex = BytesUtil.decToHex(tsjgStr);

            String workTypeHex = BytesUtil.decToHex(jsdWorkTypeSys.getGzfs() + "");
            StringBuilder scztBinary = new StringBuilder();
            StringBuilder sdztBinary = new StringBuilder();
            String otherString1 = jsdWorkTypeSys.getOtherString1();
            String otherString2 = jsdWorkTypeSys.getOtherString2();
            scztBinary.insert(0, otherString1);
            sdztBinary.insert(0, otherString2);
            for (int i = 5; i >=0 ; i--) {
                if(jsdWorkTypeSys.getSczt().get(i)){
                    scztBinary.append("1");
                }else {
                    scztBinary.append("0");
                }
                if(jsdWorkTypeSys.getSdzt().get(i)){
                    sdztBinary.append("1");
                }else {
                    sdztBinary.append("0");
                }
            }
            String scztHex = BytesUtil.binaryToHex(scztBinary.toString());
            String sdztHex = BytesUtil.binaryToHex(sdztBinary.toString());

            String tsyjHex = BytesUtil.decToHex(jsdWorkTypeSys.getTsyj() + "");

            dataList1.set(46,workTypeHex);
            dataList1.set(47,scztHex);
            dataList1.set(48,sdztHex);
            dataList1.set(49,qsjdHex);
            dataList1.set(50,jdslHex);
            dataList1.set(51,tbcsHex);
            dataList1.set(52,tbzqHex);
            dataList1.set(53,tsyjHex);
            dataList1.set(55,tsjgHex);

            //--------------------------------------工作方式组装完毕--------------------------

            //--------------------------------------语音触发规则组装开始----------------------
            for (int i = 0; i < jsdVoiceSysList.size(); i++) {
                StringBuilder sb1 = new StringBuilder(BytesUtil.hexToBinary(dataList2.get(58+8*i)));
                StringBuilder sb2 = new StringBuilder(BytesUtil.hexToBinary(dataList2.get(59+8*i)));
                StringBuilder sb3 = new StringBuilder(BytesUtil.hexToBinary(dataList2.get(60+8*i)));
                for (int j = 7; j >= 0; j--) {
                    if(jsdVoiceSysList.get(i).getTjList().get(j)){
                        sb1.setCharAt(7-j,'1');
                    }else {
                        sb1.setCharAt(7-j,'0');
                    }
                    if(jsdVoiceSysList.get(i).getCfjList().get(j)){
                        sb2.setCharAt(7-j,'1');
                    }else {
                        sb2.setCharAt(7-j,'0');
                    }
                    if(jsdVoiceSysList.get(i).getHfList().get(j)){
                        sb3.setCharAt(7-j,'1');
                    }else {
                        sb3.setCharAt(7-j,'0');
                    }
                }
                String hex5 = BytesUtil.decToHex(jsdVoiceSysList.get(i).getBfyj() + "");
                String hex6 = BytesUtil.decToHex(bbjgList.get(i)+"");
                String hex7 = BytesUtil.decToHex(bbcsList.get(i)+"");

                dataList2.set(58+8*i,BytesUtil.binaryToHex(sb1.toString()));

                dataList2.set(59+8*i,BytesUtil.binaryToHex(sb2.toString()));

                dataList2.set(60+8*i,BytesUtil.binaryToHex(sb3.toString()));
                dataList2.set(62+8*i,hex5);
                dataList2.set(63+8*i,hex6);
                dataList2.set(64+8*i,hex7);
            }

            //-------------------------------------语音触发规则组装结束---------------------

            //-------------------------------------自定义闪灯编辑组装开始-----------------
            for (int i = 0; i < 64; i++) {
                List<String> dataList;
                if (i>=0 && i<=15){
                    dataList = dataList5;
                } else if (i>15 && i<=31) {
                    dataList = dataList6;
                }else if(i>31 && i <=47){
                    dataList = dataList7;
                }else {
                    dataList = dataList8;
                }

                int n = i % 16;

                StringBuilder sb1 = new StringBuilder();
                if(jsdzdysdSysList.get(i).isSd2()){
                    sb1.append("1");
                }else {
                    sb1.append("0");
                }
                sb1.append(BytesUtil.getBinary3ByDec(jsdzdysdSysList.get(i).getSc2()));
                if(jsdzdysdSysList.get(i).isSd1()){
                    sb1.append("1");
                }else {
                    sb1.append("0");
                }
                sb1.append(BytesUtil.getBinary3ByDec(jsdzdysdSysList.get(i).getSc1()));
                String hex1 = BytesUtil.binaryToHex(sb1.toString());


                StringBuilder sb2 = new StringBuilder();
                if(jsdzdysdSysList.get(i).isSd4()){
                    sb2.append("1");
                }else {
                    sb2.append("0");
                }
                sb2.append(BytesUtil.getBinary3ByDec(jsdzdysdSysList.get(i).getSc4()));
                if(jsdzdysdSysList.get(i).isSd3()){
                    sb2.append("1");
                }else {
                    sb2.append("0");
                }
                sb2.append(BytesUtil.getBinary3ByDec(jsdzdysdSysList.get(i).getSc3()));
                String hex2 = BytesUtil.binaryToHex(sb2.toString());


                StringBuilder sb3 = new StringBuilder();
                if(jsdzdysdSysList.get(i).isSd6()){
                    sb3.append("1");
                }else {
                    sb3.append("0");
                }
                sb3.append(BytesUtil.getBinary3ByDec(jsdzdysdSysList.get(i).getSc6()));
                if(jsdzdysdSysList.get(i).isSd5()){
                    sb3.append("1");
                }else {
                    sb3.append("0");
                }
                sb3.append(BytesUtil.getBinary3ByDec(jsdzdysdSysList.get(i).getSc5()));
                String hex3 = BytesUtil.binaryToHex(sb3.toString());

                String hex4 = BytesUtil.decToHex(bcList.get(i)+"");

                dataList.set( 10 + n * 4 ,hex1);
                dataList.set( 11 + n * 4 ,hex2);
                dataList.set( 12 + n * 4 ,hex3);
                dataList.set( 13 + n * 4 ,hex4);

            }

            //------------------------------------自定义闪灯编辑组装完毕------------------

            //------------------------------------自定义闪灯触发规则组装开始---------------
            for (int i = 0; i < 2; i++) {
                JSDZDYSDCFGZ jsdzdysdcfgz = jsdzdysdcfgzList.get(i);
                StringBuilder sb1 = new StringBuilder(BytesUtil.hexToBinary(dataList3.get(10+16*i)));
                StringBuilder sb2 = new StringBuilder(BytesUtil.hexToBinary(dataList3.get(11+16*i)));
                StringBuilder sb3 = new StringBuilder(BytesUtil.hexToBinary(dataList3.get(12+16*i)));

                for (int j = 4; j >= 0; j--) {
                    if(jsdzdysdcfgz.getTjList().get(j)){
                        sb1.setCharAt(7-j,'1');
                    }else {
                        sb1.setCharAt(7-j,'0');
                    }
                    if(jsdzdysdcfgz.getCfList().get(j)){
                        sb2.setCharAt(7-j,'1');
                    }else {
                        sb2.setCharAt(7-j,'0');
                    }
                    if(jsdzdysdcfgz.getHfList().get(j)){
                        sb3.setCharAt(7-j,'1');
                    }else {
                        sb3.setCharAt(7-j,'0');
                    }
                }

                int qsjd1 = Integer.parseInt(zdysdcf_qsjdStrList.get(i)) - 1;
                String hex5 = BytesUtil.decToHex(qsjd1+"");
                String hex6 = BytesUtil.decToHex(zdysdcf_jdslStrList.get(i));
                String hex7 = BytesUtil.decToHex(zdysdcf_tbxwcStrList.get(i));
                String hex8 = BytesUtil.decToHex(zdysdcf_tbzqStrList.get(i));
                String hex10 = BytesUtil.decToHex(jsdzdysdcfgz.getBfyj() + "");
                String hex11 = BytesUtil.decToHex(zdysdcf_bbjgStrList.get(i));
                String hex12 = BytesUtil.decToHex(zdysdcf_bbcsStrList.get(i));
                dataList3.set(10+16*i,BytesUtil.binaryToHex(sb1.toString()));
                dataList3.set(11+16*i,BytesUtil.binaryToHex(sb2.toString()));
                dataList3.set(12+16*i,BytesUtil.binaryToHex(sb3.toString()));
                dataList3.set(14+16*i,hex5);
                dataList3.set(15+16*i,hex6);
                dataList3.set(16+16*i,hex7);
                dataList3.set(17+16*i,hex8);
                dataList3.set(22+16*i,hex10);
                dataList3.set(23+16*i,hex11);
                dataList3.set(24+16*i,hex12);

            }

            //--------------------------自定义闪灯触发规则组装结束-----------------------------



        resultQuery.setType(1);
        //组装测速机内参数
        StringBuilder sb = new StringBuilder();
        if (dataList1.size() > 73 && dataList2.size() > 73 && dataList3.size() > 73 && dataList4.size() > 73) {
            for (int i = 0; i < 64; i++) {
                sb.append(dataList1.get(10 + i));
            }
            for (int i = 0; i < 64; i++) {
                sb.append(dataList2.get(10 + i));
            }
            for (int i = 0; i < 64; i++) {
                sb.append(dataList3.get(10 + i));
            }
            for (int i = 0; i < 64; i++) {
                sb.append(dataList4.get(10 + i));
            }
            for (int i = 0; i < 64; i++) {
                sb.append(dataList5.get(10+i));
            }
            for (int i = 0; i < 64; i++) {
                sb.append(dataList6.get(10+i));
            }
            for (int i = 0; i < 64; i++) {
                sb.append(dataList7.get(10+i));
            }
            for (int i = 0; i < 64; i++) {
                sb.append(dataList8.get(10+i));
            }
            for (int i = 0; i < 64; i++) {
                sb.append(dataList9.get(10+i));
            }
            for (int i = 0; i < 64; i++) {
                sb.append(dataList10.get(10+i));
            }
            for (int i = 0; i < 64; i++) {
                sb.append(dataList11.get(10+i));
            }
            for (int i = 0; i < 64; i++) {
                sb.append(dataList12.get(10+i));
            }
            for (int i = 0; i < 64; i++) {
                sb.append(dataList13.get(10+i));
            }
            for (int i = 0; i < 64; i++) {
                sb.append(dataList14.get(10+i));
            }
            for (int i = 0; i < 64; i++) {
                sb.append(dataList15.get(10+i));
            }
            String otherString = jsdSys.getParameter().substring(1920);
            sb.append(otherString);
        }
        resultQuery.setMessage(sb.toString());

        return resultQuery;


    }

    /**
     * 更具parameter 字符串获取mySys
     * @param s
     */
    public static List<List<String>> splitStringToMySys(String s){
        int chunkSize = 2;
        int maxListSize = 64;
        List<String> prefix = Arrays.asList("EB","9D","FE","47","00","00","9B","00","00","40");
        String suffix = "00";
        // 获取所有 dataList 的引用
        List<List<String>> allDataLists = new ArrayList<>();
        allDataLists.add(new ArrayList<>());
        allDataLists.add(new ArrayList<>());
        allDataLists.add(new ArrayList<>());
        allDataLists.add(new ArrayList<>());

        // 当前 List 的索引
        int currentListIndex = 0;

        // 遍历字符串，将其按两个字符分割并存入列表
        for (int i = 0; i < s.length(); i += chunkSize) {
            // 截取两个字符
            String chunk = s.substring(i, Math.min(i + chunkSize, s.length()));

            // 将分割的字符加入当前 List
            allDataLists.get(currentListIndex).add(chunk);

            // 如果当前 List 已满，切换到下一个 List
            if (allDataLists.get(currentListIndex).size() >= maxListSize) {
                currentListIndex++;
                // 如果超出最大列表数量，则停止
                if (currentListIndex >= allDataLists.size()) {
                    break;
                }
            }
        }

        for (List<String> dataList : allDataLists) {
            // 如果 dataList 不为空，执行操作
            if (!dataList.isEmpty()) {
                // 添加前缀
                dataList.addAll(0, prefix);

                // 添加后缀
                dataList.add(suffix);
            }
        }
        return allDataLists;
    }


    /**
     * 更具parameter 字符串获取JSDSys
     * @param s
     */
    public static List<List<String>> splitStringToJSDSys(String s){
        int chunkSize = 2;
        int maxListSize = 64;
        List<String> prefix = Arrays.asList("EB","9D","FE","47","00","00","8B","00","00","40");
        String suffix = "00";
        // 获取所有 dataList 的引用
        List<List<String>> allDataLists = new ArrayList<>();
        allDataLists.add(new ArrayList<>());
        allDataLists.add(new ArrayList<>());
        allDataLists.add(new ArrayList<>());
        allDataLists.add(new ArrayList<>());
        allDataLists.add(new ArrayList<>());
        allDataLists.add(new ArrayList<>());
        allDataLists.add(new ArrayList<>());
        allDataLists.add(new ArrayList<>());
        allDataLists.add(new ArrayList<>());
        allDataLists.add(new ArrayList<>());
        allDataLists.add(new ArrayList<>());
        allDataLists.add(new ArrayList<>());
        allDataLists.add(new ArrayList<>());
        allDataLists.add(new ArrayList<>());
        allDataLists.add(new ArrayList<>());


        // 当前 List 的索引
        int currentListIndex = 0;

        // 遍历字符串，将其按两个字符分割并存入列表
        for (int i = 0; i < s.length(); i += chunkSize) {
            // 截取两个字符
            String chunk = s.substring(i, Math.min(i + chunkSize, s.length()));

            // 将分割的字符加入当前 List
            allDataLists.get(currentListIndex).add(chunk);

            // 如果当前 List 已满，切换到下一个 List
            if (allDataLists.get(currentListIndex).size() >= maxListSize) {
                currentListIndex++;
                // 如果超出最大列表数量，则停止
                if (currentListIndex >= allDataLists.size()) {
                    break;
                }
            }
        }

        for (List<String> dataList : allDataLists) {
            // 如果 dataList 不为空，执行操作
            if (!dataList.isEmpty()) {
                // 添加前缀
                dataList.addAll(0, prefix);

                // 添加后缀
                dataList.add(suffix);
            }
        }
        return allDataLists;
    }
}
