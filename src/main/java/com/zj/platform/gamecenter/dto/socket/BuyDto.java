package com.zj.platform.gamecenter.dto.socket;


public class BuyDto {

	private Long productId;// 商品ID

	private String count;// 商品数量

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

}
