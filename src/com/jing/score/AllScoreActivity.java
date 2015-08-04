package com.jing.score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.scorelist.R;
import com.jing.adapter.GradeAdapter;
import com.jing.adapter.GradeExpandAdapter;
import com.jing.bean.GradeInfo;
import com.jing.utils.GetNetData;
import com.umeng.analytics.MobclickAgent;

public class AllScoreActivity extends Activity implements OnChildClickListener,
		OnClickListener, OnItemClickListener {
	private List<ArrayList<GradeInfo>> gradeinfo = new ArrayList<ArrayList<GradeInfo>>();// 全部成绩信息
	private List<GradeInfo> searched = new ArrayList<GradeInfo>();// 全部成绩信息
	private String gdata = null; // 成绩源代码
	private ExpandableListView gradelist; // 成绩列表
	private MyApplication app;
	private int currentChild;
	private int currentGroup;
	private GradeExpandAdapter adapter;
	private String enrollyear;
	private String[] terms = new String[12]; // 学期分类时间
	private HashMap<String, ArrayList<GradeInfo>> map;

	private EditText searchContent;
	private Button btn_search;
	private ImageView iv_searchCancel;
	private TextView tv_searchCount;
	private GradeAdapter searchAdapter;
	private ListView lv_search;

	private View ll_search;
	private int currentSearchPosition = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_grade_list);
		app = (MyApplication) getApplication();
		enrollyear = app.getEnrollyear();
		initData();
		gradelist = (ExpandableListView) findViewById(R.id.all_grade_list);
		searchContent = (EditText) findViewById(R.id.et_grade_search);
		btn_search = (Button) findViewById(R.id.btn_grade_search);
		tv_searchCount = (TextView) findViewById(R.id.tv_search_count);
		iv_searchCancel = (ImageView) findViewById(R.id.iv_search_cancel);
		lv_search = (ListView) findViewById(R.id.lv_search_list);
		ll_search = findViewById(R.id.ll_grade_search);
		gradelist.setOnChildClickListener(this);
		btn_search.setOnClickListener(this);
		iv_searchCancel.setOnClickListener(this);
		lv_search.setOnItemClickListener(this);
		initContent();
	}

	public void initData() {
		gdata = GetNetData.getGdata();
		currentChild = -1;
		currentGroup = -1;
		createTermString();
	}

	public void initContent() {
		if (gdata != null) {
			if (gradeinfo.size() < 1) {
				filterGrade(gdata);
				if (gradeinfo.size() > 1) {
					adapter = new GradeExpandAdapter(this, gradeinfo);
					gradelist.setAdapter(adapter);
					// gradelist.expandGroup(0);
				}
			}
		}
	}

	// 时间分类
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
		map = new HashMap<String, ArrayList<GradeInfo>>();
		for (int i = 1; i < terms.length; i++) {
			ArrayList<GradeInfo> temp = new ArrayList<GradeInfo>();
			map.put(terms[i], temp);
		}

	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		// TODO Auto-generated method stub
		if (currentChild == childPosition && currentGroup == groupPosition) {
			adapter.setPosition(-1, -1);
			this.currentChild = -1;
			this.currentGroup = -1;
		} else {
			adapter.setPosition(groupPosition, childPosition);
			this.currentChild = childPosition;
			this.currentGroup = groupPosition;
		}
		adapter.notifyDataSetChanged();
		return true;
	}

	/**
	 * 分析成绩源代码获得所要的成绩数据
	 * 
	 * @param source
	 */
	public void filterGrade(String source) {
		if (null == source) {
			return;
		}
		String html = source;
		Document doc = Jsoup.parse(html); // 把HTML代码加载到doc中
		Elements table = doc.select("table[bgcolor=#F2EDF8]");
		String type[] = { "必修", "限选", "通选" };
		for (int i = 0; i < table.size(); i++) {// 三个table
			ArrayList<GradeInfo> arraytemp = new ArrayList<GradeInfo>();

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
				ginfo.setCreditname("学分");
				ginfo.setScorename("成绩");
				// gradeinfo.add(ginfo);
				arraytemp.add(ginfo);
				ArrayList<GradeInfo> maptemp = map.get(tds.get(4).text()
						.substring(0, 6));
				if (maptemp != null) {
					maptemp.add(ginfo);
				}
			}
			gradeinfo.add(arraytemp);
		}

		for (int i = 1; i < terms.length; i++) {
			gradeinfo.add(map.get(terms[i]));
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_grade_search:
			onClickSearch();
			break;
		case R.id.iv_search_cancel:
			onClickCancel();
			break;
		}
	}

	public void onClickSearch() {
		ll_search.setVisibility(View.VISIBLE);
		gradelist.setVisibility(View.GONE);
		searched.clear();
		String str = searchContent.getText().toString();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < gradeinfo.get(i).size(); j++) {
				if (gradeinfo.get(i).get(j).getName().contains(str)) {
					searched.add(gradeinfo.get(i).get(j));
				}
			}
		}

		if (searched.size() > 0) {

			if (searchAdapter == null) {
				searchAdapter = new GradeAdapter(this, searched);
				lv_search.setAdapter(searchAdapter);
			} else {
				searchAdapter.notifyDataSetChanged();
			}
			tv_searchCount.setText("共搜索到 " + searched.size() + " 门课");

		} else {
			tv_searchCount.setText("未搜到相关课程");
		}
	}

	public void onClickCancel() {
		ll_search.setVisibility(View.GONE);
		gradelist.setVisibility(View.VISIBLE);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		if (position == currentSearchPosition) {
			searchAdapter.setCurrentPosition(-1);
			currentSearchPosition = -1;
		} else {
			searchAdapter.setCurrentPosition(position);
			currentSearchPosition = position;
		}
		searchAdapter.notifyDataSetChanged();
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
