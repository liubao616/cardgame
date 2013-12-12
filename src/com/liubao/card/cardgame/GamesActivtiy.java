package com.liubao.card.cardgame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobads.IconsAd;

/**
 * 游戏界面
 * 
 * @author Administrator
 * 
 */
public class GamesActivtiy extends Activity {

	private GameLevel gameLevel;
	private int level;// 当前关卡
	private int rememberTime;// 记忆时间
	private boolean isstart;// 是否开始
	private long descSecond;// 剩余事件
	private int errorclick;// 总点击次数

	private Handler handler;
	private PowerManager.WakeLock mWakeLock;

	private DateTimer dateTimer;// 倒计时类

	private GridView gridView;
	private TextView descTime;// 显示倒计时
	private TextView levelText;// 等级
	private TextView sourceTextView;// 得分
	private TextView higherTextView;// 最高分

	// 卡片的正面资源 图片Id int
	private static int[] imagesRes = { R.drawable.pa, R.drawable.pb, R.drawable.pc,
			R.drawable.pd, R.drawable.pf, R.drawable.pg, R.drawable.ph,
			R.drawable.pi, R.drawable.pj, R.drawable.pk, R.drawable.pl,
			R.drawable.pm, R.drawable.pn, R.drawable.po, R.drawable.pp,
			R.drawable.pq, R.drawable.pr, R.drawable.ps, R.drawable.pt,
			R.drawable.pu, R.drawable.pv, R.drawable.pw, R.drawable.px,
			R.drawable.py, R.drawable.pz };

	private static List<Integer> imagesResList;

	// 卡片的背面 图片Id int
	private static int imagesResBack = R.drawable.back;

	private List<Integer> imagesBack; // 卡片背面的数据源 设配器的组装方式
	private List<Integer> imagesRight; // 卡片正面面的数据源 设配器的组装方式

	private ImageAdapter adapter;// 设配器

	private int firstIndex;// 第一次点击的图片的资源的位置，在设配器的位置
	private int firstImageId;// 第一次点击的图片的资源的Id
	private int secondIndex;// 第二次点击的图片的资源的位置，在设配器的位置
	private int secondImageId;// 第二次点击的图片的资源的位置，在设配器的位置

	private int successNums;

