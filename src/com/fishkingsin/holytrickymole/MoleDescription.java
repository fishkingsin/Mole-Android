package com.fishkingsin.holytrickymole;

import org.cocos2d.nodes.CocosNode;
import org.cocos2d.nodes.Label;
import org.cocos2d.nodes.Sprite;
import org.cocos2d.opengl.Texture2D;

public class MoleDescription extends CocosNode {
String description;
	protected MoleDescription(Label label , float x ,float y , String desc) {
		description = desc;
		addChild(label);
		setPosition(x,y);
		// TODO Auto-generated constructor stub
	}

}
