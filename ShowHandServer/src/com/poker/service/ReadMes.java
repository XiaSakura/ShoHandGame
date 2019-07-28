package com.poker.service;

import java.io.DataInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.poker.domain.Card;
import com.poker.domain.Check;
import com.poker.domain.Message;
import com.poker.domain.Player;
import com.poker.server.ServerMain;

/**
 * @author q9826
 * 
 */
public class ReadMes extends Thread {
	private Socket socket;
	private Dealer dealer;

	public ReadMes(Socket socket, Dealer dealer) {
		this.socket = socket;
		this.dealer = dealer;
	}

	public void run() {
		DataInputStream objInput = null;
		try {
			objInput = new DataInputStream(socket.getInputStream());
			while (true) {
				String clientInputStr = (String) objInput.readUTF();
				if (clientInputStr != null && clientInputStr.length() > 0) {
					System.out.println(clientInputStr);
					JSONObject jsonObject = JSONObject.parseObject(clientInputStr);
					if (jsonObject != null) {
						// 解析 jsonObject
						readMes(jsonObject);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("数据解析异常" + e.getMessage());
			// 广播给所有玩家 有一个玩家掉线了 直接判断胜利 但不重新开始游戏
			// 当前socket玩家出现掉线 通知其他玩家
			Message message = new Message();
			message.setStatus_code(404);
			// 得到当前玩家的id
			int my_id = 0;
			for (int i = 0; i < ServerMain.players.size(); i++) {
				if (socket.equals(ServerMain.players.get(i).getSocket())) {
					my_id = ServerMain.players.get(i).getId();
				}
			}
			message.setId(my_id);
			dealer.sendMes.flag = 2;
			dealer.sendMes.msg.add(JSON.toJSONString(message));
//			//清空count
//			ServerMain.count=0;
//			//清空plays 
//			ServerMain.players.clear();
//			try {
//				sleep(5000);
//			} catch (InterruptedException e1) {
//				
//				e1.printStackTrace();
//			}
//			ServerMain.main(null);
		}

	}

	/**
	 * 根据jsonObject 进行解析
	 */
	public void readMes(JSONObject jsonObject) {
		int status_code = jsonObject.getIntValue("status_code");
		switch (status_code) {
		case 200:
			System.out.println("200");
			// 发送消息给客户端成功
			// 获得玩家姓名
			String name = jsonObject.getString("player_name");
			for (int i = 0; i < ServerMain.players.size(); i++) {
				if (ServerMain.players.get(i).getSocket().equals(socket)) { // 判断是否是当前玩家 修改名字
					ServerMain.players.get(i).setName(name);
				}
			}
			if (ServerMain.players.size() < 2) {
				// 修改login显示的人数
				JSONObject JsonObject = new JSONObject();
				JsonObject.put("status_code", 101);
				JsonObject.put("people_num", ServerMain.players.size());
				dealer.sendMes.flag = 0; // 开启广播
				dealer.sendMes.msg.add(JSON.toJSONString(JsonObject));
			}

			// 进行相应的操作
			if (ServerMain.players.size() == 2) {
				Message message = new Message();
				message.setStatus_code(102); // 102代表开房成功
				message.setPlayers(ServerMain.players);
				// 判断当前玩家谁的牌大 这里只比较牌值
				int id = CompareBrand();
				message.setId(id);
				JSONObject object = new JSONObject();
				object.put("status_code", message.getStatus_code());
				JSONArray array = new JSONArray();
				for (int i = 0; i < message.getPlayers().size(); i++) {
					Player player = message.getPlayers().get(i);
					array.add(player.toJSONObject());
				}
				object.put("players", array);
				// 需要给所有 玩家发送消息
				object.put("current_player_id", message.getId());
				dealer.sendMes.flag = 0; // 开启广播
				System.out.println("开启界面");
				dealer.sendMes.msg.add(object.toJSONString());
			}

//			if (ServerMain.players.size()==3) {
//				Message message = new Message();
//				message.setStatus_code(104); // 104代表开三人房成功
//				message.setPlayers(ServerMain.players);
//				// 判断当前玩家谁的牌大 这里只比较牌值
//				int id = CompareBrand();
//				message.setId(id);
//				JSONObject object = new JSONObject();
//				object.put("status_code", message.getStatus_code());
//				JSONArray array = new JSONArray();
//				for (int i = 0; i < message.getPlayers().size(); i++) {
//					Player player = message.getPlayers().get(i);
//					array.add(player.toJSONObject());
//				}
//				object.put("players", array);
//				// 需要给所有 玩家发送消息
//				object.put("current_player_id", message.getId());
//				Dealer.sendMes.flag = 0; // 开启广播
//				Dealer.sendMes.msg.add(object.toJSONString());
//			}

			break;
		case 201:
			break;
		case 203: // 203代表客户端 请求服务器发牌 只需要返回给客户端一张牌
			System.out.println("203");
			// 获取当前玩家id
			int current_player_id = jsonObject.getIntValue("current_player_id");
			for (int i = 0; i < ServerMain.players.size(); i++) {
				Player player = ServerMain.players.get(i);
				if (player.getId() == current_player_id) {
					Card card = dealer.deal(player);// 请求发一张牌
					// 返回给客户端
					JSONObject object = new JSONObject();
					object.put("status_code", 103);// 103代表请求牌成功
					object.put("card", card.toJSONObject());// 返回给客户端一张牌
					object.put("current_player_id", CompareBrand());
					dealer.sendMes.flag = 0; // 开启广播
					dealer.sendMes.msg.add(object.toJSONString());
				}
			}

			break;
		case 204:
			// 计算规则
			// TODO 统计出赢家
			System.out.println("204");
			Check check = new Check((ArrayList<Player>) ServerMain.players);
			int winer = check.getWiner();
			System.out.println("赢家是" + winer);
			JSONObject object = new JSONObject();
			object.put("status_code", 105);// 103代表请求牌成功
			object.put("winer_id", winer);
			dealer.sendMes.flag = 0; // 开启广播
			dealer.sendMes.msg.add(object.toJSONString());
			break;
		case 205:
			// 当前玩家pass 弃牌 直接判断输
			int current_playesr_id = jsonObject.getIntValue("current_player_id");
			dealer.sendMes.flag = 0; // 开启广播
			JSONObject object6 = new JSONObject();
			object6.put("status_code", 105);//
			object6.put("winer_id", 1 - current_playesr_id);
			dealer.sendMes.msg.add(object6.toJSONString());
			break;
		case 206:
			// 清空所有cards 重新开始游戏
			for (int i = 0; i < ServerMain.players.size(); i++) {
				ServerMain.players.get(i).getCards().clear();
			}
			// 重新洗牌
			ServerMain.setCards();
			// 给所有玩家两张牌
			for (int i = 0; i < ServerMain.players.size(); i++) {
				dealer.deal(ServerMain.players.get(i));
				dealer.deal(ServerMain.players.get(i));
			}
			if (ServerMain.players.size() == 2) {
				Message message = new Message();
				message.setStatus_code(106); // 106代表客户端更新 显示
				message.setPlayers(ServerMain.players);
				// 判断当前玩家谁的牌大 这里只比较牌值
				int id = CompareBrand();
				message.setId(id);
				JSONObject object2 = new JSONObject();
				object2.put("status_code", message.getStatus_code());
				JSONArray array = new JSONArray();
				for (int i = 0; i < message.getPlayers().size(); i++) {
					Player player = message.getPlayers().get(i);
					array.add(player.toJSONObject());
				}
				object2.put("players", array);
				// 需要给所有 玩家发送消息
				object2.put("current_player_id", message.getId());
				dealer.sendMes.flag = 0; // 开启广播
				dealer.sendMes.msg.add(object2.toJSONString());
			}
			break;
		case 207: // 聊天框信息
			String messge = jsonObject.getString("messge");
			int my_id = jsonObject.getIntValue("my_id"); // 代表谁说的话
			JSONObject object3 = new JSONObject();
			object3.put("status_code", 107);
			object3.put("messge", messge);
			object3.put("my_id", my_id);
			dealer.sendMes.flag = 0; // 开启广播
			dealer.sendMes.msg.add(object3.toJSONString());
			break;
		case 404:
			//重启服务器
//			System.exit(0);
			ServerMain.main(null);
		default:
			break;
		}
	}

	private int CompareBrand() {
		List<Player> players = ServerMain.players;
		int play_num = players.size();
		// 得到每一个玩家最近的一张牌
		List<Card> cards = new ArrayList<>();
		// 得到最新的一张牌
		for (int i = 0; i < play_num; i++) {
			List<Card> card2 = players.get(i).getCards();
			cards.add(card2.get(players.get(i).getCards().size() - 1));
		}
		Collections.sort(cards);
		System.out.println(cards);
		int size = cards.size();
		Card card = cards.get(size - 1);
		// 找到最大的那张牌的玩家
		for (int i = 0; i < play_num; i++) {
			List<Card> cards2 = players.get(i).getCards();
			for (int j = 0; j < cards2.size(); j++) {
				if (cards2.get(j).equals(card)) {
					return players.get(i).getId();
				}
			}
		}

		return 0;
	}

}