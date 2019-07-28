package com.poker.view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.poker.service.Connect;

import javax.swing.JTextField;

public class Login extends JFrame {

	private static JButton jButton;
	public static JTextField jTextField;
	public static JTextField IP_addrres;
	public static JLabel peoples;
	public static Login login;

	// 主函数入口 最好使用静态
	public static void main(String[] args) {
		login = new Login();
		login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		login.setLayout(null);
		login.setSize(250, 230);
		login.setLocationRelativeTo(null);// 设置居中，要放在设置窗口大小之后

		Font font = new Font("微软雅黑", 1, 13);
		JLabel jLabel = new JLabel("请输入姓名:");
		jLabel.setFont(font);
		jLabel.setBounds(10, 20, 70, 20);

		jTextField = new JTextField();
		jTextField.setFont(font);
		jTextField.setBounds(82, 20, 150, 20);

		JLabel jLabel4 = new JLabel("服务器ip地址:");
		jLabel4.setFont(font);
		jLabel4.setBounds(10, 40, 70, 20);
		IP_addrres = new JTextField();
		IP_addrres.setFont(font);
		IP_addrres.setBounds(82, 40, 150, 20);
		IP_addrres.setText("127.0.0.1");

		JLabel jLabel2 = new JLabel("当前人数:");
		jLabel2.setFont(font);
		jLabel2.setBounds(10, 60, 60, 20);

		peoples = new JLabel("0");
		peoples.setFont(font);
		peoples.setBounds(72, 60, 10, 20);

		JLabel jLabel3 = new JLabel("(当人数到达2时,游戏开始)");
		jLabel3.setFont(font);
		jLabel3.setBounds(10, 80, 180, 20);

		jButton = new JButton("准     备");
		jButton.setBounds(10, 130, 225, 30);
		jButton.setFont(font);

		jButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String name = jTextField.getText();
				String ip = IP_addrres.getText();
				if (name != null && name.length() > 0 && ip.length() > 0) {
					jButton.setEnabled(false);
					Connect connect = Connect.getInstance();
					boolean b = connect.connect();
					if (b) {

					}
				} else {
					JOptionPane.showMessageDialog(null,
							new JLabel("<html><h2><font color='red'>请输入姓名和ip</font></h2></html>"), "提示",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		login.add(jButton);

		login.add(jLabel3);
		login.add(peoples);
		login.add(jLabel2);
		login.add(jTextField);
		login.add(jLabel);
		login.add(jLabel4);
		login.add(IP_addrres);
		login.setVisible(true);
		login.repaint();
	}

}
