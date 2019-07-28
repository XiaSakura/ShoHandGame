package com.poker.service;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.poker.domain.Player;
import com.poker.server.ServerMain;

/**
 * @author q9826 每一个玩家都有 一个这个线程 专门处理 发送和接受的方法 对传过来的数据 进行解析 之后再跟进解析出来的结果 返回给客户端
 */
public class HandlerThread implements Runnable {

	private Socket socket;

	public HandlerThread(Socket client) {
		socket = client;
		Player player = new Player();
		player.setSocket(socket);
		ServerMain.players.add(player);
		new Thread(this).start();
	}

	@Override
	public void run() {
		DataInputStream objInput = null;
		try {
			// 对客户端 传递过来的数据进行解析
			objInput = new DataInputStream(socket.getInputStream());
			while (true) {
				String clientInputStr = (String) objInput.readUTF();
				if (clientInputStr != null && clientInputStr.length() > 0) {
					JSONObject jsonObject = JSONObject.parseObject(clientInputStr);
					if (jsonObject != null) {
						// 解析 jsonObject
						readMes(jsonObject);
					}
				}
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	/**
	 * 根据jsonObject 进行解析
	 */
	public void readMes(JSONObject jsonObject) {
		int status_code = jsonObject.getIntValue("status_code");
		switch (status_code) {
		case 200:
			
			break;

		default:
			break;
		}
	}

}
