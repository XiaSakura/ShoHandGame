package com.poker.domain;


import java.io.Serializable;
import java.util.List;

/**
 * @author q9826 这个是通信的实体
 */
public class Message implements Serializable {

	// 通信的状态码
	private int status_code;
	
	private String player_name;
	/**
	 * 消息框的消息
	 */
	private String messge;
	
	/**
	 *当前线程玩家的id
	 */
	private int my_id;
	
	private int current_player_id;
	
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

	public String getPlayer_name() {
		return player_name;
	}

	public void setPlayer_name(String player_name) {
		this.player_name = player_name;
	}

	public Message(int status_code, String player_name, List<Player> players) {
		super();
		this.status_code = status_code;
		this.player_name = player_name;
		this.players = players;
	}

	public int getCurrent_player_id() {
		return current_player_id;
	}

	public void setCurrent_player_id(int current_player_id) {
		this.current_player_id = current_player_id;
	}

	public String getMessge() {
		return messge;
	}

	public void setMessge(String messge) {
		this.messge = messge;
	}

	public int getMy_id() {
		return my_id;
	}

	public void setMy_id(int my_id) {
		this.my_id = my_id;
	}

	
}

