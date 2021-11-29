## 联系我们
ToB Dev 李月喜专注B端企业服务开发，微信/企业微信开发；       
ToB Dev网址: wx.tobdev.com（完善中）;   
加企业微信开发同行群及咨询联系李月喜"li570467731";    
获取更多企业微信教程及资料关注公众号ToB Dev 微信号“tobdev”;  

## 企业微信自自建内部应用  
自建内部应用视频教程：  
https://mp.weixin.qq.com/mp/appmsgalbum?__biz=MzA5ODcyODY0Nw==&action=getalbum&album_id=1745513894715916289#wechat_redirect  

自建内部应用demo源码：  
前端vite+vuejs:https://github.com/liyuexi/qywx-vuejs  
后端java+springboot:https://github.com/liyuexi/qywx-inner-java  

在线Demo地址：  
http://h5test.tobdev.com    
进入应用后点菜单设置进行应用设置

##企业微信自自建内部应用后端demo
### 在线Demo
http://h5test.tobdev.com  
进入应用后点菜单设置进行应用设置

### 最近更新  
2021/11/29 增加在线Demo及调试工具

### demo简介  
本项目为企业微信自建内部应用后端demo
https://github.com/liyuexi/qywx-inner-java  
springboot,mysql 

将应用信息填写入表中
```sql 
CREATE TABLE `qywx_inner_company` (
 `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
 `app_id` int(10) unsigned NOT NULL DEFAULT '1' COMMENT 'tobdev应用id 可以不写',
 `corp_id` varchar(45) NOT NULL DEFAULT '' COMMENT '企业id',
 `agent_id` int(10) DEFAULT '0' COMMENT '应用id',
 `agent_secret` varchar(512) NOT NULL DEFAULT '' COMMENT '应用密钥',
 `corp_name` varchar(50) NOT NULL DEFAULT '' COMMENT '企业名称',
 `corp_full_name` varchar(100) NOT NULL DEFAULT '' COMMENT '企业全称',
 `subject_type` varchar(512) NOT NULL DEFAULT '' COMMENT '企业类型',
 `verified_end_time` varchar(512) NOT NULL DEFAULT '' COMMENT '企业认证到期时间',
 `status` tinyint(3) DEFAULT '0' COMMENT '账户状态，-1为删除，禁用为0 启用为1',
 `addtime` int(10) unsigned DEFAULT '0' COMMENT '创建时间',
 `modtime` int(10) unsigned DEFAULT '0' COMMENT '修改时间',
 `rectime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '变动时间',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='企业微信自建内部应用公司';

#20210928
ALTER TABLE `qywx_inner_company` ADD `approval_template_id` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '审批流程引擎模板id' AFTER `verified_end_time`;
#20211127
ALTER TABLE `qywx_inner_company` CHANGE `agent_id` `agent_id` VARCHAR(1045) NULL DEFAULT '' COMMENT '授权应用id';

```
