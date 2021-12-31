package com.sustechstore.utils;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserChannelUtils {
    //哈希表存储userid和channel对应关系
    public static Map<Integer,Channel> user_channel_map = new ConcurrentHashMap<Integer,Channel>();
    public static Map<Channel, Integer> channel_user_map = new ConcurrentHashMap<Channel, Integer>();

    public static void add(int userid,Channel channel){
        if(user_channel_map.containsKey(userid)){
            removeUser(userid);
        }
        user_channel_map.put(userid,channel);
        channel_user_map.put(channel,userid);
    }
    public static void removeUser(int userid){
        Channel tmp = user_channel_map.get(userid);
        user_channel_map.remove(userid);
        channel_user_map.remove(tmp);
    }
    public static Channel getChannel(int id){
        return user_channel_map.get(id);
    }
    public static int getID(Channel channel){
        return channel_user_map.get(channel);
    }
}
