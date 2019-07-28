package com.poker.service;

import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.alibaba.fastjson.JSON;
import com.poker.domain.Message;
import com.poker.view.Login;

public class Connect {
	public static final String IP_ADDR = Login.IP_addrres.getText().trim(); // ip ַ
	public static final int PORT = 8899;//

	private Socket socket;
	public SendMes sendMes;
	public ReadMes readMes;

	private Connect() {
	}

	private static Connect connect;

	public static Connect getInstance() {
		if (connect == null)
			connect = new Connect();
		return connect;
	}

	public boolean connect() {
		try {
			socket = new Socket(IP_ADDR, PORT);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					new JLabel("<html><h1><font color='red'>" + "服务器IP错误或连接失效" + "</font></h1></html>"), "错误信息",
					JOptionPane.ERROR_MESSAGE);
			// 通知服务器
			System.out.println("出异常了");
		}
		sendMes = new SendMes(socket, this);
		sendMes.start();
		readMes = new ReadMes(socket, this);
		readMes.start();
		// 测试 这里需要玩家点击准备才开始的
		sendMes.msg.add(JSON.toJSONString(new Message(200, Login.jTextField.getText().trim(), null)));
		System.out.println("连接成功");
		return true;
	}
}
