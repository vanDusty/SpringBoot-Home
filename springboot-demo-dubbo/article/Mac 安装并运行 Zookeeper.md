# Mac 安装并运行 Zookeeper

## 一、下载解压

1. [Zookeeper下载地址](http://mirror.bit.edu.cn/apache/zookeeper)，点击后下载适合稳定的版本,我选择的是 `3.4.14`。
1. 解压下载得到的ZooKeeper压缩包，发现有`bin，conf，lib`等目录。

> `bin`目录中存放有运行脚本；`conf`目录中存放有配置文件；`lib`目录”中存放有运行所需要第三方库。

## 二、配置

### 2.1 单机模式

#### 2.1.1 编辑配置文件

在`conf`目录下，新建一个名为`zoo.cfg`的文件，其中内容如下：

```
# 服务器与客户端之间交互的基本时间单元（ms） 
tickTime=2000   
# zookeeper所能接受的客户端数量 
initLimit=10  
# 服务器和客户端之间请求和应答之间的时间间隔 
syncLimit=5
# zookeeper中使用的基本时间单位, 毫秒值.
tickTime=2000
# 数据目录. 可以是任意目录.
dataDir=/tmp/zookeeper/data
# log目录, 同样可以是任意目录. 如果没有设置该参数, 将使用和#dataDir相同的设置.
dataLogDir=/tmp/zookeeper/log
# t监听client连接的端口号.
clientPort=2181
```

#### 2.1.2 运行ZooKeeper Server

进入解压目录(`zookeeper-3.4.14`)，执行以下命令：

```
## 启动ZooKeeper
./bin/zkServer.sh start
## 停止ZooKeeper
./bin/zkServer.sh stop
```
得到如图所示结果，表示后台运行ZooKeeper Server进程成功。

```
zhangfandeMacBook-Pro:zookeeper-3.4.14 zhangfan$ ./bin/zkServer.sh start
ZooKeeper JMX enabled by default
Using config: /Users/zhangfan/Documents/测试/dubbo/zookeeper-3.4.14/bin/../conf/zoo.cfg
Starting zookeeper ... STARTED
zhangfandeMacBook-Pro:zookeeper-3.4.14 zhangfan$
```


### 2.2 集群

参考[1](https://blog.csdn.net/a82793510/article/details/66971468)
