# 警告！！

**本项目中包含：**

​	**HanYuPinYin**

​	**命名不规范**

​	**代码重复**

基本操作，无需吐槽

# TIPS：

感谢大佬提供的[机器人框架](https://github.com/OPQBOT/OPQ/wiki)技术支持，有关框架的部署请参考上述文档。

本项目主要逻辑均基于SpringBoot+MyBatis+MySQL实现，采用机器人框架->调用API->返回数据->发送消息。

# 项目结构

└─main
    ├─java
    │  └─com
    │      └─wzy
    │          └─arknights
    │              ├─config --Spring配置文件
    │              ├─controller --控制层：Controller文件
    │              ├─dao --dao层：Mapper文件
    │              ├─job --定时任务
    │              ├─model --JavaBean
    │              ├─service --服务层：计算逻辑
    │              │  └─impl
    │              ├─util --Util工具类
    │              └─vo --Request返回类型
    └─resources
        ├─lua --lua插件：对接机器人框架
        ├─mapping --Mapping文件，SQL
        └─sql --数据库存档