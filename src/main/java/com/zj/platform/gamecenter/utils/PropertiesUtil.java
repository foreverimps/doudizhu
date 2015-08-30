package com.zj.platform.gamecenter.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesUtil {

	private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);

	private static ThreadLocal<String> sources = new ThreadLocal<String>();

	private static Map<String, Properties> properties = new HashMap<String, Properties>();

	public String getText(String key) {
		String source = sources.get();
		return properties.get(source).getProperty(key);
	}

	static class PropertiesUtilHolder {

		static PropertiesUtil instance = new PropertiesUtil();
	}

	public static PropertiesUtil getInstance(String source) {
		sources.set(source);
		if (properties.get(source) == null) {
			setProperties(source);
			if (logger.isDebugEnabled()) {
				logger.debug("init properties name is " + source);
			}
		}
		return PropertiesUtilHolder.instance;
	}

	private static void setProperties(String source) {
		InputStream inputStream = null;
		try {

			inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(source);
			Properties p = new Properties();
			p.load(inputStream);
			properties.put(source, p);

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				inputStream.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
