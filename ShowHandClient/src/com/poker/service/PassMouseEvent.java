package com.poker.service;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.alibaba.fastjson.JSON;
import com.poker.domain.Card;
import com.poker.domain.Message;
import com.poker.domain.Player;
import com.poker.view.MainFrame;

public class PassMouseEvent implements MouseListener {

	private JLabel pass;
	private MainFrame mainFrame;
	public PassMouseEvent(JLabel pass, MainFrame mainFrame) {
		this.pass = pass;
		this.mainFrame=mainFrame;
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
		pass.setIcon(new ImageIcon(MainFrame.class.getClassLoader().getResource("image/pass1.jpg")));
	}

	public void mouseEntered(MouseEvent e) {
		pass.setIcon(new ImageIcon(MainFrame.class.getClassLoader().getResource("image/pass2.jpg")));
	}
	public void mouseClicked(MouseEvent e) {

		if (mainFrame.players.size() == 2) {
			CanDealPoker.setCannotDeal();
			// 由于这个方法只能被 当前玩家所知道 不能通知其它玩家 所以要用到服务器
			Message message = new Message();
			message.setStatus_code(205); // 205代表请求服务器 由于这是两个人直接判断弃牌的人输
			message.setCurrent_player_id(mainFrame.current_player_id);
			Connect connect = Connect.getInstance();
			connect.sendMes.msg.add(JSON.toJSONString(message));

		}
	}
}
