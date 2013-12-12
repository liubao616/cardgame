package com.liubao.card.cardgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class FlashActivity extends Activity {

	private final static int splashtime = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flash);

		StartGameThread startGameThread = new StartGameThread();
		startGameThread.start();

	}

	private class StartGameThread extends Thread {

		public void run() {

			try {
				Thread.sleep(splashtime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			startActivity(new Intent(FlashActivity.this, MainActivity.class));
			FlashActivity.this.finish();
		}

	}

}
