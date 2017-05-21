package com.example.cardflip;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.cardflip.opengles.Renderer;
import com.example.cardflip.opengles.animation.AnimationListener;
import com.example.cardflip.opengles.animation.CardFlipAnimation;
import com.example.cardflip.opengles.model.Card;

public class MainActivity extends Activity {

	private GLSurfaceView glSurfaceView;
	private Renderer renderer;
	
	private Card card;
	private CardFlipAnimation flip;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/******************************************************************************************
		 * THE INPUTS
		 *****************************************************************************************/
		Bitmap frontBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.face);
		Bitmap backBitmap  = BitmapFactory.decodeResource(this.getResources(), R.drawable.back);
		
		int cardWidth     		= frontBitmap.getWidth();
		int cardHeight    		= frontBitmap.getHeight();
		int z 			  		= -900;
		float angleInit   	 	= 180;
		float angleEnd 	  		= 360;
		long animationDuration  = 2500;
		
		
		/******************************************************************************************
		 * THE APPLICATION
		 *****************************************************************************************/
		this.flip = new CardFlipAnimation(angleInit, angleEnd, animationDuration);
		this.flip.setLeftToRight();
		
		this.flip.setListener(new AnimationListener() {
			
			@Override
			public void onComplete() {
				flip.restart();
			}
		});
		this.flip.start();
		
		this.card = new Card(frontBitmap, backBitmap, cardWidth, cardHeight);
		this.card.setAnimation(flip);
		this.card.setPosition(0, 0, z);
		
		this.renderer = new Renderer();
		this.renderer.addModel(this.card);
		
		this.glSurfaceView = new GLSurfaceView(this);
		this.glSurfaceView.setRenderer(this.renderer);
		
   		setContentView(this.glSurfaceView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
