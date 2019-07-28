package com.poker.domain;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * @author q9826 扑克牌封装对象 实现了Comparable接口 卡片类 继承了 JLabel
 */
public class Card extends JLabel implements java.lang.Comparable<Card> {

	private int value; // 牌面的大小
	private HuaSe huaSe = HuaSe.黑桃; // 扑克牌的花色
	private String name;// 扑克牌的名称
	private String img;// 扑克牌显示的图片资源路径

	/**
	 * 扑克牌的构造器 这里可以不用传入花色 默认的花色是黑桃
	 * 
	 * @param value
	 * @param name
	 * @param img
	 */
	public Card(int value, String name, String img) {
		super();
		this.value = value;
		this.name = name;
		this.img = img;
	}

	/**
	 * 扑克牌的全参数构造
	 * 
	 * @param value
	 * @param huaSe
	 * @param name
	 * @param img
	 */
	public Card(int value, HuaSe huaSe, String name, String img) {
		super();
		setSize(100, 152);
		this.value = value;
		// 设置牌面默认为背面
		this.huaSe = huaSe;
		this.name = name;
		this.img = img;
		setIcon(new ImageIcon("Source/pk/" + img));
	}

	@Override
	public int compareTo(Card o) { // 扑克牌的比较器 这里只是比较了牌面大小 后面还会写一个 拥有5个牌的类 进行比较
		if (this.value > o.value) {
			return 1;
		} else if (this.value < o.value) {
			return -1;
		}
		return 0;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public HuaSe getHuaSe() {
		return huaSe;
	}

	public void setHuaSe(HuaSe huaSe) {
		this.huaSe = huaSe;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

//	@Override
//	public String toString()
//	{
//		return "Card [value=" + value + ", huaSe=" + huaSe + ", name=" + name + ", img=" + img + "]";
//	}

	public void overturn() {
		setIcon(new ImageIcon("Source/pk" + img));
	}
	
	public JSONObject toJSONObject() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("value", value);
			jsonObject.put("huaSe", huaSe);
			jsonObject.put("name", name);
			jsonObject.put("img", img);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	@Override
	public String toString() {
		return "Card [value=" + value + ", huaSe=" + huaSe + ", name=" + name + ", img=" + img + "]";
	}

	
	
	
	

}
