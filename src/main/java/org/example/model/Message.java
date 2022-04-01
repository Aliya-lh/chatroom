package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Message {
    private Integer id;
    private Integer userId;
    private String userNickname;
    private String content;
    private Integer channelId;
    private java.util.Date sendTime;
}
