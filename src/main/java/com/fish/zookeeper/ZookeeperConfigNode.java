package com.fish.zookeeper;

import com.fish.bean.Node;
import com.fish.util.ClassUtil;
import com.fish.util.SpringContextHolder;
import com.google.common.base.Predicate;
import com.google.common.base.Throwables;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by yudin on 2017/3/26.
 */
@Service("zookeeperNodeData")
public class ZookeeperConfigNode extends GeneralConfig {



    static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperConfigNode.class);

    @Resource
    private ZookeeperConfigProfile zookeeperConfigProfile;

    private static CuratorFramework curator;


    private CuratorListener listener = new ConfigNodeEventListener(this);


    private final String ROOT_NODE;

    public ZookeeperConfigNode() {
        super(null);
        zookeeperConfigProfile = SpringContextHolder.getBean("zookeeperConfigProfile");
        LOGGER.debug("load zookeeper config profile , address:[{}],node[{}]", zookeeperConfigProfile.getConnectStr(), zookeeperConfigProfile.getNode());
        ROOT_NODE = zookeeperConfigProfile.getNode();
        try {
            initConfigs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化节点
     */
    public void initConfigs() throws Exception {

        // 初始化zk客户端
        curator = CuratorFrameworkFactory.newClient(zookeeperConfigProfile.getConnectStr(), zookeeperConfigProfile.getRetryPolicy());
        curator.start();
        // 添加节点信息变化事件
        curator.getCuratorListenable().addListener(listener);

        loadAll(ROOT_NODE);

    }


    private void loadAll(final String nodePath) throws  Exception {

        if (curator.checkExists().forPath(nodePath) == null) {
            LOGGER.debug("path [{}] is not exists,return null", nodePath);
            return ;
        }

        if (nodePath.equals(ROOT_NODE)) createRootNode(nodePath);

        List<String> children = curator.getChildren().watched().forPath(nodePath);
        if (children != null) {
            String path = nodePath;
            Map<String, Node> configs = Maps.newHashMap();
            for (String child : children) {
                if ("/".equals(nodePath)) {
                    path = nodePath + child;
                } else {
                    path = nodePath + "/" + child;
                }
                Node node = loadData(path);

                this.put(path, node);
                configs.put(path, node);
                LOGGER.debug("load node info [{}]", node);
                loadAll(path);
            }
        }
    }

    private void createRootNode(String nodePath) throws Exception {
        Node rootNode = this.loadData(nodePath);
        rootNode.setName(ROOT_NODE);
        rootNode.setNodeName(ROOT_NODE);
        rootNode.setParentNodeName("0");
        this.put(ROOT_NODE, rootNode);
        LOGGER.debug("load root node info [{}]", rootNode);
    }

    /**
     * 加载节点并监听节点变化
     */
    void loadNode(final String nodePath) throws Exception {
        if (curator.checkExists().forPath(nodePath) == null) {
            LOGGER.debug("path [{}] is not exists,return null", nodePath);
            return ;
        }
        List<String> children = curator.getChildren().watched().forPath(nodePath);
        if (children != null) {
            Map<String, Node> configs = Maps.newHashMap();

            for (String child : children) {
                String path = nodePath + "/" + child;
                Node node = loadData(path);
                configs.put(path, node);
            }

            cleanAndputAddNode(configs);

        }
    }

    /**
     * 加载节点数据信息
     * @param nodePath
     * @return
     * @throws Exception
     */
    private Node loadData(final String nodePath) throws  Exception {
        return createNode(null, nodePath);
    }

    protected  Node createNode(final String nodePath) throws Exception {

        return createNode(null, nodePath);
    }

    private Node createNode(Node node, final String nodePath) throws Exception {
        Stat stat = null;
        try {
            stat = curator.checkExists().forPath(nodePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null == node) {
            byte[] data = curator.getData().watched().forPath(nodePath);
            String value = "";
            if (null != data) {
                value = new String(data, "utf-8");
            }
            node = new Node();
            node.setValue(value);
            node.setNodeName(nodePath);
            node.setName(nodePath.substring(nodePath.lastIndexOf("/") + 1));
            String[] nodePaths = nodePath.split("/");
            node.setParentNodeName(nodePaths.length == 2 ? ROOT_NODE : nodePath.substring(0, nodePath.lastIndexOf("/")));
        }
        ClassUtil.clone(stat, node, false);

        return node;
    }


    protected void cleanNode(final String nodePath) {

        // 过滤出已经删除的数据
        final Iterable<String> redundances = Iterables.filter(Sets.newHashSet(this.keySet()), new Predicate<String>() {

            @Override
            public boolean apply(String input) {
                return !input.startsWith(nodePath);
            }
        });
        for (String redundance : redundances) {
            super.remove(redundance);
        }

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
            return new String(curator.getData().watched().forPath(path));
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

    /**
     * 重新加载nodePath路径下的键值
     * @param nodePath
     */
    void reloadKey(final String nodePath) {
        try {
            if(nodePath != null) {
                super.put(nodePath, createNode(null, nodePath));
            }
        } catch (Exception e) {
            throw Throwables.propagate(e);
        }
    }


}
