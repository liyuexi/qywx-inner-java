# qywx-inner-java
企业微信自建内部应用demo java版
## 
基于springboot,mysql 

将应用信息填写入表中
```sql 
CREATE TABLE `qywx_inner_company` (
 `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
 `app_id` int(10) unsigned NOT NULL DEFAULT '1' COMMENT 'tobdev应用id',
 `corp_id` varchar(45) NOT NULL DEFAULT '' COMMENT '企业id',
 `agent_id` int(10) DEFAULT '0' COMMENT '授权应用id',
 `agent_secret` varchar(512) NOT NULL DEFAULT '' COMMENT '企业永久授权码',
 `corp_name` varchar(50) NOT NULL DEFAULT '' COMMENT '企业名称',
 `corp_full_name` varchar(100) NOT NULL DEFAULT '' COMMENT '企业全称',
 `subject_type` varchar(512) NOT NULL DEFAULT '' COMMENT '企业类型',
 `verified_end_time` varchar(512) NOT NULL DEFAULT '' COMMENT '企业认证到期时间',
 `status` tinyint(3) DEFAULT '0' COMMENT '账户状态，-1为删除，禁用为0 启用为1',
 `addtime` int(10) unsigned DEFAULT '0' COMMENT '创建时间',
 `modtime` int(10) unsigned DEFAULT '0' COMMENT '修改时间',
 `rectime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '变动时间',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='企业微信自建内部应用公司
```