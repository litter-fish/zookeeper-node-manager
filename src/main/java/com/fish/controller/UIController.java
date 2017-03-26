package com.fish.controller;

import com.alibaba.fastjson.JSONObject;
import com.fish.bean.AddNodeBean;
import com.fish.bean.Node;
import com.fish.service.INodeService;
import com.fish.util.ClassUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by yudin on 2017/3/26.
 */
@RequestMapping("/base")
@Controller
public class UIController {

    @Resource
    private INodeService nodeService;


    @RequestMapping(value = "/index")
    public ModelAndView loadRootNode(HttpServletRequest request) {

        ModelAndView view = new ModelAndView("main");

        List<Node> rootNode = nodeService.loadRootNode();

        view.addObject("rootNode", JSONObject.toJSONString(rootNode));

        request.setAttribute("rootNode", JSONObject.toJSONString(rootNode));

        return view;
    }

    @ResponseBody
    @RequestMapping("/getData")
    public String getData(HttpServletRequest request, @RequestBody String body) {
        JSONObject jsonObject = ClassUtil.toJSONObject(body);

        String nodeName = jsonObject.getString("nodeName");

        String nodeData = nodeService.getData(nodeName);

        return nodeData;
    }

    @ResponseBody
    @RequestMapping("/insert")
    public String insert(HttpServletRequest request, @RequestBody String body) {

        AddNodeBean addNodeBean = ClassUtil.toJavaObject(body, AddNodeBean.class);


        nodeService.insert(addNodeBean);

        System.out.println();
        return "success";
    }

    @ResponseBody
    @RequestMapping("/delete")
    public String delete(HttpServletRequest request, @RequestBody String body) {

        JSONObject jsonObject = ClassUtil.toJSONObject(body);

        String nodeName = jsonObject.getString("nodeName");

        nodeService.delete(nodeName);

        return "success";
    }

    @ResponseBody
    @RequestMapping("/modify")
    public String modify(HttpServletRequest request, @RequestBody String body) {

        AddNodeBean addNodeBean = ClassUtil.toJavaObject(body, AddNodeBean.class);

        nodeService.modify(addNodeBean);

        return "success";
    }


}
