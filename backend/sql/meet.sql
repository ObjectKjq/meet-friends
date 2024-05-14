-- 创建数据库
CREATE DATABASE `user-center` charset=utf8mb4;

USE `user-center`;

DROP TABLE IF EXISTS `number_statistics`;
CREATE TABLE `number_statistics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `day` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '当日',
  `userId` bigint(20) DEFAULT NULL COMMENT '用户id',
  `count` int(11) DEFAULT NULL COMMENT '浏览量',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `isDelete` tinyint(4) DEFAULT '0' COMMENT '逻辑删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

insert  into `number_statistics`(`id`,`day`,`userId`,`count`,`updateTime`,`createTime`,`isDelete`) values 
(1,'2024-03-22 15:18:26',3,4,'2024-03-22 15:18:38','2024-03-22 15:18:40',0),
(2,'2024-03-23 00:00:00',2,1,'2024-03-22 23:16:41','2024-03-22 23:16:41',0),
(3,'2024-03-23 00:00:00',3,359,'2024-03-22 23:20:01','2024-03-22 23:20:01',0),
(4,'2024-03-23 00:00:00',4,5,'2024-03-23 00:16:41','2024-03-23 00:16:41',0),
(5,'2024-03-23 00:00:00',5,12,'2024-03-23 00:16:43','2024-03-23 00:16:43',0),
(6,'2024-03-23 00:00:00',6,4,'2024-03-23 00:16:45','2024-03-23 00:16:45',0),
(7,'2024-03-23 00:00:00',7,23,'2024-03-23 00:16:49','2024-03-23 00:16:49',0),
(8,'2024-03-23 00:00:00',8,34,'2024-03-23 00:17:06','2024-03-23 00:17:06',0),
(9,'2024-03-23 00:00:00',9,88,'2024-03-23 00:17:07','2024-03-23 00:17:07',0),
(10,'2024-03-23 00:00:00',10,78,'2024-03-23 00:17:08','2024-03-23 00:17:08',0),
(11,'2024-03-23 00:00:00',11,56,'2024-03-23 00:17:11','2024-03-23 00:17:11',0),
(12,'2024-03-24 00:00:00',3,1,'2024-03-24 00:43:23','2024-03-24 00:43:23',0),
(13,'2024-04-22 00:00:00',4,1,'2024-04-22 14:12:01','2024-04-22 14:12:01',0),
(14,'2024-04-22 00:00:00',3,1,'2024-04-22 14:12:46','2024-04-22 14:12:46',0),
(15,'2024-04-22 00:00:00',5,1,'2024-04-22 14:29:54','2024-04-22 14:29:54',0),
(16,'2024-04-26 00:00:00',3,20,'2024-04-26 11:38:17','2024-04-26 11:38:17',0),
(17,'2024-04-25 00:00:00',2,123,'2024-04-26 17:21:37','2024-04-26 17:21:40',0),
(18,'2024-04-25 00:00:00',3,1231,'2024-04-26 17:21:59','2024-04-26 17:21:59',0),
(19,'2024-04-26 00:00:00',4,1,'2024-04-26 18:19:30','2024-04-26 18:19:30',0);

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
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

insert  into `tag`(`id`,`tagName`,`userId`,`parentId`,`isParent`,`createTime`,`updateTime`,`isDelete`) values 
(1,'性别',3,NULL,1,'2024-03-17 16:21:12','2024-03-17 16:21:12',0),
(2,'男',3,1,0,'2024-03-17 16:21:22','2024-03-17 16:21:22',0),
(3,'女',3,1,0,'2024-03-17 16:21:25','2024-03-17 16:21:25',0),
(4,'方向',3,NULL,1,'2024-03-17 16:22:26','2024-03-17 16:22:26',0),
(5,'java',3,4,0,'2024-03-17 16:22:51','2024-03-17 16:22:51',0),
(6,'c++',3,4,0,'2024-03-17 16:23:00','2024-03-17 16:23:00',0),
(7,'Go',3,4,0,'2024-03-17 16:23:17','2024-03-17 16:23:17',0),
(8,'javascript',3,4,0,'2024-03-17 16:23:43','2024-03-17 16:23:43',0),
(9,'身份',3,NULL,1,'2024-03-17 16:24:10','2024-03-17 16:24:10',0),
(10,'大一',3,9,0,'2024-03-17 16:24:35','2024-03-17 16:24:35',0),
(11,'大二',3,9,0,'2024-03-17 16:24:46','2024-03-17 16:24:46',0),
(12,'大三',3,9,0,'2024-03-17 16:24:53','2024-03-17 16:24:53',0),
(13,'大四',3,9,0,'2024-03-17 16:25:12','2024-03-17 16:25:12',0),
(14,'已就业',3,9,0,'2024-03-17 16:25:35','2024-03-17 16:25:35',0),
(15,'待就业',3,9,0,'2024-03-17 16:25:49','2024-03-17 16:25:49',0),
(16,'研一',3,9,0,'2024-03-17 16:26:04','2024-03-17 16:26:04',0),
(17,'研二',3,9,0,'2024-03-17 16:26:16','2024-03-17 16:26:16',0),
(18,'研三',3,9,0,'2024-03-17 16:26:31','2024-03-17 16:26:31',0);

DROP TABLE IF EXISTS `team`;
CREATE TABLE `team` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '队伍的主键',
  `name` varchar(100) DEFAULT NULL COMMENT '队伍的名称',
  `description` varchar(100) DEFAULT NULL COMMENT '队伍的描述',
  `maxNum` int(11) DEFAULT NULL COMMENT '队伍能加入的最大人数',
  `expireTime` datetime DEFAULT NULL COMMENT '队伍的过期时间',
  `userId` bigint(20) DEFAULT NULL COMMENT '队长',
  `status` int(11) DEFAULT NULL COMMENT '队伍状态0-公开，1-私有，2-加密',
  `password` varchar(100) DEFAULT NULL COMMENT '加密后队伍密码',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

insert  into `team`(`id`,`name`,`description`,`maxNum`,`expireTime`,`userId`,`status`,`password`,`createTime`,`updateTime`,`isDelete`) values 
(1,'帝国大厦','做项目',10,'2024-03-30 00:20:13',2,0,NULL,'2024-03-16 00:20:41','2024-03-16 00:20:44',0),
(2,'哈哈','宇宙第一',3,'2025-09-17 00:00:00',3,0,'','2024-03-17 00:11:35','2024-03-17 00:11:35',0),
(3,'哈哈','宇宙第一',3,'2024-09-17 00:00:00',2,2,'123','2024-03-17 00:12:34','2024-03-17 00:12:34',0),
(4,'hui','坏了，被你发现最厉害的队伍了',3,'2033-10-17 00:00:00',3,1,'','2024-03-17 00:14:10','2024-03-17 00:14:10',0),
(5,'kjq队伍1','公开队伍',5,'2025-03-17 00:00:00',4,0,'','2024-03-17 00:39:01','2024-03-17 00:39:01',0),
(6,'kjq队伍2','私有队伍',5,'2025-03-17 00:00:00',4,1,'','2024-03-17 00:39:15','2024-03-17 00:39:15',0),
(7,'kjq队伍3','加密队伍',5,'2025-03-17 00:00:00',4,2,'123','2024-03-17 00:39:31','2024-03-17 00:39:31',0),
(8,'wer队伍1','公开队伍',6,'2025-03-17 00:00:00',5,0,'','2024-03-17 00:40:08','2024-03-17 00:40:08',0),
(9,'wer队伍2','私有队伍',6,'2025-03-17 00:00:00',5,1,'','2024-03-17 00:40:18','2024-03-17 00:40:18',0),
(10,'wer队伍3','加密队伍',6,'2025-03-17 00:00:00',5,2,'123','2024-03-17 00:40:28','2024-03-17 00:40:28',0),
(11,'哈哈','测试用户创建队伍',6,'2025-03-17 00:00:00',3,2,'123','2024-03-17 16:08:59','2024-03-17 16:08:59',0),
(12,'哈哈','测试创建公共队伍',3,'2024-08-17 00:00:00',3,0,'','2024-03-17 16:11:06','2024-03-17 16:11:06',0);

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `username` varchar(255) DEFAULT 'qwe' COMMENT '昵称',
  `userAccount` varchar(255) DEFAULT NULL COMMENT '账号',
  `avatarUrl` varchar(1024) DEFAULT 'https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/unicloudlogo.png' COMMENT '用户头像',
  `gender` tinyint(4) DEFAULT NULL COMMENT '性别',
  `userPassword` varchar(512) NOT NULL COMMENT '密码',
  `phone` varchar(128) DEFAULT NULL COMMENT '手机号',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `userStatus` int(11) NOT NULL DEFAULT '0' COMMENT '是否有效,0正常1无效',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `userRole` int(11) DEFAULT '0' COMMENT '用户角色0，普通用户1管理员用户',
  `tags` varchar(1024) DEFAULT NULL COMMENT '用户标签列表json格式',
  `profile` varchar(512) DEFAULT NULL COMMENT '个人简介',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='用户';

insert  into `user`(`id`,`username`,`userAccount`,`avatarUrl`,`gender`,`userPassword`,`phone`,`email`,`userStatus`,`createTime`,`updateTime`,`isDelete`,`userRole`,`tags`,`profile`) values 
(2,'kjq','123','https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/unicloudlogo.png',0,'123','12312312312','12@qq.com',0,'2024-03-11 01:02:26','2024-03-11 01:02:26',0,0,'[\"男\",\"c++\",\"Go\",\"大二\",\"大一\"]','我是一名管理员'),
(3,'qw哈哈','qwe','https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/unicloudlogo.png',1,'713e1abda91ecbc38fcc383774862806','13213','sfdjkljl@qq.com',0,'2024-03-11 01:49:23','2024-03-11 01:49:23',0,1,'[\"男\",\"Go\",\"大二\",\"大一\",\"java\",\"c++\",\"javascript\"]','我是一名管理员，太长了'),
(4,'kjq','kjq','https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/unicloudlogo.png',1,'2170836ca489471cda1db8c6a87b4d02',NULL,NULL,0,'2024-03-11 22:07:01','2024-03-11 22:07:01',0,0,'[\"男\",\"c++\",\"Go\",\"大二\",\"大一\"]','我是一名管理员'),
(5,'wer','wer','https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/unicloudlogo.png',1,'a89d2efc462b211bc7e7ab429db361f1',NULL,NULL,0,'2024-03-11 22:09:28','2024-03-11 22:09:28',0,0,'[\"男\",\"c++\",\"Go\",\"大二\",\"大一\"]','我是一名管理员'),
(6,'qwe','小明1','https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/unicloudlogo.png',0,'713e1abda91ecbc38fcc383774862806',NULL,NULL,0,'2024-03-12 17:09:09','2024-03-12 17:09:09',0,0,'[\"男\",\"c++\",\"Go\",\"大二\",\"大一\"]','我是一名管理员'),
(7,'qwe','小强sd','https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/unicloudlogo.png',0,'2170836ca489471cda1db8c6a87b4d02',NULL,NULL,0,'2024-03-12 17:10:56','2024-03-12 17:10:56',0,0,'[\"男\",\"c++\",\"Go\",\"大二\",\"大一\"]','我是一名管理员'),
(8,'qwe','itcast','https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/unicloudlogo.png',0,'2170836ca489471cda1db8c6a87b4d02',NULL,NULL,0,'2024-03-12 17:11:16','2024-03-12 17:11:16',0,0,'[\"男\",\"c++\",\"Go\",\"大二\",\"大一\"]','我是一名管理员'),
(9,'qwe','safdsa','https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/unicloudlogo.png',0,'2170836ca489471cda1db8c6a87b4d02',NULL,NULL,0,'2024-03-12 17:11:34','2024-03-12 17:11:34',0,0,'[\"男\",\"c++\",\"Go\",\"大二\",\"大一\"]','我是一名管理员'),
(10,'qwe','qwedsfds','https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/unicloudlogo.png',0,'713e1abda91ecbc38fcc383774862806',NULL,NULL,0,'2024-03-12 17:26:22','2024-03-12 17:26:22',0,0,'[\"男\",\"c++\",\"Go\",\"大二\",\"大一\"]','我是一名管理员'),
(11,'kjq','123','https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/unicloudlogo.png',0,'123','12312312312','12@qq.com',0,'2024-03-12 21:52:54','2024-03-12 21:52:54',0,0,'[\"男\",\"c++\",\"Go\",\"大二\",\"大一\"]','我是一名管理员'),
(12,'qwe','新加用户','https://qiniu-web-assets.dcloud.net.cn/unidoc/zh/unicloudlogo.png',0,'2170836ca489471cda1db8c6a87b4d02','qwewqewe','weqew',0,'2024-03-17 01:57:47','2024-03-17 01:57:47',0,0,'[\"男\",\"c++\",\"Go\",\"大二\",\"大一\"]','qweqweqwe'),
(24,'ert','ert','http://localhost:9000/meet-friends/2024/04/26/d98a229627f148b3aa5d876faa98d1b41650272740786.png',0,'123','123123123','123@qq.com',0,'2024-04-26 15:18:55','2024-04-26 15:18:55',0,0,NULL,'测试添加用户');

DROP TABLE IF EXISTS `user_team`;
CREATE TABLE `user_team` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户与队伍联系的主键',
  `userId` bigint(20) NOT NULL COMMENT '外键，用户的id',
  `teamId` bigint(20) NOT NULL COMMENT '外键，队伍的id',
  `joinTime` datetime DEFAULT NULL COMMENT '加入时间',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint(4) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

insert  into `user_team`(`id`,`userId`,`teamId`,`joinTime`,`createTime`,`updateTime`,`isDelete`) values 
(1,2,1,'2024-03-24 00:21:38','2024-03-16 00:21:43','2024-03-16 00:21:46',0),
(2,3,2,'2024-03-17 00:11:36','2024-03-17 00:11:35','2024-03-17 00:11:35',0),
(3,2,3,'2024-03-17 00:12:34','2024-03-17 00:12:34','2024-03-17 00:12:34',0),
(4,3,4,'2024-03-17 00:14:10','2024-03-17 00:14:10','2024-03-17 00:14:10',0),
(5,4,5,'2024-03-17 00:39:01','2024-03-17 00:39:01','2024-03-17 00:39:01',0),
(6,4,6,'2024-03-17 00:39:15','2024-03-17 00:39:15','2024-03-17 00:39:15',0),
(7,4,7,'2024-03-17 00:39:31','2024-03-17 00:39:31','2024-03-17 00:39:31',0),
(8,5,8,'2024-03-17 00:40:09','2024-03-17 00:40:08','2024-03-17 00:40:08',0),
(9,5,9,'2024-03-17 00:40:18','2024-03-17 00:40:18','2024-03-17 00:40:18',0),
(10,5,10,'2024-03-17 00:40:29','2024-03-17 00:40:28','2024-03-17 00:40:28',0),
(11,3,1,'2024-03-17 00:41:12','2024-03-17 00:41:11','2024-03-17 00:41:11',1),
(12,3,3,'2024-03-17 00:43:19','2024-03-17 00:43:19','2024-03-17 00:43:19',0),
(13,3,5,'2024-03-17 00:43:48','2024-03-17 00:43:48','2024-03-17 00:43:48',0),
(14,3,1,'2024-03-17 01:39:18','2024-03-17 01:39:18','2024-03-17 01:39:18',1),
(15,3,11,'2024-03-17 16:08:59','2024-03-17 16:08:59','2024-03-17 16:08:59',0),
(16,3,12,'2024-03-17 16:11:06','2024-03-17 16:11:06','2024-03-17 16:11:06',0);
