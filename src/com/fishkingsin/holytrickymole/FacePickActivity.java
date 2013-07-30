package com.fishkingsin.holytrickymole;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class FacePickActivity extends FragmentActivity implements
		OnTabChangeListener {
	private String[] maleImageName={
			"donald_holy-tricky_male@2x.png",
			"ks_holy-tricky_male@2x.png",
			"lbt_holy-tricky_male@2x.png",
			"littlethunder_holy-tricky_male@x2.png",
			"peter_holy-tricky_male@2x.png",
			"sh_holy-tricky_male@2x.png",
			"tse_holy-tricky_male@x2.png",
	};
	private String[] femaleImageName={
			"donald_holy-tricky_female@2x.png",
			"ks_holy-tricky_female@2x.png",
			"lbt_holy-tricky_female@2x.png",
			"littlethunder_holy-tricky_female@x2.png",
			"peter_holy-tricky_female@2x.png",
			"sh_holy-tricky_female@2x.png",
			"tse_holy-tricky_female@2x.png",
			};
	private Integer[] mImageMaleIds = {
	R.drawable.donald_holy_tricky_male_thumb,
	R.drawable.ks_holy_tricky_male_thumb,
	R.drawable.lbt_holy_tricky_male_thumb,
	R.drawable.littlethunder_holy_tricky_male_thumb,
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
		mTabHost.setOnTabChangedListener(this);
		gridview1 = (GridView) findViewById(R.id.gridview1);
		gridview1.setAdapter(new ImageAdapter(this, mImageMaleIds));

		gridview1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(mContext);
				SharedPreferences.Editor editor = prefs.edit();
				editor.putString("keyImageName", maleImageName[position]);

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
		overridePendingTransition(R.anim.slide_in_left,
				R.anim.slide_out_right);
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
	public void onTabChanged(String tabId) {

	}
}
