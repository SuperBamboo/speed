package com.shengxuan.speed.util.tostring;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Yunzhu_Chen
 */
public class HeartBeatXMLToString {

    //请求命令码
    private String seqStr;
    private String username;

    public HeartBeatXMLToString(String s,String username) {
        this.seqStr = s;
        this.username = username;
    }



    public String heartBeatXML(){
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
        Element instance = address.addElement("Instance").addText("HLD");

        Element to = message.addElement("To");
        Element addressTo = to.addElement("Address");
        Element sysTo = addressTo.addElement("Sys").addText("TDMS");
        Element subSysTo = addressTo.addElement("SubSys").addText("");
        Element instanceTo = addressTo.addElement("Instance").addText(username);

        Element type = message.addElement("Type").addText("PUSH");

        Element seq = message.addElement("Seq").addText(seqStr);

        Element body = message.addElement("Body");
        Element Operation = body.addElement("Operation")
                .addAttribute("order", "1")
                .addAttribute("name", "Notify");
        Operation.addElement("SDO_HeartBeat");

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
