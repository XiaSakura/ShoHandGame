package com.poker.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author q9826 这个是通信的实体
 */
public class Message implements Serializable {

	private int id;// 设置当前摸牌的玩家

	// 通信的状态码
	private int status_code;

	//设置服务器端返回客户端的一张牌
	private Card card;
	
	// 运载的玩家对象
	private List<Player> players;

	public int getStatus_code() {
		return status_code;
	}

	public void setStatus_code(int status_code) {
		this.status_code = status_code;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public Message(int status_code, List<Player> players) {
		super();
		this.status_code = status_code;
		this.players = players;
	}

	public Message() {
		super();

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", status_code=" + status_code + ", players=" + players + "]";
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

}
