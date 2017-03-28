package com.fish.zookeeper;

import com.fish.bean.Node;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yudin on 2017/3/27.
 */
public class GeneralConfigGroup extends ConcurrentHashMap<String, Node> implements ConfigGroup {

    private static final long serialVersionUID = 1L;

    private ConfigGroup internalConfigGroup;

    protected GeneralConfigGroup(ConfigGroup internalConfigGroup) {
        this.internalConfigGroup = internalConfigGroup;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralConfigGroup.class);

    @Override
    public final Node get(String key) {
        Node val = super.get(key);
        if (val == null && internalConfigGroup != null) {
            val = internalConfigGroup.get(key);
        }
        return val;
    }

    @Override
    public final Node get(Object key) {
        return get(key.toString());
    }

    public final void cleanAndputAddNode(Map<? extends String, ? extends Node> configs) {

        if (configs != null && configs.size() > 0) {
            // clear
            if (this.size() > 0) {
                final Set<String> newKeys = Sets.newHashSet();
                final Set<String> removeKeys = Sets.newHashSet();
                newKeys.addAll(configs.keySet());
                for (String redundance : newKeys) {

                    redundance = redundance.substring(0, redundance.lastIndexOf("/") + 1);
                    for (String oldKey : this.keySet()) {
                        if (oldKey.startsWith(redundance)) {
                            removeKeys.add(oldKey);
                        }

                    }
                }
                removeKeys.removeAll(newKeys);
                for (String redundance : removeKeys) {
                    super.remove(redundance);
                    LOGGER.debug("remove node [{}]", redundance);
                }

            }

            // update 存储键值对
            for (Map.Entry<? extends String, ? extends Node> entry : configs.entrySet()) {
                this.put(entry.getKey(), entry.getValue());
                LOGGER.debug("add node info [{}]", entry.getValue());
            }

        } else {
            LOGGER.debug("Config group has none keys, clear.");
            super.clear();
        }
    }

    @Override
    public final Node put(String key, Node value) {
        Node preValue = super.get(key);
        // 判断当前值是否与旧的数据相等
        if (!value.equals(preValue)) {
            super.put(key, value);
        }
        return preValue;
    }


    @Override
    public Node remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() throws IOException {

    }
}
