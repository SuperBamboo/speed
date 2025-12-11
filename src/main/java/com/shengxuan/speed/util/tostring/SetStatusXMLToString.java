package com.shengxuan.speed.util.tostring;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringWriter;

public class SetStatusXMLToString {

    private String deviceID;
    private String seqStr;
    private String fromInstance;
    private String toInstance;
    private String value;
    private String relay1;
    private String relay2;
    private String holdTime;


    public SetStatusXMLToString(String seqStr,String from_instance,String to_instance,String deviceID,String value,String relay1,String relay2,String holdTime){
        this.deviceID = deviceID;
        this.seqStr = seqStr;
        this.fromInstance = from_instance;
        this.toInstance = to_instance;
        this.value = value;
        this.relay1 = relay1;
        this.relay2 = relay2;
        this.holdTime = holdTime;
    }

    public String setStatus(){
        //生成心跳xml文件
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
        Element instance = address.addElement("Instance").addText(fromInstance);

        Element to = message.addElement("To");
        Element addressTo = to.addElement("Address");
        Element sysTo = addressTo.addElement("Sys").addText("TDMS");
        Element subSysTo = addressTo.addElement("SubSys").addText("");
        Element instanceTo = addressTo.addElement("Instance").addText(toInstance);

        Element type = message.addElement("Type").addText("REQUEST");

        Element seq = message.addElement("Seq").addText(seqStr);

        Element body = message.addElement("Body");
        Element Operation = body.addElement("Operation")
                .addAttribute("order", "1")
                .addAttribute("name", "Set");
        Element deviceControlModeSet = Operation.addElement("DeviceControlModeSet");
        deviceControlModeSet.addElement("DeviceID").addText(deviceID);
        deviceControlModeSet.addElement("Value").addText(value);
        deviceControlModeSet.addElement("Relay1").addText(relay1);
        deviceControlModeSet.addElement("Relay2").addText(relay2);
        deviceControlModeSet.addElement("HoldTime").addText(holdTime);

        OutputFormat format = OutputFormat.createPrettyPrint();
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
