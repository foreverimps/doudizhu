package com.zj.platform.gamecenter.test;

import java.lang.reflect.Field;

import com.zj.platform.gamecenter.dto.socket.PlayGameDto;

public class SetMain {

	/**
	 * main函数.
	 * 
	 * @param args
	 *            启动参数
	 * @throws Exception
	 *             Exception
	 */
	public static void main(String... args) throws Exception {

		Class<?> clazz = PlayGameDto.class;
		String objectName = "playGameDto";
		StringBuffer sb = new StringBuffer();
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			String fieldName = field.getName();
			sb.append(objectName + ".set" + fieldName.toUpperCase().substring(0, 1) + fieldName.substring(1, fieldName.length()) + "(" + fieldName
			        + ");\n");
		}
		System.out.println(sb.toString());

	}

	public static void reflect(Class<?> clazz) {

	}
}
