package com.zj.platform.gamecenter.utils.autoGen.oracle;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class EntityGenUtil {

	public static String daoBasicQueryCode(String className, String tableName) {
		StringBuffer daoBasicQueryCode = new StringBuffer();
		daoBasicQueryCode.append("	public List<Map<String, Object>> queryForList() {\n");
		daoBasicQueryCode.append("		return  queryForListMap(QUERY_ALL);\n");
		daoBasicQueryCode.append("	}\n\n");

		daoBasicQueryCode.append("	public Page queryForList(int pageNo, int pageSize, " + className + " obj) {\n");
		daoBasicQueryCode.append("		return queryForPage(\"\", obj, pageNo, pageSize);\n");
		daoBasicQueryCode.append("	}\n\n");

		daoBasicQueryCode.append("	public void save(" + className + " obj) {\n");
		daoBasicQueryCode.append("		save(INSERT, obj);\n");
		daoBasicQueryCode.append("	}\n\n");

		return daoBasicQueryCode.toString();
	}

	public static String daoBasicQuerySqlCode(String tableName, List<ColumnBean> list) {
		String priCloumnName = list.get(0).getColumnName();
		String priName = list.get(0).getName();
		StringBuffer daoBasicQuerySqlCode = new StringBuffer();
		daoBasicQuerySqlCode.append("	private final static String QUERY_ALL  = \"SELECT * FROM " + tableName + "\";\n\n");
		daoBasicQuerySqlCode.append("	private final static String QUERY_BY_" + priCloumnName + " = \"SELECT * FROM " + tableName + " WHERE "
		        + priCloumnName + " = :" + priName + "\";\n\n");
		daoBasicQuerySqlCode.append("	private final static String QUERY_BY_PROPERTY = \"SELECT * FROM " + tableName
		        + " WHERE #propertyName# = :#propertyValue#\";\n\n");

		daoBasicQuerySqlCode.append("	private final static String INSERT  = \"INSERT INTO  " + tableName + " (");
		for (int i = 0; i < list.size(); i++) {
			daoBasicQuerySqlCode.append(list.get(i).getColumnName());
			if (i == list.size() - 1) {
				daoBasicQuerySqlCode.append(")");
			} else {
				daoBasicQuerySqlCode.append(",");
			}
		}
		daoBasicQuerySqlCode.append(" VALUES (");
		for (int i = 0; i < list.size(); i++) {
			daoBasicQuerySqlCode.append(":" + list.get(i).getName());
			if (i == list.size() - 1) {
				daoBasicQuerySqlCode.append(")\";\n\n");
			} else {
				daoBasicQuerySqlCode.append(",");
			}
		}

		daoBasicQuerySqlCode.append("	private final static String DELETE  = \" DELETE FROM  " + tableName + " WHERE " + priCloumnName + " = :"
		        + priName + "\";\n\n");

		daoBasicQuerySqlCode.append("	private final static String DELETE_BY_PROPERTY = \" DELETE FROM  " + tableName
		        + " WHERE #propertyName# = :#propertyName#\";\n\n");

		daoBasicQuerySqlCode.append("	private final static String UPDATE_PROPERTY = \" UPDATE " + tableName
		        + " SET  #propertyName# = :#propertyValue# WHERE " + priCloumnName + " = :" + priName + "\";\n\n");

		return daoBasicQuerySqlCode.toString();
	}

	public static String getterCode(ColumnBean cb) {
		StringBuffer getterCode = new StringBuffer();
		// if (!cb.getColumnName().equals("ID")) {
		// getterCode.append("	@Column(name = \"" + cb.getColumnName() +
		// "\")\n");
		getterCode.append("	public " + cb.getType() + " " + cb.getGetMethod() + "(){\n");
		getterCode.append("		return " + cb.getName() + ";\n");
		getterCode.append("	}\n\n");
		// }

		return getterCode.toString();
	}

	public static Set<String> importsCode(List<ColumnBean> list) {

		Set<String> set = new LinkedHashSet<String>();
		int n = list.size();
		for (int i = 0; i < n; i++) {
			ColumnBean cb = list.get(i);
			if ("Date".equals(cb.getType())) {
				set.add("import java.util.Date;\n");
			} else if ("BigDecimal".equals(cb.getType())) {
				set.add("import java.math.BigDecimal;\n");
			} else if ("Blob".equals(cb.getType())) {
				set.add("import java.sql.Blob;\n");
			}

		}
		set.add("import java.io.Serializable;\n");
		// set.add("import javax.persistence.Column;\n");
		// set.add("import javax.persistence.Entity;\n");
		// set.add("import javax.persistence.GeneratedValue;\n");
		// set.add("import javax.persistence.GenerationType;\n");
		// set.add("import javax.persistence.Id;\n");
		// set.add("import javax.persistence.SequenceGenerator;\n");
		// set.add("import javax.persistence.Table;\n");
		set.add("\n");
		return set;
	}

	public static Set<String> importsDaoCode(String className) {
		Set<String> set = new LinkedHashSet<String>();
		set.add("import org.springframework.data.domain.Page;\n");
		set.add("import org.springframework.data.domain.Pageable;\n");
		set.add("import org.springframework.data.jpa.repository.JpaSpecificationExecutor;\n");
		set.add("import org.springframework.data.jpa.repository.Modifying;\n");
		set.add("import org.springframework.data.jpa.repository.Query;\n");
		set.add("import org.springframework.data.repository.PagingAndSortingRepository;\n");
		set.add("import com.migu.dg.entity." + className + ";\n");
		// set.add("import com.migu.game.utils.Page;\n");
		// set.add("import com.migu.game.entity." + className + ";\n");
		set.add("\n");
		return set;
	}

	public static Set<String> importsServiceCode(String className) {
		Set<String> set = new LinkedHashSet<String>();
		set.add("import org.springframework.beans.factory.annotation.Autowired;\n");
		set.add("import org.springframework.stereotype.Service;\n");
		set.add("import org.springframework.transaction.annotation.Transactional;\n");
		set.add("import com.migu.dg.dao." + className + "Dao;\n");
		set.add("\n");
		return set;
	}

	public static String packageCode(String packagePath) {
		return "package " + packagePath + ";\n\n";
	}

	public static String propertyCode(ColumnBean cb) {
		StringBuffer propertyCode = new StringBuffer();
		// if (!(cb.getColumnName().equals("ID") ||
		// cb.getColumnName().equals("id"))) {
		if (cb.getComment() != null && !"".equals(cb.getComment())) {
			propertyCode.append("	/**" + cb.getComment() + "    " + cb.getColumnName() + "**/\n");
		}
		propertyCode.append("	private " + cb.getType() + " " + cb.getName() + ";\n\n");

		// }
		return propertyCode.toString();
	}

	public static String setterCode(ColumnBean cb) {
		StringBuffer setterCode = new StringBuffer();
		// if (!(cb.getColumnName().equals("ID") ||
		// cb.getColumnName().equals("id"))) {
		setterCode.append("	public void " + cb.getSetMethod() + "(" + cb.getType() + " " + cb.getName() + "){\n");
		setterCode.append("		this." + cb.getName() + "=" + cb.getName() + ";\n");
		setterCode.append("	}\n\n");
		// }

		return setterCode.toString();
	}
}
