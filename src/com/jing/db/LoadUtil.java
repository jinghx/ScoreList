package com.jing.db;

import java.util.Vector;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class LoadUtil {

	@SuppressLint("SdCardPath")
	public static SQLiteDatabase createOrOpenDatabase()// �������ݿ�
	{
		SQLiteDatabase sld = null;
		try {
			sld = SQLiteDatabase.openDatabase// ���Ӳ��������ݿ⣬����������򴴽�
					("/data/data/com.example.scorelist/mydb", null,
							SQLiteDatabase.OPEN_READWRITE
									| SQLiteDatabase.CREATE_IF_NECESSARY);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sld;// ���ظ�����
	}

	public static void createTable(String sql) {// ������
		SQLiteDatabase sld = createOrOpenDatabase();// �������ݿ�
		try {
			sld.execSQL(sql);// ִ��SQL���
			sld.close();// �ر�����
		} catch (Exception e) {

		}
	}

	public static boolean insert(String sql)// ��������
	{
		SQLiteDatabase sld = createOrOpenDatabase();// �������ݿ�
		try {
			sld.execSQL(sql);
			sld.close();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public static boolean delete(String sql)// ��������
	{
		SQLiteDatabase sld = createOrOpenDatabase();// �������ݿ�
		try {
			sld.execSQL(sql);
			sld.close();
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public static Vector<Vector<String>> query(String sql)// ��ѯ
	{
		Vector<Vector<String>> vector = new Vector<Vector<String>>();// �½���Ų�ѯ���������
		SQLiteDatabase sld = createOrOpenDatabase();// �õ��������ݿ������

		try {
			Cursor cur = sld.rawQuery(sql, new String[] {});// �õ������

			while (cur.moveToNext())// ���������һ��
			{
				Vector<String> v = new Vector<String>();
				int col = cur.getColumnCount(); // �����������
				for (int i = 0; i < col; i++) {
					v.add(cur.getString(i));
				}
				vector.add(v);
			}
			cur.close();// �رս����
			sld.close();// �ر�����
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

			// �鿴�����
			if (cur.moveToNext()) {
				id = cur.getInt(0);
			}
			// �رս����,��估����
			cur.close();
			sld.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		id++;
		return id;
	}
}
