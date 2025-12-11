package com.shengxuan.speed.util.tostring;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.IOException;
import java.io.StringWriter;

public class LoginXMLToString {

    private String username;
    private String password;

    public LoginXMLToString(String username,String password) {
        this.username = username;
        this.password = password;
    }

    public String loginXMLToString(){
        //获取用户输入信息后，生成对应的xml文件
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
        Element instance = address.addElement("Instance").addText("");

        Element to = message.addElement("To");
        Element addressTo = to.addElement("Address");
        Element sysTo = addressTo.addElement("Sys").addText("TDMS");
        Element subSysTo = addressTo.addElement("SubSys").addText("");
        Element instanceTo = addressTo.addElement("Instance").addText("");

        Element type = message.addElement("Type").addText("REQUEST");

        Element seq = message.addElement("Seq").addText("2019041816020003");

        Element body = message.addElement("Body");
        Element Operation = body.addElement("Operation")
                .addAttribute("order", "1")
                .addAttribute("name", "Login");
        Element sdo_user = Operation.addElement("SDO_User");
        Element userName = sdo_user.addElement("UserName").addText(username);
        Element pwd = sdo_user.addElement("Pwd").addText(password);

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

    public static void main(String[] args) {
        LoginXMLToString cyz = new LoginXMLToString("陈韵竹", "12345");
        System.out.println(cyz.loginXMLToString());
    }

}
