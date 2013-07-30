package com.fishkingsin.holytrickymole;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Director;
import org.cocos2d.opengl.CCGLSurfaceView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
public class TouchSceneActivity extends Activity{
	private CCGLSurfaceView mGLSurfaceView;

	  @Override
	 public void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  requestWindowFeature(Window.FEATURE_NO_TITLE);
	  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	    WindowManager.LayoutParams.FLAG_FULLSCREEN);
	  getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
	    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

	   mGLSurfaceView = new CCGLSurfaceView(this);
	  Director director = Director.sharedDirector();
	  director.attachInView(mGLSurfaceView);
	  setContentView(mGLSurfaceView);

	   // show FPS
	  Director.sharedDirector().setDisplayFPS(true);

	   // frames per second
	  Director.sharedDirector().setAnimationInterval(1.0f / 60f);

	   SimpleScene simpleScene = new SimpleScene();
	   Scene scene = Scene.node();
	  scene.addChild(simpleScene);
	  Director.sharedDirector().runWithScene(scene);
	 }

	  @Override
	 public void onStart() {
	  super.onStart();
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
	 public void onDestroy() {
	  super.onDestroy();
	  Director.sharedDirector().end();
	 }
}
