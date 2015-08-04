package com.jing.broadcast;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.scorelist.R;
import com.jing.score.MainActivity;
import com.jing.service.StartListenService;
import com.jing.utils.ServiceUtils;

public class StartBroadcast extends BroadcastReceiver {

	private static final String TAG = "StartBroadcast";
	private static final String BOOT_ACTION = Intent.ACTION_BOOT_COMPLETED;
	private static final String USER_PRESENT = Intent.ACTION_USER_PRESENT;
	private static final String SERVICE_ACTION = "com.jing.service.StartListenService";
	private NotificationManager manager;

	public StartBroadcast() {

	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Log.i(TAG, "Boot this system , Receive()");
		// Intent newIntent = new Intent(context, TestActivity.class);
		// newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //
		// 注意，必须添加这个标记，否则启动会失败
		// context.startActivity(newIntent);
		if (BOOT_ACTION.equals(intent.getAction())) {
			Log.i(TAG, "ACTION_BOOT_COMPLETED , onReceive()");
		}
		if (SERVICE_ACTION.equals(intent.getAction())) {
			Log.i(TAG, SERVICE_ACTION + " , onReceive()");
		}
		if (USER_PRESENT.equals(intent.getAction())) {
			Log.i(TAG, USER_PRESENT + " , onReceive()");
		}

		// 服务没有运行 则启动
		if (!ServiceUtils.isModifyServiceRunning(context)) {
			Intent newIntent = new Intent(context, StartListenService.class);
			context.startService(newIntent);
		}

		// manager = (NotificationManager) context
		// .getSystemService(Context.NOTIFICATION_SERVICE);
		// NotificationCompat.Builder builder = new NotificationCompat.Builder(
		// context);
		// builder.setSmallIcon(R.drawable.ic_launcher);
		// builder.setContentTitle("超级成绩单");
		// builder.setContentText("广播启动");
		// manager.notify(1000, builder.build());

	}
}
