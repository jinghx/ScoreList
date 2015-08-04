package com.jing.score;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.scorelist.R;
import com.jing.adapter.GradeAdapter;
import com.jing.bean.GradeInfo;
import com.jing.utils.GetNetData;
import com.umeng.analytics.MobclickAgent;

public class CurrentScoreActivity extends Activity implements
		OnItemClickListener {
	private List<GradeInfo> currentgrade = new ArrayList<GradeInfo>();// ��ǰ��ʾ�ĳɼ�
	private String cgdata = "";// ��ǰ��ʾ�ɼ�Դ����
	private ListView gradelist; // ƽ��ѧ�ּ����б�
	private TextView tvAvgScore;
	private GradeAdapter adapter;
	private int currentPosition;
	private String uripm = "http://202.194.40.15:7778/pls/wwwbks/bkscjcx.cursco";
	private String pmResult = "";
	private ProgressDialog progressDialog;
	private final Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.current_grade_list);
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(true);
		initData();
		gradelist = (ListView) findViewById(R.id.current_grade_list);
		tvAvgScore = (TextView) findViewById(R.id.tv_current_avg_score);
		gradelist.setOnItemClickListener(this);
		initContent();
	}

	public void initData() {
		cgdata = GetNetData.getCgdata();
		currentPosition = -1;
	}

	public void initContent() {

		if (cgdata != "") {
			if (currentgrade.size() < 1) {
				filterCurrGrade(cgdata);
				adapter = new GradeAdapter(this, currentgrade);
				gradelist.setAdapter(adapter);
			}
		}
		if (currentgrade.size() > 0) {
			tvAvgScore.setText("ƽ���ɼ�:"
					+ new DecimalFormat("0.000000")
							.format(calculateCreditScore()));
		} else {
			tvAvgScore.setText("�������޳ɼ���ʾ");
		}

	}

	public void filterCurrGrade(String source) {
		if (null == source) {
			return;
		}
		String toWrite = "";
		String html = source;
		Document doc = Jsoup.parse(html); // ��HTML������ص�doc��
		// Elements trs = doc.select("table").select("tr");
		Elements table = doc.select("table[bgcolor=#F2EDF8]");
		Elements trs = table.select("tr");
		for (int j = 1; j < trs.size(); j++) {
			GradeInfo ginfo = new GradeInfo();
			Elements tds = trs.get(j).select("td");
			if (tds.select("input") != null) {
				ginfo.setPm(tds.select("input").attr("value"));
			}
			ginfo.setCoursenumber(tds.get(1).text());
			ginfo.setName(tds.get(2).text());
			ginfo.setOrdernumber(tds.get(3).text());
			ginfo.setCredit(tds.get(4).text().trim());
			ginfo.setTesttime(tds.get(5).text());
			ginfo.setExpscore(tds.get(6).text());
			ginfo.setComscore(tds.get(7).text());
			ginfo.setTestscore(tds.get(8).text());
			ginfo.setFinalscore(tds.get(9).text().trim());
			ginfo.setCourseprop(tds.get(10).text().trim());
			ginfo.setTesttype(tds.get(12).text());
			ginfo.setCreditname("ѧ��");
			ginfo.setScorename("�ɼ�");

			if (!"".equals(ginfo.getFinalscore())
					&& !"0".equals(ginfo.getFinalscore())) {
				toWrite += ginfo.getName() + "," + ginfo.getFinalscore() + ";";
			}

			currentgrade.add(ginfo);
		}
		// �����ѳ��ɼ��Ŀγ�
		SharedPreferences sp = getSharedPreferences("publish",
				Context.MODE_APPEND);
		String flag = sp.getString("flag", "");
		Editor edit = sp.edit();
		edit.putInt("count", currentgrade.size());
		if (!flag.equals("ok")) {
			edit.putString("flag", "ok");
			edit.putString("grade", toWrite);
			Log.i("publish", "grade:" + toWrite);
		}
		edit.commit();
	}

	public void filterPm(String source) {
		if (null == source) {
			return;
		}
		String html = source;
		Document doc = Jsoup.parse(html); // ��HTML������ص�doc��
		Elements table = doc.select("table[bgcolor=#F2EDF8]");
		Elements trs = table.select("tr");
		GradeInfo info = currentgrade.get(currentPosition);
		if (trs != null && trs.size() > 0) {
			Elements tds = trs.get(1).select("td");
			info.setChoosedNum(tds.get(3).text().trim());
			info.setMax(tds.get(4).text().trim());
			info.setMin(tds.get(5).text().trim());
			info.setRank(tds.get(6).text().trim());
		}

	}

	/**
	 * ����ѧ�ּ���
	 * 
	 * @return
	 */
	public double calculateCreditScore() {

		double ctemp = 0.0;
		double stemp = 0.0;
		double total_credit = 0.0;
		double total_score = 0.0;
		String credit = "";
		String score = "";

		for (int i = 0; i < currentgrade.size(); i++) {
			GradeInfo info = currentgrade.get(i);
			if (info.getCourseprop().trim().equals("����")
					|| info.getCourseprop().trim().equals("��ѡ")) {
				if (isNumeric(info.getFinalscore())) {
					credit = info.getCredit().trim();
					score = info.getFinalscore().trim();

					ctemp = Double.parseDouble(credit);
					stemp = Double.parseDouble(score);
					if (stemp != 0) {
						if (stemp < 60)
							stemp = 0;
						total_credit += ctemp;
						total_score += ctemp * stemp;
					}
				}
			}
		}
		if (total_credit > 0.0) {
			return total_score / total_credit;
		}
		return 0;
	}

	/**
	 * �ж�str�ǲ�������
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*.[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == currentPosition) {
			adapter.setCurrentPosition(-1);
			currentPosition = -1;
			adapter.notifyDataSetChanged();// �ֿ�ˢ��
		} else {
			adapter.setCurrentPosition(position);
			currentPosition = position;
			getPmInfo(currentPosition);
		}
	}

	public void getPmInfo(int position) {
		if (!currentgrade.get(position).getPm().equals("")) {
			if (currentgrade.get(position).getRank().equals("")) {
				progressDialog = ProgressDialog.show(CurrentScoreActivity.this,
						"", "������...");
				new Thread(new pmThread()).start();
			} else {
				adapter.notifyDataSetChanged();// �ѻ����Ϣ��ֱ��ˢ��
			}
		} else {
			adapter.notifyDataSetChanged();// û�������ִ���ֱ��ˢ��
		}
	}

	// ��¼ʧ��
	final Runnable pmSuccess = new Runnable() {
		public void run() {
			progressDialog.dismiss();
			adapter.notifyDataSetChanged(); // ��������Ϣ�Ļ�ú���ˢ��
		}
	};

	private class pmThread implements Runnable {

		@Override
		public void run() {
			HttpPost httpPM = new HttpPost(uripm);
			List<NameValuePair> param = new ArrayList<NameValuePair>();

			param.add(new BasicNameValuePair("p_pm", currentgrade.get(
					currentPosition).getPm()));
			try {
				// ����HTTP request
				httpPM.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));

				// ��֮ǰ��cookie�ŵ��˴�get����Ҫ��ͷ��Ϣ��
				httpPM.setHeader("Cookie", GetNetData.getCookies().get(0)
						.getName()
						+ "=" + GetNetData.getCookies().get(0).getValue());
				// ����HTTP request /
				HttpResponse httpp = GetNetData.getClient().execute(httpPM);
				// ��״̬��Ϊ200 ok /
				if (httpp.getStatusLine().getStatusCode() == 200) {
					// �������Ĵ�����Ϊ�˰Ѵ���ҳ��ȡ�������ݶ�����
					StringBuffer sb = new StringBuffer();
					HttpEntity entity = httpp.getEntity();
					InputStream is = entity.getContent();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(is, "GB2312"));
					String data = "";
					while ((data = br.readLine()) != null) {
						sb.append(data);
					}
					pmResult = sb.toString();
					filterPm(pmResult);
					handler.post(pmSuccess);
				} else {
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
