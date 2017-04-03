<%--
  Created by IntelliJ IDEA.
  User: yudin
  Date: 2017/3/26
  Time: 18:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>


    <!-- zTree -->
    <link href="/Zookeeper-manager-web/static/js/ztree/css/zTreeStyle.css?version=${versionTime}" rel="stylesheet" type="text/css"/>
    <link href="/Zookeeper-manager-web/static/js/ztree/css/ztree.css?version=${versionTime}" rel="stylesheet" type="text/css"/>
    <link href="/Zookeeper-manager-web/static/js/ztree/css/menuTree.css?version=${versionTime}" rel="stylesheet" type="text/css"/>
    <link href="/Zookeeper-manager-web/static/js/layer/skin/default/layer.css?version=${versionTime}" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

    <script type="text/javascript" src="/Zookeeper-manager-web/static/js/jquery-3.2.0.js?version=${versionTime}"></script>
    <!-- zTree -->
    <script type="text/javascript" src="/Zookeeper-manager-web/static/js/ztree/jquery.ztree.core-3.5.js?version=${versionTime}"> </script>
    <script type="text/javascript" src="/Zookeeper-manager-web/static/js/ztree/jquery.ztree.excheck-3.5.js?version=${versionTime}"> </script>
    <script type="text/javascript" src="/Zookeeper-manager-web/static/js/layer/layer.js?version=${versionTime}"> </script>

    <link rel="stylesheet" href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
    <%--<script src="//cdn.bootcss.com/jquery.form/4.2.1/jquery.form.min.js"></script>--%>
</head>
<body>
    <div class="container">
        <div class="row">
            <div class="col-xs-6 col-md-4">
                <ul id="org_menuTree" class="ztree"></ul>
            </div>

            <div class="col-xs-12 col-md-8">
                <div id="dataContent">

                </div>

                <div id="dataBtn" style="display:none;">
                    <input class="btn btn-default" type="button" value="add" id="add">
                    <input class="btn btn-default" type="button" value="modify" id="modify">
                    <input class="btn btn-default" type="button" value="delete" id="delete">
                    <input class="btn btn-default" type="button" value="upload" id="upload">
                </div>
            </div>
        </div>
    </div>
