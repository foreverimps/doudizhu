package com.zj.platform.gamecenter.entity;

public class Card {

	private int num;

	private int color;// 1:黑桃 2:红桃 3:梅花 4:方块

	public Card(int num, int color) {
		this.num = num;
		this.color = color;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	@Override
	public int hashCode() {
		return new Integer(color * 100).hashCode() + new Integer(num).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Card)) {
			return false;
		}
		Card card = (Card) obj;
		if (card.color == color && card.num == num) {
			return true;
		}
		return false;
	}

}
