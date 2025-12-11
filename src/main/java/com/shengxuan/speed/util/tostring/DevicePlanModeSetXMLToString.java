package com.shengxuan.speed.util.tostring;

import com.shengxuan.speed.entity.PlanParam;
import com.shengxuan.speed.entity.pojo.DevicePlanModeSet;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Objects;

public class DevicePlanModeSetXMLToString {
    private String seqStr;
    private String from_Instance;
    private String to_Instance;

    private DevicePlanModeSet devicePlanModeSet;

    public DevicePlanModeSetXMLToString(String seqStr, String from_Instance, String to_Instance, DevicePlanModeSet devicePlanModeSet) {
        this.seqStr = seqStr;
        this.from_Instance = from_Instance;
        this.to_Instance = to_Instance;
        this.devicePlanModeSet = devicePlanModeSet;
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
        Element deviceControlModeSetE = Operation.addElement("DevicePlanModeSet");
        deviceControlModeSetE.addElement("DeviceID").addText(devicePlanModeSet.getDeviceId());
        deviceControlModeSetE.addElement("Value").addText(devicePlanModeSet.getValue());
        if(devicePlanModeSet.getPlanNo() !=null && !Objects.equals(devicePlanModeSet.getPlanNo(), "")){
            deviceControlModeSetE.addElement("PlanNo").addText(devicePlanModeSet.getPlanNo());
            deviceControlModeSetE.addElement("Save").addText(devicePlanModeSet.getSave());
            deviceControlModeSetE.addElement("Auto").addText(devicePlanModeSet.getAuto());
            deviceControlModeSetE.addElement("HoldTime").addText(devicePlanModeSet.getHoldTime());
            deviceControlModeSetE.addElement("IntervalTime").addText(devicePlanModeSet.getIntervalTime());
            deviceControlModeSetE.addElement("Repetitions").addText(devicePlanModeSet.getRepetitions());
            deviceControlModeSetE.addElement("SysTime").addText(devicePlanModeSet.getSysTime());
            Element planParamListE = deviceControlModeSetE.addElement("PlanParamList");
            for (PlanParam planParam : devicePlanModeSet.getPlanParamList()) {
                Element planParamE = planParamListE.addElement("PlanParam");
                planParamE.addElement("PlanParamNo").addText(planParam.getPlanParamNo());
                planParamE.addElement("PlanParamValue").addText(planParam.getDefaultValue());
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
