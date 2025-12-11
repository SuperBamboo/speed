package com.shengxuan.speed.util.tostring;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringWriter;

/**
 * 请求获取区域对象String
 */
public class RegionParamXMLToString {

    private String seqStr;
    private String regionID;

    public RegionParamXMLToString(String seqStr,String regionID){
        this.seqStr = seqStr;
        this.regionID = regionID;
    }

    public String RegionParamXMLToString(){
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
        Element subSys = address.addElement("SubSys").addText("ATDMS");
        Element instance = address.addElement("Instance").addText("HLD");

        Element to = message.addElement("To");
        Element addressTo = to.addElement("Address");
        Element sysTo = addressTo.addElement("Sys").addText("TDMS");
        Element subSysTo = addressTo.addElement("SubSys").addText("ATDMS");
        Element instanceTo = addressTo.addElement("Instance").addText("HLD");

        Element type = message.addElement("Type").addText("REQUEST");

        Element seq = message.addElement("Seq").addText(seqStr);

        Element body = message.addElement("Body");
        Element Operation = body.addElement("Operation")
                .addAttribute("order", "1")
                .addAttribute("name", "Get");
        Element TSCCmd = Operation.addElement("RegionParam");
        TSCCmd.addElement("ObjName").addText("SysInfo");
        TSCCmd.addElement("ID").addText(regionID);

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
