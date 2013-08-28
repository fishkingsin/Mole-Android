package com.fishkingsin.holytrickymole;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.InterstitialAd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class Main extends Activity implements OnClickListener, AdListener {
	protected static final String TAG = "Main";
	private Context mContext;
	private InterstitialAd interstitial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main);
		// Create the interstitial
	    interstitial = new InterstitialAd(this, "a1521c74c858d66");

	    // Create ad request
	    AdRequest adRequest = new AdRequest();
//	    adRequest.addTestDevice("43466EBBCA9A9D93451946825E862160");
	    // Begin loading your interstitial
	    interstitial.loadAd(adRequest);

	    // Set Ad Listener to use the callbacks below
	    interstitial.setAdListener(this);
		Button button = (Button) findViewById(R.id.startbutton);

		button.setOnClickListener((OnClickListener) this);

		TextView tv = (TextView) findViewById(R.id.editText1);

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String userName = prefs.getString(getString(R.string.keyUserName), "");
		if (!userName.equals("")) {
			tv.setText(userName);
			button.setEnabled(true);
		} else {
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
		Button creditButton = (Button) findViewById(R.id.credit_button);
		mContext = this;
		creditButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Utilities.setupCreditPopWindow(getString(R.string.credit_text),
						(Activity) mContext);

			}

		});

	}

	@Override
	public void onClick(View arg) {
		if (arg.getId() == R.id.startbutton) {

			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(getString(R.string.keyUserName),
					((TextView) findViewById(R.id.editText1)).getText()
							.toString());

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
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
						popUp.dismiss();
						return true;
					}
					return false;
				}

			});
			popUp.update(0, 0, (int) (currentView.getWidth() * 0.7),
					currentView.getHeight());
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onDismissScreen(Ad arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLeaveApplication(Ad arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPresentScreen(Ad arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReceiveAd(Ad ad) {
		// TODO Auto-generated method stub
		Log.d("OK", "Received ad");
		if (ad == interstitial) {
			interstitial.show();
		}
	}
}
