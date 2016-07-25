package com.example.roman.androidserverdemo.model.server.host;

import java.io.IOException;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.example.roman.androidserverdemo.model.server.MessageListener;
import com.example.roman.androidserverdemo.model.server.NetworkProtocol;
import hugo.weaving.DebugLog;

public class ChatServer {
    private Server mServer;
    private MessageListener mMessageListener;


    public ChatServer(MessageListener messageListener) {
        mMessageListener = messageListener;
        mServer = new Server();
    }

    @DebugLog
    public void start() throws IOException {
        // For consistency, the classes to be sent over the network are
        // registered by the same method for both the client and mServer.
        NetworkProtocol.register(mServer);
        mServer.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                // We know all connections for this mServer are actually ChatConnections.
                if (object instanceof NetworkProtocol.ChatMessage) {
                    NetworkProtocol.ChatMessage chatMessage = (NetworkProtocol.ChatMessage) object;
                    messageReceived(chatMessage);
                }
            }
            @DebugLog
            public void disconnected(Connection c) {
                announceDisconnect();
            }
        });
        mServer.bind(NetworkProtocol.PORT_NUMBER);
        mServer.start();
    }
    @DebugLog
    private void messageReceived(NetworkProtocol.ChatMessage chatMessage){
        String message = chatMessage.text;
        if (message == null) return;
        message = message.trim();
        if (message.length() == 0) return;
        mServer.sendToAllTCP(chatMessage);
        mMessageListener.onMessageReceived(chatMessage.text);
    }
    @DebugLog
    private void announceDisconnect(){
        sendMessage("someone has disconnected");
    }

    @DebugLog
    public void sendMessage(final String msg) {
        new Thread("SendMessage"){
            @Override
            public void run() {
                super.run();
                NetworkProtocol.ChatMessage chatMessage = new NetworkProtocol.ChatMessage();
                chatMessage.text = msg;
                mServer.sendToAllTCP(chatMessage);
            }
        }.start();

    }

    @DebugLog
    public void stop() {
        mServer.stop();
    }
}