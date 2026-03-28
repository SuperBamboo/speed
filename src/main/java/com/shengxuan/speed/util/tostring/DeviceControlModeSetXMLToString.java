package com.shengxuan.speed.util.tostring;


import com.shengxuan.speed.entity.pojo.DeviceControlModeSet;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Objects;

public class DeviceControlModeSetXMLToString {
    private String seqStr;
    private String from_Instance;
    private String to_Instance;

    private DeviceControlModeSet deviceControlModeSet;

    public DeviceControlModeSetXMLToString(String seqStr, String from_Instance, String to_Instance, DeviceControlModeSet deviceControlModeSet) {
        this.seqStr = seqStr;
        this.from_Instance = from_Instance;
        this.to_Instance = to_Instance;
        this.deviceControlModeSet = deviceControlModeSet;
    }

    public String toXMLString(){
        Document document = DocumentHelper.createDocument();

        document.setXMLEncoding("UTF-8");

        //向xml文件里填写对应内容
        Element message = document.addElement("Message");

        Element version = message.addElement("Version").addText("1.0");

        Element token = message.addElement("Token").addText("");

        Element from = message.addElement("From");
        Element address = from.addElement("Address");
        Element sys = address.addElement("Sys").addText("TICP");
        Element subSys = address.addElement("SubSys").addText("");
        Element instance = address.addElement("Instance").addText(from_Instance);

        Element to = message.addElement("To");
        Element addressTo = to.addElement("Address");
        Element sysTo = addressTo.addElement("Sys").addText("TDMS");
        Element subSysTo = addressTo.addElement("SubSys").addText("");
        Element instanceTo = addressTo.addElement("Instance").addText(to_Instance);

        Element type = message.addElement("Type").addText("REQUEST");

        Element seq = message.addElement("Seq").addText(seqStr);

        Element body = message.addElement("Body");
        Element Operation = body.addElement("Operation")
                .addAttribute("order", "1")
                .addAttribute("name", "Set");
        Element deviceControlModeSetE = Operation.addElement("DeviceControlModeSet");
        deviceControlModeSetE.addElement("DeviceID").addText(deviceControlModeSet.getDeviceId());
        deviceControlModeSetE.addElement("Value").addText(deviceControlModeSet.getValue());
        if(deviceControlModeSet.getCtrlNo() !=null && !Objects.equals(deviceControlModeSet.getCtrlNo(), "")){
            deviceControlModeSetE.addElement("CtrlNo").addText(deviceControlModeSet.getCtrlNo());
            deviceControlModeSetE.addElement("Save").addText(deviceControlModeSet.getSave());
            deviceControlModeSetE.addElement("Auto").addText(deviceControlModeSet.getAuto());
            if(deviceControlModeSet.getRelay1()!=null && !Objects.equals(deviceControlModeSet.getRelay1(), "") && !Objects.equals(deviceControlModeSet.getRelay1(), "null")){
                deviceControlModeSetE.addElement("Relay1").addText(deviceControlModeSet.getRelay1());
            }
            if(deviceControlModeSet.getRelay2()!=null && deviceControlModeSet.getRelay2()!="" && deviceControlModeSet.getRelay2()!="null"){
                deviceControlModeSetE.addElement("Relay2").addText(deviceControlModeSet.getRelay2());
            }
            if(deviceControlModeSet.getDisplayNo()!=null && deviceControlModeSet.getDisplayNo()!="" && deviceControlModeSet.getDisplayNo()!="null"){
                deviceControlModeSetE.addElement("DisplayNo").addText(deviceControlModeSet.getDisplayNo());
            }
            if(deviceControlModeSet.getWarningTone()!=null && deviceControlModeSet.getWarningTone()!="" && deviceControlModeSet.getWarningTone()!="null"){
                deviceControlModeSetE.addElement("WarningTone").addText(deviceControlModeSet.getWarningTone());
            }
            if(deviceControlModeSet.getVolume()!=null && deviceControlModeSet.getVolume()!="" && deviceControlModeSet.getVolume()!="null"){
                deviceControlModeSetE.addElement("Volume").addText(deviceControlModeSet.getVolume());
            }
            if(deviceControlModeSet.getPlayInteval()!=null && deviceControlModeSet.getPlayInteval()!="" && deviceControlModeSet.getPlayInteval()!="null"){
                deviceControlModeSetE.addElement("PlayInteval").addText(deviceControlModeSet.getPlayInteval());
            }
            if(deviceControlModeSet.getPlayNumbers()!=null && deviceControlModeSet.getPlayNumbers()!="" && deviceControlModeSet.getPlayNumbers()!="null"){
                deviceControlModeSetE.addElement("PlayNumbers").addText(deviceControlModeSet.getPlayNumbers());
            }
            if(deviceControlModeSet.getCtrlType()!=null && deviceControlModeSet.getCtrlType()!="" && deviceControlModeSet.getCtrlType()!="null"){
                deviceControlModeSetE.addElement("CtrlType").addText(deviceControlModeSet.getCtrlType());
            }
            if(deviceControlModeSet.getPhaseNo()!=null && deviceControlModeSet.getPhaseNo()!="" && deviceControlModeSet.getPhaseNo()!="null"){
                deviceControlModeSetE.addElement("PhaseNo").addText(deviceControlModeSet.getPhaseNo());
            }
            if(deviceControlModeSet.getPlanNo()!=null && deviceControlModeSet.getPlanNo()!="" && deviceControlModeSet.getPlanNo()!="null"){
                deviceControlModeSetE.addElement("PlanNo").addText(deviceControlModeSet.getPlanNo());
            }
            if(deviceControlModeSet.getCycleLen()!=null && deviceControlModeSet.getCycleLen()!="" && deviceControlModeSet.getCycleLen()!="null"){
                deviceControlModeSetE.addElement("CycleLen").addText(deviceControlModeSet.getCycleLen());
            }
            if(deviceControlModeSet.getCroodLampNo()!=null && deviceControlModeSet.getCroodLampNo()!="" && deviceControlModeSet.getCroodLampNo()!="null"){
                deviceControlModeSetE.addElement("CoordLampNo").addText(deviceControlModeSet.getCroodLampNo());
            }
            if(deviceControlModeSet.getOffset()!=null && deviceControlModeSet.getOffset()!="" && deviceControlModeSet.getOffset()!="null"){
                deviceControlModeSetE.addElement("OffSet").addText(deviceControlModeSet.getOffset());
            }
            if(deviceControlModeSet.getHoldTime()!=null && deviceControlModeSet.getHoldTime()!="" && deviceControlModeSet.getHoldTime()!="null"){
                deviceControlModeSetE.addElement("HoldTime").addText(deviceControlModeSet.getHoldTime());
            }
        }
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setSuppressDeclaration(false);
        format.setIndent(false);//不缩进
        format.setNewlines(false);  //不换行
        format.setNewLineAfterDeclaration(false);
        StringWriter writer = new StringWriter();
        //格式化输出流
        XMLWriter output = new XMLWriter(writer, format);
        try {
            output.write(document);
            writer.close();
            output.close();
        }  catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return writer.toString();
    }
}
