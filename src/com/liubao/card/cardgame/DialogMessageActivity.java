package com.liubao.card.cardgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DialogMessageActivity extends Activity {
	
	private TextView titleMessageTextView;
	private TextView messageMessageTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog_message);
		
		titleMessageTextView=(TextView)findViewById(R.id.titleMessageTextView);
		messageMessageTextView=(TextView)findViewById(R.id.messageMessageTextView);
		
		Intent intent =getIntent();

		String title = intent.getStringExtra("title");
		String message = intent.getStringExtra("message");


		titleMessageTextView.setText(title);
		messageMessageTextView.setText(message);

		
	}

    public void confirm(View v) {
    	this.finish();
    }
}
