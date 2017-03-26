package com.fish.service;

import com.fish.bean.Node;
import com.fish.util.ClassUtil;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yudin on 2017/3/26.
 */
public class ZookeeperConfigNode {

    private final int CONNECT_TIMEOUT = 15000;
    private final int RETRY_TIME = Integer.MAX_VALUE;
    private final int RETRY_INTERVAL = 1000;

    static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperConfigNode.class);

    private CuratorFramework curator;

    private volatile static ZookeeperConfigNode instance;


    private CuratorFramework newCurator(String zkServers) {
        return CuratorFrameworkFactory.builder().connectString(zkServers)
                .retryPolicy(new RetryNTimes(RETRY_TIME, RETRY_INTERVAL))
                .connectionTimeoutMs(CONNECT_TIMEOUT).build();
    }

    public static ZookeeperConfigNode getInstance(String zkServers) {
        if (instance == null) {
            synchronized (CuratorZookeeperClient.class) {
                if (instance == null) {
                    LOGGER.info("initial CuratorZookeeperClient instance");
                    instance = new ZookeeperConfigNode(zkServers);
                }
            }
        }
        return instance;
    }

    private ZookeeperConfigNode(String zkServers) {
        if (curator == null) {
            curator = newCurator(zkServers);
            curator.getConnectionStateListenable().addListener(new ConnectionStateListener() {
                public void stateChanged(CuratorFramework client, ConnectionState state) {
                    if (state == ConnectionState.LOST) {
                        //连接丢失
                        LOGGER.info("lost session with zookeeper");
                    } else if (state == ConnectionState.CONNECTED) {
                        //连接新建
                        LOGGER.info("connected with zookeeper");
                    } else if (state == ConnectionState.RECONNECTED) {
                        LOGGER.info("reconnected with zookeeper");
                        //连接重连
                       /* for (ZkStateListener s : stateListeners) {
                            s.reconnected();
                        }*/
                    }
                }
            });
            curator.start();
        }
    }

    /**
     * 初始化节点
     */
    public List<Node> initConfigs() throws Exception {

        List<Node> nodes = new ArrayList<Node>();
        Node rootNode = this.loadData("/");
        rootNode.setName("/");
        rootNode.setNodeName("/");
        rootNode.setParentNodeName("0");
        nodes.add(rootNode);

        loadAll("/", rootNode, nodes);

        return nodes;
    }


    private void loadAll(final String nodePath, Node rootNode, List<Node> nodes) throws  Exception {

        if (curator.checkExists().forPath(nodePath) == null) {
            LOGGER.debug("path [{}] is not exists,return null", nodePath);
            return ;
        }

        List<String> children = curator.getChildren().forPath(nodePath);
        if (children != null) {
            String path = nodePath;
            for (String child : children) {
                if ("/".equals(nodePath)) {
                    path = nodePath + child;
                } else {
                    path = nodePath + "/" + child;
                }
                Node node = loadData(path);
                node.setParentNodeName(nodePath);

                nodes.add(node);
                loadAll(path, node, nodes);
            }
        }
        //rootNode.setChildren(nodes);
    }

    private Node loadData(final String nodePath) throws  Exception {
        byte[] data = curator.getData().forPath(nodePath);

        String value = new String(data, "utf-8");

        Node node = new Node();
        node.setValue(value);
        node.setNodeName(nodePath);
        node.setName(nodePath.substring(nodePath.lastIndexOf("/") + 1));

        Stat stat = curator.checkExists().forPath(nodePath);

        ClassUtil.clone(stat, node, false);

        return node;
    }


    /**
     * 创建节点
     * @param path
     * @param createMode
     * @param data
     */
    public void createNode(String path, CreateMode createMode, String data) throws Exception {
        try {
            curator.create().withMode(createMode).forPath(path, data.getBytes());
        } catch (Exception e) {
            System.out.println("创建节点失败, elog=" + e.getMessage());
            throw e;
        }
    }

    /**
     * 获取节点数据
     * @param path
     * @return
     */
    public String getData(String path) {
        try {
            return new String(curator.getData().forPath(path));
        } catch (Exception e) {
            System.out.println("获取数据失败, elog=" + e.getMessage());
        }
        return null;
    }

    /**
     * 更新节点
     * @param path
     * @param data
     */
    public void updateNodeDate(String path, String data) {
        try {
            curator.setData().forPath(path, data.getBytes());
        } catch (Exception e) {
            System.out.println("更新节点数据失败, elog=" + e.getMessage());
        }
    }

    /**
     * 删除节点
     * @param path
     */
    public void deleteNode(String path) {
        try {
            curator.delete().forPath(path);
        } catch (Exception e) {
            System.out.println("删除节点失败, elog=" + e.getMessage());
        }
    }


    public static void main(String[] args) throws Exception {
        ZookeeperConfigNode zookeeperConfigNode = ZookeeperConfigNode.getInstance("localhost:2181");
        zookeeperConfigNode.initConfigs();

    }


}
