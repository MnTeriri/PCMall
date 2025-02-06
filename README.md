# 电脑配件商城（PCMall）

## 项目说明

这是一个Java项目，采用前后端分离，实现电脑配件商城的基本功能，比如：

* 用户登录注册
* 商品的查询和浏览
* 商品品牌管理
* 商品分类管理
* 购物车管理
* 地址管理
* 订单管理（可退货可取消）
* 用户基本信息管理
* ...

Android App项目说明：[PCMall-Mobile](https://github.com/MnTeriri/PCMall-Mobile)<br>
Vue项目说明：[PCMall-Vue](https://github.com/MnTeriri/PCMall-Vue)

## 使用的框架

* Spring Boot
* Spring Cloud
* Nacos
* Seata
* Sentinel
* Spring Cloud GateWay
* Loadbalancer
* OpenFeign
* Spring Security
* Spring AOP
* Spring Cache
* Spring Quartz
* Spring Redis
* MyBatis
* MyBatis-Plus
* Knife4j
* ...

## 版本说明

### v1.0：

1. 使用Nacos作为服务发现、注册、配置中心
2. 使用Spring Cloud GateWay作为API网关
3. 使用Spring Security、JWT实现鉴权
4. 使用Loadbalancer、OpenFeign完成微服务远程调用
5. 使用MyBatis和MyBatis-Plus实现持久层
6. 使用Redis实现MyBatis二级缓存
7. 使用Quartz完成定时任务处理
8. 使用存储过程，配合锁表和事务，完成订单的创建和取消，并使用MyBatis调用存储过程

### v2.0：

#### 相比v1.0增添如下功能：

1. 引入分布式事务Seata
2. 对服务提供者采取更细致的分割，为后续能实现分库分表做准备
3. 使用Spring AOP，灵活的对方法的功能进行拓展
4. 使用Spring Cache，对品牌、分类等服务提供者的缓存进行细致化管理
5. 使用CompletableFuture和ThreadPoolTaskExecutor完成异步远程调用
6. 使用Knife4j，完成API文档编写

## 架构图

### v1.0：

![图片](image/架构图v1.0.jpg)

### v2.0：

![图片](image/架构图v2.0.jpg)

## 界面效果

### 移动端

<table>
    <tr>
        <td><img src="image/Screenshot_20240809_162603.png"/></td>
        <td><img src="image/Screenshot_20240809_162616.png"/></td>
        <td><img src="image/Screenshot_20240809_162721.png"/></td>
        <td><img src="image/Screenshot_20240809_162730.png"/></td>
    </tr>
    <tr>
        <td><img src="image/Screenshot_20240809_162755.png"/></td>
        <td><img src="image/Screenshot_20240809_162816.png"/></td>
        <td><img src="image/Screenshot_20240809_162827.png"/></td>
        <td><img src="image/Screenshot_20240809_162841.png"/></td>
    </tr>
    <tr>
        <td><img src="image/Screenshot_20240809_162857.png"/></td>
        <td><img src="image/Screenshot_20240809_162903.png"/></td>
        <td><img src="image/Screenshot_20240809_162629.png"/></td>
        <td><img src="image/Screenshot_20240809_162638.png"/></td>
    </tr>
    <tr>
        <td><img src="image/Screenshot_20240809_162652.png"/></td>
    </tr>
</table>

### 管理端

## 项目结构

### v1.0：

~~~
PCMall
├── pcmall-common         // 通用模块
├── pcmall-gateway        // 网关模块 [10000]
├── pcmall-user-service   // 认证中心 [10001]
├── pcmall-image          // 图片中心 [10002]
├── pcmall-consumer       // 服务消费者模块
│      └── pcmall-consumer-admin                 // 管理端模块 [12000]
│      └── pcmall-consumer-mobile                // 移动端模块 [12001]
├── pcmall-provider       // 服务提供者模块
│      └── pcmall-provider-goods                 // 商品中心 [11000]
│      └── pcmall-provider-payment               // 订单中心 [11001]
│      └── pcmall-provider-user                  // 用户中心 [11002]
├──pom.xml                // 公共依赖
~~~

### v2.0：

~~~
PCMall
├── pcmall-common         // 通用模块
├── pcmall-gateway        // 网关模块 [10000]
├── pcmall-user-service   // 认证中心 [10001]
├── pcmall-image          // 图片中心 [10002]
├── pcmall-consumer       // 服务消费者模块
│      └── pcmall-consumer-admin                 // 管理端模块 [12000]
│      └── pcmall-consumer-mobile                // 移动端模块 [12001]
├── pcmall-provider       // 服务提供者模块
│      └── pcmall-provider-address               // 地址模块 [11000]
│      └── pcmall-provider-brand                 // 商品品牌模块 [11001]
│      └── pcmall-provider-cart                  // 购物车模块 [11002]
│      └── pcmall-provider-category              // 商品分类模块 [11003]
│      └── pcmall-provider-goods                 // 商品模块 [11004]
│      └── pcmall-provider-order                 // 订单模块 [11005]
│      └── pcmall-provider-storage               // 商品库存模块 [11006]
│      └── pcmall-provider-user                  // 用户模块 [11007]
├──pom.xml                // 公共依赖
~~~

## 核心代码

### 1. Spring Cloud GateWay配置

### 2. Seata的使用

1. 下载Seata  
   先在[Seata官网](https://seata.apache.org/zh-cn/unversioned/download/seata-server/)下载Seata安装包，然后解压
2. 配置Seata-Service  
   打开config/application.yml文件，修改如下部分

~~~yaml
seata:
  config:
    # support: nacos 、 consul 、 apollo 、 zk  、 etcd3
    type: nacos
    nacos:
      server-addr: 127.0.0.1:8848
      namespace: seata
      group: SEATA_GROUP
      data-id: seataServer.properties
    
  registry:
    # support: nacos 、 eureka 、 redis 、 zk  、 consul 、 etcd3 、 sofa
    type: nacos
    preferred-networks: 30.240.*
    nacos:
      application: seata-server
      server-addr: 127.0.0.1:8848
      namespace: seata
      group: SEATA_GROUP
      cluster: default

  store:
    # support: file 、 db 、 redis 、 raft
    mode: db
~~~

然后在Nacos当中创建命名空间（seata），然后在此命名空间创建seataServer.properties配置，
group设置为SEATA_GROUP（需要和上述配置的namespace、group属性相对应）。
设置好后将script/config-center/config.txt当中的内容复制，并修改此部分为下述代码（配置模式为db，并填写自己数据库配置）

~~~properties
#Transaction storage configuration, only for the server. The file, db, and redis configuration values are optional.
store.mode=db
store.lock.mode=db
store.session.mode=db
#Used for password encryption
store.publicKey=

#These configurations are required if the `store mode` is `db`. If `store.mode,store.lock.mode,store.session.mode` are not equal to `db`, you can remove the configuration block.
store.db.datasource=druid
store.db.dbType=mysql
store.db.driverClassName=com.mysql.cj.jdbc.Driver
store.db.url=jdbc:mysql://127.0.0.1:3306/seata?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8
store.db.user=root
store.db.password=root
store.db.minConn=5
store.db.maxConn=30
store.db.globalTable=global_table
store.db.branchTable=branch_table
store.db.distributedLockTable=distributed_lock
store.db.queryLimit=100
store.db.lockTable=lock_table
store.db.maxWait=5000
~~~

然后打开script/server/db/mysql.sql，创建数据库

3. 配置微服务模块  
pom.xml添加如下依赖
~~~xml
<!-- Spring Cloud Ailibaba Seata -->
<dependency>
    <groupId>io.seata</groupId>
    <artifactId>seata-spring-boot-starter</artifactId>
</dependency>
<dependency>
    <groupId>com.alibaba.cloud</groupId>
    <artifactId>spring-cloud-starter-alibaba-seata</artifactId>
    <exclusions>
        <exclusion>
            <groupId>io.seata</groupId>
            <artifactId>seata-spring-boot-starter</artifactId>
       </exclusion>
    </exclusions>
</dependency>
~~~
application.properties添加如下配置
~~~
seata.tx-service-group=my_test_tx_group ---------------> 事务分组配置（在v1.5之后默认值为default_tx_group）
seata.service.vgroup-mapping.my_test_tx_group=default  ---------------> 指定事务分组至集群映射关系（等号右侧的集群名需要与Seata-server注册到Nacos的cluster保持一致）
seata.registry.type=nacos      ---------------> 使用nacos作为注册中心
seata.registry.nacos.server-addr=127.0.0.1:8848
seata.registry.nacos.namespace=seata              ---------------> Seata命名空间（应与seata-server实际注册的命名空间一致）
seata.registry.nacos.application=seata-server     ---------------> Seata服务名（应与seata-server实际注册的服务名一致）
seata.registry.nacos.group=SEATA_GROUP            ---------------> Seata分组名（应与seata-server实际注册的分组名一致）
seata.data-source-proxy-mode=AT
~~~

4. 使用  
在所需的方法上加上注解@GlobalTransactional，代码如下
~~~java
@GlobalTransactional(rollbackFor = Exception.class)
public void outboundDelivery(Storage storage) {
    //业务逻辑
}
~~~

### 2. RocketMQ的使用

1. broker配置
   messageDelayLevel用于配置延迟等级对应的时间，brokerIP1用于配置broker的IP（Docker环境下如果不配置则会使用容器内地址，在宿主机环境下的程序无法访问到）。配置如下：

   ~~~
   messageDelayLevel=1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 15m 20m 30m 1h 2h
   brokerIP1=192.168.31.109
   ~~~

2. broker执行

   ~~~
   sh mqadmin updateTopic -c DefaultCluster -t test-topic
   ~~~