package com.fishkingsin.holytrickymole;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.IntBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;
import org.cocos2d.actions.ActionManager;
import org.cocos2d.actions.interval.MoveTo;
import org.cocos2d.events.TouchDispatcher;
import org.cocos2d.layers.ColorLayer;
import org.cocos2d.layers.Layer;
import org.cocos2d.menus.*;
import org.cocos2d.nodes.CocosNode;
import org.cocos2d.nodes.Director;
import org.cocos2d.nodes.RenderTexture;
import org.cocos2d.nodes.Scene;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.nodes.TextureManager;
import org.cocos2d.opengl.CCGLSurfaceView;
import org.cocos2d.types.CCColor4B;
import org.cocos2d.types.CCPoint;
import org.cocos2d.types.CCSize;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.fishkingsin.holytrickymole.GameCoreActivity.MainLayer.MyListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

public class GameCoreActivity extends Activity implements OnCancelListener {
	private static final String LOG_TAG = GameCoreActivity.class
			.getSimpleName();
	private static final int SAVED_IMAGE = 0x13;

	private static final boolean DEBUG = true;

	private CCGLSurfaceView mGLSurfaceView;
	private static Context mContext;
	private MainLayer mainLayer;
	public static Bitmap bitmap;
	public static boolean bSaved = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// new AlertDialog.Builder(this).setTitle("Welcome")
		// .setMessage("Click on the screen to move and rotate Grossini")
		// .setPositiveButton("Start", null).show();
		getHashKey();
		mGLSurfaceView = new CCGLSurfaceView(this);

