package com.jing.db;

public class CreateTable {
	//�����α�
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
			for (String o : sqll) {// ѭ������SQL��䣬���н���ͳ�ʼ��һЩ���ݲ���
				LoadUtil.createTable(o);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
