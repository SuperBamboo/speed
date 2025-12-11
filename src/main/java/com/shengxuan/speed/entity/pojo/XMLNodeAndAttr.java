package com.shengxuan.speed.entity.pojo;

import java.io.Serializable;

/**
 * xml文件节点属性类
 */
public class XMLNodeAndAttr implements Serializable {
    private int parentNode;  //父节点在数组中下标
    private String NodeName;
    private String NodeValue;
    private String AttrName;
    private String AttrValue;

    public int getParentNode() {
        return parentNode;
    }

    public void setParentNode(int parentNode) {
        this.parentNode = parentNode;
    }

    public String getNodeName() {
        return NodeName;
    }

    public void setNodeName(String nodeName) {
        NodeName = nodeName;
    }

    public String getAttrValue() {
        return AttrValue;
    }

    public void setNodeValue(String nodeValue) {
        NodeValue = nodeValue;
    }

    public String getAttrName() {
        return AttrName;
    }

    public String getNodeValue() {
        return NodeValue;
    }

    public void setAttrName(String attrName) {
        AttrName = attrName;
    }

    public void setAttrValue(String attrValue) {
        AttrValue = attrValue;
    }
}
