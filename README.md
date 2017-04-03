# Zookeeper节点后台管理系统
包括新增节点、删除节点、修改节点信息、及上传文件并根据文件的键值生成对应zookeeper的键值

# 文档
文档: https://github.com/litter-fish/zookeeper-node-manager/wiki

# 项目配置
配置zookeeper地址、及加载的根路径创建zookeeper-config.properties文件，修改如下内容
```
#zookeeper地址及端口信息，集群环境请使用","分开
zk.configs.address=localhost:2181
#加载的根节点
zk.configs.node=/hk4
```
