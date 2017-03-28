package com.fish.service;

import com.fish.bean.AddNodeBean;
import com.fish.bean.Node;
import com.fish.util.DecriptUtil;
import com.fish.zookeeper.ZookeeperConfigNode;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by yudin on 2017/3/26.
 */
@Service
public class NodeServiceImpl implements INodeService {

   @Resource(name = "zookeeperNodeData")
   private ZookeeperConfigNode zookeeperConfigNode;

    @Override
    public List<Node> loadRootNode() {

        Map<String, Node> nodeMap = zookeeperConfigNode;
        List<Node> nodes = Lists.newArrayList();
        if (null != nodeMap) {

            Collection<Node> node = nodeMap.values();
            nodes.addAll(node);

            return nodes;

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

        Map<String, Node> nodeMap = new ZookeeperConfigNode();
        if (null != nodeMap) {
            return nodeMap.get(nodeName).getValue();
        }

       /* try {
            return zookeeperConfigNode.getData(nodeName);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return null;
    }


    /**
     * 数据加密
     * @param nodeBean
     */
    private static void decriptData(AddNodeBean nodeBean) {

        String encryptType = nodeBean.getEncryptType();


        if (StringUtils.isEmpty(encryptType)) return;

        switch (encryptType) {
            case "sha1" : nodeBean.setNodeData(DecriptUtil.sha1(nodeBean.getNodeData())); break;
            case "md5" : nodeBean.setNodeData(DecriptUtil.md5(nodeBean.getNodeData())); break;
            default : break;
        }

    }
}
