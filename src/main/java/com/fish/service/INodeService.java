package com.fish.service;

import com.fish.bean.AddNodeBean;
import com.fish.bean.Node;

import java.util.List;

/**
 * Created by yudin on 2017/3/26.
 */
public interface INodeService {

    List<Node> loadRootNode();

    boolean insert(AddNodeBean nodeBean);

    boolean delete(String nodeName);

    boolean modify(AddNodeBean nodeBean);

    String getData(String nodeName);
}
