package com.example.roman.androidserverdemo.model.server.client;


import com.apkfuns.logutils.LogUtils;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.example.roman.androidserverdemo.model.server.MessageListener;
import com.example.roman.androidserverdemo.model.server.NetworkProtocol;



public class ChatClient {
    private Client mClient;
    private MessageListener mMessageListener;

    public ChatClient(MessageListener messageListener) {
        mClient = new Client();
        mMessageListener = messageListener;
    }

    public void startClient() {
        mClient.start();

        // For consistency, the classes to be sent over the network are
        // registered by the same method for both the mClient and server.
        NetworkProtocol.register(mClient);

        mClient.addListener(new Listener() {
            public void connected(Connection connection) {
                mMessageListener.onConnected();
                //do nothing
            }
            public void received(Connection connection, Object object) {
                if (object instanceof NetworkProtocol.ChatMessage) {
                    NetworkProtocol.ChatMessage chatMessage = (NetworkProtocol.ChatMessage) object;
                    mMessageListener.onMessageReceived(chatMessage.text);
                    return;
                }
            }
            public void disconnected(Connection connection) {
                mMessageListener.onDisconnected();
            }
        });

        new Thread("Connect") {
            public void run () {
                try {
                    mClient.connect(NetworkProtocol.TIMEOUT_TIME, NetworkProtocol.HOTSPOT_IP, NetworkProtocol.PORT_NUMBER);
                    // Server communication after connection can go here, or in Listener#connected().
                } catch (Exception e) {
                    LogUtils.d(e.getMessage());
                    e.printStackTrace();
                }
            }
        }.start();



    }

    public void stopClient(){
        mClient.stop();
    }

    public void sendMessage(final String msg) {
        LogUtils.d("sendMessage");
        new Thread("SendMessage") {
            public void run () {
                NetworkProtocol.ChatMessage chatMessage = new NetworkProtocol.ChatMessage();
                chatMessage.text = msg;
                mClient.sendTCP(chatMessage);
                mMessageListener.onMessageReceived(msg);
            }
        }.start();

    }
}
