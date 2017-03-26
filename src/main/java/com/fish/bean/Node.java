package com.fish.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by yudin on 2017/3/26.
 */
@Data
public class Node  implements Serializable {

    private String nodeName;

    private String name;

    private String value;

    private String parentNodeName;

    private long czxid;

    private long mzxid;

    private long ctime;

    private long mtime;

    private int version;

    private int cversion;

    private int aversion;

    private long ephemeralOwner;

    private int dataLength;

    private int numChildren;

    private long pzxid;

    @Override
    public String toString() {
        return "Node{" +
                "nodeName='" + nodeName + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", parentNodeName='" + parentNodeName + '\'' +
                ", czxid=" + czxid +
                ", mzxid=" + mzxid +
                ", ctime=" + ctime +
                ", mtime=" + mtime +
                ", version=" + version +
                ", cversion=" + cversion +
                ", aversion=" + aversion +
                ", ephemeralOwner=" + ephemeralOwner +
                ", dataLength=" + dataLength +
                ", numChildren=" + numChildren +
                ", pzxid=" + pzxid +
                '}';
    }
}
