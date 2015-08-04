package com.jing.score;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.cookie.Cookie;

import com.jing.bean.People;

import android.app.Activity;
import android.app.Application;

public class MyApplication extends Application {
	private List<Cookie> cookies = null; 
	private String enrollyear = "";//��ѧ���
	private List<Activity> activitys = new LinkedList<Activity>();//activity����

	private boolean loginstate = false; //�Ƿ��¼
	private boolean chatstate = false;//���ӷ������Ƿ�����
	private String username = "";
	private String password = "";

	private People people; //�û���Ϣ��

	// private String realname = "";
	//
	// private String stunum = "";// ѧ��
	// private String gender = "";// �Ա�
	// private String institude = "";// ѧԺ
	// private int classnum; // �༶
	// private String major = "";// רҵ

	// ���������������޸ģ����񣬻ᱨ��
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

	// ���Activity��������
	public void addActivity(Activity activity) {
		if (activitys != null && activitys.size() > 0) {
			if (!activitys.contains(activity)) {
				activitys.add(activity);
			}
		} else {
			activitys.add(activity);
		}

	}

	// ��������Activity��finish
	public void exit() {
		if (activitys != null && activitys.size() > 0) {
			for (Activity activity : activitys) {
				activity.finish();
			}
		}
		// System.exit(0);
	}
}
