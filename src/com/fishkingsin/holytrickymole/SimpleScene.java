package com.fishkingsin.holytrickymole;
import org.cocos2d.layers.*;
import org.cocos2d.nodes.*;
import org.cocos2d.types.*;

import android.view.MotionEvent;
public class SimpleScene extends Layer{
	Sprite touchingSprite;
	 CCSize winSize;

	  public SimpleScene() {
	  this.setIsTouchEnabled(true);
	  winSize = Director.sharedDirector().displaySize();
	  touchingSprite = Sprite.sprite("icon.png");
	  touchingSprite.setPosition(winSize.width / 2, winSize.height / 2);
	  addChild(touchingSprite);
	 }

	  @Override
	 public boolean ccTouchesMoved(MotionEvent event) {
	  CCPoint location = Director.sharedDirector().convertToGL(
	    event.getX(), event.getY());
	  touchingSprite.setPosition(location.x,location.y);
	  return super.ccTouchesMoved(event);
	 }
}
