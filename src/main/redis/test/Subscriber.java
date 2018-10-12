package main.redis.test;

import org.bukkit.Bukkit;
import redis.clients.jedis.JedisPubSub;

public class Subscriber  {
    private static Subscriber subscriber;
    public static Subscriber getInstance() {
        if (subscriber == null) subscriber = new Subscriber();
        return subscriber;
    }

    private Subscribe subscribe;

    private Subscriber(){
        subscribe = new Subscribe();

    }

    public Subscribe getSubscribe() {
        return subscribe;
    }
}

class Subscribe extends JedisPubSub {

    @Override
    public void onMessage(String channel, String message) {
        super.onMessage(channel, message);
        Bukkit.broadcastMessage("Received MSG: "+message+" from "+channel);
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        super.onSubscribe(channel, subscribedChannels);
        Bukkit.broadcastMessage("Subscribed "+channel+", now u have subscribed "+subscribedChannels+" channels");
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        super.onUnsubscribe(channel, subscribedChannels);
        Bukkit.broadcastMessage("Unsubscribed "+channel+", now u have subscribed "+subscribedChannels+" channels");
    }

    @Override
    public void subscribe(String... channels) {
        super.subscribe(channels);
    }

    @Override
    public boolean isSubscribed() {
        return super.isSubscribed();
    }

    @Override
    public int getSubscribedChannels() {
        return super.getSubscribedChannels();
    }
}
