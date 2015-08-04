package com.jing.score;

import java.util.List;

import android.app.ActivityGroup;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.example.scorelist.R;
import com.jing.service.StartListenService;
import com.jing.test.TestActivity;
import com.jing.utils.ServiceUtils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;

public class MainActivity extends ActivityGroup implements OnClickListener {

	private TabHost tabHost;
	private TextView tv_calculate;
	private ImageView iv_person;
	private long exitTime = 0;
	private FeedbackAgent fb;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		fb = new FeedbackAgent(this);
		fb.sync();
		
		
		/**
		 * 测试代码
		 */
		TextView tv_test = (TextView) this.findViewById(R.id.tv_test);
		tv_test.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(MainActivity.this,TestActivity.class));
			}
		});
		
		tv_calculate = (TextView) this.findViewById(R.id.tv_calculate_score);
		tv_calculate.setOnClickListener(this);
		iv_person = (ImageView) this.findViewById(R.id.iv_person);
		iv_person.setOnClickListener(this);
		tabHost = (TabHost) this.findViewById(R.id.main_score_tabHost);
		tabHost.setup(this.getLocalActivityManager());

		TabSpec tab1 = tabHost.newTabSpec("all_score");
		tab1.setIndicator(createContent("全部", R.drawable.toplabelleft));
		tab1.setContent(new Intent(this, AllScoreActivity.class));
		tabHost.addTab(tab1);

		TabSpec tab2 = tabHost.newTabSpec("current_score");
		tab2.setIndicator(createContent("当前", R.drawable.toplabelright));
		tab2.setContent(new Intent(this, CurrentScoreActivity.class));
		tabHost.addTab(tab2);
		tabHost.setCurrentTab(0);

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {
				// TODO Auto-generated method stub
				tabChanged(tabId);
			}
		});
		
		//服务没有运行 则启动
		if(!ServiceUtils.isModifyServiceRunning(MainActivity.this)){
			Intent newIntent = new Intent(MainActivity.this, StartListenService.class);
			startService(newIntent);
		}
	}


	// 捕获tab变化事件
	private void tabChanged(String tabId) {
		// 当前选中项
		if (tabId.equals("all_score")) {
			tabHost.setCurrentTabByTag("全部");
		} else if (tabId.equals("current_score")) {
			tabHost.setCurrentTabByTag("当前");
		}
	}

	private View createContent(String text, int resid) {
		View view = LayoutInflater.from(this).inflate(R.layout.toptabwidget,
				null, false);
		TextView tv_name = (TextView) view.findViewById(R.id.toptabwidgettext);
		tv_name.setText(text);
		tv_name.setBackgroundResource(resid);
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_calculate_score:
			Intent calculate = new Intent(this, AverageActivity.class);
			startActivity(calculate);
			break;
		case R.id.iv_person:
			Intent person = new Intent(this, PersonalActivity.class);
			startActivity(person);
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次退出成绩单",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			finish();
			System.exit(0);
		}

	}

	public void goToAboutView() {
		Intent about = new Intent(this, AboutActivity.class);
		startActivity(about);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem item_feedback = menu
				.add(Menu.NONE, Menu.NONE, Menu.NONE, "反馈");
		MenuItem item_about = menu.add(Menu.NONE, Menu.NONE, Menu.NONE, "关于");
		MenuItem item_exit = menu.add(Menu.NONE, Menu.NONE, Menu.NONE, "退出");
		MenuItem item_test = menu.add(Menu.NONE, Menu.NONE, Menu.NONE, "服务");

		item_feedback
				.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						fb.startFeedbackActivity();
						return true;
					}
				});

		item_exit
				.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						MobclickAgent.onKillProcess(MainActivity.this);
						System.exit(0);
						return true;
					}
				});
		item_about
				.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item) {
						goToAboutView();
						return true;
					}
				});
		
		item_test
		.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			public boolean onMenuItemClick(MenuItem item) {
				startActivity(new Intent(MainActivity.this,TestActivity.class));
				return true;
			}
		});
		return true;
	}
}
