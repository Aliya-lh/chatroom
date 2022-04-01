drop database if exists chatroom;
create database chatroom character set utf8mb4;

use chatroom;

create table user(
    id int primary key auto_increment,
    username varchar(20) not null unique comment '账号',
    password varchar(50) not null comment '密码',
    nickname varchar(20) not null comment '昵称',
    head varchar(50) comment '头像url（相对路径）',
    logout_time datetime comment '退出登录时间'
) comment '用户表';

insert into user(username, password, nickname) values('a', '123', '海绵宝宝');
insert into user(username, password, nickname) values('b', '123', '派大星');
insert into user(username, password, nickname) values('c', '123', '章鱼哥');

create table channel(
    id int primary key auto_increment,
    name varchar(20) not null unique comment '频道名称'
) comment '频道';

insert into channel(name) values ('海底公寓');
insert into channel(name) values ('蟹老板的店');
insert into channel(name) values ('秘密基地');

create table message(
    id int primary key auto_increment,
    user_id int comment '消息发送方：用户id',
    user_nickname varchar(20) comment '消息发送方：用户昵称（历史消息展示需要）',
    channel_id int comment '消息接收方：频道id',
    content varchar(255) comment '消息内容',
    send_time datetime comment '消息发送时间',
    foreign key (user_id) references user(id),
    foreign key (channel_id) references channel(id)
) comment '发送的消息记录';