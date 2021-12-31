package com.sustechstore.dao;

public class ItemPicture {
    public int itemId;
    public String content;
    public ItemPicture(){}
    public ItemPicture(int itemId, String fileName) {
        this.itemId = itemId;
        this.content = fileName;
    }
}
