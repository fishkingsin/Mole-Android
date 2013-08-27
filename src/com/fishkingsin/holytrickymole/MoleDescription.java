package com.fishkingsin.holytrickymole;

import org.cocos2d.nodes.CocosNode;
import org.cocos2d.nodes.Label;

public class MoleDescription extends CocosNode {
String description;
	protected MoleDescription(Label label , float x ,float y , String desc) {
		description = desc;
		addChild(label);
		setPosition(x,y);
		// TODO Auto-generated constructor stub
	}

}
