package com.example.cardflip.opengles.animation;

import javax.microedition.khronos.opengles.GL10;

public interface Animation {
	void update(GL10 gl);
	
	void setListener(AnimationListener listener);
	
	void start();
	
	void restart();
}
