package com.jing.db;

import java.util.Vector;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LoadUtil {

	@SuppressLint("SdCardPath")
	public static SQLiteDatabase createOrOpenDatabase()// 连接数据库
	{
		SQLiteDatabase sld = null;
		try {
			sld = SQLiteDatabase.openDatabase// 连接并创建数据库，如果不存在则创建
					("/data/data/com.example.scorelist/mydb", null,
							SQLiteDatabase.OPEN_READWRITE
									| SQLiteDatabase.CREATE_IF_NECESSARY);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sld;// 返回该连接
	}

	public static void createTable(String sql) {// 创建表
		SQLiteDatabase sld = createOrOpenDatabase();// 连接数据库
		try {
			sld.execSQL(sql);// 执行SQL语句
			sld.close();// 关闭连接
		} catch (Exception e) {

		}
	}

	public static boolean insert(String sql)// 插入数据
	{
		SQLiteDatabase sld = createOrOpenDatabase();// 连接数据库
		try {
			sld.execSQL(sql);
			sld.close();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public static boolean delete(String sql)// 插入数据
	{
		SQLiteDatabase sld = createOrOpenDatabase();// 连接数据库
		try {
			sld.execSQL(sql);
			sld.close();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public static Vector<Vector<String>> query(String sql)// 查询
	{
		Vector<Vector<String>> vector = new Vector<Vector<String>>();// 新建存放查询结果的向量
		SQLiteDatabase sld = createOrOpenDatabase();// 得到连接数据库的连接

		try {
			Cursor cur = sld.rawQuery(sql, new String[] {});// 得到结果集

			while (cur.moveToNext())// 如果存在下一条
			{
				Vector<String> v = new Vector<String>();
				int col = cur.getColumnCount(); // 将其放入向量
				for (int i = 0; i < col; i++) {
					v.add(cur.getString(i));
				}
				vector.add(v);
			}
			cur.close();// 关闭结果集
			sld.close();// 关闭连接
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vector;
	}

	public static Vector<Vector<String>> getDefault() {
		String sql = "select cname,climit,cnum,conum,cprop,ctest,croom,ctime,cweek from course";
		Vector<Vector<String>> vtemp = query(sql);

		return vtemp;

	}
	public static Vector<Vector<String>> getAccount() {
		String sql = "select name,pwd from account";
		Vector<Vector<String>> vtemp = query(sql);
		return vtemp;
	}

	public static int getInsertId(String num, String cid) {
		int id = 0;
		String sql = "select Max(  cstunum  ) from course where cstunum = '"
				+ num + "'";

		SQLiteDatabase sld = createOrOpenDatabase();

		try {
			Cursor cur = sld.rawQuery(sql, new String[] {});

			// 查看结果集
			if (cur.moveToNext()) {
				id = cur.getInt(0);
			}
			// 关闭结果集,语句及连接
			cur.close();
			sld.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		id++;
		return id;
	}
}
