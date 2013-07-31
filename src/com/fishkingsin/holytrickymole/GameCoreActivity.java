package com.fishkingsin.holytrickymole;

import java.util.Calendar;

import org.cocos2d.actions.ActionManager;
import org.cocos2d.actions.base.RepeatForever;
import org.cocos2d.actions.interval.FadeIn;
import org.cocos2d.actions.interval.FadeOut;
import org.cocos2d.actions.interval.JumpTo;
import org.cocos2d.actions.interval.MoveTo;
import org.cocos2d.actions.interval.RotateTo;
import org.cocos2d.actions.interval.Sequence;
import org.cocos2d.events.TouchDispatcher;
import org.cocos2d.layers.ColorLayer;
import org.cocos2d.layers.Layer;
import org.cocos2d.nodes.CocosNode;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.Label;
import org.cocos2d.nodes.RenderTexture;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.nodes.TextureManager;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.types.CCColor4B;
import org.cocos2d.types.CCMacros;
import org.cocos2d.types.CCPoint;
import org.cocos2d.types.CCSize;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

public class GameCoreActivity extends Activity {
	private static final String LOG_TAG = GameCoreActivity.class
			.getSimpleName();

	private static final boolean DEBUG = true;

	private CCGLSurfaceView mGLSurfaceView;
	private static Context mContext;
//	private MainLayer mainLayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

//		new AlertDialog.Builder(this).setTitle("Welcome")
//				.setMessage("Click on the screen to move and rotate Grossini")
//				.setPositiveButton("Start", null).show();

		mGLSurfaceView = new CCGLSurfaceView(this);
		setContentView(mGLSurfaceView);
	}

	@Override
	public void onStart() {
		super.onStart();

		// attach the OpenGL view to a window
		Director.sharedDirector().attachInView(mGLSurfaceView);

		// set landscape mode
		Director.sharedDirector().setLandscape(false);

		// show FPS
		Director.sharedDirector().setDisplayFPS(true);

		// frames per second
		Director.sharedDirector().setAnimationInterval(1.0f / 60);

		Scene scene = Scene.node();
		//mainLayer = new MainLayer();
		scene.addChild(new MainLayer(), 2);

		// Make the Scene active
		Director.sharedDirector().runWithScene(scene);

	}

	@Override
	public void onPause() {
		super.onPause();

		Director.sharedDirector().pause();
	}

	@Override
	public void onResume() {
		super.onResume();

		Director.sharedDirector().resume();
	}
	@Override
    public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left,
				R.anim.slide_out_right);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();

		ActionManager.sharedManager().removeAllActions();
		TextureManager.sharedTextureManager().removeAllTextures();
		//mainLayer = null;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.gamecord_menu, menu);
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
			popUp.update(0, 0, (int)(currentView.getWidth() *0.7),
					currentView.getHeight());
			return true;
		case R.id.saveimage:
			//mainLayer.saveScreen();
			return true;
			
		case R.id.facebook:
//			mainLayer.saveScreen();
			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	static class MainLayer extends Layer {
		static final int kTagSprite = 1;
		Sprite sprite;
		RenderTexture target;
		public MainLayer() {
			CCSize s = Director.sharedDirector().winSize();
//            target = RenderTexture.renderTexture((int)s.width, (int)s.height);
//            addChild(target, 1);
            isTouchEnabled_ = true;
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(mContext);
			sprite = Sprite.sprite(prefs.getString(mContext.getString(R.string.keyImageName), "grossini.png"));
			
			Layer layer = ColorLayer.node(new CCColor4B(93, 113, 112, 255));
			addChild(layer, -1);

			addChild(sprite, 0, kTagSprite);
			sprite.setPosition(0, 0);
//			target = RenderTexture.renderTexture((int)s.width, (int)s.height);

			// sprite.runAction(JumpTo.action(4, 300, 48, 100, 4));
			//
			// layer.runAction(RepeatForever.action(Sequence.actions(FadeIn.action(1),
			// FadeOut.action(1))));
		}

		// @Override
		// public boolean ccTouchesBegan(MotionEvent event) {
		//
		//
		// return TouchDispatcher.kEventHandled;
		// }
		@Override
		public boolean ccTouchesMoved(MotionEvent event) {
			// convert event location to CCPoint
			CCPoint p = Director.sharedDirector().convertToGL(event.getX(),
					event.getY());

			// update the position of your sprite
			sprite.setPosition(p.x, p.y);

			return true;

		}
		
		public void saveScreen()
		{
			Calendar c = Calendar.getInstance();
			c.getTimeInMillis();
			//target.begin();
//			target.end();
//			target.saveBuffer(c.toString()+".png");
		}

	}
}
