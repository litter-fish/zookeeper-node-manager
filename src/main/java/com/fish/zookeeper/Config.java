package com.fish.zookeeper;

import com.fish.bean.Node;

import java.io.Closeable;
import java.util.Map;

/**
 * Created by yudin on 2017/3/27.
 */
public interface Config extends Map<String, Node>, Closeable {

    Node get(String key);

}
