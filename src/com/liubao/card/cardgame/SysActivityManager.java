package com.liubao.card.cardgame;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.media.MediaPlayer;

public class SysActivityManager extends Application {

	private static SysActivityManager sysActivityManager = null;

	private List<Activity> gameActivities = new ArrayList<Activity>();

	private MediaPlayer mediaPlayer;

	private SysActivityManager() {

	}



	public static SysActivityManager getInstance() {
		if (sysActivityManager == null) {
			sysActivityManager = new SysActivityManager();
		}
		return sysActivityManager;
	}

	public void addGameActivity(Activity activity) {
		gameActivities.add(activity);
	}

	public void closeGameActiviy() {
		if (gameActivities != null && gameActivities.size() > 0) {
			for (Activity activity : gameActivities) {
				if (activity instanceof GamesActivtiy) {
					activity.finish();
				}

			}
		}
		gameActivities.removeAll(gameActivities);
	}

	public void closeActiviy() {
		if (gameActivities != null && gameActivities.size() > 0) {
			for (Activity activity : gameActivities) {
				activity.finish();
			}
		}

	}

	@Override
	public void onLowMemory() {

		super.onLowMemory();
		System.gc();
	}

}
