package com.fishkingsin.holytrickymole;

import android.app.Activity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class Utilities {
	static PopupWindow setupPopWindow(String text , Activity activity)
	{
		final PopupWindow popUp = new PopupWindow(activity);
		final View currentView = activity.getWindow().getDecorView().findViewById(android.R.id.content);	
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		final RelativeLayout rl = new RelativeLayout(activity);
		rl.setLayoutParams(params);
		//ll.setOrientation(LinearLayout.VERTICAL);
	
		final ScrollView scrollview = new ScrollView(activity);
		final TextView tv= new TextView(activity);
		tv.setText(Html.fromHtml(text));
	
		tv.setMovementMethod(LinkMovementMethod.getInstance());
		LayoutParams sparams = new LayoutParams(LayoutParams.MATCH_PARENT, (int) (currentView.getHeight() * 0.8));
		scrollview.addView(tv, sparams);
		scrollview.setLayoutParams(new ViewGroup.LayoutParams(480, 800));
		rl.addView(scrollview);
		
		final LinearLayout hl = new LinearLayout(activity);
		hl.setLayoutParams(params);
		hl.setOrientation(LinearLayout.HORIZONTAL);
		Button button1 = new Button(activity);
		button1.setText("Cancel");
		button1.setGravity(Gravity.BOTTOM);
		Button button2 = new Button(activity);
		button2.setText("Facebook");
		button2.setGravity(Gravity.BOTTOM);
		Button button3 = new Button(activity);
		button3.setText("Save");
		button3.setGravity(Gravity.BOTTOM);
		hl.addView(button1);
		hl.addView(button2);
		hl.addView(button3);
		hl.setGravity(Gravity.BOTTOM);
		rl.addView(hl);
	
		popUp.setContentView(rl);
	
		
		popUp.showAtLocation(currentView, Gravity.BOTTOM, 0, 0);
	
		popUp.setFocusable(false); popUp.setOutsideTouchable(true);
		popUp.setTouchable(true);
	
//		popUp.setTouchInterceptor(new OnTouchListener() {
	
//		@Override public boolean onTouch(View v, MotionEvent event) 
//		{
//			if(event.getAction() == MotionEvent.ACTION_OUTSIDE) { 
//				popUp.dismiss();
//				return true; 
//		}
//			return false; 
//			}
//		
//		}); 
		popUp.update(0, 0, (int) (currentView.getWidth() * 0.8),currentView.getHeight()); 
		return popUp;
	}
	static PopupWindow setupCreditPopWindow(String text , Activity activity)
	{
		final PopupWindow popUp = new PopupWindow(activity);
		final View currentView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.TOP);
		final FrameLayout fl = new FrameLayout(activity);
		fl.setLayoutParams(params);
		
	
		final ScrollView scrollview = new ScrollView(activity);
		final TextView tv= new TextView(activity);
		tv.setText(Html.fromHtml(text));
	
		tv.setMovementMethod(LinkMovementMethod.getInstance());
		LinearLayout.LayoutParams sparams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (int) (currentView.getHeight() * 0.8), Gravity.TOP);
		scrollview.addView(tv, sparams);
	
		fl.addView(scrollview);
		
	
		popUp.setContentView(fl);
	
		
		popUp.showAtLocation(currentView, Gravity.BOTTOM, 0, 0);
	
		popUp.setFocusable(false); popUp.setOutsideTouchable(true);
		popUp.setTouchable(true);
	
		popUp.setTouchInterceptor(new OnTouchListener() {
	
		@Override public boolean onTouch(View v, MotionEvent event) 
		{
			if(event.getAction() == MotionEvent.ACTION_OUTSIDE) { 
				popUp.dismiss();
				
				return true; 
		}
			return false; 
			}
		
		}); 
		popUp.update(0, 0, (int) (currentView.getWidth() * 0.7),currentView.getHeight());
		return popUp;
	}
}	
