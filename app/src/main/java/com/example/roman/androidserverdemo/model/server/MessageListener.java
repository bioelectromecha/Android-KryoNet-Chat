package com.example.roman.androidserverdemo.model.server;

/**
 * Created by roman on 7/24/16.
 */
public interface MessageListener {
    public void onMessageReceived(String message);
    public void onConnected();
    public void onDisconnected();
}
