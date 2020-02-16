package com.intcore.chatdemo.model;

import com.stfalcon.chatkit.commons.models.IUser;

import java.io.Serializable;

public class BuddyModel implements Serializable, IUser {

    private String id;
    private String name;
    private String photo;

    public BuddyModel(String id, String name, String photo) {
        this.id = id;
        this.name = name;
        this.photo = photo;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return photo;
    }
}
