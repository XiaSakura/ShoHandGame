package com.poker.service;

import java.io.DataInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.poker.domain.Card;
import com.poker.domain.HuaSe;
import com.poker.domain.Message;
import com.poker.domain.Player;
import com.poker.view.Loading;
import com.poker.view.Login;
import com.poker.view.MainFrame;

/**
 * @author q9826
 * 
 */
public class ReadMes extends Thread {
	private Socket socket;
	private JSONArray array;
	private Connect connect;
	private MainFrame mainFrame = MainFrame.getInstance();

	public ReadMes(Socket socket, Connect connect) {
		this.socket = socket;
		this.connect = connect;
	}

	public void run() {
		DataInputStream objInput = null;
		try {
			objInput = new DataInputStream(socket.getInputStream());
			while (true) {
				String serverInputStr = (String) objInput.readUTF();
				System.out.println(serverInputStr);
				if (serverInputStr != null && serverInputStr.length() > 0) {

					JSONObject jsonObject = JSONObject.parseObject(serverInputStr);
					if (jsonObject != null) {
						// 解析 jsonObject
						readMes(jsonObject);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 根据jsonObject 进行解析
	 */
	public void readMes(JSONObject jsonObject) {
		int status_code = jsonObject.getIntValue("status_code");

		List<Player> players = new ArrayList<Player>();// 游戏开始获取的玩家
		switch (status_code) {
		case 101:
			System.out.println("开房成功");
			// 设置log的人数
			int people_num = jsonObject.getIntValue("people_num");
			Login.peoples.setText(String.valueOf(people_num));
			Login.login.repaint();
			break;
		case 102:// 当客户端有两个人进行连接时
			System.out.println("两个人已经准备好");
			array = jsonObject.getJSONArray("players");
			// 得到初始化 玩家信息 充json数组里面获得
			for (int i = 0; i < array.size(); i++) {
				JSONObject jsonObject2 = (JSONObject) array.get(i);
				Player player = new Player();
				player.setId(jsonObject2.getInteger("id"));
				player.setName(jsonObject2.getString("name"));
				List<Card> cards = new ArrayList<Card>();
				JSONArray array2 = jsonObject2.getJSONArray("cards"); // 得到该玩家的手牌
				for (int j = 0; j < array2.size(); j++) {
					JSONObject jsonObject3 = (JSONObject) array2.get(j);
					Card card = new Card();
					int huase = jsonObject3.getIntValue("huaSe");
					switch (huase) {
					case 0:
						card.setHuaSe(HuaSe.红桃);
						break;
					case 1:
						card.setHuaSe(HuaSe.梅花);
						break;
					case 2:
						card.setHuaSe(HuaSe.方块);
						break;
					case 3:
						card.setHuaSe(HuaSe.黑桃);
						break;
					}
					String img = jsonObject3.getString("img");
					card.setImg(img);
					String name = jsonObject3.getString("name");
					card.setName(name);
					int value = jsonObject3.getIntValue("value");
					card.setValue(value);
					cards.add(card);
				}
				player.setCards(cards);
				players.add(player);
				mainFrame.players.add(player);
			}
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).getName().equals(Login.jTextField.getText())) {
					Loading loading = new Loading(players, i);
					loading.setVisible(true);
					// 关闭login窗口
					Login.login.dispose();
				}
			}
			try {
				sleep(2500);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
			// 得到当前出牌的人
			int current_player_id = jsonObject.getIntValue("current_player_id");
			mainFrame.current_player_id = current_player_id;

			// 先将 所有人设置不可出牌
			for (int i = 0; i < players.size(); i++) {
				CanDealPoker.setCannotDeal();
				CanDealPoker.setCountShow(1, mainFrame.naoZhong);
				// 将 可以出牌的人设置可以出牌
				if (mainFrame.myId == current_player_id) {
					CanDealPoker.setCanDeal();
					CanDealPoker.setCountShow(1, mainFrame.naoZhong);
				}
			}
			// 开启聊天框的点击事件
			mainFrame.buttonChatSend.addMouseListener(new SendMsgMouseEvent(mainFrame));
			break;
		case 103: // 103代表 服务器向客户端传递一张牌
			// 将这张牌给当前玩家
			System.out.println("103");
			JSONObject json = jsonObject.getJSONObject("card");
			Card card = new Card();
			int huase = json.getIntValue("huaSe");
			String img = json.getString("img");
			String name = json.getString("name");
			int value = json.getIntValue("value");
			switch (huase) {
			case 0:
				card.setHuaSe(HuaSe.红桃);
				break;
			case 1:
				card.setHuaSe(HuaSe.梅花);
				break;
			case 2:
				card.setHuaSe(HuaSe.方块);
				break;
			case 3:
				card.setHuaSe(HuaSe.黑桃);
				break;
			}
			card.setImg(img);
			card.setValue(value);
			card.setName(name);
			// 跟新所有玩家的视图
			for (int i = 0; i < mainFrame.players.size(); i++) {
				if (mainFrame.players.get(i).getId() == mainFrame.current_player_id) {
					// 添加到该玩家的卡牌中
					mainFrame.players.get(i).getCards().add(card);
				}
			}
			// 更新 玩家手牌 更新界面
			mainFrame.updatePoker(mainFrame.players, mainFrame.myId);

			// 让另一个玩家可以下注 注意这里只有两个玩家
			if (mainFrame.players.size() == 2) {
				Player player0 = mainFrame.players.get(0);

				Player player1 = mainFrame.players.get(1);
				int card_size0 = player0.getCards().size();
				int card_size1 = player1.getCards().size();
				if (player0.getCards().size() == player1.getCards().size()) {
					// 判断两个玩家哪个的牌大
					System.out.println("card0" + player0.getCards().get(card_size0 - 1).getValue());
					System.out.println("card1" + player1.getCards().get(card_size1 - 1).getValue());
					if (player0.getCards().get(card_size0 - 1).getValue() > player1.getCards().get(card_size1 - 1)
							.getValue()) {
						// 玩家player0的牌大 所以给player0设置点击事件
						if (mainFrame.myId == player0.getId()) {
							CanDealPoker.setCanDeal();
							CanDealPoker.setCountShow(1, mainFrame.naoZhong);
						}
						mainFrame.current_player_id = player0.getId();
					}
					if (player0.getCards().get(card_size0 - 1).getValue() < player1.getCards().get(card_size1 - 1)
							.getValue()) {
						// 玩家player1的牌大 所以给player1设置点击事件
						if (mainFrame.myId == player1.getId()) {
							CanDealPoker.setCanDeal();
							CanDealPoker.setCountShow(1, mainFrame.naoZhong);
						}
						mainFrame.current_player_id = player1.getId();
					}
				}
				if (player0.getCards().size() != player1.getCards().size()) {
					if (mainFrame.myId != mainFrame.current_player_id) {
						CanDealPoker.setCanDeal();
						CanDealPoker.setCountShow(1, mainFrame.naoZhong);
						mainFrame.current_player_id = mainFrame.myId;
					} else {
						mainFrame.current_player_id = 1 - mainFrame.myId;
					}
				}

				// 当全部玩家的牌 到了5张的时候 显示所有玩家的牌
				int count = 0;
				for (int i = 0; i < mainFrame.players.size(); i++) {
					if (mainFrame.players.get(i).getCards().size() == 5) {
						count++;
					}
					if (count == 2) {
						// 显示底牌
						mainFrame.show_diPai = 1;
						// 更新显示
						mainFrame.updatePoker(mainFrame.players, mainFrame.myId);
					}
				}

				if (mainFrame.myId == mainFrame.current_player_id) {
					int count2 = 0;
					for (int i = 0; i < mainFrame.players.size(); i++) {
						if (mainFrame.players.get(i).getCards().size() == 5) {
							count2++;
						}
						if (count2 == 2) {
							System.out.println("所有人都摸到5张了 请求服务器 统计数据");
							CanDealPoker.setCannotDeal();
							CanDealPoker.setCountShow(1, mainFrame.naoZhong);

							Message message = new Message();
							message.setStatus_code(204); // 204代表请求服务器统计数据

							connect.sendMes.msg.add(JSON.toJSONString(message));
						}
					}
				}

			}
//			if (MainFrame.players.size() == 3) {
//				// 判断当前玩家 是谁 进行轮转 顺时针
//				if (MainFrame.current_player_id == 0) {
//
//				}
//
//				Player player = MainFrame.players.get(MainFrame.myId);
//				ArrayList<Player> list = new ArrayList<>();
//				for (int i = 0; i < players.size(); i++) {
//					if (!players.get(i).equals(player)) {
//						list.add(players.get(i));
//					}
//				}
//				// 让另一个玩家可以下注 修改当前可以点击的玩家 顺时针发牌
////				if (MainFrame.) {
////					
////				}
//
//			}
			break;

		case 104: // 代表三个人的场子
			System.out.println("三个人已经准备好");
			array = jsonObject.getJSONArray("players");
			// 得到初始化 玩家信息 充json数组里面获得
			for (int i = 0; i < array.size(); i++) {
				JSONObject jsonObject2 = (JSONObject) array.get(i);
				Player player = new Player();
				player.setId(jsonObject2.getInteger("id"));
				player.setName(jsonObject2.getString("name"));
				List<Card> cards = new ArrayList<Card>();
				JSONArray array2 = jsonObject2.getJSONArray("cards"); // 得到该玩家的手牌
				for (int j = 0; j < array2.size(); j++) {
					JSONObject jsonObject3 = (JSONObject) array2.get(j);
					Card card2 = new Card();
					int huase2 = jsonObject3.getIntValue("huaSe");
					switch (huase2) {
					case 0:
						card2.setHuaSe(HuaSe.红桃);
						break;
					case 1:
						card2.setHuaSe(HuaSe.梅花);
						break;
					case 2:
						card2.setHuaSe(HuaSe.方块);
						break;
					case 3:
						card2.setHuaSe(HuaSe.黑桃);
						break;
					}
					String img2 = jsonObject3.getString("img");
					card2.setImg(img2);
					String name2 = jsonObject3.getString("name");
					card2.setName(name2);
					int value2 = jsonObject3.getIntValue("value");
					card2.setValue(value2);
					cards.add(card2);
				}
				player.setCards(cards);
				players.add(player);
				mainFrame.players.add(player);
			}
			for (int i = 0; i < players.size(); i++) {
				if (players.get(i).getName().equals(Login.jTextField.getText())) {
					// 打开相应 玩家
//					MainFrame.startMain(players, i);
					// 关闭login窗口
					Login.login.dispose();
				}
			}
			// 得到当前出牌的人
			int current_player_id2 = jsonObject.getIntValue("current_player_id");
			mainFrame.current_player_id = current_player_id2;
			// 先将 所有人设置不可出牌
			for (int i = 0; i < players.size(); i++) {
				CanDealPoker.setCannotDeal();
				CanDealPoker.setCountShow(1, mainFrame.naoZhong);
				// 将 可以出牌的人设置可以出牌
				if (mainFrame.myId == current_player_id2) {
					CanDealPoker.setCanDeal();
					CanDealPoker.setCountShow(1, mainFrame.naoZhong);
				}
			}
			break;
		case 105: // 计算出获胜者
			System.out.println("105");
			String winner_name = "";
			int winer_id = jsonObject.getIntValue("winer_id");
			for (int i = 0; i < mainFrame.players.size(); i++) {
				if (mainFrame.players.get(i).getId() == winer_id) {
					winner_name = mainFrame.players.get(i).getName();
				}
			}
			JOptionPane.showMessageDialog(null, new JLabel(
					"<html><h1><font color='red'>" + winner_name + "该玩家获得胜利,点击确定自动开始下一场游戏" + "</font></h1></html>"),
					"Game Over", JOptionPane.DEFAULT_OPTION);
			// 赢家发送
			if (mainFrame.myId == winer_id) {
				// 给服务器发送重新开始游戏的消息
				Message sendMes = new Message();
				sendMes.setStatus_code(206);
				connect.sendMes.msg.add(JSON.toJSONString(sendMes));
			}
			// 清空所有玩家的牌
			for (int i = 0; i < mainFrame.players.size(); i++) {
				mainFrame.players.get(i).getCards().clear();
			}

			break;
		case 106:// 服务器返回客户端 游戏重新开始的逻辑
			mainFrame.show_diPai = 0;
			array = jsonObject.getJSONArray("players");
			mainFrame.players.clear();// 先清空所有玩家 后面会加上
			// 得到初始化 玩家信息 充json数组里面获得
			for (int i = 0; i < array.size(); i++) {
				JSONObject jsonObject2 = (JSONObject) array.get(i);
				Player player = new Player();
				player.setId(jsonObject2.getInteger("id"));
				player.setName(jsonObject2.getString("name"));
				List<Card> cards = new ArrayList<Card>();
				JSONArray array2 = jsonObject2.getJSONArray("cards"); // 得到该玩家的手牌
				for (int j = 0; j < array2.size(); j++) {
					JSONObject jsonObject3 = (JSONObject) array2.get(j);
					Card card1 = new Card();
					int huase1 = jsonObject3.getIntValue("huaSe");
					switch (huase1) {
					case 0:
						card1.setHuaSe(HuaSe.红桃);
						break;
					case 1:
						card1.setHuaSe(HuaSe.梅花);
						break;
					case 2:
						card1.setHuaSe(HuaSe.方块);
						break;
					case 3:
						card1.setHuaSe(HuaSe.黑桃);
						break;
					}
					String img1 = jsonObject3.getString("img");
					card1.setImg(img1);
					String name1 = jsonObject3.getString("name");
					card1.setName(name1);
					int value1 = jsonObject3.getIntValue("value");
					card1.setValue(value1);
					cards.add(card1);
				}
				player.setCards(cards);
				players.add(player);
				mainFrame.players.add(player);
			}
			// 更新客户端
			mainFrame.updatePoker(players, mainFrame.myId);
			// 得到当前出牌的人
			int current_player_id1 = jsonObject.getIntValue("current_player_id");
			mainFrame.current_player_id = current_player_id1;
			// 先将 所有人设置不可出牌
			for (int i = 0; i < players.size(); i++) {
				CanDealPoker.setCannotDeal();
				CanDealPoker.setCountShow(1, mainFrame.naoZhong);
				// 将 可以出牌的人设置可以出牌
				if (mainFrame.myId == current_player_id1) {
					CanDealPoker.setCanDeal();
					CanDealPoker.setCountShow(1, mainFrame.naoZhong);
				}
			}
			mainFrame.textChatArea.setText("");
			break;
		case 107:
			String messge = jsonObject.getString("messge");// 得到消息
			String send_name = "";
			int send_id = jsonObject.getIntValue("my_id"); // 得到说话的人
			for (int i = 0; i < mainFrame.players.size(); i++) {
				if (mainFrame.players.get(i).getId() == send_id) {
					send_name = mainFrame.players.get(i).getName();
				}
			}
			// 添加在 聊天框里面
			mainFrame.textChatArea.append(send_name + ":" + messge + "\r\n");
			mainFrame.repaint();
			break;
		case 404: // 出现掉线异常
			int diaoXian_id = jsonObject.getIntValue("id"); // 得到掉线的人
			String diapXoam_name = "";
			for (int i = 0; i < mainFrame.players.size(); i++) {
				if (mainFrame.players.get(i).getId() == diaoXian_id) {
					diapXoam_name = mainFrame.players.get(i).getName();
				}
			}
			// 弹出对话框 判断胜利 关闭游戏
			JOptionPane
					.showMessageDialog(null,
							new JLabel("<html><h1><font color='red'>" + diapXoam_name
									+ "该玩家出现掉线,你的对手已经离开了游戏,你获得胜利,请你重新登录" + "</font></h1></html>"),
							"Game Over", JOptionPane.DEFAULT_OPTION);
			mainFrame.dispose();
			// 停止音乐
			mainFrame.aau.stop();
			// 开启新的login界面
			Message message = new Message();
			message.setStatus_code(404);
			connect.sendMes.msg.add(JSON.toJSONString(message));
			// 清空mainframe
			System.exit(0);
		case 108: // 重启服务器

		default:
			break;
		}
	}

}