		setContentView(mGLSurfaceView);
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
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
		MyListener myListener = new MyListener() {

			@Override
			public void MyListenerDone() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						onClickPostPhoto();
					}
				});
			}

		};
		mainLayer = new MainLayer(myListener);
		scene.addChild(mainLayer, 2);

		// Make the Scene active
		Director.sharedDirector().runWithScene(scene);

	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
		Director.sharedDirector().pause();
	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
		Director.sharedDirector().resume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
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
		/*
		 * switch (item.getItemId()) { case R.id.credit: final PopupWindow popUp
		 * = new PopupWindow(this);
		 * 
		 * LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
		 * LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.TOP);
		 * final LinearLayout ll = new LinearLayout(this);
		 * ll.setLayoutParams(params); ll.setOrientation(LinearLayout.VERTICAL);
		 * 
		 * final ScrollView scrollview = new ScrollView(this); final TextView tv
		 * = new TextView(this);
		 * tv.setText(Html.fromHtml(getString(R.string.credit_text)));
		 * 
		 * tv.setMovementMethod(LinkMovementMethod.getInstance());
		 * scrollview.addView(tv, params);
		 * 
		 * ll.addView(scrollview);
		 * 
		 * popUp.setContentView(ll);
		 * 
		 * final View currentView = this.getWindow().getDecorView()
		 * .findViewById(android.R.id.content);
		 * popUp.showAtLocation(currentView, Gravity.BOTTOM, 0, 0);
		 * 
		 * popUp.setFocusable(false); popUp.setOutsideTouchable(true);
		 * popUp.setTouchable(true);
		 * 
		 * popUp.setTouchInterceptor(new OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) { if
		 * (event.getAction() == MotionEvent.ACTION_OUTSIDE) { popUp.dismiss();
		 * return true; } return false; }
		 * 
		 * }); popUp.update(0, 0, (int) (currentView.getWidth() * 0.7),
		 * currentView.getHeight()); return true; case R.id.saveimage:
		 * mainLayer.bSave = true; while(!bSaved) { try {
		 * 
		 * Thread.sleep(100); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } } bSaved = false;
		 * Calendar timestamp = Calendar.getInstance(); SimpleDateFormat format
		 * = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 * MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
		 * bitmap, format.format(timestamp.getTime()), ""); return true;
		 * 
		 * case R.id.facebook:
		 * 
		 * mainLayer.bSave = true;
		 * 
		 * 
		 * while(!bSaved) { try {
		 * 
		 * Thread.sleep(100); } catch (InterruptedException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } } bSaved = false;
		 * onClickPostPhoto(); // Intent intent = new Intent(this,
		 * FacebookShareActivity.class); // // intent.putExtra("BitmapImage",
		 * bitmap); // this.startActivity(intent);
		 * 
		 * return true;
		 * 
		 * default: return super.onOptionsItemSelected(item); }
		 */
		return super.onOptionsItemSelected(item);
	}

	static class MainLayer extends Layer {
		static final int kTagSprite = 1;

		Sprite sprite, selSprite;
		CocosNode mainNode;
		RenderTexture target;
		List<Sprite> moles;
		private boolean bSave = false;
		int curentMoleIndex = 0;

		public MainLayer(MyListener myListener) {
			CCSize s = Director.sharedDirector().winSize();
			mainNode = CocosNode.node();
			isTouchEnabled_ = true;
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(mContext);
			sprite = Sprite.sprite(prefs.getString(
					mContext.getString(R.string.keyImageName), "grossini.png"));
			float scale = s.width / sprite.getWidth();
			sprite.setScale(scale);

			Layer layer = ColorLayer.node(new CCColor4B(93, 113, 112, 255));
			mainNode.addChild(layer, -1);

			mainNode.addChild(sprite, 0, kTagSprite);
			sprite.setPosition((int) (s.width * 0.5), (int) (s.height * 0.5));
			moles = new ArrayList<Sprite>();
			for (int i = 0; i < 10; i++) {
				moles.add(Sprite.sprite("mole01@2x.png"));

				moles.get(i).setPosition((int) (s.width * 0.5), -50);
				mainNode.addChild(moles.get(i), 0, i + 1);
				moles.get(i).setScale(scale);
				// moles.get(i).runAction(
				// MoveTo.action(1.0f, (int) (s.width * 0.5)
				// + ((i - 5) * 100), s.height / 2));
			}

			addChild(mainNode);

			Sprite itemSprite1 = Sprite.sprite("button_short_normal@2x.png");
			Sprite itemSprite2 = Sprite.sprite("button_short_select@2x.png");
			Sprite itemSprite3 = Sprite.sprite("button_short_disable@2x.png");
			Sprite itemSprite4 = Sprite.sprite("button_short_normal@2x.png");
			Sprite itemSprite5 = Sprite.sprite("button_short_select@2x.png");
			Sprite itemSprite6 = Sprite.sprite("button_short_disable@2x.png");

			MenuItemSprite item1 = MenuItemAtlasSprite.item(itemSprite1,
					itemSprite2, itemSprite3, this, "addMole");
			MenuItemSprite item2 = MenuItemAtlasSprite.item(itemSprite4,
					itemSprite5, itemSprite6, this, "minusMole");
			MenuItemSprite item3 = MenuItemAtlasSprite.item(itemSprite4,
					itemSprite5, itemSprite6, this, "FacebookAction");

			org.cocos2d.menus.Menu menu = org.cocos2d.menus.Menu.menu(item3,
					item2, item1);
			// menu.alignItemsVertically();
			menu.alignItemsHorizontally(10);
			menu.setPosition(menu.getPositionX(), 0);
			addChild(menu);
			this.myListener = myListener;
		}

		public void FacebookAction() {
			bSave = true;

			// onClickPostPhoto();
			Log.d("MainLayer", "FacebookAction ");
		}

		public static interface MyListener {
			public void MyListenerDone();
		}

		private MyListener myListener;

		/**
		 * @return the parseListener
		 */
		public MyListener getListener() {
			return myListener;
		}

		/**
		 * @param parseListener
		 *            the parseListener to set
		 */
		public void setListener(MyListener myListener) {
			this.myListener = myListener;
		}

		public void addMole() {
			Log.d("MainLayer",
					"addMole curentMoleIndex:"
							+ String.valueOf(curentMoleIndex));

			if (curentMoleIndex < moles.size()) {
				CCSize s = Director.sharedDirector().winSize();

				moles.get(curentMoleIndex)
						.runAction(
								MoveTo.action(1.0f, (int) (s.width * 0.5),
										s.height / 2));
				curentMoleIndex++;
			}
			if (curentMoleIndex >= moles.size()) {
				curentMoleIndex = moles.size() - 1;
				// disable addButton;
			}

		}

		public void minusMole() {
			Log.d("MainLayer",
					"minusMole curentMoleIndex:"
							+ String.valueOf(curentMoleIndex));

			if (curentMoleIndex > -1) {
				// enable minusButton;
				CCSize s = Director.sharedDirector().winSize();
				if (moles.get(curentMoleIndex).isRunning())
					moles.get(curentMoleIndex).stopAllActions();
				moles.get(curentMoleIndex).setPosition((int) (s.width * 0.5),
						-50);
				curentMoleIndex--;
			}
			if (curentMoleIndex < 0) {
				curentMoleIndex = 0;
				// disable minusButton;
			}
		}

		@Override
		public boolean ccTouchesBegan(MotionEvent event) {
			CCPoint touchPoint = Director.sharedDirector().convertToGL(
					event.getX(), event.getY());
			selectSpriteForTouch(touchPoint);

			return TouchDispatcher.kEventHandled;
		}

		@Override
		public boolean ccTouchesMoved(MotionEvent event) {
			// convert event location to CCPoint
			CCPoint p = Director.sharedDirector().convertToGL(event.getX(),
					event.getY());
			if (selSprite != null) {
				selSprite.setPosition(p.x, p.y);
			}

			return true;

		}

		@Override
		public boolean ccTouchesEnded(MotionEvent event) {
			selSprite = null;

			return TouchDispatcher.kEventIgnored; // TODO Auto-generated method
													// stub
		}

		void selectSpriteForTouch(CCPoint touchLocation) {
			Sprite newSprite = null;

			for (Sprite sprite : moles) {

				if (sprite.getBoundingBox().contains(touchLocation.x,
						touchLocation.y)) {
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
			super.draw(gl);
			if (bSave) {
				try {

					CCSize s = Director.sharedDirector().winSize();
					mainNode.visit(gl);
					bitmap = SavePixels(0, 0, (int) (s.width),
							(int) (s.height), gl);
					saveBitmap(bitmap);
					myListener.MyListenerDone();

				} catch (Exception e) {
					Log.e("SaveImage", e.toString());
				}
				bSave = false;
			}

		}

		public void saveBitmap(Bitmap bmp) {

			// File file = new
			// File(Environment.getExternalStorageDirectory().getPath()+"test.jpg");
			File file = new File("/sdcard/test.jpg");
			try {
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				bmp.compress(CompressFormat.JPEG, 100, fos);

				bSaved = true;

				// Bundle mBundle = new Bundle();
				// Message msg = Message.obtain(mHandler, SAVED_IMAGE);
				// mBundle.putParcelable("Bitmap", bmp);
				// msg.setData(mBundle);
				// msg.sendToTarget();

				// Toast.makeText(mContext.getApplicationContext(),
				// "Image Saved", 0).show();
				// Log.i("Menu Save Button", "Image saved as JPEG");
			}

			catch (Exception e) {
				e.printStackTrace();
			}
		}

		public static Bitmap SavePixels(int x, int y, int w, int h, GL10 gl) {
			int b[] = new int[w * (y + h)];
			int bt[] = new int[w * h];
			IntBuffer ib = IntBuffer.wrap(b);
			ib.position(0);
			gl.glReadPixels(x, 0, w, y + h, GL10.GL_RGBA,
					GL10.GL_UNSIGNED_BYTE, ib);

			for (int i = 0, k = 0; i < h; i++, k++) {
				// remember, that OpenGL bitmap is incompatible with Android
				// bitmap
				// and so, some correction need.
				for (int j = 0; j < w; j++) {
					int pix = b[i * w + j];
					int pb = (pix >> 16) & 0xff;
					int pr = (pix << 16) & 0xffff0000;
					int pix1 = (pix & 0xff00ff00) | pr | pb;
					bt[(h - k - 1) * w + j] = pix1;
				}
			}

			Bitmap sb = Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);
			return sb;
		}
	}

	ProgressHUD mProgressHUD;

	private enum PendingAction {
		NONE, POST_PHOTO, POST_STATUS_UPDATE
	}

	private PendingAction pendingAction = PendingAction.NONE;
	private static final List<String> PERMISSIONS = Arrays
			.asList("publish_actions");
	private UiLifecycleHelper uiHelper;
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			// handle the callback of Facebook
			onSessionStateChange(session, state, exception);
			Log.d("Facebook", "Callback");
		}
	};

	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		Log.d("Facebook", "onSessionStateChange " + state.toString());
		if (pendingAction != PendingAction.NONE
				&& (exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException)) {
			// If the user wants to post photo or update status but the
			// permission is not granted by user
			new AlertDialog.Builder(GameCoreActivity.this)
					.setTitle("Fail")
					.setMessage(
							"Unable to perform selected action because permissions were not granted.")
					.setPositiveButton("Ok", null).show();
			pendingAction = PendingAction.NONE;
		} else if (state == SessionState.OPENED_TOKEN_UPDATED) {
			handlePendingAction();
		}
	}

	private void handlePendingAction() {
		Log.d("Facebook", "handlePendingAction");
		PendingAction previouslyPendingAction = pendingAction;
		// These actions may re-set pendingAction if they are still pending, but
		// we assume they
		// will succeed.
		pendingAction = PendingAction.NONE;

		switch (previouslyPendingAction) {
		case POST_PHOTO:
			postPhoto();
			break;
		case POST_STATUS_UPDATE:
			postText();
			break;
		case NONE:
			// Do Nothing
			break;
		default:
			break;
		}
	}

	// @SuppressWarnings("unused")
	private void getHashKey() {
		// Use to retrieve the hash key needed for the facebook app. Compiling
		// and the actual apk have different hash key.
		PackageInfo info;
		try {
			info = getPackageManager().getPackageInfo(
					"com.fishkingsin.holytrickymole",
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md;
				md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				String something = new String(Base64.encode(md.digest(), 0));
				Log.e("hash key", something);
			}
		} catch (NameNotFoundException e1) {
			Log.e("name not found", e1.toString());
		} catch (NoSuchAlgorithmException e) {
			Log.e("no such an algorithm", e.toString());
		} catch (Exception e) {
			Log.e("exception", e.toString());
		}
	}

	private void facebookLogin() {
		Log.d("Facebook", "Start Facebook Login");
		Session.openActiveSession(this, true, new Session.StatusCallback() {

			// callback when session changes state
			@Override
			public void call(Session session, SessionState state,
					Exception exception) {
				if (session.isOpened()) {
					// make request to the /me API
					Request.executeMeRequestAsync(session,
							new Request.GraphUserCallback() {

								// callback after Graph API response with user
								// object
								@Override
								public void onCompleted(GraphUser user,
										Response response) {
									if (user != null) {
										Log.d("Facebook",
												"Login Sucessful with Username:"
														+ user.getName());
									}
								}
							});
				}
			}
		});
	}

	private void facebookLogout() {
		Log.d("Facebook", "Start Facebook Logout");
		if (Session.getActiveSession() != null) {
			Session.getActiveSession().closeAndClearTokenInformation();
		}

		Session.setActiveSession(null);
		Log.d("Facebook", "Logout");

	}

	private void onClickPostStatusUpdate() {
		performPublish(PendingAction.POST_STATUS_UPDATE);
	}

	private void onClickPostPhoto() {
		performPublish(PendingAction.POST_PHOTO);
	}

	private void postPhoto() {
		if (hasPublishPermission()) {
			// Bitmap image = BitmapFactory.decodeResource(this.getResources(),
			// R.drawable.icon);
			Request request = Request.newUploadPhotoRequest(
					Session.getActiveSession(), bitmap, new Request.Callback() {
						@Override
						public void onCompleted(Response response) {
							showPublishResult("Photo Post",
									response.getGraphObject(),
									response.getError());
						}
					});
			Bundle params = request.getParameters();
			params.putString("message", "description  goes here");
			
			request.executeAsync();
		} else {
			pendingAction = PendingAction.POST_PHOTO;
		}

		// if (hasPublishPermission()) {
		// mProgressHUD = ProgressHUD.show(GameCoreActivity.this,"Posting",
		// true,true,this);
		// Request request =
		// Request.newUploadPhotoRequest(Session.getActiveSession(), bitmap, new
		// Request.Callback() {
		// @Override
		// public void onCompleted(Response response) {
		// showPublishResult("Photo Post", response.getGraphObject(),
		// response.getError());
		// }
		// });
		// request.executeAsync();
		// }
	}

	private void postText() {
		if (hasPublishPermission()) {

			mProgressHUD = ProgressHUD.show(GameCoreActivity.this, "Posting",
					true, true, this);
			final String message = ((EditText) findViewById(R.id.facebook_share_textfield))
					.getText().toString();
			Request request = Request.newStatusUpdateRequest(
					Session.getActiveSession(), message,
					new Request.Callback() {
						@Override
						public void onCompleted(Response response) {
							showPublishResult(message,
									response.getGraphObject(),
									response.getError());
						}
					});
			request.executeAsync();
		}
	}

	private interface GraphObjectWithId extends GraphObject {
		String getId();
	}

	private void showPublishResult(String message, GraphObject result,
			FacebookRequestError error) {
		String title = null;
		String alertMessage = null;
		if (error == null) {
			title = "Success";
			String id = result.cast(GraphObjectWithId.class).getId();
			alertMessage = "Sucessfully posted";// getString(R.string.successfully_posted_post,
												// message, id);
		} else {
			title = "Error";
			alertMessage = error.getErrorMessage();
		}

		new AlertDialog.Builder(this).setTitle(title).setMessage(alertMessage)
				.setPositiveButton("OK", null).show();
	}

	private void performPublish(PendingAction action) {
		Session session = Session.getActiveSession();

		pendingAction = action;
		if (session != null) {
			if (session.isOpened()) {
				if (hasPublishPermission()) {
					// We can do the action right away.
					handlePendingAction();
				} else {
					// We need to get new permissions, then complete the action
					// when we get called back.
					session.requestNewPublishPermissions(new Session.NewPermissionsRequest(
							this, PERMISSIONS));
				}
			} else {
				facebookLogin();
			}
		} else {
			facebookLogin();
		}
	}

	// private boolean hasPublishPermission() {
	// Session session = Session.getActiveSession();
	// return session != null &&
	// session.getPermissions().contains("publish_actions");
	// }
	private boolean hasPublishPermission() {
		Session session = Session.getActiveSession();
		return session != null
				&& session.getPermissions().contains("publish_actions");
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub
		mProgressHUD.dismiss();
	}

}
