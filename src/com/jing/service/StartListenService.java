package com.jing.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.scorelist.R;
import com.jing.broadcast.StartBroadcast;
import com.jing.db.LoadUtil;
import com.jing.score.LoginActivity;
import com.jing.score.MainActivity;
import com.jing.score.MyApplication;
import com.jing.utils.ServiceUtils;

/**
 * 防止杀死 1.ondestory()重启 2.多种广播 3.服务优先级
 * 
 * @author Jing
 *
 */
public class StartListenService extends Service {
	private String TAG = "StartListenService";
	private String uriLogin = "http://202.194.40.15:8080/xsxt/xsxt.jsp";
	private String uriresult = "http://202.194.40.15:8080/xsxt/right.jsp";
	private String uridata = "http://202.194.40.15:7778/pls/wwwbks/bkscjcx.curscopre";// 当前公示成绩链接
	private List<Cookie> cookies; // 保存获取的cookie
	private static final long SHORT_PERIOD = 5 * 60 * 1000;
	private static final long MIDDLE_PERIOD = 60 * 60 * 1000;
	private static final long LONG_PERIOD = 2 * 60 * 60 * 1000;

	public StartListenService() {
		super();
	}

	@Override
	public void onCreate() {
		// new Random().nextInt(100) +
		toNotify(1100, "小单来了", "超级成绩单", "当有新成绩出来时，会提醒你哦!");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "onStartCommand");
		new Timer().schedule(new ListenTask(), 0, MIDDLE_PERIOD);
		return Service.START_REDELIVER_INTENT;
	}

	@Override
	public void onDestroy() {
		// new Random().nextInt(100) +
		toNotify(1200, "小单来了", "超级成绩单", "服务已关闭");
		Intent i = new Intent(getApplicationContext(), StartBroadcast.class);
		i.setAction("com.jing.service.StartListenService");
		sendBroadcast(i);
		super.onDestroy();
	}

	class ListenTask extends TimerTask {
		@Override
		public void run() {
			if (!ServiceUtils.isNetworkConnected(getApplicationContext())) {
				return;
			}

			String[] info = getUserInfo();
			if (info == null || "".equals(info[1]) || "".equals(info[0])) {
				Log.i(TAG, "can't find userinfo");
				return;
			}
			// toNotify(new Random().nextInt(100) + 1300, "小单来了", "超级成绩单",
			// "努力盯着成绩中...");
			HttpClient client = toLogin(info[0], info[1]);
			if (client == null) {
				Log.i(TAG, "login failed");
				cookies = null;
				return;
			}

			String data = getGradeData(client);
			if (data == null) {
				Log.i(TAG, "data error");
				return;
			}
			toCheck(data);
		}
	}

	private void toCheck(String data) {
		Log.i(TAG, "source:" + data);
		Map<String, String> newMap = filterData(data);
		Log.i(TAG, "new map:" + newMap.toString());
		if (newMap.size() > 0) {
			NotifyScore(new Random().nextInt(100) + 1001, newMap);
		}
	}

	private void NotifyScore(int code, Map map) {
		String content = "";
		int count = map.size();
		Iterator iterator = map.entrySet().iterator();
		int temp = 0;
		while (iterator.hasNext() && temp < 2) {
			Map.Entry entry = (Map.Entry) iterator.next();
			content += entry.getKey() + " : " + entry.getValue() + "  ";
			temp++;
		}
		if (count > 2) {
			content += "...";
		}
		toNotify(code, "出新成绩了", map.size(), content);
	}

	private void toNotify(int code, String ticker, int count, String content) {
		MyApplication app = (MyApplication) getApplication();
		NotificationManager manager;
		manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				getApplicationContext());
		builder.setTicker(ticker);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(count + "门新成绩");
		builder.setContentText(content);
		//停顿0秒 震0.3秒 停0.5秒 震0.7秒
		// builder.setVibrate(new long[] { 0, 300, 500, 700 });
		// builder.setOnlyAlertOnce(true);
		builder.setDefaults(Notification.DEFAULT_VIBRATE|Notification.DEFAULT_LIGHTS);
		// NotificationCompat.InboxStyle style = new
		// NotificationCompat.InboxStyle();
		// builder.setStyle(style);
		// style.setBigContentTitle(map.size()+"门新成绩");
		// style.setSummaryText("点击进入应用，查看排名信息");
		// Iterator iterator = map.entrySet().iterator();
		// while (iterator.hasNext()) {
		// Map.Entry entry = (Map.Entry) iterator.next();
		// Log.i("map", entry.getKey() + " : " + entry.getValue()+"...");
		// style.addLine(entry.getKey() + " : " + entry.getValue());
		// }
		// style.addLine("===========");
		Intent intent = null;
		if (app.isLogin()) {
			intent = new Intent(getApplicationContext(), MainActivity.class);
		} else {
			intent = new Intent(getApplicationContext(), LoginActivity.class);
		}

		PendingIntent pending = PendingIntent
				.getActivity(getApplicationContext(), 0, intent,
						PendingIntent.FLAG_ONE_SHOT);
		builder.setContentIntent(pending);

		manager.notify(code, builder.build());
	}

	private void toNotify(int code, String ticker, String title, String content) {
		NotificationManager manager;
		manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				getApplicationContext());
		builder.setTicker(ticker);
		builder.setSmallIcon(R.drawable.ic_launcher);
		builder.setContentTitle(title);
		builder.setContentText(content);
		builder.setAutoCancel(true);
		
		manager.notify(code, builder.build());
	}

	private String[] getUserInfo() {
		Vector<Vector<String>> vtemp;
		vtemp = LoadUtil.getAccount();
		String[] strs = new String[2];
		if (vtemp.size() > 0) {
			strs[0] = vtemp.get(0).get(0).trim();
			strs[1] = vtemp.get(0).get(1).trim();
			return strs;
		}
		return null;
	}

	public String getGradeData(HttpClient httpclient) {
		if (cookies == null) {
			return null;
		}
		HttpGet httpgetdata = new HttpGet(uridata);
		try {

			// 把之前的cookie放到此次get所需要的头信息中
			httpgetdata.setHeader("Cookie", cookies.get(0).getName() + "="
					+ cookies.get(0).getValue());

			// 发出HTTP request /
			HttpResponse httpRdata = httpclient.execute(httpgetdata);

			// 若状态码为200 ok /
			if (httpRdata.getStatusLine().getStatusCode() == 200) {
				// tdata.append("333...\n");
				StringBuffer sb = new StringBuffer();
				HttpEntity entity = httpRdata.getEntity();
				InputStream is = entity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, "GB2312"));
				String data = "";
				while ((data = br.readLine()) != null) {
					sb.append(data);
				}
				return sb.toString();

			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			httpclient.getConnectionManager().shutdown();
		}
		return null;

	}

	public Map<String, String> filterData(String source) {
		if (null == source) {
			return null;
		}
		String toWrite = "";
		SharedPreferences sp = getSharedPreferences("publish",
				Context.MODE_APPEND);
		String oldData = sp.getString("grade", "");
		Log.i(TAG, "olddata:" + oldData);
		Map<String, String> oldMap = new HashMap<String, String>();
		String[] strs = oldData.split(";");
		Log.i(TAG, "len:" + strs.length);
		if ((strs.length == 1 && !"".equals(strs[0])) || strs.length > 1) {
			for (int i = 0; i < strs.length; ++i) {
				String[] score = strs[i].split(",");
				oldMap.put(score[0], score[1]);
				Log.i(TAG, "[]:" + score[0] + " " + score[1]);
			}
		}
		Log.i(TAG, "old map:" + oldMap.toString());

		Map<String, String> newMap = new HashMap<String, String>();
		String html = source;
		Document doc = Jsoup.parse(html);
		Elements table = doc.select("table[bgcolor=#F2EDF8]");
		Elements trs = table.select("tr");
		for (int j = 1; j < trs.size(); j++) {
			Elements tds = trs.get(j).select("td");
			String name = tds.get(2).text();
			String score = tds.get(9).text().trim();
			// && !"0".equals(score)
			if (!"".equals(score) && !"0".equals(score)
					&& !oldMap.containsKey(name)) {
				newMap.put(name, score);
			}
		}
		// 保存已出成绩的课程
		if (newMap.size() > 0) {
			oldMap.putAll(newMap);
			Iterator iterator = oldMap.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				toWrite += entry.getKey() + "," + entry.getValue() + ";";
			}

			Editor edit = sp.edit();
			edit.putString("grade", toWrite);
			Log.i("publish", "new write:" + toWrite);
			edit.commit();
		}
		return newMap;
	}

	private HttpClient toLogin(String username, String userpass) {
		HttpClient client = new DefaultHttpClient();
		HttpResponse httpResponse;

		// 登录
		// 建立HTTP Post连线
		HttpPost httpRequest = new HttpPost(uriLogin);
		httpRequest.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS,
				false);
		List<NameValuePair> params = new ArrayList<NameValuePair>();

		params.add(new BasicNameValuePair("userPass", userpass)); // 密码
		params.add(new BasicNameValuePair("userId", username)); // 这是学号

		try {
			// 发出HTTP request
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			// 取得HTTP response
			httpResponse = client.execute(httpRequest); // 执行
			// 若状态码为200 ok
			if (httpResponse.getStatusLine().getStatusCode() == 200) { // 返回值正常
				// 获取返回的cookie
				cookies = ((AbstractHttpClient) client).getCookieStore()
						.getCookies();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		// 获取登录成功信息
		HttpGet httpLoginResult = new HttpGet(uriresult);
		try {

			// 把之前的cookie放到此次get所需要的头信息中
			httpLoginResult.setHeader("Cookie", cookies.get(0).getName() + "="
					+ cookies.get(0).getValue());
			// 发出HTTP request /
			HttpResponse httpRdata = client.execute(httpLoginResult);
			// 若状态码为200 ok /
			if (httpRdata.getStatusLine().getStatusCode() == 200) {
				cookies = ((AbstractHttpClient) client).getCookieStore()
						.getCookies();
				// 接下来的代码是为了把从网页获取到的内容读出来
				StringBuffer sb = new StringBuffer();
				HttpEntity entity = httpRdata.getEntity();
				InputStream is = entity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(
						is, "GB2312"));
				String data = "";
				while ((data = br.readLine()) != null) {
					sb.append(data);
				}
				if (sb.toString().length() > 200) {
					return client;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
