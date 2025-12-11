package com.shengxuan.speed.util.toxml;


import com.shengxuan.speed.entity.pojo.XMLNodeAndAttr;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 根据xml遍历所有的节点并且存储在集合中
 */
public class StringToXML {

    private String xmlString;
    private ArrayList<XMLNodeAndAttr> list = new ArrayList<>();
    private int num = 0 ;

    public  StringToXML(String xmlString){
        this.xmlString = xmlString;
    }

    public ArrayList<XMLNodeAndAttr> getXMLList() {
        Document document = null;
        try {
            document = DocumentHelper.parseText(xmlString);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element rootElement = document.getRootElement();
        this.getNodes(rootElement);//从根节点开始遍历所有节点
        return list;
    }

    public void getNodes(Element node){
        //System.out.println("============================================");
        XMLNodeAndAttr a = new XMLNodeAndAttr();
        //当前节点的名称，文本内容和属性
        //System.out.println("当前节点名称："+node.getName());
        a.setNodeName(node.getName());
        //System.out.println("当前节点内容："+node.getTextTrim());
        a.setNodeValue(node.getTextTrim());
        a.setParentNode(num);
       // System.out.println("此节点的父节点在数组标的位置是: "+num);
        List<Attribute> listAttr = node.attributes();//当前节点所有属性的list
        for (Attribute attr : listAttr) {
            String attrName = attr.getName();//属性名称
            String attrValue = attr.getValue();//属性值
            a.setAttrName(attrName);
            a.setAttrValue(attrValue);
            //System.out.println("属性名称："+attrName+"属性值："+attrValue);
        }
        list.add(a);//添加当前节点的内容
        num++;
        //递归遍历当前传过来节点的所有子节点
        List<Element> listElement = node.elements();
        for (Element e : listElement) {//遍历所有一级子节点

            this.getNodes(e);//递归
        }
    }

}
