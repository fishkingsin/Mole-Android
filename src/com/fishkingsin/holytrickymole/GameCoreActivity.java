package com.fishkingsin.holytrickymole;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.IntBuffer;
import java.util.Calendar;

import javax.microedition.khronos.opengles.GL10;

import org.cocos2d.actions.ActionManager;
import org.cocos2d.actions.base.RepeatForever;
import org.cocos2d.actions.interval.IntervalAction;
import org.cocos2d.actions.interval.Sequence;
import org.cocos2d.actions.interval.TintBy;
import org.cocos2d.events.TouchDispatcher;
import org.cocos2d.layers.ColorLayer;
import org.cocos2d.layers.Layer;
import org.cocos2d.menus.MenuItemFont;
import org.cocos2d.menus.MenuItemImage;
import org.cocos2d.menus.MenuItemLabel;
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
			saveScreen();
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

            isTouchEnabled_ = true;
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(mContext);
			sprite = Sprite.sprite(prefs.getString(mContext.getString(R.string.keyImageName), "grossini.png"));
			sprite.setScale(s.width/sprite.getWidth());
			Layer layer = ColorLayer.node(new CCColor4B(93, 113, 112, 255));
			addChild(layer, -1);

			addChild(sprite, 0, kTagSprite);
			sprite.setPosition((int)(s.width*0.5), (int)(s.height*0.5));
			
			
			MenuItemFont.setFontSize(30);
            MenuItemFont.setFontName("DroidSansMono");
			 // Image Item
//            MenuItem item2 = MenuItemImage.item("SendScoreButton.png", "SendScoreButtonPressed.png", this, "menuCallback2");
//
//            Menu menu = Menu.menu(item2);
//            menu.alignItemsVertically();
//            addChild(menu);

		}

		 @Override
		 public boolean ccTouchesBegan(MotionEvent event) {
		
		
			 return TouchDispatcher.kEventHandled;
		 }
		@Override
		public boolean ccTouchesMoved(MotionEvent event) {
			// convert event location to CCPoint
			CCPoint p = Director.sharedDirector().convertToGL(event.getX(),
					event.getY());


			return true;

		}
		@Override
		public boolean ccTouchesEnded(MotionEvent event) {
	        return TouchDispatcher.kEventIgnored;  // TODO Auto-generated method stub
	    }


	}
	public static Bitmap SavePixels(int x, int y, int w, int h, GL10 gl)
	{  
	     int b[]=new int[w*(y+h)];
	     int bt[]=new int[w*h];
	     IntBuffer ib=IntBuffer.wrap(b);
	     ib.position(0);
	     gl.glReadPixels(x, 0, w, y+h, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, ib);

	     for(int i=0, k=0; i<h; i++, k++)
	     {//remember, that OpenGL bitmap is incompatible with Android bitmap
	      //and so, some correction need.        
	          for(int j=0; j<w; j++)
	          {
	               int pix=b[i*w+j];
	               int pb=(pix>>16)&0xff;
	               int pr=(pix<<16)&0x00ff0000;
	               int pix1=(pix&0xff00ff00) | pr | pb;
	               bt[(h-k-1)*w+j]=pix1;
	          }
	     }

	    Bitmap sb = Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888); 
	    return sb;
	}
	public void saveScreen()
	{
//		Bitmap bmp = SavePixels(0, 0, 800, 400, Director.sharedDirector().getOpenGLView());
//
//        File file = new File("/sdcard/test.jpg");
//        try
//        {
//            file.createNewFile();
//            FileOutputStream fos = new FileOutputStream(file);
//            bmp.compress(CompressFormat.JPEG, 100, fos);
//
//            Toast.makeText(getApplicationContext(), "Image Saved", 0).show();
//            Log.i("Menu Save Button", "Image saved as JPEG");
//        }
//
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }

        
	}
}
