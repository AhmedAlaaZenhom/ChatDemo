package com.intcore.chatdemo.model;

import androidx.annotation.Nullable;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.util.Date;

public class MessageModel implements IMessage, MessageContentType, MessageContentType.Image {

    private String id;
    private String body;
    private IUser sender;
    private Date createdAt;
    @MessageType
    private int type;

    public MessageModel(String id, String body, IUser sender, Date createdAt) {
        this(id, body, sender, createdAt, MessageType.TEXT);
    }

    public MessageModel(String id, String body, IUser sender, Date createdAt, @MessageType int type) {
        this.id = id;
        this.body = body;
        this.sender = sender;
        this.createdAt = createdAt;
        this.type = type;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return body;
    }

    @Override
    @Nullable
    public IUser getUser() {
        return sender;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    public int getType() {
        return type;
    }

    @Nullable
    @Override
    public String getImageUrl() {
        return this.type == MessageType.IMAGE ? body : null;
    }

    public String getVoiceUrl() {
        return this.type == MessageType.VOICE ? body : null;
    }
}
