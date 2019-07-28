package com.poker.service;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.alibaba.fastjson.JSON;
import com.poker.domain.Message;
import com.poker.view.MainFrame;

import sun.applet.Main;

public class OutMouseEvent implements MouseListener {

	private JLabel out;
	private MainFrame mainFrame;
	public OutMouseEvent(JLabel out, MainFrame mainFrame) {
		this.out = out;
		this.mainFrame=mainFrame;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
		out.setIcon(new ImageIcon(MainFrame.class.getClassLoader().getResource("image/gengzhu1.jpg")));
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		out.setIcon(new ImageIcon(MainFrame.class.getClassLoader().getResource("image/gengzhu2.jpg")));
	}

	/*
	 * 当玩家点击跟注的时候 请求服务器发牌
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// 给指定玩家id发牌
		String text = "押注了";
		// 所有玩家显示消息
		Message message2 = new Message();
		message2.setStatus_code(207); // 207代表请求服务器 广播给所有玩家显示 聊天框
		message2.setMessge(text);
		message2.setMy_id(mainFrame.current_player_id);
		// 不能连续点击
		CanDealPoker.setCannotDeal();
		int current_player_id = mainFrame.current_player_id;
		Message message = new Message();
		message.setStatus_code(203); // 203代表请求服务器发牌
		message.setCurrent_player_id(current_player_id);
		Connect connect = Connect.getInstance();
		connect.sendMes.msg.add(JSON.toJSONString(message2));
		try {
			Thread.sleep(200);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		connect.sendMes.msg.add(JSON.toJSONString(message));
	}

}
