package com.zj.platform.gamecenter.utils.autoGen.oracle;

public class ColumnBean {

	private String columnName;

	private String comment;

	private String getMethod;

	private String name;

	private String setMethod;

	private String type;

	public String getColumnName() {
		return columnName;
	}

	public String getComment() {
		return comment;
	}

	public String getGetMethod() {
		return getMethod;
	}

	public String getName() {
		return name;
	}

	public String getSetMethod() {
		return setMethod;
	}

	public String getType() {
		return type;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setGetMethod(String getMethod) {
		this.getMethod = getMethod;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSetMethod(String setMethod) {
		this.setMethod = setMethod;
	}

	public void setType(String type) {
		this.type = type;
	}

}
