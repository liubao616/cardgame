package com.liubao.card.cardgame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.baidu.mobads.IconsAd;

public class MainActivity extends Activity {

	private Button startButton;
	private Button levelButton;
	private Button sourceButton;
	private Button gameconfigButton;
	private Button aboutButton;

	private SharedPreferences sharedPreferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// 百度广告
		IconsAd iconsAd = new IconsAd(this);
		iconsAd.loadAd(this);

		startButton = (Button) findViewById(R.id.startButton);
		levelButton = (Button) findViewById(R.id.levelButton);
		sourceButton = (Button) findViewById(R.id.sourceButton);
		gameconfigButton = (Button) findViewById(R.id.gameconfigButton);
		aboutButton = (Button) findViewById(R.id.aboutButton);

		startButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setClass(MainActivity.this, GamesActivtiy.class);

				Bundle bundle = new Bundle();
				bundle.putInt("level", 1);
				intent.putExtras(bundle);

				startActivity(intent);

				sharedPreferences = getSharedPreferences(
						SystemStatic.APPLICATION, Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putFloat(SystemStatic.CURRENTSOURCE, 0.0f);
				editor.commit();

				MainActivity.this.finish();
			}
		});

		levelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				String[] items = { "第一关", "第二关", "第三关", "第四关", "第五关", "第六关",
						"第七关", "第八关", "第九关" };

				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);

				builder.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int which) {
						Intent intent = null;
						intent = new Intent(MainActivity.this,
								GamesActivtiy.class);
						intent.putExtra("level", which + 1);
						startActivity(intent);
						MainActivity.this.finish();
					}
				});

				builder.create().show();

			}
		});

		sourceButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				SharedPreferences sharedPreferences = getSharedPreferences(
						SystemStatic.APPLICATION, Activity.MODE_PRIVATE);

				float maxsource = sharedPreferences.getFloat(
						SystemStatic.MAXSOURCE, 0.0f);
				float currentsource = sharedPreferences.getFloat(
						SystemStatic.CURRENTSOURCE, 0.0f);

				Intent intent = new Intent(MainActivity.this,
						DialogMessageActivity.class);
				intent.putExtra("title", "得分记录");
				intent.putExtra(
						"message",
						"最高分数：" + String.format("%.2f", maxsource)
								+ "分\r\n最新分数："
								+ String.format("%.2f", currentsource) + "分");
				startActivity(intent);
			}
		});

		gameconfigButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this,
						ConfigActivity.class);
				startActivity(intent);
			}
		});

		aboutButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(MainActivity.this,
						DialogMessageActivity.class);
				intent.putExtra("title", "关于游戏");
				intent.putExtra("message", "这是一个锻炼和提高儿童记忆力的趣味游戏，游戏适合4~12岁的小孩");
				startActivity(intent);
			}
		});

	}

}
