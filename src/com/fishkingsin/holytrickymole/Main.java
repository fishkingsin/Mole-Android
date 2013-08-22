package com.fishkingsin.holytrickymole;

import java.io.IOException;
import java.io.InputStream;

import com.longevitysoft.android.xml.plist.PListXMLHandler;
import com.longevitysoft.android.xml.plist.PListXMLHandler.PListParserListener;
import com.longevitysoft.android.xml.plist.PListXMLHandler.ParseMode;
import com.longevitysoft.android.xml.plist.PListXMLParser;
import com.longevitysoft.android.xml.plist.domain.PList;
import com.longevitysoft.android.xml.plist.domain.PListObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

public class Main extends Activity implements OnClickListener {
	protected static final String TAG = "Main";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		PListXMLParser parser = new PListXMLParser();
		PListXMLHandler pHandler = new PListXMLHandler();
		PListParserListener parseListener = new PListParserListener(){
			int count = 0; 
			@Override
			public void onPListParseDone(PList pList, ParseMode mode) {
				
			}
			
		};	
		pHandler.setParseListener(parseListener);
		parser.setHandler(pHandler);
		AssetManager am = this.getAssets();
		InputStream is;
		try {
			is = am.open("donald_holy-tricky_female.plist");
			parser.parse(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Button button = (Button) findViewById(R.id.startbutton);
		
		button.setOnClickListener((OnClickListener) this);

		TextView tv = (TextView) findViewById(R.id.editText1);
		
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String userName = prefs.getString(getString(R.string.keyUserName), "");
		if(!userName.equals(""))
		{
			tv.setText(userName);
			button.setEnabled(true);
		}
		else
		{
			button.setEnabled(false);
		}
		tv.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				if (s.length() > 0) {
					
					((Button) findViewById(R.id.startbutton)).setEnabled(true);
				} else {
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
			
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(getString(R.string.keyUserName),((TextView) findViewById(R.id.editText1)).getText().toString());

			// Commit the edits!
			editor.commit();
			
			Intent intent = new Intent(this, FacePickActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.slide_in_right,
					R.anim.slide_out_left);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.credit:
			final PopupWindow popUp = new PopupWindow(this);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
					Gravity.TOP);
			final LinearLayout ll = new LinearLayout(this);
			ll.setLayoutParams(params);
			ll.setOrientation(LinearLayout.VERTICAL);

			final ScrollView scrollview = new ScrollView(this);
			final TextView tv = new TextView(this);
			tv.setText(Html.fromHtml(getString(R.string.credit_text)));

			tv.setMovementMethod(LinkMovementMethod.getInstance());
			scrollview.addView(tv, params);

			ll.addView(scrollview);

			popUp.setContentView(ll);

			final View currentView = this.getWindow().getDecorView()
					.findViewById(android.R.id.content);
			popUp.showAtLocation(currentView, Gravity.BOTTOM, 0, 0);
			
			popUp.setFocusable(false);
			popUp.setOutsideTouchable(true);
			popUp.setTouchable(true);
			
			popUp.setTouchInterceptor(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event)
				{
					if (event.getAction() == MotionEvent.ACTION_OUTSIDE)
					{
						popUp.dismiss();
						return true;
					}
					return false;
				}

			});
			popUp.update(0, 0,  (int)(currentView.getWidth() *0.7),
					currentView.getHeight());
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
