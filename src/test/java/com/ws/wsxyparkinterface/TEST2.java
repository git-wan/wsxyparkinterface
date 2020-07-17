package com.ws.wsxyparkinterface;

import java.security.MessageDigest;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class TEST2 {
	public static void main(String[] args) throws Exception {
		// 接口地址
		String url = "http://syx.jslife.com.cn/jsaims/as";
		// 客户号
		String cid = "880075500000001";
		// 接口版本号
		String v = "2";
		// 参数
		//String p = "{\"serviceId\":\"3c.pay.querycarbycarno\",\"requestType\":\"DATA\",\"attributes\":{\"parkCode\":\"0010028888\",\"carNo\":\"藏-DKFJ69\"}}";
		//藏-SDDFD9 藏-FDFM92 藏-DKFJ69
		//请求计费
		String p = "{\"serviceId\":\"3c.pay.createorderbycarno\",\"requestType\":\"DATA\",\"attributes\":{\"businesserCode\":\"880075500000001\",\"parkCode\":\"0010028888\",\"orderType\":\"VNP\",\"carNo\":\"藏-DKFJ69\"}}";
		//缴费
		//String p = "{\"serviceId\":\"3c.pay.notifyorderresult\",\"requestType\":\"DATA\",\"attributes\":{\"orderNo \":\"ddd\",\"tradeStatus\":\"0\",\"payType\":\"WX\",\"payTypeName\":\"微信微信\",\"payTime\":\"2020-06-30 17:50:50\",\"transactionId\":\"123456789\"}}";
		//String p = "{\"serviceId\":\"3c.pay.notifyorderresult\",\"requestType\":\"DATA\",\"attributes\":{\"orderNo\":\"aca2a01a93d24d1e8e13a542c4cec192\",\"tradeStatus\":\"0\",\"payType\":\"WX\",\"payTypeName\":\"微信微信\",\"payTime\":\"2020-06-30 17:50:50\",\"transactionId\":\"123456789\"}}";
		// 参数
		String signKey = "7ac3e2ee1075bf4bb6b816c1e80126c0";
		String tn = "b96b6bb4c9596ba1b1bc4470d777e3ed";
		// 生成MD5签名
		MessageDigest md5Tool = MessageDigest.getInstance("MD5");
		byte[] md5Data = md5Tool.digest((p + signKey).getBytes("UTF-8"));
		String sn = toHexString(md5Data);

		// 构造参数
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("cid", cid));
		list.add(new BasicNameValuePair("tn", tn));
		list.add(new BasicNameValuePair("sn", sn));
		list.add(new BasicNameValuePair("v", v));
		list.add(new BasicNameValuePair("p", p));
		HttpEntity en = new UrlEncodedFormEntity(list, HTTP.UTF_8);
		post.setEntity(en);

		// 发送消息和处理结果
		HttpResponse response = client.execute(post);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			System.out.println("执行成功！");
			String s = EntityUtils.toString(response.getEntity(), "UTF-8");
			System.out.println(s);
		} else {
			System.out.println("执行失败！");
		}
	}

	private static String toHexString(byte[] bytes) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			buffer.append(String.format("%02X", bytes[i]));
		}
		return buffer.toString();
	}
}
