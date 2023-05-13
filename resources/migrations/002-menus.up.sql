create table menus (
  `id` int unsigned not null auto_increment primary key,
  `type` char(1) default null comment 'P=Private,O=Open',
  `admin` char(1) default null comment 'T=true,F=false',
  `secure` int(1) comment '0=Public menu,1=System,2=Admin/System,3=Everyone',
  `root` varchar(500) default null,
  `link` varchar(200) default null,
  `description` varchar(500) default null
) engine=innodb default charset=utf8
