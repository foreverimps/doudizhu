package com.zj.platform.gamecenter.utils;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.HttpException;

public class SmsUtil {

	private static final String url = PropertiesUtil.getInstance("config.properties").getText("sms_url");

	private static final boolean isTest = true;

	public static String getSmsCode(String phone, String accountId, String optType, String gameName) throws HttpException, IOException {
		String sRand = "123123";
		if (isTest) {
			// 测试环境
			sRand = "123123";
			System.out.println("*******************验证码：" + sRand + " ************************");
		} else {
			// 调用短信发送接口
			sRand = sendSmsSync(phone, accountId, optType, gameName);
		}
		return sRand;
	}

	public static String getSmsCode(String phone, String accountId, int optType, String gameName) throws HttpException, IOException {
		return getSmsCode(phone, accountId, optType + "", gameName);
	}

	private static String sendSmsSync(String phone, String accountId, String type, String gameName) throws HttpException, IOException {
		System.out.println("--------------");
		HttpClient client = new HttpClient();
		client.setTimeout(30000);
		PostMethod method = new PostMethod(url);
		method.addRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
		NameValuePair[] data = { new NameValuePair("phone", phone), new NameValuePair("accountId", accountId),
		        new NameValuePair("gameName", gameName), new NameValuePair("type", type) };
		method.addParameters(data);
		client.executeMethod(method);
		String retmsg = method.getResponseBodyAsString().trim();
		System.out.println(retmsg);
		method.releaseConnection();
		method = null;
		client = null;
		return retmsg;
	}

}
