package com.intcore.chatdemo.model;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.io.Serializable;

public class ChatModel implements Serializable {

    private String id;
    private IUser buddy;
    private IMessage lastMessage;
    private int unreadCount;

    public ChatModel(String id, IUser buddy, IMessage lastMessage, int unreadCount) {
        this.id = id;
        this.buddy = buddy;
        this.lastMessage = lastMessage;
        this.unreadCount = unreadCount;
    }

    public String getId() {
        return id;
    }

    public IUser getBuddy() {
        return buddy;
    }

    public IMessage getLastMessage() {
        return lastMessage;
    }

    public int getUnreadCount() {
        return unreadCount;
    }
}
