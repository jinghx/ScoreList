package com.jing.score;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.scorelist.R;
import com.jing.bean.People;
import com.jing.db.CreateTable;
import com.jing.db.LoadUtil;
import com.jing.utils.GetNetData;
import com.umeng.analytics.MobclickAgent;

public class LoginActivity extends Activity {

	// 超时时间
	private static final int TIME_LIMIT = 20000;
	Timer timer = null;
	Thread thread;
	List<Cookie> cookies; // 保存获取的cookie

	String result = ""; // 登录成功信息
	String username = "";
	String userpass = "";

	private EditText txtUserName;
	private EditText txtPassword;
	private CheckBox cb;

	private HttpClient client;
	private String uriLogin = "http://202.194.40.15:8080/xsxt/xsxt.jsp";
	private String uriresult = "http://202.194.40.15:8080/xsxt/right.jsp";
	private final Handler handler = new Handler();
	private String enrollyear = "";
	private People people;
	private ProgressDialog progressDialog;// 不能在这初始化
	private MyApplication app;
	private boolean chatState;
	Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		app = (MyApplication) this.getApplication();
		context = getApplicationContext();
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(true);
		
		initViews();
	}
	
	public void initViews() {
		CreateTable.creattable();
		txtUserName = (EditText) findViewById(R.id.user_id);
		txtPassword = (EditText) findViewById(R.id.user_pwd);

		Vector<Vector<String>> vtemp;
		vtemp = LoadUtil.getAccount();
		if (vtemp.size() > 0) {
			txtUserName.setText(vtemp.get(0).get(0).trim());
			txtPassword.setText(vtemp.get(0).get(1).trim());
		}

		Button btn_land = (Button) findViewById(R.id.user_land);
		cb = (CheckBox) findViewById(R.id.user_check);
		cb.setChecked(true);

		btn_land.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isNetworkConnected(context)) {
					progressDialog = ProgressDialog.show(LoginActivity.this,
							"正在登录", "请稍后...", true, false);

					threadLogin();

					// 设定定时器
					timer = new Timer();
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							handler.post(loginTimeOut);
						}
					}, TIME_LIMIT);
				} else {
					handler.post(loginFailed);
					return;
				}
			}
		});
	}
	
	
	/**
	 * 登录线程
	 * 根据服务器返回的数据判断时候登录成功
	 */
	public void threadLogin() {
		thread = new Thread() {
			public void run() {

				username = txtUserName.getText().toString().trim();
				if (username.length() > 4)
					enrollyear = username.substring(0, 4);
				userpass = txtPassword.getText().toString().trim();

				LoadUtil.delete("delete from account");
				String temp = "";							
				if(cb.isChecked()){
					temp = txtPassword.getText().toString().trim();
				}
				LoadUtil.insert("insert into account values ('"+txtUserName.getText().toString()+"','"+temp+"')");
				
				client = new DefaultHttpClient();
				HttpResponse httpResponse;

				// 登录
				// 建立HTTP Post连线
				HttpPost httpRequest = new HttpPost(uriLogin);
				httpRequest.getParams().setParameter(
						ClientPNames.HANDLE_REDIRECTS, false);
				List<NameValuePair> params = new ArrayList<NameValuePair>();

				params.add(new BasicNameValuePair("userPass", userpass)); // 密码
				params.add(new BasicNameValuePair("userId", username)); // 这是学号

				try {
					// 发出HTTP request
					httpRequest.setEntity(new UrlEncodedFormEntity(params,
							HTTP.UTF_8));
					// 取得HTTP response
					httpResponse = client.execute(httpRequest); // 执行
					// 若状态码为200 ok
					if (httpResponse.getStatusLine().getStatusCode() == 200) { // 返回值正常
						// 获取返回的cookie
						cookies = ((AbstractHttpClient) client)
								.getCookieStore().getCookies();
					} else {
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				// 获取登录成功信息
				HttpGet httpLoginResult = new HttpGet(uriresult);
				try {

					// 把之前的cookie放到此次get所需要的头信息中
					httpLoginResult.setHeader("Cookie", cookies.get(0)
							.getName() + "=" + cookies.get(0).getValue());
					// 发出HTTP request /
					HttpResponse httpRdata = client.execute(httpLoginResult);
					// 若状态码为200 ok /
					if (httpRdata.getStatusLine().getStatusCode() == 200) {
						cookies = ((AbstractHttpClient) client)
								.getCookieStore().getCookies();
						// 接下来的代码是为了把从网页获取到的内容读出来
						StringBuffer sb = new StringBuffer();
						HttpEntity entity = httpRdata.getEntity();
						InputStream is = entity.getContent();
						BufferedReader br = new BufferedReader(
								new InputStreamReader(is, "GB2312"));
						String data = "";
						while ((data = br.readLine()) != null) {
							sb.append(data);
						}
						result = sb.toString();// 登录成功信息
						if (result.length() > 200) {
							GetNetData.setCookies(cookies);
							GetNetData.setClient(client);
							GetNetData.getCourseData();
							GetNetData.getCurrentGradeData();
							GetNetData.getGradeData();
							GetNetData.getInfoData();
							handler.post(loginSuccess);
						} else {
							handler.post(loginFailed);
						}
					} else {
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		thread.start();

	}
	
	
	// 登录成功
	final Runnable loginSuccess = new Runnable() {
		public void run() {
			timer.cancel();
			progressDialog.dismiss();
			Toast.makeText(LoginActivity.this, "登录成功...", Toast.LENGTH_SHORT)
					.show();
			MyApplication app = (MyApplication) getApplication();
			app.setCookies(cookies);
			app.setEnrollyear(enrollyear);
			app.setLoginstate(true);
			app.setUsername(username);

			String data = GetNetData.getIdata();
			filterRealName(data);
			app.setPeople(people);
			app.setChatstate(chatState);

			Intent score = new Intent(LoginActivity.this,MainActivity.class);
			startActivity(score);
			LoginActivity.this.finish();
		}
	};

	// 登录失败
	final Runnable loginFailed = new Runnable() {
		public void run() {
			if (timer != null)
				timer.cancel();
			progressDialog.dismiss();
			Toast.makeText(LoginActivity.this, "登录失败...可能是账号密码错误或网络原因",
					Toast.LENGTH_SHORT).show();
		}
	};
	// 登录超时
	final Runnable loginTimeOut = new Runnable() {
		public void run() {
			if (timer != null)
				timer.cancel();
			thread.interrupt();
			progressDialog.dismiss();
			Toast.makeText(LoginActivity.this, "网络超时...", Toast.LENGTH_SHORT)
					.show();
		}
	};
	
	

	/**
	 * 从个人信息源代码获得真实姓名
	 * @param data
	 */
	public void filterRealName(String data) {
		if (data == null) {
			return;
		}
		people = new People();
		Document doc = Jsoup.parse(data);

		Elements table = doc.select("table[bgcolor=#F2EDF8]");

		Elements trs = table.select("tr");
		Elements td1 = trs.get(0).select("td");
		people.setStunum(td1.get(2).text());
		// stunum = td1.get(2).text(); // 学号
		people.setRealname(td1.get(4).text());
		// realname = td1.get(4).text(); // 姓名
		people.setGender(trs.get(1).select("td").get(3).text());
		// gender = trs.get(1).select("td").get(3).text(); // 性别
		Elements td2 = trs.get(5).select("td");
		people.setInstitude(td2.get(1).text());
		// institude = td2.get(1).text();
		String classStr = td2.get(3).text();
		String classnumStr = classStr.substring(classStr.length() - 1);
		people.setMajor(trs.get(6).select("td").get(3).text());
		// major = trs.get(6).select("td").get(3).text();
		try {
			// classnum = Integer.parseInt(classnumStr);
			int i = Integer.parseInt(classnumStr);
			people.setClassnum(i);

		} catch (NumberFormatException e) {
			// classnum = 0;
			people.setClassnum(0);
		}
	}
	
	/**
	 * 判断手机网络状况
	 * @param context
	 * @return
	 */
	public boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
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
