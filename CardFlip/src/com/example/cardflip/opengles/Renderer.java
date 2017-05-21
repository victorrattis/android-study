package com.example.cardflip.opengles;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import com.example.cardflip.opengles.model.Model;

public class Renderer implements GLSurfaceView.Renderer {
	private ArrayList<Model> models;

	public Renderer() {
		this.models = new ArrayList<Model>();
	}

	public void addModel(Model model) {
		if (model != null) {
			this.models.add(model);
		}
	}
	
	/**********************************************************************************************
	 * Override GLSurfaceView.Renderer
	 *********************************************************************************************/
	
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glEnable(GL10.GL_TEXTURE_2D); // Enable Texture Mapping ( NEW )
		gl.glShadeModel(GL10.GL_SMOOTH); // Enable Smooth Shading

		gl.glClearColor(0.86f, 0.86f, 0.86f, 1.0f); // Black Background
		gl.glClearDepthf(1.0f); // Depth Buffer Setup

		gl.glEnable(GL10.GL_DEPTH_TEST); // Enables Depth Testing
		gl.glDepthFunc(GL10.GL_LEQUAL); // The Type Of Depth Testing To Do

		// Really Nice Perspective Calculations
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
		
		gl.glEnable(GL10.GL_CULL_FACE);
		
		for (Model model : models) {
			model.loadGLTexture(gl);
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL10.GL_TEXTURE);   //Select The Projection Matrix
        gl.glLoadIdentity();                    //Reset The Projection Matrix
        
		gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45.0f, (float)width / (float)height, 0.1f, 10000.0f);
	}

	@Override
	public void onDrawFrame(GL10 gl) {
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		
		gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        
		for (Model model : models) {
			model.update(gl);
			
			model.draw(gl);
		}
	}
}
