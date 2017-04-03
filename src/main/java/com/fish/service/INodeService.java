package com.fish.service;

import com.fish.bean.AddNodeBean;
import com.fish.bean.Node;

import java.util.List;

/**
 * zookeeper节点服务类
 * Created by yudingm on 2017/3/26.
 */
public interface INodeService {

    /**
     * 加载zookeeper节点信息
     * @return
     */
    List<Node> loadRootNode();

    /**
     * 新增节点
     * @param nodeBean
     * @return
     */
    boolean insert(AddNodeBean nodeBean);

    /**
     * 删除节点，及其子节点
     * @param nodeName
     * @return
     */
    boolean delete(String nodeName);

    /**
     * 修改节点对应的值
     * @param nodeBean
     * @return
     */
    boolean modify(AddNodeBean nodeBean);

    /**
     * 根据节点路径获取节点的值
     * @param nodeName
     * @return
     */
    String getData(String nodeName);
}
