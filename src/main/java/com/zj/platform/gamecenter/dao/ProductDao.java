package com.zj.platform.gamecenter.dao;

import java.util.List;

import com.zj.platform.gamecenter.entity.Product;

public interface ProductDao {

	public List<Product> queryByType(int type);

}
