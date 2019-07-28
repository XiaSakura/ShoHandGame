package com.poker.service;

import javax.swing.JLabel;

import com.poker.view.MainFrame;

/**
 * @author q9826 判断能否摸牌 一个工具类
 */
public class CanDealPoker {

	private static MainFrame mainFrame = MainFrame.getInstance();
	private static OutMouseEvent outMouseEvent = new OutMouseEvent(mainFrame.call, mainFrame);
	private static PassMouseEvent passMouseEvent = new PassMouseEvent(mainFrame.discard, mainFrame);

	/**
	 * 设置能够摸牌
	 * 
	 * @param out
	 * @param choose
	 * @param pass
	 */
	public static void setCanDeal() {
		// 现移除所有监听器
		mainFrame.call.removeMouseListener(outMouseEvent);
		mainFrame.discard.removeMouseListener(passMouseEvent);
		// 添加监听器
		mainFrame.call.addMouseListener(outMouseEvent);
		mainFrame.call.setEnabled(true);
		mainFrame.discard.addMouseListener(passMouseEvent);
		mainFrame.discard.setEnabled(true);
	}

	/**
	 * 设置不能摸牌
	 * 
	 * @param out
	 * @param pass
	 */
	public static void setCannotDeal() {
		// 现移除所有监听器
		mainFrame.call.removeMouseListener(outMouseEvent);
		mainFrame.discard.removeMouseListener(passMouseEvent);
		mainFrame.call.addMouseListener(null);
		mainFrame.call.setEnabled(false);
		mainFrame.discard.addMouseListener(null);
		mainFrame.discard.setEnabled(false);
	}

	/**
	 * 设置倒计时
	 * 
	 * @param who    是谁的倒计时 0 自己 1右边 2左边
	 * @param jLabel
	 */
	public static void setCountShow(int who, JLabel jLabel) {
//		for(int i=0;i<list.size();i++){
//			list.get(i).stopRequest();
//		}
//		list.retainAll(list);
//		if(who==0){
//			jLabel.setBounds(380, 355, 50, 50);
//		}else if (who==1) {
//			jLabel.setBounds(680, 200, 50, 50);
//		}else if (who==2) {
//			jLabel.setBounds(70, 200, 50, 50);
//		}
//		//jLabel.setVisible(true);
//		// 倒计时
//		CountThread countThread=new CountThread(jLabel,30);
//		list.add(countThread);
//		countThread.start();
	}

}
