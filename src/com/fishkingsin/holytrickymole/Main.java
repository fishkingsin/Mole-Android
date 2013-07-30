package com.fishkingsin.holytrickymole;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

public class Main extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);

		Button button = (Button) findViewById(R.id.startbutton);
		button.setEnabled(false);
		button.setOnClickListener((OnClickListener) this);

		TextView tv = (TextView) findViewById(R.id.editText1);
		tv.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				if (s.length() > 0) {
					((Button) findViewById(R.id.startbutton)).setEnabled(true);
				}
				else
				{
					((Button) findViewById(R.id.startbutton)).setEnabled(false);
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				
			}
		});

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
