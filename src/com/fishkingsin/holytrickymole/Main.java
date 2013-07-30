package com.fishkingsin.holytrickymole;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		Button button = (Button) findViewById(R.id.startbutton);
		button.setOnClickListener((OnClickListener) this);
	}

	@Override
	public void onClick(View arg) {
		if (arg.getId() == R.id.startbutton) {
			Intent intent = new Intent(this, FacePickActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
		}
	}
}
