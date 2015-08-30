package com.zj.platform.gamecenter.utils.autoGen.mysql;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;

public class EntityGen {

	private final static String packagePath = "com.zj.platform.gamecenter.entity";

	private final static String SQL_QUERY_ALL_TABLE = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='new_migu'";

	public static void genAllClass(String packagePath) throws Exception {
		List<String> list = queryAllTable();
		Connection con = DBUtils.getConn();
		ResultSet rs = null;
		if (!CollectionUtils.isEmpty(list)) {
			Map<String, List<ColumnBean>> classAttr = new HashMap<String, List<ColumnBean>>();// Map<table_name,List<ColumnBean>>
			for (int i = 0; i < list.size(); i++) {
				List<ColumnBean> columnBeanList = new ArrayList<ColumnBean>();
				String sql = "show full fields from " + list.get(i) + "";
				rs = con.createStatement().executeQuery(sql);
				while (rs.next()) {
					String colName = genEntityAttrname(rs.getString("Field"));
					String getMethod = genAttrGetSetMethod(colName, "get");
					String setMethod = genAttrGetSetMethod(colName, "set");
					String comments = rs.getString("Comment");
					String dataType = rs.getString("Type");
					ColumnBean cb = new ColumnBean();
					cb.setName(colName);
					if (dataType != null && !dataType.equals("")) {
						if ("BIGINT".equals(dataType) || "bigint".equals(dataType) || dataType.contains("bigint") || dataType.contains("BIGINT")) {
							cb.setType("Long");
						}
						if ("decimal".equals(dataType) || "DECIMAL".equals(dataType) || dataType.contains("DECIMAL") || dataType.contains("decimal")) {
							cb.setType("BigDecimal");
						}
						if (dataType.contains("varchar") || dataType.contains("VARCHAR") || dataType.contains("CHAR") || dataType.contains("char")) {
							cb.setType("String");
						}
						if ("timestamp".equals(dataType) || "TIMESTAMP".equals(dataType)) {
							cb.setType("Date");
						}
						if ("longtext".equals(dataType) || "LONGTEXT".equals(dataType)) {
							cb.setType("Blob");
						}
						if (!dataType.contains("bigint") && dataType.contains("int")) {
							cb.setType("int");
						}
					}
					cb.setGetMethod(getMethod);
					cb.setSetMethod(setMethod);
					cb.setComment(comments);
					cb.setColumnName(rs.getString("Field"));
					columnBeanList.add(cb);
				}
				String className = genEntityClassName(list.get(i));
				String tableName = list.get(i);
				classAttr.put(className + "#" + tableName, columnBeanList);
			}
			DBUtils.closeConn(con, null, rs);

			Set<String> classNameSet = classAttr.keySet();
			@SuppressWarnings("rawtypes")
			Iterator it = classNameSet.iterator();
			while (it.hasNext()) {
				String classNameAndTableName = (String) it.next();
				String[] strs = classNameAndTableName.split("#");
				String className = strs[0];
				String tableName = strs[1];
				List<ColumnBean> cbList = classAttr.get(classNameAndTableName);
				genClassFile(className, cbList, packagePath, tableName);
			}

		}
	}

	public static String genAttrGetSetMethod(String entityAttrName, String method) {
		StringBuffer getMethod = new StringBuffer();
		getMethod.append(method);
		entityAttrName = entityAttrName.substring(0, 1).toUpperCase() + entityAttrName.substring(1, entityAttrName.length());
		getMethod.append(entityAttrName);
		return getMethod.toString();
	}

	public static void genClassByTableName(String packagePath, String tableName) throws Exception {
		Connection con = DBUtils.getConn();
		ResultSet rs = null;
		Map<String, List<ColumnBean>> classAttr = new HashMap<String, List<ColumnBean>>();// Map<table_name,List<ColumnBean>>
		List<ColumnBean> columnBeanList = new ArrayList<ColumnBean>();
		String sql = "show full fields from " + tableName + "";
		rs = con.createStatement().executeQuery(sql);
		while (rs.next()) {
			String colName = genEntityAttrname(rs.getString("Field"));
			String getMethod = genAttrGetSetMethod(colName, "get");
			String setMethod = genAttrGetSetMethod(colName, "set");
			String comments = rs.getString("Comment");
			String dataType = rs.getString("Type");
			ColumnBean cb = new ColumnBean();
			cb.setName(colName);
			if (dataType != null && !dataType.equals("")) {
				if ("BIGINT".equals(dataType) || "bigint".equals(dataType) || dataType.contains("bigint") || dataType.contains("BIGINT")) {
					cb.setType("Long");
				}
				if ("decimal".equals(dataType) || "DECIMAL".equals(dataType) || dataType.contains("DECIMAL") || dataType.contains("decimal")
				        || dataType.contains("BigDecimal")) {
					cb.setType("BigDecimal");
				}
				if (dataType.contains("varchar") || dataType.contains("VARCHAR") || dataType.contains("CHAR") || dataType.contains("char")) {
					cb.setType("String");
				}
				if ("timestamp".equals(dataType) || "TIMESTAMP".equals(dataType)) {
					cb.setType("Date");
				}
				if ("longtext".equals(dataType) || "LONGTEXT".equals(dataType)) {
					cb.setType("Blob");
				}
				if (!dataType.contains("bigint") && dataType.contains("int")) {
					cb.setType("int");
				}
			}
			cb.setGetMethod(getMethod);
			cb.setSetMethod(setMethod);
			cb.setComment(comments);
			cb.setColumnName(rs.getString("Field"));
			columnBeanList.add(cb);
		}
		String className = genEntityClassName(tableName);
		classAttr.put(className + "#" + tableName, columnBeanList);

		genClassFile(className, columnBeanList, packagePath, tableName);
	}

