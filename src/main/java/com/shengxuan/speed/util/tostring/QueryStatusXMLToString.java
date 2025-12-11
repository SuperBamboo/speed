package com.shengxuan.speed.util.tostring;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringWriter;

public class QueryStatusXMLToString {

    private String deviceID;
    private String seqStr;
    private String fromInstance;
    private String toInstance;


    public QueryStatusXMLToString(String seqStr,String from_instance,String to_instance,String deviceID){
        this.deviceID = deviceID;
        this.seqStr = seqStr;
        this.fromInstance = from_instance;
        this.toInstance = to_instance;
    }

    public String queryStatus(){
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
                .addAttribute("name", "Get");
        Element tsccmd = Operation.addElement("TSCCMD");
        tsccmd.addElement("ObjName").addText("DeviceStau");
        tsccmd.addElement("DeviceID").addText(deviceID);

        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setSuppressDeclaration(false);
        format.setIndent(false);//不缩进
        format.setNewLineAfterDeclaration(false);
        format.setNewlines(false);  //不换行
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
