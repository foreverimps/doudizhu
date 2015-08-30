package com.zj.platform.gamecenter.utils;

import org.apache.ibatis.session.RowBounds;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * Mybatis - 分页对象
 */
public class Page<E> {

	/**
	 * 不进行count查询
	 */
	public static final int NO_SQL_COUNT = -1;

	public static final int SQL_COUNT = 0;

	public static final int PAGE_SIZE = 10;

	private int pageNo;

	private int pageSize;

	private int startRow;

	private int endRow;

	private int totalPages;

	private long totalNum;

	private List<E> result;

	public Page(int pageNum, int pageSize) {
		this(pageNum, pageSize, SQL_COUNT);
	}

	public Page(int pageNo, int pageSize, int totalNum) {
		result = new ArrayList<>(pageSize);
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.totalNum = totalNum;
		this.startRow = pageNo > 0 ? (pageNo - 1) * pageSize : 0;
		this.endRow = this.startRow + this.pageSize;
	}

	public Page(RowBounds rowBounds, int totalNum) {
		result = new ArrayList<>(rowBounds.getLimit());
		this.pageSize = rowBounds.getLimit();
		this.startRow = rowBounds.getOffset();
		// RowBounds方式默认不求count总数，如果想求count,可以修改这里为SQL_COUNT
		this.totalNum = totalNum;
		this.endRow = this.startRow + this.pageSize;
	}

	/**
	 * 获取 pageNo
	 * 
	 * @return pageNo
	 */
	public int getPageNo() {
		return pageNo;
	}

	/**
	 * 设置 pageNo
	 * 
	 * @param pageNo
	 *            pageNo
	 */
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * 获取 pageSize
	 * 
	 * @return pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置 pageSize
	 * 
	 * @param pageSize
	 *            pageSize
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 获取 startRow
	 * 
	 * @return startRow
	 */
	public int getStartRow() {
		return startRow;
	}

	/**
	 * 设置 startRow
	 * 
	 * @param startRow
	 *            startRow
	 */
	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	/**
	 * 获取 endRow
	 * 
	 * @return endRow
	 */
	public int getEndRow() {
		return endRow;
	}

	/**
	 * 设置 endRow
	 * 
	 * @param endRow
	 *            endRow
	 */
	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	/**
	 * 获取 totalPages
	 * 
	 * @return totalPages
	 */
	public int getTotalPages() {
		return totalPages;
	}

	/**
	 * 设置 totalPages
	 * 
	 * @param totalPages
	 *            totalPages
	 */
	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	/**
	 * 获取 totalNum
	 * 
	 * @return totalNum
	 */
	public long getTotalNum() {
		return totalNum;
	}

	/**
	 * 设置 totalNum
	 * 
	 * @param totalNum
	 *            totalNum
	 */
	public void setTotalNum(long totalNum) {
		this.totalNum = totalNum;
	}

	/**
	 * 获取 result
	 * 
	 * @return result
	 */
	public List<E> getResult() {
		return result;
	}

	/**
	 * 设置 result
	 * 
	 * @param result
	 *            result
	 */
	public void setResult(List<E> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}