package com.jing.bean;

public class GradeInfo {
	// 还有个排名否
	private String coursenumber; // 课程号
	private String name; // 课名
	private String ordernumber; // 课序号
	private String credit = ""; // 学分
	private String testtime; // 考试时间
	private String expscore; // 实验成绩
	private String comscore; // 平时成绩
	private String testscore; // 期末成绩
	private String finalscore = ""; // 总成绩
	private String courseprop; // 课程属性
	private String alternum; // 替代课程号
	private String testtype; // 考试类型
	private String remark;// 备注

	private String creditname = "";
	private String scorename = "";
	private String pm = "";
	private String max = "";
	private String min = "";
	private String rank = "";
	private String choosedNum = "";

	public GradeInfo() {

	}

	public String getCoursenumber() {
		return coursenumber;
	}

	public void setCoursenumber(String coursenumber) {
		this.coursenumber = coursenumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrdernumber() {
		return ordernumber;
	}

	public void setOrdernumber(String ordernumber) {
		this.ordernumber = ordernumber;
	}

	public String getCredit() {
		return credit;
	}

	public void setCredit(String credit) {
		this.credit = credit;
	}

	public String getTesttime() {
		return testtime;
	}

	public void setTesttime(String testtime) {
		this.testtime = testtime;
	}

	public String getExpscore() {
		return expscore;
	}

	public void setExpscore(String expscore) {
		this.expscore = expscore;
	}

	public String getComscore() {
		return comscore;
	}

	public void setComscore(String comscore) {
		this.comscore = comscore;
	}

	public String getTestscore() {
		return testscore;
	}

	public void setTestscore(String testscore) {
		this.testscore = testscore;
	}

	public String getFinalscore() {
		return finalscore;
	}

	public void setFinalscore(String finalscore) {
		this.finalscore = finalscore;
	}

	public String getCourseprop() {
		return courseprop;
	}

	public void setCourseprop(String courseprop) {
		this.courseprop = courseprop;
	}

	public String getAlternum() {
		return alternum;
	}

	public void setAlternum(String alternum) {
		this.alternum = alternum;
	}

	public String getTesttype() {
		return testtype;
	}

	public void setTesttype(String testtype) {
		this.testtype = testtype;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreditname() {
		return creditname;
	}

	public void setCreditname(String creditname) {
		this.creditname = creditname;
	}

	public String getScorename() {
		return scorename;
	}

	public void setScorename(String scorename) {
		this.scorename = scorename;
	}

	public String getPm() {
		return pm;
	}

	public void setPm(String pm) {
		this.pm = pm;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getChoosedNum() {
		return choosedNum;
	}

	public void setChoosedNum(String choosedNum) {
		this.choosedNum = choosedNum;
	}
}
