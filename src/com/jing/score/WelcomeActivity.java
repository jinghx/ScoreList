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
 * ��ӭ����
 * 
 * @author Jing
 * 
 */
public class WelcomeActivity extends Activity implements AnimationListener {
	private ImageView imageView = null;
	private TextView tv = null;
	private Animation ivAnimation = null; // ͼƬ����
	private Animation tvAnimation = null;// ���ֶ���

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

		ivAnimation.setFillEnabled(true); // ����Fill����
		ivAnimation.setFillAfter(true); // ���ö��������һ֡�Ǳ�����View����

		imageView.setAnimation(ivAnimation);
		tv.setAnimation(tvAnimation);

		ivAnimation.setAnimationListener(this); // Ϊ�������ü���
	}

	@Override
	public void onAnimationStart(Animation animation) {

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// ��������ʱ������ӭ���沢ת�������������
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		this.finish();
	}

	@Override
	public void onAnimationRepeat(Animation animation) {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// �ڻ�ӭ��������BACK��
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
