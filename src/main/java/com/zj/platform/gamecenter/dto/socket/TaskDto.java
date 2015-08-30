package com.zj.platform.gamecenter.dto.socket;

public class TaskDto {

	private Long id;// 任务ID

	private int type;// 每日任务，新手任务，成就

	private String name;// 任务名称

	private String descs;// 任务描述

	private Long amount;// 数量

	private int count;// 次数

	private int hasCount;// 已经完成的次数

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescs() {
		return descs;
	}

	public void setDescs(String descs) {
		this.descs = descs;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getHasCount() {
		return hasCount;
	}

	public void setHasCount(int hasCount) {
		this.hasCount = hasCount;
	}

	public int getType() {
	    return type;
    }

	public void setType(int type) {
	    this.type = type;
    }

}
