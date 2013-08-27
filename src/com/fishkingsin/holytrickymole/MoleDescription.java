package com.fishkingsin.holytrickymole;

import org.cocos2d.nodes.CocosNode;
import org.cocos2d.nodes.Label;

public class MoleDescription extends CocosNode {
	private String description;
	protected MoleDescription(Label label , float x ,float y , String desc) {
		setDescription(desc);
		addChild(label);
		setPosition(x,y);
		// TODO Auto-generated constructor stub
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
