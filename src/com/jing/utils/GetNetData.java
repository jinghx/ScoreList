package com.jing.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;

public class GetNetData {
	private static List<Cookie> cookies = null; // 保存获取的cookie
	private static String uricdata = "http://202.194.40.15:7778/pls/wwwbks/xk.CourseView";// 课程链接
	private static String urigdata = "http://202.194.40.15:7778/pls/wwwbks/bkscjcx.yxkc";// 成绩链接
	private static String uricgdata = "http://202.194.40.15:7778/pls/wwwbks/bkscjcx.curscopre";// 当前公示成绩链接
	private static String cdata = "";
	private static String gdata = "";
	private static String cgdata = "";
	private static String infodata = "";// 个人信息源代码
	private static HttpClient client;

	public GetNetData() {

	}

	// 清空数据
	public static void clear() {
		cookies = null;
		cdata = "";
		gdata = "";
		cgdata = "";
		infodata = "";
	}

	public static void setCookies(List<Cookie> cookie) {
		cookies = cookie;
	}

	public static List<Cookie> getCookies() {
		return cookies;
	}

	public static String getIdata() {
		if (infodata == "") {
			getInfoData();
		}
		return infodata;
	}

	public static String getCdata() {
		if (cdata == "") {
			getCourseData();
		}
		return cdata;
	}

	public static String getGdata() {
		if (gdata == "") {
			getGradeData();
		}
		return gdata;
	}

	public static String getCgdata() {
		if (cgdata == "") {
			getCurrentGradeData();
		}
		return cgdata;
	}

	public static HttpClient getClient() {
		return client;
	}

	public static void setClient(HttpClient client) {
		GetNetData.client = client;
	}

	/**
	 * 获得课程源代码
	 */
	public static void getCourseData() {
		if (cookies == null) {
			return;
		}
		// 获取课程信息源代码
		HttpGet httpgetdata = new HttpGet(uricdata);
		try {

			// 把之前的cookie放到此次get所需要的头信息中
			httpgetdata.setHeader("Cookie", cookies.get(0).getName() + "="
					+ cookies.get(0).getValue());

			// 发出HTTP request /
			HttpResponse httpRdata = client.execute(httpgetdata);

			// 若状态码为200 ok /
			if (httpRdata.getStatusLine().getStatusCode() == 200) {
				// tdata.append("333...\n");
				StringBuffer sb = new StringBuffer();
				HttpEntity entity = httpRdata.getEntity();
				InputStream is = entity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, "GB2312"));
				String data = "";
				while ((data = br.readLine()) != null) {
					sb.append(data);
				}
				cdata = sb.toString();
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void getGradeData() {
		if (cookies == null) {
			return;
		}
		// 获取所有成绩源代码
		HttpGet httpgetdata = new HttpGet(urigdata);
		try {

			// 把之前的cookie放到此次get所需要的头信息中
			httpgetdata.setHeader("Cookie", cookies.get(0).getName() + "="
					+ cookies.get(0).getValue());

			// 发出HTTP request /
			HttpResponse httpRdata = client.execute(httpgetdata);

			// 若状态码为200 ok /
			if (httpRdata.getStatusLine().getStatusCode() == 200) {
				StringBuffer sb = new StringBuffer();
				HttpEntity entity = httpRdata.getEntity();
				InputStream is = entity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, "GB2312"));
				String data = "";
				while ((data = br.readLine()) != null) {
					sb.append(data);
				}
				gdata = sb.toString();
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void getCurrentGradeData() {
		if (cookies == null) {
			return;
		}
		HttpGet httpgetdata = new HttpGet(uricgdata);
		try {

			// 把之前的cookie放到此次get所需要的头信息中
			httpgetdata.setHeader("Cookie", cookies.get(0).getName() + "="
					+ cookies.get(0).getValue());

			// 发出HTTP request /
			HttpResponse httpRdata = client.execute(httpgetdata);

			// 若状态码为200 ok /
			if (httpRdata.getStatusLine().getStatusCode() == 200) {
				// tdata.append("333...\n");
				StringBuffer sb = new StringBuffer();
				HttpEntity entity = httpRdata.getEntity();
				InputStream is = entity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, "GB2312"));
				String data = "";
				while ((data = br.readLine()) != null) {
					sb.append(data);
				}
				cgdata = sb.toString();
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 获得个人信息源代码
	 */
	public static void getInfoData() {
		if (cookies == null) {
			return;
		}

		HttpGet httpGet = new HttpGet(
				"http://202.194.40.15:7778/pls/wwwbks/bks_xj.xjcx");

		try {

			// 把之前的cookie放到此次get所需要的头信息中
			httpGet.setHeader("Cookie", cookies.get(0).getName() + "="
					+ cookies.get(0).getValue());

			// 发出HTTP request /
			HttpResponse httpRdata = client.execute(httpGet);

			// 若状态码为200 ok /
			if (httpRdata.getStatusLine().getStatusCode() == 200) {
				// tdata.append("333...\n");
				StringBuffer sb = new StringBuffer();
				HttpEntity entity = httpRdata.getEntity();
				InputStream is = entity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, "GB2312"));
				String data = "";
				while ((data = br.readLine()) != null) {
					sb.append(data);
				}
				infodata = sb.toString();
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
}
