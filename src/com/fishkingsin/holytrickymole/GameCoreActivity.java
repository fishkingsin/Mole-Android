package com.fishkingsin.holytrickymole;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL10Ext;

import org.cocos2d.actions.ActionManager;
import org.cocos2d.actions.base.RepeatForever;
import org.cocos2d.actions.interval.IntervalAction;
import org.cocos2d.actions.interval.MoveTo;
import org.cocos2d.actions.interval.Sequence;
import org.cocos2d.actions.interval.TintBy;
import org.cocos2d.events.TouchDispatcher;
import org.cocos2d.layers.ColorLayer;
import org.cocos2d.layers.Layer;
import org.cocos2d.menus.*;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.LabelAtlas;
import org.cocos2d.nodes.RenderTexture;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.nodes.TextureManager;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.types.CCColor3B;
import org.cocos2d.types.CCColor4B;
import org.cocos2d.types.CCPoint;
import org.cocos2d.types.CCSize;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
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
import android.widget.Toast;

public class GameCoreActivity extends Activity {
	private static final String LOG_TAG = GameCoreActivity.class
			.getSimpleName();

	private static final boolean DEBUG = true;

	private CCGLSurfaceView mGLSurfaceView;
	private static Context mContext;
	private MainLayer mainLayer;
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
		mainLayer = new MainLayer();
		scene.addChild(mainLayer, 2);

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
		mainLayer = null;
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
			
			return true;
			
		case R.id.facebook:

			return true;
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	static class MainLayer extends Layer {
		static final int kTagSprite = 1;
		Sprite sprite, selSprite;	
		
		RenderTexture target;
		List<Sprite>moles;
		private boolean bSave = false;
		public MainLayer() {
			CCSize s = Director.sharedDirector().winSize();

            isTouchEnabled_ = true;
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(mContext);
			sprite = Sprite.sprite(prefs.getString(mContext.getString(R.string.keyImageName), "grossini.png"));
			float scale = s.width/sprite.getWidth();
			sprite.setScale(scale);
			
			
			
			Layer layer = ColorLayer.node(new CCColor4B(93, 113, 112, 255));
			addChild(layer, -1);

			addChild(sprite, 0, kTagSprite);
			sprite.setPosition((int)(s.width*0.5), (int)(s.height*0.5));
			moles = new ArrayList<Sprite>();
			for(int  i= 0 ;i < 10 ; i++)
			{
				moles.add(Sprite.sprite("mole01@2x.png"));
				
				moles.get(i).setPosition((int)(s.width*0.5), 0);
				addChild(moles.get(i), 0, i+1);
				moles.get(i).setScale(scale);
				moles.get(i).runAction(MoveTo.action(1.0f, (int)(s.width*0.5)+((i-5)*100) , s.height/2));
			}
			bSave = true;
		}

		 @Override
		 public boolean ccTouchesBegan(MotionEvent event) {
			 CCPoint touchPoint =  Director.sharedDirector().convertToGL(event.getX(),
						event.getY());
			 selectSpriteForTouch(touchPoint);
		
			 return TouchDispatcher.kEventHandled;
		 }
		@Override
		public boolean ccTouchesMoved(MotionEvent event) {
			// convert event location to CCPoint
			CCPoint p = Director.sharedDirector().convertToGL(event.getX(),
					event.getY());
			if(selSprite!=null)
			{
				selSprite.setPosition(p.x, p.y);
			}

			return true;

		}
		@Override
		public boolean ccTouchesEnded(MotionEvent event) {
			selSprite = null;
			
	        return TouchDispatcher.kEventIgnored;  // TODO Auto-generated method stub
	    }
		void selectSpriteForTouch( CCPoint touchLocation ){
		    Sprite newSprite = null;
		    
		    for (Sprite sprite : moles) {
		    	
		        if (sprite.getBoundingBox().contains(touchLocation.x,touchLocation.y)) {            
		            newSprite = sprite;
		            break;
		        }
		    }    
		    if (newSprite != selSprite) {
		                 
		        selSprite = newSprite;
		    }
		    
		}
		@Override
		public void draw(GL10 gl) {
	        // Do nothing by default
			if(bSave)
			{
				//TO-DO
//				CCSize s = Director.sharedDirector().winSize();
//				RenderTexture rt = RenderTexture.renderTexture((int)(s.width), (int)(s.height));
//				rt.begin();
//				rt.clear(0,0,0,1);
//				this.visit(gl);
//				rt.end();
//				rt.saveBuffer("/sdcard/test.jpg");
				bSave = false;
			}
			super.draw(gl);
	    }
	}
	

        
	
}
