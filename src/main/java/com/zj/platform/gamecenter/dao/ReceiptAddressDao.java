package com.zj.platform.gamecenter.dao;

import java.util.List;

import com.zj.platform.gamecenter.entity.ReceiptAddress;

public interface ReceiptAddressDao {

	public List<ReceiptAddress> queryByUserId(Long userId);

	public void insert(ReceiptAddress receiptAddress);

}
