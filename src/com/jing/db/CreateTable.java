package com.jing.db;

public class CreateTable {
	//创建课表
	public static void creattable() {
		try {
			String sqll[] = new String[] {
			// cid integer primary key,
			"create table if not exists course "
					+ "(cname char(20),climit char(20),cnum char(20),"
					+ "conum char(20),cprop char(10),ctest char(10),croom char(20),"
					+ "ctime char(10),cweek char(10),cstunum char(20))",
			"create table if not exists account "
					+ "(name char(20),pwd char(20) )"};
			for (String o : sqll) {// 循环所有SQL语句，进行建表和初始化一些数据操作
				LoadUtil.createTable(o);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
