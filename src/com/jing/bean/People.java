package com.jing.bean;

public class People {
	private String realname = "";//真实姓名
	private String stunum = "";// 学号
	private String gender = "";// 性别
	private String institude = "";// 学院
	private int classnum; // 班级
	private String major = "";// 专业

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getStunum() {
		return stunum;
	}

	public void setStunum(String stunum) {
		this.stunum = stunum;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getInstitude() {
		return institude;
	}

	public void setInstitude(String institude) {
		this.institude = institude;
	}

	public int getClassnum() {
		return classnum;
	}

	public void setClassnum(int classnum) {
		this.classnum = classnum;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}
}
