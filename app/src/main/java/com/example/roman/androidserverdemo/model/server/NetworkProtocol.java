package com.example.roman.androidserverdemo.model.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class NetworkProtocol {
	public static final int PORT_NUMBER = 54555;
	public static final String HOTSPOT_IP = "192.168.43.1";
	public static final int TIMEOUT_TIME = 5000;

	// This registers objects that are going to be sent over the network.
	static public void register (EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(String[].class);
		kryo.register(ChatMessage.class);
	}

	static public class ChatMessage {
		public String text;
	}
}