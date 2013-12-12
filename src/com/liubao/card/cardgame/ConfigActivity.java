package com.liubao.card.cardgame;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class ConfigActivity extends Activity {

	private CheckBox checkBoxMusic;
	private CheckBox checkBoxSound;

	private SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);

		checkBoxMusic = (CheckBox) findViewById(R.id.checkBoxMusic);
		checkBoxSound = (CheckBox) findViewById(R.id.checkBoxSound);

		sharedPreferences = getSharedPreferences(SystemStatic.APPLICATION,
				Activity.MODE_PRIVATE);

		boolean music = sharedPreferences.getBoolean(SystemStatic.PLAYMUSIC,
				false);
		boolean sound = sharedPreferences.getBoolean(SystemStatic.PLAYSOUND,
				true);

		checkBoxMusic.setChecked(music);
		checkBoxSound.setChecked(sound);

		checkBoxMusic
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						SharedPreferences.Editor editor = sharedPreferences
								.edit();
						editor.putBoolean(SystemStatic.PLAYMUSIC, isChecked);
						editor.commit();

					}
				});

		checkBoxSound
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						SharedPreferences.Editor editor = sharedPreferences
								.edit();
						editor.putBoolean(SystemStatic.PLAYSOUND, isChecked);
						editor.commit();
					}
				});

	}

}
