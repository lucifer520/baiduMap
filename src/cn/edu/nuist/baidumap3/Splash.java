package cn.edu.nuist.baidumap3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;



public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View view = getLayoutInflater().inflate(R.layout.activity_splash, null);
		//设置透明度动画效�? 
		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
		//设置持续时间 
		animation.setDuration(1000);
		//设置监听�? 
		animation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			//结束动画后进入下�?��界面
			@Override
			public void onAnimationEnd(Animation animation) {
				skip();
			}
		});
		//设置动画  
		view.setAnimation(animation);
		setContentView(view);
	}

	private void skip() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}
}