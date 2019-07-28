package com.poker.service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.poker.server.ServerMain;

public class SendMes extends Thread {
	private Socket socket;
	private Dealer dealer;
	public static int flag = 1; // 0 开启广播 1关闭广播 2通知除了当前socket玩家的其它玩家
	public BlockingQueue<String> msg = new ArrayBlockingQueue<String>(10);

	public SendMes(Socket socket, Dealer dealer) {
		this.socket = socket;
		this.dealer = dealer;
	}

	public void run() {
		DataOutputStream objOut = null;

		try {
			objOut = new DataOutputStream(socket.getOutputStream());
			while (true) {
				if (msg != null && msg.size() > 0) {
					if (flag == 0) {// 广播给所有玩家
						String string = msg.take();
						for (int i = 0; i < ServerMain.players.size(); i++) {
							DataOutputStream objOut2 = new DataOutputStream(
									ServerMain.players.get(i).getSocket().getOutputStream());
							objOut2.writeUTF(string);
						}
						flag = -1;
					}
					if (flag == 1) { // 只给当前玩家发送消息
						objOut.writeUTF(msg.take());
						flag = -1;
					}
					if (flag == 2) { // 通知除了当前socket玩家的其它玩家
						String string = msg.take();
						for (int i = 0; i < ServerMain.players.size(); i++) {
							if (socket.equals(ServerMain.players.get(i).getSocket())) {
								continue;
							}
							DataOutputStream objOut2 = new DataOutputStream(
									ServerMain.players.get(i).getSocket().getOutputStream());
							objOut2.writeUTF(string);
						}
						flag = -1;
					}
				}
				Thread.sleep(100);

			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					new JLabel("<html><h1><font color='red'>" + e.getMessage() + "</font></h1></html>"), "错误",
					JOptionPane.ERROR_MESSAGE);
		} finally {
			try {
				objOut.close();
			} catch (IOException e) {
			}
		}
	}

}
