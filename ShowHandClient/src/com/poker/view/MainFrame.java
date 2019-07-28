package com.poker.view;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import com.poker.domain.Card;
import com.poker.domain.Player;

public class MainFrame extends JFrame {
	
	private MainFrame() {
	}
	// 声明一个本类的引用 给外界使用
	private static MainFrame mainFrame;
	// 对外提供公共的访问方法
	public static MainFrame getInstance() {
		if(mainFrame == null)
			mainFrame = new MainFrame();
		return mainFrame;
	}

	public  int myId;
	// 存放上下左右玩家的信息
	public  Player minePlayer;
	/**
	 * 底牌是否显示 1显示 0 不显示
	 */
	public  int show_diPai = 0;

	public  Player leftPlayer;
	public  Player rightPlayer;
	public  Player topPlayer;
	public  int current_player_id;

	public  List<Player> players = new ArrayList<Player>();// 游戏的玩家
	/**
	 * 扑克牌的集合 存放所有玩家的扑克牌
	 */
	private  List<JLabel> pokerLabels = new ArrayList<JLabel>();
	// 玩家头像和 玩家名字
	private JLabel touXiangMine;
	private JLabel playerNameMine;
	// 左边玩家的头像和名字
	private JLabel touXiangLeft;
	private JLabel playerNameLeft;
	// 右边
	private JLabel touXiangRight;
	private JLabel playerNameRight;
	// 上边
	private JLabel touXiangTop;
	private JLabel playerNameTop;
	// 闹钟
	public  JLabel naoZhong;
	// 跟注和弃牌
	public  JLabel call = null;
	public  JLabel discard = null;
	// 摊牌按钮
	
	// 聊天框
	public  JTextField textChat;
	public  JButton buttonChatSend;
	public  JTextArea textChatArea;

	// 扑克牌的画面
	private JPanel pokerPanel;
	// 显示自己的扑克牌
	private  JPanel showPokerPanel;
	// 显示左边的扑克牌
	private  JPanel showPokerPanelLeft;
	// 显示右边的扑克牌
	private  JPanel showPokerPanelRight;
	// 显示上边的扑克牌
	private  JPanel showPokerPanelTop;
	//背景音乐
	public AudioClip aau;

	public void startMain(List<Player> players, int my) {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

		} catch (Exception e) {
			e.printStackTrace();
		}
		mainFrame.getContentPane().setLayout(null);
		mainFrame.setSize(1020, 600);
		mainFrame.setLocationRelativeTo(null);// 设置居中，要放在设置窗口大小之后
		// 得到玩家自己的id 当前玩家
		myId = players.get(my).getId();
		if (players.size() == 2) {
			// 初始化自己和上方玩家的信息
			setIdentityView(mainFrame); // 显示玩家自己的信息
			setPlayerTopView(mainFrame);// 上方玩家的信息
			setChatView(mainFrame);// 聊天窗口
			setBtnView(mainFrame);// 跟注 pass 按钮
			setAllInfo(players, my); // 初始化自己和上方玩家的信息
			setShowPokerView(mainFrame);// 显示自己的牌
			setShowPokerTopView(mainFrame); // 显示上方玩家的牌
		}
		if (players.size() == 3) {
			// 初始化自己和上方玩家的信息 左边的玩家
			setIdentityView(mainFrame); // 显示玩家自己的信息
			setPlayerTopView(mainFrame);// 上方玩家的信息
			setPlayerLeftView(mainFrame); // 显示左边的玩家的信息
			setBtnView(mainFrame);// 跟注 pass 按钮
			setAllInfo(players, my); // 初始化自己和上方玩家的信息
			setShowPokerView(mainFrame);// 显示自己的牌
			setShowPokerTopView(mainFrame); // 显示上方玩家的牌
			setShowPokerLeftView(mainFrame); // 显示左方玩家的牌
		}

