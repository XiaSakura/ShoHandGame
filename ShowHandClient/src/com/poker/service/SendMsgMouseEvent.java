package com.poker.service;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextField;

import com.alibaba.fastjson.JSON;
import com.poker.domain.Message;
import com.poker.view.MainFrame;

public class SendMsgMouseEvent implements MouseListener {

	private MainFrame mainFrame;
	
	
	public SendMsgMouseEvent(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public void mouseClicked(MouseEvent e) {
		if (mainFrame.players.size() == 2) {
			String text = mainFrame.textChat.getText();
			mainFrame.textChat.setText("");
			// 所有玩家显示消息 
			Message message = new Message();
			message.setStatus_code(207); // 207代表请求服务器 广播给所有玩家显示 聊天框
			message.setMessge(text);
			message.setMy_id(mainFrame.myId);
			Connect connect=Connect.getInstance();
			connect.sendMes.msg.add(JSON.toJSONString(message));
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
