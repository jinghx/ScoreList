package com.jing.score;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.cookie.Cookie;

import com.jing.bean.People;

import android.app.Activity;
import android.app.Application;

public class MyApplication extends Application {
	private List<Cookie> cookies = null; 
	private String enrollyear = "";//入学年份
	private List<Activity> activitys = new LinkedList<Activity>();//activity集合

	private boolean loginstate = false; //是否登录
	private boolean chatstate = false;//连接服务器是否正常
	private String username = "";
	private String password = "";

	private People people; //用户信息类

	// private String realname = "";
	//
	// private String stunum = "";// 学号
	// private String gender = "";// 性别
	// private String institude = "";// 学院
	// private int classnum; // 班级
	// private String major = "";// 专业

	// 构造器不能随意修改，好像，会报错
	/*
	 * private MyApplication() { activitys = new LinkedList<Activity>(); }
	 */

	public void logOut() {
		loginstate = false;
		enrollyear = "";
		cookies = null;
		activitys.clear();
		username = "";
		password = "";
	}

	public People getPeople() {
		return people;
	}

	public void setPeople(People people) {
		this.people = people;
	}

	public List<Cookie> getCookies() {
		return cookies;
	}

	public void setCookies(List<Cookie> cookies) {
		this.cookies = cookies;
	}

	public String getEnrollyear() {
		return enrollyear;
	}

	public void setEnrollyear(String enrollyear) {
		this.enrollyear = enrollyear;
	}

	public boolean isChatstate() {
		return chatstate;
	}

	public void setChatstate(boolean chatstate) {
		this.chatstate = chatstate;
	}

	public boolean isLogin() {
		return loginstate;
	}

	public void setLoginstate(boolean loginstate) {
		this.loginstate = loginstate;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		if (activitys != null && activitys.size() > 0) {
			if (!activitys.contains(activity)) {
				activitys.add(activity);
			}
		} else {
			activitys.add(activity);
		}

	}

	// 遍历所有Activity并finish
	public void exit() {
		if (activitys != null && activitys.size() > 0) {
			for (Activity activity : activitys) {
				activity.finish();
			}
		}
		// System.exit(0);
	}
}
