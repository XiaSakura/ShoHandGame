package com.poker.service;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.poker.domain.Card;
import com.poker.domain.Player;
import com.poker.server.ServerMain;

/**
 * @author q9826 每一位玩家都有一个荷官 荷官具体分析玩家的牌和要做什么 荷官类 实现了 两个线程 专门用来接收和发送给客户端数据
 */
public class Dealer {
	public SendMes sendMes;
	public ReadMes readMes;

	public Dealer(Socket client, Player player) {
		// 每一个荷官对应一个 玩家
		deal(player);// 给玩家分牌
		deal(player);
		ServerMain.players.add(player);
		//将当前Dealer对象传入线程中   方便进行管理
		sendMes = new SendMes(client,this);
		sendMes.start();
		readMes = new ReadMes(client, this);
		readMes.start();
	}
	public  Card deal(Player player) {
		// 得到一张牌
		Card card = ServerMain.allCards.get(0);
		if (card == null) {
			ServerMain.setCards();// 重新洗牌
			card = ServerMain.allCards.get(0);
		}
		ServerMain.allCards.remove(0);
		player.getCards().add(card);
		return card;
	}

}
