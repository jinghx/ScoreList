package com.jing.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.jing.service.StartListenService;

public class ServiceUtils {

	public static void toStartService(Context context) {
		// ����û������ ������
		if (!ServiceUtils.isModifyServiceRunning(context)
				&& !isComplished(context)) {
			Intent newIntent = new Intent(context, StartListenService.class);
			context.startService(newIntent);
		}
	}

	public static boolean isComplished(Context context) {
		SharedPreferences sp = context.getSharedPreferences("publish",
				Context.MODE_APPEND);
		String oldData = sp.getString("grade", "");
		int count = sp.getInt("count", 100);
		Map<String, String> oldMap = new HashMap<String, String>();
		String[] strs = oldData.split(";");
		if ((strs.length == 1 && !"".equals(strs[0])) || strs.length > 1) {
			for (int i = 0; i < strs.length; ++i) {
				String[] score = strs[i].split(",");
				oldMap.put(score[0], score[1]);
			}
		}
		if (oldMap.size() == count) {
			return true;
		}
		return false;
	}

	/**
	 * �ж��ֻ�����״��
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
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

	public static boolean isModifyServiceRunning(Context mContext) {
		String name = "com.jing.service.StartListenService";
		return isServiceRunning(mContext, name);
	}

	/**
	 * �����жϷ����Ƿ�����.
	 * 
	 * @param context
	 * @param className�жϵķ�������
	 * @return true ������ false ��������
	 */
	public static boolean isServiceRunning(Context mContext, String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(100);
		if (!(serviceList.size() > 0)) {
			return false;
		}
		for (int i = 0; i < serviceList.size(); i++) {
			Log.i("isServiceRunning", serviceList.get(i).service.getClassName());
			if (serviceList.get(i).service.getClassName().equals(className)) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}
}
