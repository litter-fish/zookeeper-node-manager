package com.fish.bean;

import com.fish.annotation.Type;
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

    @Type(value="true", format = "yyyy-MM-dd HH:mm:ss")
    private String ctime;

    @Type(value="true", format = "yyyy-MM-dd HH:mm:ss")
    private String mtime;

    private int version;

    private int cversion;

    private int aversion;

    private long ephemeralOwner;

    private int dataLength;

    private int numChildren;

    private long pzxid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Node node = (Node) o;

        if (czxid != node.czxid) return false;
        if (mzxid != node.mzxid) return false;
        if (version != node.version) return false;
        if (cversion != node.cversion) return false;
        if (aversion != node.aversion) return false;
        if (ephemeralOwner != node.ephemeralOwner) return false;
        if (dataLength != node.dataLength) return false;
        if (numChildren != node.numChildren) return false;
        if (pzxid != node.pzxid) return false;
        if (nodeName != null ? !nodeName.equals(node.nodeName) : node.nodeName != null) return false;
        if (name != null ? !name.equals(node.name) : node.name != null) return false;
        if (value != null ? !value.equals(node.value) : node.value != null) return false;
        if (parentNodeName != null ? !parentNodeName.equals(node.parentNodeName) : node.parentNodeName != null)
            return false;
        if (ctime != null ? !ctime.equals(node.ctime) : node.ctime != null) return false;
        return mtime != null ? mtime.equals(node.mtime) : node.mtime == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (nodeName != null ? nodeName.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (parentNodeName != null ? parentNodeName.hashCode() : 0);
        result = 31 * result + (int) (czxid ^ (czxid >>> 32));
        result = 31 * result + (int) (mzxid ^ (mzxid >>> 32));
        result = 31 * result + (ctime != null ? ctime.hashCode() : 0);
        result = 31 * result + (mtime != null ? mtime.hashCode() : 0);
        result = 31 * result + version;
        result = 31 * result + cversion;
        result = 31 * result + aversion;
        result = 31 * result + (int) (ephemeralOwner ^ (ephemeralOwner >>> 32));
        result = 31 * result + dataLength;
        result = 31 * result + numChildren;
        result = 31 * result + (int) (pzxid ^ (pzxid >>> 32));
        return result;
    }

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
