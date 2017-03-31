package com.fish.zookeeper;

import com.fish.bean.Node;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by yudin on 2017/3/27.
 */
public class GeneralConfig extends ConcurrentHashMap<String, Node> implements Config {

    private static final long serialVersionUID = 1L;

    private Config internalConfigGroup;

    protected GeneralConfig(Config internalConfigGroup) {
        this.internalConfigGroup = internalConfigGroup;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralConfig.class);

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

    public final void cleanAndputAddNode(final String nodePath, Map<? extends String, ? extends Node> configs) {

        if (configs != null && configs.size() > 0) {
            // clear
            if (this.size() > 0) {
                final Set<String> newKeys = Sets.newHashSet();
                final Set<String> removeKeys = Sets.newHashSet();
                newKeys.addAll(configs.keySet());

                // 过滤出已经删除的数据
                final Iterable<String> redundances = Iterables.filter(Sets.newHashSet(this.keySet()), new Predicate<String>() {

                    @Override
                    public boolean apply(String input) {
                        return !newKeys.contains(input) && input.startsWith(nodePath);
                    }
                });

                for (String redundance : redundances) {
                    removeKeys.add(redundance);
                }

                removeKeys.removeAll(newKeys);
                if (CollectionUtils.isNotEmpty(removeKeys)) {
                    for (String key : removeKeys) this.remove(key);
                }

                System.out.println();



            }

            // update 存储键值对
            for (Map.Entry<? extends String, ? extends Node> entry : configs.entrySet()) {
                LOGGER.debug("add node info [{}]", entry.getKey());
                this.put(entry.getKey(), entry.getValue());
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
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() throws IOException {

    }
}
