package com.liubao.card.cardgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DialogLevelActivity extends Activity {
	

	private TextView titleTextView;
	private TextView messageTextView;
	private Button confirmButton;
	int level;
	
	private String test;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_dialog);

		titleTextView = (TextView) findViewById(R.id.titleTextView);
		messageTextView = (TextView) findViewById(R.id.messageTextView);
		confirmButton=(Button)findViewById(R.id.confirmButton);

		Intent intent =getIntent();

		String title = intent.getStringExtra("title");
		String message = intent.getStringExtra("message");
		level = intent.getIntExtra("level", 1);
		boolean win=intent.getBooleanExtra("win", false);
		
		if(!win){
		
			confirmButton.setText("重来");
		}

		titleTextView.setText(title);
		messageTextView.setText(message);

		
	}



	public void confirm(View v) {
		
		SysActivityManager.getInstance().closeGameActiviy();

		Intent intent = new Intent();
		intent.setClass(DialogLevelActivity.this, GamesActivtiy.class);

		Bundle bundle = new Bundle();
		bundle.putInt("level", level);
		intent.putExtras(bundle);

		startActivity(intent);

		this.finish();
	}

	public void exit(View v) {
		
		SysActivityManager.getInstance().closeGameActiviy();
		
		Intent intent = new Intent();
		intent.setClass(DialogLevelActivity.this, MainActivity.class);
		startActivity(intent);
		
		//停止背景音乐
		stopService(new Intent(SystemStatic.MUSICSERVICE));
		this.finish();

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.e("KEYCODE_HOME", "KEYCODE_HOME");
		if(keyCode==KeyEvent.KEYCODE_HOME){
			Log.e("KEYCODE_HOME", "KEYCODE_HOME");
			return false;
		}

		// 判定用到底按了那个键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}

		return super.onKeyDown(keyCode, event);
	}
}
