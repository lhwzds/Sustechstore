package com.sustechstore.dao;

public class AvatarPicture {
    public int userId;
    public String content;
    public AvatarPicture(){}
    public AvatarPicture(int userId, String fileName) {
        this.userId = userId;
        this.content = fileName;
    }
}