		// 添加背景
		JLabel bg = new JLabel();
		bg.setIcon(new ImageIcon(MainFrame.class.getClassLoader().getResource("image/bg.png")));
		bg.setBounds(0, 0, 800, 600);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().add(bg);
		mainFrame.repaint();
		mainFrame.setVisible(true);
		// 音频添加
		try {
			URL url = MainFrame.class.getClassLoader().getResource("667.wav"); // 先得到类加载器,获得src的路径然后得到音频资源文件
			aau = Applet.newAudioClip(url);
			aau.loop(); // 循环播放
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 设置跟注和 盖牌的相应主键
	 * 
	 * @param mainFrame
	 */
	private  void setBtnView(MainFrame mainFrame) {
		JPanel BtnView = new JPanel();
		BtnView.setOpaque(false);
		BtnView.setBounds(260, 350, 305, 40);
		BtnView.setLayout(null);

		call = new JLabel();
		call.setIcon(new ImageIcon(MainFrame.class.getClassLoader().getResource("image/gengzhu1.jpg")));
		call.setBounds(0, 0, 65, 40);
		discard = new JLabel();
		discard.setIcon(new ImageIcon(MainFrame.class.getClassLoader().getResource("image/pass1.jpg")));
		discard.setBounds(70, 0, 65, 40);
		BtnView.add(call);
		BtnView.add(discard);
		mainFrame.add(BtnView);
		mainFrame.repaint();
	}

	/**
	 * 当玩家跟注的时候 服务器会发牌 跟新所有客户端的显示
	 * 
	 * @param players
	 * @param my
	 */
	public  void updatePoker(List<Player> players, int my) {
		if (players.size() == 2) {
			topPlayer = players.get(1 - my);
			minePlayer = players.get(my);
			updatePokerTopView(mainFrame);
			updateShowPokerView(mainFrame);
		}

	}

	private  void updatePokerTopView(MainFrame mainFrame2) {
		List<Card> cards = topPlayer.getCards();
		int x = cards.size() * 30 - 25;
		showPokerPanelTop.removeAll();
		for (int i = cards.size() - 1; i >= 0; i--) {
			Card card = cards.get(i);
			JLabel jLabel = new JLabel(); // 存放自己的每一张扑克牌
			jLabel.setName(card.getName() + "");
			jLabel.setIcon(new ImageIcon(MainFrame.class.getClassLoader().getResource("pk/" + card.getImg())));
			jLabel.setBounds(x + 50, 10, 100, 152);
			// 设置底牌
			if (i == 0) {
				if (show_diPai == 0) {
					jLabel.setIcon(new ImageIcon(MainFrame.class.getClassLoader().getResource("pk/rear.jpg")));
				}
			}
			showPokerPanelTop.add(jLabel, cards.size() - 1 - i);
			x = x - 30;
		}
		mainFrame.repaint();
	}

	private  void updateShowPokerView(MainFrame mainFrame2) {
		List<Card> cards = minePlayer.getCards();
		int x = cards.size() * 30 - 25;
		showPokerPanel.removeAll();
		for (int i = cards.size() - 1; i >= 0; i--) {
			Card card = cards.get(i);
			JLabel jLabel = new JLabel(); // 存放自己的每一张扑克牌
			jLabel.setName(card.getName() + "");
			jLabel.setIcon(new ImageIcon(MainFrame.class.getClassLoader().getResource("pk/" + card.getImg())));
			jLabel.setBounds(x + 50, 10, 100, 152);
			// 设置底牌
//			if (i == 0) {
//				if (show_diPai == 0) {
//					jLabel.setIcon(new ImageIcon(MainFrame.class.getClassLoader().getResource("pk/rear.jpg")));
//				}
//			}
			showPokerPanel.add(jLabel, cards.size() - 1 - i);
			x = x - 30;
		}
		mainFrame.repaint();
	}

	/**
	 * 显示左方玩家的牌
	 * 
	 * @param mainFrame2
	 */
	private  void setShowPokerLeftView(MainFrame mainFrame2) {
		List<Card> cards = leftPlayer.getCards();
		showPokerPanelLeft = new JPanel(); // 存放自己的所有扑克牌
		showPokerPanelLeft.setOpaque(false);
		showPokerPanelLeft.setBounds(0, 200, 680, 152);
		showPokerPanelLeft.setLayout(null);
		int x = cards.size() * 30 - 25;
		for (int i = cards.size() - 1; i >= 0; i--) {
			Card card = cards.get(i);
			JLabel jLabel = new JLabel(); // 存放自己的每一张扑克牌
//			jLabel.setText(card.getHuaSe()()+"");
			jLabel.setName(card.getName() + "");
			jLabel.setIcon(new ImageIcon(MainFrame.class.getClassLoader().getResource("pk/" + card.getImg())));
			jLabel.setBounds(x + 50, 10, 100, 152);
			// 设置底牌
			if (i == 0) {
				jLabel.setIcon(new ImageIcon(MainFrame.class.getClassLoader().getResource("pk/rear.jpg")));
			}

//			pokerLabels.add(jLabel);
			showPokerPanelLeft.add(jLabel, cards.size() - 1 - i);
			x = x - 30;
		}
		mainFrame.getContentPane().add(showPokerPanelLeft);
		mainFrame.repaint();
	}

	/**
	 * 显示上方玩家的牌
	 * 
	 * @param mainFrame
	 */
	private  void setShowPokerTopView(MainFrame mainFrame) {
		List<Card> cards = topPlayer.getCards();
		showPokerPanelTop = new JPanel(); // 存放自己的所有扑克牌
		showPokerPanelTop.setOpaque(false);
		showPokerPanelTop.setBounds(130, 0, 680, 160);
		showPokerPanelTop.setLayout(null);
		int x = cards.size() * 30 - 25;
		for (int i = cards.size() - 1; i >= 0; i--) {
			// 底牌隐藏掉
			Card card = cards.get(i);
			JLabel jLabel = new JLabel(); // 存放自己的每一张扑克牌
//			jLabel.setText(card.getHuaSe()()+"");
			jLabel.setName(card.getName() + "");
			jLabel.setIcon(new ImageIcon(MainFrame.class.getClassLoader().getResource("pk/" + card.getImg())));
			if (i == 0) {
				jLabel.setIcon(new ImageIcon(MainFrame.class.getClassLoader().getResource("pk/rear.jpg")));
			}

			jLabel.setBounds(x + 50, 10, 100, 160);
//			pokerLabels.add(jLabel);
			showPokerPanelTop.add(jLabel, cards.size() - 1 - i);
			x = x - 30;
		}
		mainFrame.getContentPane().add(showPokerPanelTop);
		mainFrame.repaint();
	}

	/**
	 * 显示自己的牌
	 * 
	 * @param mainFrame
	 */
	private  void setShowPokerView(MainFrame mainFrame) {
		List<Card> cards = minePlayer.getCards();
		showPokerPanel = new JPanel(); // 存放自己的所有扑克牌
		showPokerPanel.setOpaque(false);
		showPokerPanel.setBounds(130, 400, 680, 160);
		showPokerPanel.setLayout(null);
		int x = cards.size() * 30 - 25;
		for (int i = cards.size() - 1; i >= 0; i--) {
			Card card = cards.get(i);
			JLabel jLabel = new JLabel(); // 存放自己的每一张扑克牌
//			jLabel.setText(card.getHuaSe()()+"");
			jLabel.setName(card.getName() + "");
			jLabel.setIcon(new ImageIcon(MainFrame.class.getClassLoader().getResource("pk/" + card.getImg())));
			jLabel.setBounds(x + 50, 10, 100, 160);
//			if (i == 0) {
//				jLabel.setIcon(new ImageIcon(MainFrame.class.getClassLoader().getResource("pk/rear.jpg")));
//			}
//			pokerLabels.add(jLabel);
			showPokerPanel.add(jLabel, cards.size() - 1 - i);
			x = x - 30;
		}
		mainFrame.getContentPane().add(showPokerPanel);
		mainFrame.repaint();
	}

	/**
	 * 加载聊天窗口组件
	 * 
	 * @param mainFrame
	 */
	public  void setChatView(MainFrame mainFrame) {
//		JPanel jPanel = new JPanel();

//		jPanel.setBounds(800, 0, 200, 600);
//		jPanel.setBackground(Color.darkGray);
//		jPanel.setLayout(null);

		textChatArea = new JTextArea();// 实例化大型文本区
		textChatArea.setBackground(Color.WHITE);
		textChatArea.disable();// 设置成只读属性
		textChatArea.setBounds(800, 0, 200, 600);
		Font font = new Font("宋体", Font.BOLD, 15);
		textChatArea.setFont(font);
		textChatArea.setForeground(Color.RED);

		textChat = new JTextField();
		textChat.setBounds(0, 530, 130, 30);
		buttonChatSend = new JButton("发送");
		buttonChatSend.setBounds(140, 530, 70, 30);
		textChatArea.add(buttonChatSend);
		textChatArea.add(textChat);

		mainFrame.add(textChatArea);
		mainFrame.repaint();
	}

	/**
	 * 从服务器获取玩家信息 填充组件信息 这里先只有两个玩家
	 * 
	 * @param players
	 * @param my
	 */
	public  void setAllInfo(List<Player> players, int my) {
		if (players.size() == 2) {
			topPlayer = players.get(1 - my);
			minePlayer = players.get(my);
			playerNameMine.setText(minePlayer.getName());
			playerNameTop.setText(topPlayer.getName());
			// 设置头像
			ImageIcon play0 = new ImageIcon(MainFrame.class.getClassLoader().getResource("image/1play.jpg"));
			ImageIcon play1 = new ImageIcon(MainFrame.class.getClassLoader().getResource("image/2play.jpg"));
			playerNameMine.setText(minePlayer.getName());
			playerNameTop.setText(topPlayer.getName());
			if (myId == 0) { // 给0号玩家设置一个 头像
				touXiangMine.setIcon(play0);
				touXiangTop.setIcon(play1);
			} else { //// 给1号玩家设置一个 头像
				touXiangMine.setIcon(play1);
				touXiangTop.setIcon(play0);
			}

		}
		if (players.size() == 3) {
			// 这里有三个人 规定
			// my代表当前 窗口玩家是哪个
			// 我们需要知道 左边是谁 和右边是谁
			minePlayer = players.get(my);
			if (my == 0) {
				// 代表左边是1 和 顶部2
				leftPlayer = players.get(1);
				topPlayer = players.get(2);
			} else if (my == 1) {
				// 代表左边是 2和 顶部是1
				leftPlayer = players.get(2);
				topPlayer = players.get(1);
			} else if (my == 2) {
				// 代表左边是 0和 顶部是1
				leftPlayer = players.get(0);
				topPlayer = players.get(1);
			}
			playerNameMine.setText(minePlayer.getName());
			playerNameTop.setText(topPlayer.getName());
			playerNameLeft.setText(leftPlayer.getName());
			// 设置头像
			ImageIcon nongmin = new ImageIcon(MainFrame.class.getClassLoader().getResource("image/nongmin.png"));
			ImageIcon dizhu = new ImageIcon(MainFrame.class.getClassLoader().getResource("image/dizhu.png"));
			playerNameLeft.setIcon(nongmin);
			touXiangMine.setIcon(dizhu);
			touXiangTop.setIcon(nongmin);
		}
	}

	/**
	 * 设置 上方玩家的位置
	 * 
	 * @param mainFrame
	 */
	private  void setPlayerTopView(MainFrame mainFrame) {
		JPanel jPanel = new JPanel();
		jPanel.setBounds(90, 50, 130, 85);
		jPanel.setOpaque(false);
		jPanel.setLayout(null);

		touXiangTop = new JLabel();
		touXiangTop.setIcon(new ImageIcon(MainFrame.class.getClassLoader().getResource("image/user.png")));
		touXiangTop.setBounds(2, 2, 93, 83);
		jPanel.add(touXiangTop);
		playerNameTop = new JLabel("韩xx");
		playerNameTop.setForeground(Color.RED);
		playerNameTop.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		playerNameTop.setBounds(50, 2, 50, 30);

		jPanel.add(playerNameTop);
		jPanel.add(touXiangTop);
		mainFrame.getContentPane().add(jPanel);
		mainFrame.repaint();
	}

	/**
	 * 设置 当前玩家的位置
	 * 
	 * @param mainFrame
	 */
	private  void setIdentityView(MainFrame mainFrame) {
		JPanel jPanel = new JPanel();
		jPanel.setBounds(90, 470, 130, 85);
		jPanel.setOpaque(false);
		// jPanel.setBorder(BorderFactory.createLineBorder(Color.green));
		jPanel.setLayout(null);
		touXiangMine = new JLabel();
		touXiangMine.setIcon(new ImageIcon(MainFrame.class.getClassLoader().getResource("image/user.png")));
		touXiangMine.setBounds(2, 2, 93, 83);
		playerNameMine = new JLabel("韩xx");
		playerNameMine.setForeground(Color.RED);
		playerNameMine.setFont(new Font("微软雅黑", Font.PLAIN, 12));
		playerNameMine.setBounds(50, 2, 50, 30);
		jPanel.add(playerNameMine);
		jPanel.add(touXiangMine);
		mainFrame.getContentPane().add(jPanel);
		mainFrame.repaint();
	}

	/**
	 * 显示左边的玩家的信息
	 * 
	 * @param mainFrame2
	 */
	private  void setPlayerLeftView(MainFrame mainFrame2) {
		JPanel jPanel = new JPanel();
		jPanel.setBounds(0, 100, 60, 160);
		jPanel.setOpaque(false);
		jPanel.setLayout(null);

		touXiangLeft = new JLabel();
		touXiangLeft.setIcon(new ImageIcon(MainFrame.class.getClassLoader().getResource("image/user.png")));
		touXiangLeft.setBounds(5, 0, 60, 80);
		jPanel.add(touXiangLeft);

		playerNameLeft = new JLabel("韩xx");
		playerNameLeft.setForeground(Color.WHITE);
		playerNameLeft.setFont(new Font("微软雅黑", Font.PLAIN, 13));
		playerNameLeft.setBounds(5, 80, 100, 30);

		jPanel.add(playerNameLeft);

		mainFrame.getContentPane().add(jPanel);
		mainFrame.repaint();
	}

}