</body>
<script type="text/javascript">
    var _data = ${rootNode};
    var setting = {
        data: {
            simpleData: {
                enable: true,
                idKey: "nodeName",
                pIdKey: "parentNodeName",
                rootPId: ""
            }
        },
        callback: {
            onClick: zTreeOnClick
        }
    };

    $(document).ready(function(){
        $.fn.zTree.init($("#org_menuTree"), setting, _data);

        var zTree = $.fn.zTree.getZTreeObj("org_menuTree");//获取ztree对象
        var node = zTree.getNodeByParam('nodeName', $("#tree_topOrg").val()+"");//获取id为  的点
        zTree.selectNode(node);//选择点
        zTree.setting.callback.onClick(null, zTree.setting.treeId, node);
    });


    function zTreeOnClick(event, treeId, treeNode) {

        $("#dataContent").empty();

        if (treeNode.nodeName == 'null') return false;

        var content = "<div>name: " + treeNode.nodeName + "</div>";

        content += "<div>value: " + treeNode.value + "</div>";
        content += "<div>czxid: " + treeNode.czxid + "</div>";
        content += "<div>mzxid: " + treeNode.mzxid + "</div>";
        content += "<div>ctime: " + treeNode.ctime + "</div>";
        content += "<div>mtime: " + treeNode.mtime + "</div>";
        content += "<div>version: " + treeNode.version + "</div>";
        content += "<div>cversion: " + treeNode.cversion + "</div>";
        content += "<div>aversion: " + treeNode.aversion + "</div>";
        content += "<div>ephemeralOwner: " + treeNode.ephemeralOwner + "</div>";
        content += "<div>pzxid: " + treeNode.pzxid + "</div>";
        content += "<div>numChildren: " + treeNode.numChildren + "</div>";




        $("#dataContent").append(content);
        $("#dataBtn").attr("nodeName", treeNode.nodeName);
        $("#dataBtn").show();
        /*if (treeNode.numChildren > 0) {
            $("#delete").hide();
        } else {
            $("#delete").show();
        }*/

    }

    $(document).on("click", "#add", function () {
        var nodeName = $(this).parent().attr("nodeName");

        var content = "<form>" +
            "<div class='form-group'><label for='Node Name' class='col-sm-2 control-label'>NodeName</label>" +
            "<div class=\"col-sm-10\"><input type=\"type\" class=\"form-control\" id=\"nodeName\" placeholder=\"NodeName\"></div></div><br />"+
            "<div class='form-group'><label for='Node Data' class='col-sm-2 control-label'>NodeData</label>" +
            "<div class=\"col-sm-10\"><input type=\"type\" class=\"form-control\" id=\"nodeData\" placeholder=\"NodeData\"></div></div><br />"+
            "<div class='form-group'><label for='Node Name' class='col-sm-2 control-label'>encryptType</label>" +
            "<div class=\"col-sm-10\">"+
            "<select class=\"form-control\" id=\"encryptType\" name=\"encryptType\">" +
            "<option value=\"\">not encrypt</option>" +
            "<option value=\"sha1\">sha1</option>" +
            "<option value=\"md5\">md5</option>" +
            "</select></div></div>" +

            "</form>";

        //页面层
        layer.open({
            type: 1,
            skin: 'layui-layer-rim', //加上边框
            area: ['600px', '400px'], //宽高
            content: content,
            btn: ['yes', 'no'],
            yes : function (index, layer1) {
                var reqParam = {"nodeName":nodeName + "/" + $("#nodeName").val(), "nodeData" : $("#nodeData").val(), "encryptType" :$("#encryptType").val()};

                ajaxBody("/Zookeeper-manager-web/base/insert", reqParam, function (resData) {
                    if (resData == "success") {
                        loadFunction('Add Node Success', 5000);
                    }
                });

                layer.close(index);
            }
        });
    });

    function loadFunction(msg, waitTime) {
        var index = layer.load(2, {time: 10 * 1000}); //又换了种风格，并且设定最长等待10秒
        setTimeout(function () {
            layer.msg(msg, {
                icon: 1,
                time: 2000 //2秒关闭（如果不配置，默认是3秒）
            }, function () {
                //关闭
                layer.close(index);
                location.reload();
            });
        }, waitTime);
    }
    $(document).on("click", "#delete", function () {
        var nodeName = $(this).parent().attr("nodeName");
        layer.confirm('您确定删除该节点（包括子节点）？', {
            btn: ['确定','取消'] //按钮
        }, function() {
            var reqParam = {"nodeName":nodeName};
            ajaxBody("/Zookeeper-manager-web/base/delete", reqParam, function (resData) {
                if (resData == "success") {
                    loadFunction('Delete Node Success', 10000);
                }
            });
        });
    });

    $(document).on("click", "#modify", function () {
        var nodeName = $(this).parent().attr("nodeName");

        var reqParam = {"nodeName":nodeName };

        ajaxBody("/Zookeeper-manager-web/base/getData", reqParam, function (resData) {
            if (resData != null) {
                var content = "<form>" +
                    "<div class='form-group'><label for='Node Name' class='col-sm-2 control-label'>NodeName</label>" +
                    "<div class=\"col-sm-10\"><input type=\"type\" class=\"form-control\" id=\"nodeName\" placeholder=\"NodeName\" readonly=\"readonly\" value=" + nodeName + "></div></div><br />"+
                    "<div class='form-group'><label for='Node Data' class='col-sm-2 control-label'>NodeData</label>" +
                    "<div class=\"col-sm-10\"><input type=\"type\" class=\"form-control\" id=\"nodeData\" placeholder=\"NodeData\" value=" + resData + "></div></div><br />"+
                    "<div class='form-group'><label for='Node Name' class='col-sm-2 control-label'>encryptType</label>" +
                    "<div class=\"col-sm-10\">"+
                    "<select class=\"form-control\" id=\"encryptType\" name=\"encryptType\">" +
                    "<option value=\"\">not encrypt</option>" +
                    "<option value=\"sha1\">sha1</option>" +
                    "<option value=\"md5\">md5</option>" +
                    "</select></div></div>" +

                    "</form>";

                //页面层
                layer.open({
                    type: 1,
                    skin: 'layui-layer-rim', //加上边框
                    area: ['600px', '400px'], //宽高
                    content: content,
                    btn: ['yes', 'no'],
                    yes : function (index, layer1) {
                        reqParam = {"nodeName":nodeName, "nodeData" : $("#nodeData").val(), "encryptType" :$("#encryptType").val()};

                        ajaxBody("/Zookeeper-manager-web/base/modify", reqParam, function (resData) {
                            if (resData == "success") {
                                loadFunction('Modify Node Success', 5000);
                            }
                        });

                        layer.close(index);
                    }
                });
            }
        });
    });



    $(document).on("click", "#upload", function () {
        var nodeName = $(this).parent().attr("nodeName");

        var content = "<form method=\"post\" action=\"/Zookeeper-manager-web/uploadController/upload\" id=\"nodeDataForm\" enctype=\"multipart/form-data\">" +
                "<div class=\"form-group\">" +
                "<label for=\"exampleInputFile\">File input</label>" +
                "<input type=\"file\" id=\"file\" name=\"file\">" +
                "<p class=\"help-block\">Example block-level help text here.</p>" +
                "</div>" +
                "<input type=hidden value=" + nodeName + " id=\"nodeName\" name=\"nodeName\">" +
                "<button type=\"submit\" class=\"btn btn-default\" id=\"uploadBtn\">Submit</button>" +
                "</form>";

        //页面层
        layer.open({
            type: 1,
            skin: 'layui-layer-rim', //加上边框
            area: ['600px', '400px'], //宽高
            content: content
        });


        $(document).on("click", "#uploadBtn", function () {
            loadFunction('upload file Success', 10000);
        });
    });


    function ajaxBody(url, reqParam, callback) {
        $.ajax({
            url:url,
            type:"post",
            data: JSON.stringify(reqParam),
            contentType:"application/json",
            async: true,
            success:function(data){
                callback(data, reqParam);
            },
            error: function(XMLHttpRequest, textStatus, errorThrown) {
                console.log("请求异常状态:" + XMLHttpRequest.status);
                console.log(XMLHttpRequest);
                alert('请求服务器异常:' + XMLHttpRequest.status)
            }
        });
    }




</script>
</html>
