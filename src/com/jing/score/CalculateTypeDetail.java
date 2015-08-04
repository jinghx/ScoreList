package com.jing.score;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import com.example.scorelist.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.TextView;

public class CalculateTypeDetail extends Activity {
	
	TextView tv;
	ProgressDialog pd ;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			tv.setText((String)msg.obj);
			pd.cancel();
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.grade_calcul_info);
		
		tv = (TextView) findViewById(R.id.txt_cal_info);
		pd = new ProgressDialog(this);
		pd.setCancelable(true);
		new Thread(new methodRun()).start();
		pd.show();
	}

	class methodRun implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			InputStream is = getResources().openRawResource(R.raw.calculate);
			InputStreamReader inputStreamReader = null;
			try {
				inputStreamReader = new InputStreamReader(is, "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			BufferedReader reader = new BufferedReader(inputStreamReader);
			StringBuffer sb = new StringBuffer("");
			String line;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line);
					sb.append("\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			Message m = Message.obtain();
			m.obj = sb.toString();
			handler.sendMessage(m);
		}

	}
}
