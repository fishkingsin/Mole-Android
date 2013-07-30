package com.fishkingsin.holytrickymole;

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
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.nodes.TextureManager;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.types.CCColor4B;
import org.cocos2d.types.CCMacros;
import org.cocos2d.types.CCPoint;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class GameCoreActivity extends Activity {
	private static final String LOG_TAG = GameCoreActivity.class
			.getSimpleName();

	private static final boolean DEBUG = true;

	private CCGLSurfaceView mGLSurfaceView;
	private static Context mContext;
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
	}

	static class MainLayer extends Layer {
		static final int kTagSprite = 1;
		Sprite sprite;

		public MainLayer() {

			isTouchEnabled_ = true;
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(mContext);
			sprite = Sprite.sprite(prefs.getString("keyImageName", "grossini.png"));
			sprite.setScaleX(sprite.getWidth()/this.getWidth());
			sprite.setScaleY(sprite.getWidth()/this.getWidth());
			Layer layer = ColorLayer.node(new CCColor4B(255, 255, 0, 255));
			addChild(layer, -1);

			addChild(sprite, 0, kTagSprite);
			sprite.setPosition(20, 150);

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

	}
}
