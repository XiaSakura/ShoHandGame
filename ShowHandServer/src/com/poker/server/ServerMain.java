package com.poker.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.poker.domain.Card;
import com.poker.domain.HuaSe;
import com.poker.domain.Player;
import com.poker.service.Dealer;
import com.poker.service.HandlerThread;

import jdk.internal.org.objectweb.asm.Handle;

/**
 * @author q9826 服务器类 使用单例模式
 */
public class ServerMain {
	public static final int PORT = 8899;// 端口号
	public static  List<Player> players;
	public static  List<Card> allCards;
	public static int count; //记录连接量
	
	private ServerMain() {
	}

	private static ServerMain server;

	public static ServerMain getInstance() {
		if (server == null)
			server = new ServerMain();
		return server;
	}

	public static void main(String[] args) {
		allCards = new ArrayList<>();
		players = new ArrayList<>();
		setCards(); // 设置所有牌 并洗牌 初始化牌组
		ServerMain server =ServerMain.getInstance();
		ServerSocket serverSocket;
		System.out.println("服务器开启成功!!");
		try {
			serverSocket = new ServerSocket(PORT);
			count = 0;
			while (true) {
				// 一旦有堵塞, 则表示服务器与客户端获得了连接
				Socket client = serverSocket.accept();
				// 创建对应的玩家
				Player player = new Player();
				player.setSocket(client);
				player.setId(players.size());
				player.setName("xia"+count);
				count++;
				// 创建荷官类 对每一个玩家进行管理
				new Dealer(client, player);
			}
		} catch (IOException e) {
			System.out.println("服务器异常: " + e.getMessage());
		}

	}

	public static  void setCards() {
		// 先清空 所有牌 因为有重新发牌的可能
		allCards.clear();
		allCards.add(new Card(201, HuaSe.方块, "2", "fang02.jpg"));
		allCards.add(new Card(202, HuaSe.梅花, "2", "mei02.jpg"));
		allCards.add(new Card(203, HuaSe.红桃, "2", "hong02.jpg"));
		allCards.add(new Card(204, HuaSe.黑桃, "2", "hei02.jpg"));

		allCards.add(new Card(301, HuaSe.方块, "3", "fang03.jpg"));
		allCards.add(new Card(302, HuaSe.梅花, "3", "mei03.jpg"));
		allCards.add(new Card(303, HuaSe.红桃, "3", "hong03.jpg"));
		allCards.add(new Card(304, HuaSe.黑桃, "3", "hei03.jpg"));

		allCards.add(new Card(401, HuaSe.方块, "4", "fang04.jpg"));
		allCards.add(new Card(402, HuaSe.梅花, "4", "mei04.jpg"));
		allCards.add(new Card(403, HuaSe.红桃, "4", "hong04.jpg"));
		allCards.add(new Card(404, HuaSe.黑桃, "4", "hei04.jpg"));

		allCards.add(new Card(501, HuaSe.方块, "5", "fang05.jpg"));
		allCards.add(new Card(502, HuaSe.梅花, "5", "mei05.jpg"));
		allCards.add(new Card(503, HuaSe.红桃, "5", "hong05.jpg"));
		allCards.add(new Card(504, HuaSe.黑桃, "5", "hei05.jpg"));

		allCards.add(new Card(601, HuaSe.方块, "6", "fang06.jpg"));
		allCards.add(new Card(602, HuaSe.梅花, "6", "mei06.jpg"));
		allCards.add(new Card(603, HuaSe.红桃, "6", "hong06.jpg"));
		allCards.add(new Card(604, HuaSe.黑桃, "6", "hei06.jpg"));

		allCards.add(new Card(701, HuaSe.方块, "7", "fang07.jpg"));
		allCards.add(new Card(702, HuaSe.梅花, "7", "mei07.jpg"));
		allCards.add(new Card(703, HuaSe.红桃, "7", "hong07.jpg"));
		allCards.add(new Card(704, HuaSe.黑桃, "7", "hei07.jpg"));

		allCards.add(new Card(801, HuaSe.方块, "8", "fang08.jpg"));
		allCards.add(new Card(802, HuaSe.梅花, "8", "mei08.jpg"));
		allCards.add(new Card(803, HuaSe.红桃, "8", "hong08.jpg"));
		allCards.add(new Card(804, HuaSe.黑桃, "8", "hei08.jpg"));

		allCards.add(new Card(901, HuaSe.方块, "9", "fang09.jpg"));
		allCards.add(new Card(902, HuaSe.梅花, "9", "mei09.jpg"));
		allCards.add(new Card(903, HuaSe.红桃, "9", "hong09.jpg"));
		allCards.add(new Card(904, HuaSe.黑桃, "9", "hei09.jpg"));

		allCards.add(new Card(1001, HuaSe.方块, "10", "fang10.jpg"));
		allCards.add(new Card(1002, HuaSe.梅花, "10", "mei10.jpg"));
		allCards.add(new Card(1003, HuaSe.红桃, "10", "hong10.jpg"));
		allCards.add(new Card(1004, HuaSe.黑桃, "10", "hei10.jpg"));

		allCards.add(new Card(1101, HuaSe.方块, "J", "fang_j.jpg"));
		allCards.add(new Card(1102, HuaSe.梅花, "J", "mei_j.jpg"));
		allCards.add(new Card(1103, HuaSe.红桃, "J", "hong_j.jpg"));
		allCards.add(new Card(1104, HuaSe.黑桃, "J", "hei_j.jpg"));

		allCards.add(new Card(1201, HuaSe.方块, "Q", "fang_q.jpg"));
		allCards.add(new Card(1202, HuaSe.梅花, "Q", "mei_q.jpg"));
		allCards.add(new Card(1203, HuaSe.红桃, "Q", "hong_q.jpg"));
		allCards.add(new Card(1204, HuaSe.黑桃, "Q", "hei_q.jpg"));

		allCards.add(new Card(1301, HuaSe.方块, "K", "fang_k.jpg"));
		allCards.add(new Card(1302, HuaSe.梅花, "K", "mei_k.jpg"));
		allCards.add(new Card(1303, HuaSe.红桃, "K", "hong_k.jpg"));
		allCards.add(new Card(1304, HuaSe.黑桃, "K", "hei_k.jpg"));

		allCards.add(new Card(1401, HuaSe.方块, "A", "fang01.jpg"));
		allCards.add(new Card(1402, HuaSe.梅花, "A", "mei01.jpg"));
		allCards.add(new Card(1403, HuaSe.红桃, "A", "hong01.jpg"));
		allCards.add(new Card(1404, HuaSe.黑桃, "A", "hei01.jpg"));

		Collections.shuffle(allCards);
	}

}
