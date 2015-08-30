package com.zj.platform.gamecenter.entity;

import java.io.Serializable;

public class RoomCard implements Serializable {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

	private Long id;

	private Long roomId;

	private Long userId;

	private String cards;

	private int status;

	private int sort;

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id=id;
	}

	public Long getRoomId(){
		return roomId;
	}

	public void setRoomId(Long roomId){
		this.roomId=roomId;
	}

	public Long getUserId(){
		return userId;
	}

	public void setUserId(Long userId){
		this.userId=userId;
	}

	public String getCards(){
		return cards;
	}

	public void setCards(String cards){
		this.cards=cards;
	}

	public int getStatus(){
		return status;
	}

	public void setStatus(int status){
		this.status=status;
	}

	public int getSort(){
		return sort;
	}

	public void setSort(int sort){
		this.sort=sort;
	}

}