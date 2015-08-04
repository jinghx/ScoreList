package com.jing.score;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scorelist.R;
import com.jing.adapter.AvgAdapter;
import com.jing.bean.GradeInfo;
import com.jing.utils.GetNetData;
import com.umeng.analytics.MobclickAgent;

public class AverageActivity extends Activity implements OnItemClickListener,
		OnCheckedChangeListener {
	private final static int CAL_CREDIT = 0;
	private final static int CAL_GPA_STANDARD = 1;
	private final static int CAL_GPA_FOUR = 2;
	private List<GradeInfo> gradeinfo = new ArrayList<GradeInfo>();// ƽ��ѧ�ּ���ĳɼ���Ϣ
	private ListView avggradelist; // ƽ��ѧ�ּ����б�
	private AvgAdapter avgAdapter;

	private String[] terms = new String[12]; // ѧ�ڷ���ʱ��
	private String enrollyear = "";
	private MyApplication app;
	private String avgtemp = ""; // �������Ŀγ�
	private TextView scoretxt; // ��ʾѧ�ּ���
	double creditScore = 0.0; // ѧ�ּ���
	private String gdata = null; // �ɼ�Դ����

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.grade_avg);

		app = (MyApplication) getApplication();
		enrollyear = app.getEnrollyear();
		gdata = GetNetData.getGdata();
		createTermString();
		goToAverageGrade();
	}

	// ʱ�����
	public void createTermString() {

		int year = Integer.parseInt(enrollyear);
		terms[0] = "2";
		terms[1] = year + "12";
		terms[2] = (year + 1) + "06";
		terms[3] = (year + 1) + "07";
		terms[4] = (year + 1) + "12";
		terms[5] = (year + 2) + "06";
		terms[6] = (year + 2) + "07";
		terms[7] = (year + 2) + "12";
		terms[8] = (year + 3) + "06";
		terms[9] = (year + 3) + "07";
		terms[10] = (year + 3) + "12";
		terms[11] = (year + 4) + "06";
	}

	/**
	 * ����ѧ�ּ������
	 */
	public void goToAverageGrade() {
		scoretxt = (TextView) findViewById(R.id.txt_finalscore);
		avggradelist = (ListView) findViewById(R.id.grade_avg_list);
		Button btn_cal = (Button) findViewById(R.id.btn_calculate);
		final Button btn_gpa = (Button) findViewById(R.id.btn_gpa);
		ImageView image = (ImageView) findViewById(R.id.avg_imagev);
		// ����ѡ���checkbox
		CheckBox check_all = (CheckBox) findViewById(R.id.check_all);// ȫ��
		CheckBox check_must = (CheckBox) findViewById(R.id.check_must);// ����
		CheckBox check_limit = (CheckBox) findViewById(R.id.check_limit);// ��ѡ
		CheckBox check_option = (CheckBox) findViewById(R.id.check_option);// ͨѡ
		CheckBox check_1 = (CheckBox) findViewById(R.id.check_1);// ��һ
		CheckBox check_2 = (CheckBox) findViewById(R.id.check_2);// ���
		CheckBox check_3 = (CheckBox) findViewById(R.id.check_3);// ����
		CheckBox check_4 = (CheckBox) findViewById(R.id.check_4);// ����

		avggradelist.setOnItemClickListener(this);

		image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ImageView iv = (ImageView) v;
				LinearLayout ll = (LinearLayout) findViewById(R.id.check_linear);
				if (ll.getVisibility() == View.VISIBLE) {
					iv.setBackgroundResource(R.drawable.group_arrow_right);
					ll.setVisibility(View.GONE);
				} else {
					iv.setBackgroundResource(R.drawable.group_arrow_down);
					ll.setVisibility(View.VISIBLE);
				}
			}
		});

		btn_cal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				double temp = calculateCreditScore(CAL_CREDIT);
				toSetScoreText(temp, "ƽ��ѧ�ּ���");
			}
		});

		btn_gpa.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String[] sts = new String[] { "��׼���㷨", "��׼�ķ���", "�Ľ�4����(1)",
						"�Ľ�4����(2)", "����4.0", "���ô�4.3", "�пƴ�4.3", "�Ϻ�����4.3",
						"�㷨����" };
				PopupMenu pm = createPopMenu(v, sts);
				pm.setOnMenuItemClickListener(new OnMenuItemClickListener() {

					@Override
					public boolean onMenuItemClick(MenuItem item) {
						// TODO Auto-generated method stub
						switch (item.getTitle().toString()) {
						case "��׼���㷨":
							toSetScoreText(calculateStandardGPA(), "��׼GPA");
							break;
						case "��׼�ķ���":
							toSetScoreText(calculateCreditScore(CAL_GPA_FOUR), "��׼4����");
							break;
						case "�㷨����":
							toShowCalDetail();
							break;
						}
						return false;
					}

					
				});
				pm.show();

			}
		});

		scoretxt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(AverageActivity.this,CalculatedCourseDetail.class);
				i.putExtra("info", avgtemp);
				startActivity(i);
			}
		});

		check_all.setOnCheckedChangeListener(this);

		check_must.setOnCheckedChangeListener(this);

		check_limit.setOnCheckedChangeListener(this);

		check_option.setOnCheckedChangeListener(this);

		check_1.setOnCheckedChangeListener(this);

		check_2.setOnCheckedChangeListener(this);

		check_3.setOnCheckedChangeListener(this);

		check_4.setOnCheckedChangeListener(this);

		if (gdata != null) {
			if (gradeinfo.size() < 1) {
				filterGrade(gdata);
				avgAdapter = new AvgAdapter(this, gradeinfo);
				avggradelist.setAdapter(avgAdapter);
			}
		}
		scoretxt.append("���� : " + creditScore);
	}
	private void toShowCalDetail() {
		Intent i = new Intent(AverageActivity.this,CalculateTypeDetail.class);
		startActivity(i);
	}

	/**
	 * 
	 * @param achor
	 * @param items
	 * @return
	 */
	public PopupMenu createPopMenu(View achor, String[] items) {
		PopupMenu pmenu = new PopupMenu(AverageActivity.this, achor);
		int i = 0;
		for (String item : items) {
			i++;
			pmenu.getMenu().add(0, i, i, item);

		}
		return pmenu;
	}

	/**
	 * 
	 * @param score
	 */
	public void toSetScoreText(double score, String title) {
		DecimalFormat dcmFmt = new DecimalFormat("0.000000");
		scoretxt.setText(title + " : " + dcmFmt.format(score));
	}

	/**
	 * ����GPA ��׼
	 * 
	 * @return
	 */

	public double calculateStandardGPA() {
		return calculateCreditScore(CAL_GPA_STANDARD) * 0.04;
	}

	/**
	 * �ķ���
	 * 
	 * @param score
	 * @return
	 */
	public double getFourGpaPoint(double score) {
		if (score >= 90 && score <= 100) {
			return 4;
		}
		if (score >= 80 && score < 90) {
			return 3;
		}
		if (score >= 70 && score < 80) {
			return 2;
		}
		if (score >= 60 && score < 70) {
			return 1;
		}

		return 0;
	}

	/**
	 * ƽ��ѧ�ּ���
	 * 
	 * @param score
	 * @return
	 */

	public double getCreditPoint(double score) {
		if (score < 60)
			return 0;
		return score;
	}

	/**
	 * ���㼨��
	 * 
	 * @return
	 */
	public double calculateCreditScore(int type) {

		
		if (!app.isLogin()) {
			Toast.makeText(AverageActivity.this, "δ��¼",
					Toast.LENGTH_SHORT).show();
			return 0;
		}
		if (gradeinfo.size() < 1) {
			Toast.makeText(AverageActivity.this, "δ�������",
					Toast.LENGTH_SHORT).show();
			return 0;
		}
		
		
		double ctemp = 0.0;
		double stemp = 0.0;
		double total_credit = 0.0;
		double total_score = 0.0;
		String credit = "";
		String score = "";
		avgtemp = "";
		HashMap<Integer, Boolean> map = AvgAdapter.getIsSelected();
		Iterator iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			// Object key = entry.getKey();
			// Object val = entry.getValue();
			if ((Boolean) entry.getValue()) {

				GradeInfo info = gradeinfo.get((Integer) entry.getKey());

				if (isNumeric(info.getFinalscore())) {
					credit = info.getCredit().trim();
					score = info.getFinalscore().trim();

					ctemp = Double.parseDouble(credit);

					switch (type) {
					case CAL_CREDIT:// ƽ��ѧ�ּ���
					case CAL_GPA_STANDARD:// ��׼GPA
						stemp = getCreditPoint(Double.parseDouble(score));
						break;
					case CAL_GPA_FOUR:// ��׼4��
						stemp = getFourGpaPoint(Double.parseDouble(score));
						break;
					}

					avgtemp += info.getName() + "\n";

					total_credit += ctemp;

					total_score += ctemp * stemp;
				}
			}

		}

		if (total_credit > 0.0) {
			creditScore = total_score / total_credit;
			return creditScore;
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
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.check_must:
			if (isChecked) {
				for (int i = 0; i < gradeinfo.size(); i++) {
					if (gradeinfo.get(i).getCourseprop().equals("����"))
						AvgAdapter.getIsSelected().put(i, true);
				}
				// ˢ��listview��TextView����ʾ
				updateAvgList();
			} else {
				for (int i = 0; i < gradeinfo.size(); i++) {
					if (gradeinfo.get(i).getCourseprop().equals("����"))
						AvgAdapter.getIsSelected().put(i, false);
				}
				// ˢ��listview��TextView����ʾ
				updateAvgList();
			}
			break;
		case R.id.check_limit:
			if (isChecked) {
				for (int i = 0; i < gradeinfo.size(); i++) {
					if (gradeinfo.get(i).getCourseprop().equals("��ѡ"))
						AvgAdapter.getIsSelected().put(i, true);
				}
				// ˢ��listview��TextView����ʾ
				updateAvgList();
			} else {
				for (int i = 0; i < gradeinfo.size(); i++) {
					if (gradeinfo.get(i).getCourseprop().equals("��ѡ"))
						AvgAdapter.getIsSelected().put(i, false);
				}
				// ˢ��listview��TextView����ʾ
				updateAvgList();
			}
			break;
		case R.id.check_option:
			if (isChecked) {
				for (int i = 0; i < gradeinfo.size(); i++) {
					if (gradeinfo.get(i).getCourseprop().equals("ͨѡ"))
						AvgAdapter.getIsSelected().put(i, true);
				}
				// ˢ��listview��TextView����ʾ
				updateAvgList();
			} else {
				for (int i = 0; i < gradeinfo.size(); i++) {
					if (gradeinfo.get(i).getCourseprop().equals("ͨѡ"))
						AvgAdapter.getIsSelected().put(i, false);
				}
				// ˢ��listview��TextView����ʾ
				updateAvgList();
			}
			break;
		case R.id.check_all:
			if (isChecked) {
				for (int i = 0; i < gradeinfo.size(); i++) {
					AvgAdapter.getIsSelected().put(i, true);
				}
				// ˢ��listview��TextView����ʾ
				updateAvgList();
			} else {
				for (int i = 0; i < gradeinfo.size(); i++) {
					AvgAdapter.getIsSelected().put(i, false);
				}
				// ˢ��listview��TextView����ʾ
				updateAvgList();
			}
			break;
		case R.id.check_1:
			if (isChecked) {
				for (int i = 0; i < gradeinfo.size(); i++) {
					if (gradeinfo.get(i).getTesttime().contains(terms[1])
							|| gradeinfo.get(i).getTesttime()
									.contains(terms[2])
							|| gradeinfo.get(i).getTesttime()
									.contains(terms[3]))
						AvgAdapter.getIsSelected().put(i, true);
				}
				// ˢ��listview��TextView����ʾ
				updateAvgList();
			} else {
				for (int i = 0; i < gradeinfo.size(); i++) {
					if (gradeinfo.get(i).getTesttime().contains(terms[1])
							|| gradeinfo.get(i).getTesttime()
									.contains(terms[2])
							|| gradeinfo.get(i).getTesttime()
									.contains(terms[3]))
						AvgAdapter.getIsSelected().put(i, false);
				}
				// ˢ��listview��TextView����ʾ
				updateAvgList();
			}
			break;
		case R.id.check_2:
			if (isChecked) {
				for (int i = 0; i < gradeinfo.size(); i++) {
					if (gradeinfo.get(i).getTesttime().contains(terms[4])
							|| gradeinfo.get(i).getTesttime()
									.contains(terms[5])
							|| gradeinfo.get(i).getTesttime()
									.contains(terms[6]))
						AvgAdapter.getIsSelected().put(i, true);
				}
				// ˢ��listview��TextView����ʾ
				updateAvgList();
			} else {
				for (int i = 0; i < gradeinfo.size(); i++) {
					if (gradeinfo.get(i).getTesttime().contains(terms[4])
							|| gradeinfo.get(i).getTesttime()
									.contains(terms[5])
							|| gradeinfo.get(i).getTesttime()
									.contains(terms[6]))
						AvgAdapter.getIsSelected().put(i, false);
				}
				// ˢ��listview��TextView����ʾ
				updateAvgList();
			}
			break;
		case R.id.check_3:
			if (isChecked) {
				for (int i = 0; i < gradeinfo.size(); i++) {
					if (gradeinfo.get(i).getTesttime().contains(terms[7])
							|| gradeinfo.get(i).getTesttime()
									.contains(terms[8])
							|| gradeinfo.get(i).getTesttime()
									.contains(terms[9]))
						AvgAdapter.getIsSelected().put(i, true);
				}
				// ˢ��listview��TextView����ʾ
				updateAvgList();
			} else {
				for (int i = 0; i < gradeinfo.size(); i++) {
					if (gradeinfo.get(i).getTesttime().contains(terms[7])
							|| gradeinfo.get(i).getTesttime()
									.contains(terms[8])
							|| gradeinfo.get(i).getTesttime()
									.contains(terms[9]))
						AvgAdapter.getIsSelected().put(i, true);
				}
				// ˢ��listview��TextView����ʾ
				updateAvgList();
			}
			break;
		case R.id.check_4:
			if (isChecked) {
				for (int i = 0; i < gradeinfo.size(); i++) {
					if (gradeinfo.get(i).getTesttime().contains(terms[10])
							|| gradeinfo.get(i).getTesttime()
									.contains(terms[11]))
						AvgAdapter.getIsSelected().put(i, true);
				}
				// ˢ��listview��TextView����ʾ
				updateAvgList();
			} else {
				for (int i = 0; i < gradeinfo.size(); i++) {
					if (gradeinfo.get(i).getTesttime().contains(terms[10])
							|| gradeinfo.get(i).getTesttime()
									.contains(terms[11]))
						AvgAdapter.getIsSelected().put(i, false);
				}
				// ˢ��listview��TextView����ʾ
				updateAvgList();
			}
			break;
		}

	}

	/**
	 * �����ɼ�Դ��������Ҫ�ĳɼ�����
	 * 
	 * @param source
	 */
	public void filterGrade(String source) {
		if (null == source) {
			return;
		}
		String html = source;
		Document doc = Jsoup.parse(html); // ��HTML������ص�doc��
		Elements table = doc.select("table[bgcolor=#F2EDF8]");
		String type[] = { "����", "��ѡ", "ͨѡ" };
		for (int i = 0; i < table.size(); i++) {// ����table
			Elements trs = table.get(i).select("tr");
			for (int j = 1; j < trs.size(); j++) {
				GradeInfo ginfo = new GradeInfo();
				Elements tds = trs.get(j).select("td");

				ginfo.setCoursenumber(tds.get(0).text());
				ginfo.setName(tds.get(1).text());
				ginfo.setOrdernumber(tds.get(2).text());
				ginfo.setCredit(tds.get(3).text().trim());
				ginfo.setTesttime(tds.get(4).text());
				ginfo.setExpscore(tds.get(5).text());
				ginfo.setComscore(tds.get(6).text());
				ginfo.setTestscore(tds.get(7).text());
				ginfo.setFinalscore(tds.get(8).text().trim());
				ginfo.setCourseprop(type[i]);
				ginfo.setTesttype(tds.get(11).text());
				ginfo.setCreditname("ѧ��");
				ginfo.setScorename("�ɼ�");
				// gradeinfo.add(ginfo);
				gradeinfo.add(ginfo);
			}

		}
	}

	@Override
	public void onItemClick(AdapterView<?> listview, View arg1, int position,
			long arg3) {
		AvgAdapter.ViewHolder holder = (AvgAdapter.ViewHolder) arg1.getTag();
		// �ı�CheckBox��״̬
		holder.fileNameText.toggle();
		// ��CheckBox��ѡ��״����¼����
		AvgAdapter.getIsSelected().put(position,
				holder.fileNameText.isChecked());

	}

	private void updateAvgList() {
		// ֪ͨlistViewˢ��
		if (avgAdapter != null) {
			avgAdapter.notifyDataSetChanged();
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
