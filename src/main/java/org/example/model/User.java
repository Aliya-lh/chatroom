package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据库实体类：
 * http请求可能把请求数据转换为一个实体类对象
 * http响应可能把实体类对象/List<实体类>返回给客户端
 * 数据库CRUD操作，都可能使用实体类对象来操作
 * 查询返回一个或多个实体类对象
 * 插入/修改就是使用实体类对象的属性来作为插入/修改的值
 */
@Setter//生成所有属性的setter方法
@Getter
@ToString
public class User {
    private Integer id;
    private String username;
    private String password;
    private String nickname;
    private String head;
    private java.util.Date logoutTime;
}
