package com.fish.zookeeper;

import com.fish.bean.Node;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by yudin on 2017/3/27.
 */
public class ConfigNodeEventListener implements CuratorListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigNodeEventListener.class);

    private final ZookeeperConfigNode configNode;

    public ConfigNodeEventListener(ZookeeperConfigNode configNode) {
        super();
        this.configNode = Preconditions.checkNotNull(configNode);
    }

    @Override
    public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(event.toString());
        }

        final WatchedEvent watchedEvent = event.getWatchedEvent();
        if (watchedEvent != null) {
            LOGGER.debug("Watched event: {}" + watchedEvent);

            if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {
                switch (watchedEvent.getType()) {
                    case NodeChildrenChanged:// 目录节点变化
                        Map<String, Node> configs = Maps.newHashMap();
                        final String nodePath = watchedEvent.getPath();
                        configs.put(nodePath, configNode.createNode(nodePath));
                        configNode.loadNode(nodePath, configs);
                        configNode.cleanAndputAddNode(nodePath, configs);
                        break;
                    /*case NodeCreated :
                        configNode.createNode(watchedEvent.getPath());
                        break;
                    case NodeDeleted :
                        configNode.cleanNode(watchedEvent.getPath());
                        break;*/
                    case NodeDataChanged: // 数据节点变化
                        configNode.reloadKey(watchedEvent.getPath());
                        break;
                    default:
                        break;
                }

            }
        }
    }
}
