package com.jing.score;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.example.scorelist.R;
import com.jing.utils.GetNetData;

public class PersonalActivity extends Activity {

	MyApplication app;
	TextView tv_info;
	String infodata = "";
	String temp = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		app = (MyApplication) getApplication();
		app.addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.personalinfo);

		tv_info = (TextView) findViewById(R.id.txt_stu_info);

		infodata = GetNetData.getIdata();

		if (app.isLogin()) {
			filterInfo(infodata);
			tv_info.setText(temp);
		} else {
			tv_info.setText("δ��¼!!");
		}
	}

	public void filterInfo(String data) {
		if (data == null) {
			return;
		}
		Document doc = Jsoup.parse(data);

		Elements table = doc.select("table[bgcolor=#F2EDF8]");

		Elements trs = table.select("tr");

		Elements td1 = trs.get(0).select("td");
		String stunum = td1.get(2).text(); // ѧ��
		String name = td1.get(4).text(); // ����

		String sex = trs.get(1).select("td").get(3).text(); // �Ա�

		Elements td2 = trs.get(5).select("td");
		String institude = td2.get(1).text();
		String classStr = td2.get(3).text();
		String classnum = classStr.substring(classStr.length() - 1);

		String major = trs.get(6).select("td").get(3).text();

		temp += "ѧ�� : " + stunum + "\n";
		temp += "���� : " + name + "\n";
		temp += "�Ա� : " + sex + "\n";
		temp += "ѧԺ : " + institude + "\n";
		temp += "רҵ : " + major + "\n";
		try {
			Integer.parseInt(classnum);
			temp += "�༶ : " + classnum + "\n";
		} catch (NumberFormatException e) {
			// temp += "�༶ : 1\n";
		}

	}

}
