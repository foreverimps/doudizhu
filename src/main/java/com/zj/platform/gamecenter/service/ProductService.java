package com.zj.platform.gamecenter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zj.platform.gamecenter.dao.ProductDao;
import com.zj.platform.gamecenter.entity.Product;

@Service
public class ProductService {

	@Autowired
	private ProductDao productDao;

	public List<Product> queryByType(int type) {
		return productDao.queryByType(type);
	}

}
