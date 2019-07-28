package com.poker.domain;

import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;




/**
 * @author q9826 创建玩家类
 */
public class Player implements Serializable {
	/**
	 * 唯一id 区别玩家
	 */
	private int id;
	/**
	 * 玩家name
	 */
	private String name;
	/**
	 * 该玩家的牌
	 */
	private ArrayList<Card> cards=new ArrayList<>();

	/**
	 * 该玩家对应的socket
	 */
	private Socket socket;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Card> getCards() {
		return cards;
	}
	

	public void setCards(List<Card> cards) {
		this.cards = (ArrayList<Card>) cards;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public Player(int id, String name, ArrayList<Card> cards) {
		super();
		this.id = id;
		this.name = name;
		this.cards = cards;
	}

	public Player(int id, String name, ArrayList<Card> cards, Socket socket) {
		super();
		this.id = id;
		this.name = name;
		this.cards = cards;
		this.socket = socket;
	}

	public Player() {
		super();
	}

	@Override
	public String toString() {
		return "Player [id=" + id + ", name=" + name + ", cards=" + cards + "]";
	}
	

	public JSONObject toJSONObject() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("id", id);
			jsonObject.put("name", name);
			JSONArray array=new JSONArray();
			for (int i = 0; i < cards.size(); i++) {
				array.add(cards.get(i).toJSONObject());
			}
			jsonObject.put("cards", array);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return jsonObject;

	}


}
