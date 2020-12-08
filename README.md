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

main：

​	java：

​		config:Spring配置文件

​		controller：控制层

​		dao：Mapper文件，mapping的interface

​		job：定时任务

​		model：数据封装bean类

​		service：服务类，主要计算逻辑

​		util：工具类，存放静态方法

​		vo：返回类型封装

​	resource：

​		lua：lua插件留底

​		mapping：mapping文件，SQL语句

​		sql：数据库结构留底