	public static void genClassFile(String className, List<ColumnBean> cbList, String packagePath, String tableName) throws Exception {
		if (!CollectionUtils.isEmpty(cbList)) {
			StringBuffer code = new StringBuffer();
			code.append(EntityGenUtil.packageCode(packagePath));// 1.package
			Set<String> set = EntityGenUtil.importsCode(cbList);// 2.import

			Iterator<String> iter = set.iterator();
			while (iter.hasNext()) {
				code.append(iter.next());
			}
			// code.append("@Entity\n");
			// code.append("@Table(name = \"" + tableName + "\")\n");
			code.append("public class " + className + " implements Serializable {\n\n");
			code.append("    /** serialVersionUID */\n");
			code.append("    private static final long serialVersionUID = 1L;\n\n");
			// 3.property
			for (int i = 0; i < cbList.size(); i++) {
				ColumnBean cb = cbList.get(i);
				code.append(EntityGenUtil.propertyCode(cb));
			}
			// 4.get/set
			for (int i = 0; i < cbList.size(); i++) {
				ColumnBean cb = cbList.get(i);
				code.append(EntityGenUtil.getterCode(cb));
				code.append(EntityGenUtil.setterCode(cb));
			}
			code.append("}");
			// 写入java文件

			String filePath = System.getProperty("user.dir") + "\\src\\main\\java\\" + packagePath.replace(".", "\\") + "\\" + className + ".java";
			File file = new File(filePath);
			// 不用手工创建包名，会自动生成一个包名
			file.getParentFile().mkdirs();
			OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(file), "utf-8");
			fw.write(code.toString());
			fw.flush();
			fw.close();
		}
	}

	public static String genEntityAttrname(String cloName) {
		StringBuffer entityAttrName = new StringBuffer();

		String[] strs = cloName.split("_");
		if (strs.length > 1) {// cloName:CLOUMN_NAME_A_B
			for (int i = 0; i < strs.length; i++) {
				String s = strs[i];
				if (i > 0) {
					String firstStr = s.substring(0, 1);
					String afterStr = s.substring(1, s.length());
					entityAttrName.append(firstStr.toUpperCase());
					entityAttrName.append(afterStr.toLowerCase());
				} else {
					entityAttrName.append(s.toLowerCase());
				}
			}
		} else {
			entityAttrName.append(cloName.toLowerCase());

		}
		return entityAttrName.toString();
	}

	public static String genEntityClassName(String tableName) {
		StringBuffer entityClassName = new StringBuffer();

		String[] strs = tableName.split("_");
		if (strs.length > 1) {// tableName:TABLE_NAME_A_B
			for (int i = 1; i < strs.length; i++) {
				String s = strs[i];
				String firstStr = s.substring(0, 1);
				String afterStr = s.substring(1, s.length());
				entityClassName.append(firstStr.toUpperCase());
				entityClassName.append(afterStr.toLowerCase());
			}
		} else {
			String firstStr = tableName.substring(0, 1);
			String afterStr = tableName.substring(1, tableName.length());
			entityClassName.append(firstStr.toUpperCase());
			entityClassName.append(afterStr.toLowerCase());

		}
		return entityClassName.toString();
	}

	public static void main(String[] args) {
		try {
			// genAllClass(packagePath);// 生成全部表，一般不要用，请慎用

			String tableName = "T_PRODUCT";
			genClassByTableName(packagePath, tableName);
			System.out.print("finished!");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<String> queryAllTable() throws Exception {
		List<String> list = new ArrayList<String>();
		Connection con = DBUtils.getConn();

		ResultSet rs = con.createStatement().executeQuery(SQL_QUERY_ALL_TABLE);
		while (rs.next()) {
			String tableName = rs.getString("TABLE_NAME");
			list.add(tableName);
		}

		rs.close();
		return list;
	}
}
