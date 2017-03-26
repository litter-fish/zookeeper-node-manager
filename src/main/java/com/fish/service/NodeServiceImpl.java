package com.fish.service;

import com.fish.bean.AddNodeBean;
import com.fish.bean.Node;
import com.fish.util.DecriptUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yudin on 2017/3/26.
 */
@Service
public class NodeServiceImpl implements INodeService {

    private static ZookeeperConfigNode zookeeperConfigNode;

    static {
        zookeeperConfigNode = ZookeeperConfigNode.getInstance("localhost:2181");
    }


    @Override
    public List<Node> loadRootNode() {

        ZookeeperConfigNode zookeeperConfigNode = ZookeeperConfigNode.getInstance("/");

        try {
            return zookeeperConfigNode.initConfigs();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean insert(AddNodeBean nodeBean) {

        decriptData(nodeBean);

        try {
            zookeeperConfigNode.createNode(nodeBean.getNodeName(), CreateMode.PERSISTENT, nodeBean.getNodeData());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean delete(String nodeName) {

        try {
            zookeeperConfigNode.deleteNode(nodeName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean modify(AddNodeBean nodeBean) {
        decriptData(nodeBean);
        try {
            zookeeperConfigNode.updateNodeDate(nodeBean.getNodeName(), nodeBean.getNodeData());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String getData(String nodeName) {
        try {
            return zookeeperConfigNode.getData(nodeName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static void decriptData(AddNodeBean nodeBean) {

        String encryptType = nodeBean.getEncryptType();


        if (StringUtils.isEmpty(encryptType)) return;

        switch (encryptType) {
            case "sha1" : nodeBean.setNodeData(DecriptUtil.sha1(nodeBean.getNodeData())); break;
            case  "md5" : nodeBean.setNodeData(DecriptUtil.md5(nodeBean.getNodeData())); break;
            default : break;
        }

    }
}
