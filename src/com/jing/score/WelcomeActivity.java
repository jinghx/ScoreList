package com.jing.score;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.scorelist.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 欢迎界面
 * 
 * @author Jing
 * 
 */
public class WelcomeActivity extends Activity implements AnimationListener {
	private ImageView imageView = null;
	private TextView tv = null;
	private Animation ivAnimation = null; // 图片动画
	private Animation tvAnimation = null;// 文字动画

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		MobclickAgent.updateOnlineConfig(this);
		imageView = (ImageView) findViewById(R.id.welcome_image_view);
		tv = (TextView) findViewById(R.id.welcome_txt_view);

		ivAnimation = AnimationUtils
				.loadAnimation(this, R.anim.welcome_iv_anim);
		tvAnimation = AnimationUtils.loadAnimation(this,
				R.anim.welcome_txt_anim);

		ivAnimation.setFillEnabled(true); // 启动Fill保持
		ivAnimation.setFillAfter(true); // 设置动画的最后一帧是保持在View上面

		imageView.setAnimation(ivAnimation);
		tv.setAnimation(tvAnimation);

		ivAnimation.setAnimationListener(this); // 为动画设置监听
	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// 动画结束时结束欢迎界面并转到软件的主界面
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		this.finish();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 在欢迎界面屏蔽BACK键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
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
