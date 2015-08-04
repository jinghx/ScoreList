package com.jing.test;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.scorelist.R;
import com.jing.broadcast.StartBroadcast;
import com.jing.service.StartListenService;
import com.jing.utils.ServiceUtils;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		Button btn = (Button)findViewById(R.id.btn_broadcast);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TestActivity.this,StartBroadcast.class);
				sendBroadcast(intent);
			}
		});
		Button start_service = (Button)findViewById(R.id.btn_start_service);
		start_service.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 服务没有运行 则启动
				if (!ServiceUtils.isModifyServiceRunning(TestActivity.this)) {
					Intent newIntent = new Intent(TestActivity.this, StartListenService.class);
					startService(newIntent);
				}
			}
		});
		Button stop_service = (Button)findViewById(R.id.btn_stop_service);
		stop_service.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(TestActivity.this,StartListenService.class);
				stopService(intent);
			}
		});
		
		Button clear = (Button)findViewById(R.id.clear);
		clear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences sp = getSharedPreferences("publish", Context.MODE_APPEND);
				Editor edit = sp.edit();
				edit.remove("flag");
				edit.remove("grade");
				edit.commit();
				Toast.makeText(TestActivity.this, "已清除", Toast.LENGTH_SHORT).show();
			}
		});
		
	}

}
