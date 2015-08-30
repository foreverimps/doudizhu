package com.zj.platform.gamecenter.dto.socket;

import java.math.BigDecimal;

public class ProductDto {

	private Long id;// 商品ID

	private String name;// 商品名称

	private int type;// 类型(1:商品兑换 2：宝箱)

	private BigDecimal price;// 价格

	private BigDecimal proPrice;// 促销价格

	private String descs;// 商品描述

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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getProPrice() {
		return proPrice;
	}

	public void setProPrice(BigDecimal proPrice) {
		this.proPrice = proPrice;
	}

	public String getDescs() {
		return descs;
	}

	public void setDescs(String descs) {
		this.descs = descs;
	}

	public int getType() {
	    return type;
    }

	public void setType(int type) {
	    this.type = type;
    }

}
