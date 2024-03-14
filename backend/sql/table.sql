-- 创建数据
CREATE DATABASE `user-center`;

USE `user-center`;

DROP TABLE IF EXISTS `tag`;

CREATE TABLE `tag` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `tagName` varchar(255) DEFAULT NULL COMMENT '标签名称',
   `userId` bigint(20) DEFAULT NULL COMMENT '上传标签的用户',
   `parentId` bigint(20) DEFAULT NULL COMMENT '该标签是哪个分类下的',
   `isParent` tinyint(4) DEFAULT NULL COMMENT '是否为分类标签',
   `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `updateTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
   `isDelete` tinyint(4) DEFAULT NULL COMMENT '是否删除',
   PRIMARY KEY (`id`),
   UNIQUE KEY `unildx_tagName` (`tagName`),
   KEY `idx_userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username` varchar(255) DEFAULT 'qwe' COMMENT '昵称',
    `userAccount` varchar(255) DEFAULT NULL COMMENT '账号',
    `avatarUrl` varchar(1024) DEFAULT NULL COMMENT '用户头像',
    `gender` tinyint(4) DEFAULT NULL COMMENT '性别',
    `userPassword` varchar(512) NOT NULL COMMENT '密码',
    `phone` varchar(128) DEFAULT NULL COMMENT '手机号',
    `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
    `userStatus` int(11) NOT NULL DEFAULT '0' COMMENT '是否有效,0正常1无效',
    `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updateTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
    `userRole` int(11) DEFAULT '0' COMMENT '用户角色0，普通用户1管理员用户',
    `tags` varchar(1024) DEFAULT NULL COMMENT '用户标签列表',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='用户';


-- 插入数据
insert  into
    `user`(`id`,`username`,`userAccount`,`avatarUrl`,`gender`,`userPassword`,`phone`,`email`,`userStatus`,`createTime`,`updateTime`,`isDelete`,`userRole`,`tags`)
values
    (2,'kjq','123','https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/unicloudlogo.png',0,'123','12312312312','12@qq.com',0,'2024-03-11 01:02:26','2024-03-11 01:02:26',0,0,NULL),
    (3,'qwe','qwe','https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/unicloudlogo.png',NULL,'713e1abda91ecbc38fcc383774862806',NULL,NULL,0,'2024-03-11 01:49:23','2024-03-11 01:49:23',0,1,NULL),
    (4,'kjq','kjq',NULL,NULL,'2170836ca489471cda1db8c6a87b4d02',NULL,NULL,0,'2024-03-11 22:07:01','2024-03-11 22:07:01',0,0,NULL),
    (5,'wer','wer',NULL,NULL,'a89d2efc462b211bc7e7ab429db361f1',NULL,NULL,0,'2024-03-11 22:09:28','2024-03-11 22:09:28',0,0,NULL),
    (6,'qwe','小明1',NULL,NULL,'713e1abda91ecbc38fcc383774862806',NULL,NULL,0,'2024-03-12 17:09:09','2024-03-12 17:09:09',0,0,NULL),
    (7,'qwe','小强sd',NULL,NULL,'2170836ca489471cda1db8c6a87b4d02',NULL,NULL,0,'2024-03-12 17:10:56','2024-03-12 17:10:56',0,0,NULL),
    (8,'qwe','itcast',NULL,NULL,'2170836ca489471cda1db8c6a87b4d02',NULL,NULL,0,'2024-03-12 17:11:16','2024-03-12 17:11:16',0,0,NULL),
    (9,'qwe','safdsa',NULL,NULL,'2170836ca489471cda1db8c6a87b4d02',NULL,NULL,0,'2024-03-12 17:11:34','2024-03-12 17:11:34',0,0,NULL),
    (10,'qwe','qwedsfds',NULL,NULL,'713e1abda91ecbc38fcc383774862806',NULL,NULL,0,'2024-03-12 17:26:22','2024-03-12 17:26:22',0,0,NULL),
    (11,'kjq','123','https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/unicloudlogo.png',0,'123','12312312312','12@qq.com',0,'2024-03-12 21:52:54','2024-03-12 21:52:54',0,0,NULL);