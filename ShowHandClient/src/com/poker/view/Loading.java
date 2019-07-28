package com.poker.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;

import com.poker.domain.Player;

public class Loading extends JFrame {

	private static final long serialVersionUID = 1L;
	private JProgressBar processBar;
	private ImageIcon background = null;
	private JLabel background_label = null;// 背景面板二
	public JPanel image_panel = null;

	public static Loading loading;

	public Loading(List<Player> players, int my_id) {
		loading = this;
		setTitle("进度条使用"); // 设置窗体标题

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗体退出的操作

		JPanel contentPane = new JPanel(); // 创建内容面板

		contentPane.setBorder(new EmptyBorder(290, 5, 5, 5));// 设置内容面板边框

		setContentPane(contentPane);// 应用(使用)内容面板

		background = new ImageIcon(MainFrame.class.getClassLoader().getResource("image/tiao.jpg")); // 背景图片
		int WIDTH = background.getIconWidth();
		int HEIGHT = background.getIconHeight();
		this.setSize(WIDTH, HEIGHT);
		this.setResizable(false);// 设置为用户不可改变窗口大小
		this.setLocationRelativeTo(getOwner());// 将窗口设置为正中央
		image_panel = (JPanel) this.getContentPane();
		image_panel.setLayout(null);
		background_label = new JLabel(background);
		background_label.setBounds(0, 0, WIDTH, HEIGHT);
		image_panel.setOpaque(false);
		this.getLayeredPane().setLayout(null);// 固定窗口大小
		this.getLayeredPane().add(background_label, new Integer(Integer.MIN_VALUE));
			
		contentPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));// 设置为流式布局

		processBar = new JProgressBar();// 创建进度条

		processBar.setStringPainted(true);// 设置进度条上的字符串显示，false则不能显示

		processBar.setBackground(Color.blue);

		// 创建线程显示进度
		new Thread() {
			public void run() {
				for (int i = 0; i < 101; i++) {
					try {
						Thread.sleep(20); // 让当前线程休眠0.02s
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					processBar.setValue(i); // 设置进度条数值
				}
				processBar.setString("游戏开始");// 设置提示信息
				MainFrame mainFrame = MainFrame.getInstance();
				//新建一个主界面
				mainFrame.startMain(players, my_id);
				loading.dispose();
			}
		}.start(); // 启动进度条线程

		contentPane.add(processBar);// 向面板添加进度控件
	}

}
