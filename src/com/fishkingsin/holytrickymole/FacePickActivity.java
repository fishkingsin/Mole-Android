package com.fishkingsin.holytrickymole;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;

public class FacePickActivity extends FragmentActivity {
	private String[] maleImageName = { "donald_holy-tricky_male@2x.png",
			"ks_holy-tricky_male@2x.png", "lbt_holy-tricky_male@2x.png",
			"peter_holy-tricky_male@2x.png", "sh_holy-tricky_male@2x.png",
			"tse_holy-tricky_male@x2.png", };
	private String[] femaleImageName = { "donald_holy-tricky_female@2x.png",
			"ks_holy-tricky_female@2x.png", "lbt_holy-tricky_female@2x.png",
			"littlethunder_holy-tricky_female@x2.png",
			"peter_holy-tricky_female@2x.png", "sh_holy-tricky_female@2x.png",
			"tse_holy-tricky_female@2x.png", };
	String defaultDescription = "deafult_description.plist";

	private String[] malePlist = { "donald_holy-tricky_male.plist",
			"ks_holy-tricky_male.plist", "lbt_holy-tricky_male.plist",
			"peter_holy-tricky_male.plist", "sh_holy-tricky_male.plist",
			"tse_holy-tricky_male.plist" };

	private String[] femalePlist = { "donald_holy-tricky_female.plist",
			"ks_holy-tricky_female.plist", "lbt_holy-tricky_female.plist",
			"littlethunder_holy-tricky_female.plist",
			"peter_holy-tricky_female.plist", "sh_holy-tricky_female.plist",
			"tse_holy-tricky_female.plist", };
	private Integer[] mImageMaleIds = {
			R.drawable.donald_holy_tricky_male_thumb,
			R.drawable.ks_holy_tricky_male_thumb,
			R.drawable.lbt_holy_tricky_male_thumb,
			R.drawable.peter_holy_tricky_male_thumb,
			R.drawable.sh_holy_tricky_male_thumb,
			R.drawable.tse_holy_tricky_male_thumb, };
	private Integer[] mImageFemaleIds = {
			R.drawable.donald_holy_tricky_female_thumb,
			R.drawable.ks_holy_tricky_female_thumb,
			R.drawable.lbt_holy_tricky_female_thumb,
			R.drawable.littlethunder_holy_tricky_female_thumb,
			R.drawable.peter_holy_tricky_female_thumb,
			R.drawable.sh_holy_tricky_female_thumb,
			R.drawable.tse_holy_tricky_female_thumb, };

	private GridView gridview1 = null;
	private GridView gridview2 = null;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;
		System.gc();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.facepick_activity);
		TabHost mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();

		View indicator = LayoutInflater.from(this).inflate(R.layout.tab,
				(ViewGroup) findViewById(android.R.id.tabs), false);

		mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.male))
				.setIndicator(getString(R.string.male)).setContent(R.id.tab1));
		mTabHost.addTab(mTabHost.newTabSpec(getString(R.string.female))
				.setIndicator(getString(R.string.female)).setContent(R.id.tab2));

		gridview1 = (GridView) findViewById(R.id.gridview1);
		gridview1.setAdapter(new ImageAdapter(this, mImageMaleIds));

		gridview1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(mContext);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString(getString(R.string.keyImageName),
						maleImageName[position]);
				editor.putString(getString(R.string.keyPlistName),
						malePlist[position]);
				
				// Commit the edits!
				editor.commit();

				Intent i = new Intent(FacePickActivity.this,
						GameCoreActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
		gridview2 = (GridView) findViewById(R.id.gridview2);
		gridview2.setAdapter(new ImageAdapter(this, mImageFemaleIds));

		gridview2.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(mContext);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("keyImageName", femaleImageName[position]);
				editor.putString(getString(R.string.keyPlistName),
						femalePlist[position]);
				// Commit the edits!
				editor.commit();

				Intent i = new Intent(FacePickActivity.this,
						GameCoreActivity.class);
				startActivity(i);
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_left);
			}
		});
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	/**
	 * This class loads the image gallery in grid view.
	 * 
	 */
	public class ImageAdapter extends BaseAdapter {
		private Context mContext;
		private Integer[] targetImageIds;

		public ImageAdapter(Context c, Integer _targetImageIds[]) {
			mContext = c;
			targetImageIds = _targetImageIds;
		}

		public int getCount() {
			return targetImageIds.length;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		// create a new ImageView for each item referenced by the Adapter
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView i = new ImageView(mContext);
			if (targetImageIds.length > position) {
				i.setImageResource(targetImageIds[position]);
				i.setScaleType(ImageView.ScaleType.FIT_CENTER);

				i.setDrawingCacheEnabled(true);
				i.setBackgroundColor(0x00000000);
			}
			return i;

		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	/**
	 * This method is to scale down the image
	 */
	public Bitmap decodeURI(String filePath) {

		Options options = new Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Only scale if we need to
		// (16384 buffer for img processing)
		Boolean scaleByHeight = Math.abs(options.outHeight - 100) >= Math
				.abs(options.outWidth - 100);
		if (options.outHeight * options.outWidth * 2 >= 16384) {
			// Load, scaling to smallest power of 2 that'll get it <= desired
			// dimensions
			double sampleSize = scaleByHeight ? options.outHeight / 100
					: options.outWidth / 100;
			options.inSampleSize = (int) Math.pow(2d,
					Math.floor(Math.log(sampleSize) / Math.log(2d)));
		}

		// Do the actual decoding
		options.inJustDecodeBounds = false;
		options.inTempStorage = new byte[512];
		Bitmap output = BitmapFactory.decodeFile(filePath, options);

		return output;
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
}