	private SharedPreferences sharedPreferences;
	private float source;// 本关得分
	private float maxsource;// 最高得分
	private float currentsource;// 本次累计得分
	private boolean playsound;// 是否播放音效
	private boolean playmusic;// 是否播放背景音乐
	private int sound1;
	private SoundPool soundPool;
	private Intent musicIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_games);
		
		//百度广告
		IconsAd iconsAd=new IconsAd(this);
		iconsAd.loadAd(this);

		init();// //初始化

		// 实现游戏逻辑判断
		gridView.setOnItemClickListener(new GameLogic());

	}

	@Override
	protected void onStart() {

		super.onStart();
		if (playmusic) {
			startService(musicIntent);
		}
	}

	// 初始化
	private void init() {
		
		gridView = (GridView) findViewById(R.id.gridView);
		descTime = (TextView) findViewById(R.id.descTime);
		levelText = (TextView) findViewById(R.id.levelText);
		sourceTextView = (TextView) findViewById(R.id.sourceTextView);
		higherTextView = (TextView) findViewById(R.id.higherTextView);

		// 初始化所有的资源图片
		imagesResList = new ArrayList<Integer>();
		for (int r : imagesRes) {
			imagesResList.add(r);
		}

		// 打乱顺序
		Collections.shuffle(imagesResList);

		musicIntent = new Intent("com.liubao.cardgame");

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
				"liubaoTag");
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		sharedPreferences = getSharedPreferences(SystemStatic.APPLICATION,
				Activity.MODE_PRIVATE);

		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
		sound1 = soundPool.load(this, R.raw.playsound, 0);

		maxsource = sharedPreferences.getFloat(SystemStatic.MAXSOURCE, 0.0f);
		currentsource = sharedPreferences.getFloat(SystemStatic.CURRENTSOURCE,
				0.0f);
		playsound = sharedPreferences.getBoolean(SystemStatic.PLAYSOUND, true);
		playmusic = sharedPreferences.getBoolean(SystemStatic.PLAYMUSIC, false);

		SysActivityManager.getInstance().addGameActivity(this);

		handler = new Handler();

		firstIndex = -1;// 初始值为-1 ，表示用户还没有点击过
		successNums = 0;// 成功数都是0
		isstart = false;

		// 取得关卡
		gameLevel = new GameLevel();
		Bundle bundle = getIntent().getExtras();
		level = bundle.getInt("level");
		gameLevel.setLevel(level);
		
		int gameColumn=(gameLevel.getImagenums()*2)%4==0?4:3;
		gridView.setNumColumns(gameColumn);

		rememberTime = gameLevel.getRemembertime();

		dateTimer = new DateTimer(
				(gameLevel.getTotalgametime() + gameLevel.getRemembertime()) * 1000,
				1000);

		
		levelText.setText("第" + level + "关");
		sourceTextView.setText("得分：" + String.format("%.2f", currentsource));
		higherTextView.setText("最高分：" + String.format("%.2f", maxsource));

		adapter = new ImageAdapter(GamesActivtiy.this);
		// 设置卡片正面的数据源
		imagesRight = new ArrayList<Integer>();
		for (int i = 0; i < gameLevel.getImagenums(); i++) {
			// 根据游戏逻辑，有两张一样图片，所以添加两次
			imagesRight.add(imagesResList.get(i));
			imagesRight.add(imagesResList.get(i));
		}

		// 卡片背面图片
		imagesBack = new ArrayList<Integer>();
		for (int i = 0; i < gameLevel.getImagenums() * 2; i++) {
			imagesBack.add(imagesResBack);
		}

		// 打乱顺序
		Collections.shuffle(imagesRight);

		// 放到设配器中
		adapter.setImages(imagesRight);

		gridView.setAdapter(adapter);

	}

	private class GameLogic implements AdapterView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			// 如果两次点击的位置都是同一张，不进行任何处理
			if (!isstart || firstIndex == position) {
				return;// 返回这个方法。
			}

			ImageView imageView1 = null;
			ImageView imageView2 = null;
			// 表示用户第一次点击
			if (firstIndex == -1) {
				// 获得用户第一次点击的图片的位置
				firstIndex = position;

				// 获得用户第一次点击的图片的id
				firstImageId = imagesRight.get(position);

				// 翻出正面
				imageView1 = (ImageView) view;
				imageView1.setImageResource(firstImageId);
			} else {

				// //表示用户第二次点击
				// 获得用户第二次点击的图片的位置
				secondIndex = position;

				// 获得用户第二次点击的图片的id
				secondImageId = imagesRight.get(position);

				// 翻出正面
				imageView2 = (ImageView) view;
				imageView2.setImageResource(secondImageId);

				// 休眠
				handler.post(new Runnable() {

					// 跟position拿到图片的视图
					ImageView imageView1 = (ImageView) gridView
							.getChildAt(firstIndex);
					ImageView imageView2 = (ImageView) gridView
							.getChildAt(secondIndex);

					@Override
					public void run() {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						// 判断两幅图片是否一样
						if (firstImageId == secondImageId) {// 正确

							// 播放音效
							if (playsound) {
								soundPool.play(sound1, 1, 1, 0, 0, 1);
							}

							// 两张卡片会隐藏
							imageView1.setVisibility(GridView.GONE);
							imageView2.setVisibility(GridView.GONE);

							successNums++;// 成功一次加一次

							if (successNums == gameLevel.getImagenums()) {

								// 赢了就计算得分
								// 得分=100-错误次数+图片数量×1.5-（所花时间/总时间）×100

								long counttime = gameLevel.getTotalgametime()
										- descSecond;

								source = 100f
										- (float) errorclick
										+ gameLevel.getImagenums()
										* 1.5f
										- (counttime * 1.0f / gameLevel
												.getTotalgametime()) * 100f;

								currentsource += source;// 本次累计得分
								maxsource = currentsource > maxsource ? currentsource
										: maxsource;

								SharedPreferences.Editor editor = sharedPreferences
										.edit();
								editor.putFloat(SystemStatic.CURRENTSOURCE,
										currentsource);
								editor.putFloat(SystemStatic.MAXSOURCE,
										maxsource);
								editor.commit();

								Intent intent = new Intent(GamesActivtiy.this,
										DialogLevelActivity.class);
								// 已经是最后一关了。直接调回主页
								if (level == GameLevel.TOTALLEVLE) {

									intent.putExtra("title", "恭喜");
									intent.putExtra(
											"message",
											"你太厉害了，你已经过了全部的关卡了。你的总得分是："
													+ String.format("%.2f",
															currentsource)
													+ "分");

									SharedPreferences.Editor editor2 = sharedPreferences
											.edit();
									editor2.putFloat(
											SystemStatic.CURRENTSOURCE, 0.0f);
									editor2.commit();

								} else {
									intent.putExtra("title", "恭喜");
									intent.putExtra(
											"message",
											"你赢了，进入下一关\n本关得分："
													+ String.format("%.2f",
															source));
									intent.putExtra("win", true);
									intent.putExtra("level", level + 1);
								}

								GamesActivtiy.this.startActivity(intent);

							}

						} else {
							// 错误
							errorclick++;// 错误次数加一
							// 重新显示反面
							imageView1.setImageResource(imagesResBack);
							imageView2.setImageResource(imagesResBack);
						}

					}
				});

				// 重新开始判断
				firstIndex = -1;

			}

		}
	}

	@Override
	protected void onResume() {
		dateTimer.start();// 倒计时开始
		mWakeLock.acquire();
		super.onResume();
	}

	@Override
	protected void onPause() {
		dateTimer.cancel();// 倒计时结束
		mWakeLock.release();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	// 实现倒计时类
	class DateTimer extends CountDownTimer {

		// 总时长 ， 步长 单位都是毫秒
		public DateTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);

		}

		// 每一步长都是执行
		@Override
		public void onTick(long millisUntilFinished) {
			descSecond = millisUntilFinished / 1000;

			if (rememberTime > 0) {
				rememberTime--;
				// 修改倒计时的显示
				descTime.setText("预备：0" + rememberTime + "秒");
			} else if (rememberTime == 0) {
				rememberTime--;
				descTime.setText("开始：" + descSecond + "秒");
				isstart = true;
				adapter.setImages(imagesBack);
				gridView.setAdapter(adapter);
			} else {
				descTime.setText("开始："
						+ (descSecond < 10 ? "0" + descSecond : descSecond)
						+ "秒");
			}

		}

		// 倒计时结束时执行
		@Override
		public void onFinish() {

			descTime.setText("开始：00秒");

			// 判断是否已经全部完成
			if (successNums < gameLevel.getImagenums()) {

				Intent intent = new Intent(GamesActivtiy.this,
						DialogLevelActivity.class);

				intent.putExtra("title", "不好意思");
				intent.putExtra("message", "你输了，重新来一次");
				intent.putExtra("level", level);
				intent.putExtra("win", false);

				GamesActivtiy.this.startActivity(intent);

			}
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// 判定用到底按了那个键
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			Intent intent = new Intent(GamesActivtiy.this,
					DialogLevelActivity.class);

			intent.putExtra("title", "要退出吗？");
			intent.putExtra("message", "确定要退出吗？");
			intent.putExtra("level", level);
			intent.putExtra("win", false);

			GamesActivtiy.this.startActivity(intent);
			return false;
		}

		return super.onKeyDown(keyCode, event);
	}

}
