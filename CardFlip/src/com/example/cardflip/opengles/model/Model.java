package com.example.cardflip.opengles.model;

import javax.microedition.khronos.opengles.GL10;

import com.example.cardflip.opengles.animation.Animation;

public interface Model {
	
	void setAnimation(Animation animation);
	
	void update(GL10 gl);
	
	void draw(GL10 gl);
	
	void loadGLTexture(GL10 gl);

}
