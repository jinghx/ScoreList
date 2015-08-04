package com.jing.score;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.scorelist.R;

public class CalculatedCourseDetail extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grade_avg_info);
		TextView tv = (TextView) findViewById(R.id.txt_avg_info);
		Intent i = getIntent();
		String info = i.getStringExtra("info");
		tv.setText(info);
	}

}
