package com.example.cardflip.opengles.model;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

import com.example.cardflip.opengles.animation.Animation;

public class Card implements Model {
	
	private final int FRONT_FACE_INDEX = 0;
	private final int BACK_FACE_INDEX = 1;
	
	private FloatBuffer vertexBuffer;
	private FloatBuffer textureBuffer;

	private int numberOfVertices = 0;

	private int[] textures = new int[2];

	private Bitmap frontFaceBtimap;
	private Bitmap backFaceBitmap;

	private Animation animation;

	private float cardWidth;
	private float cardHeight;
	
	private float x, y, z;

	public Card(Bitmap frontBitmap, Bitmap backBitmap, float width, float height) {
		this.cardWidth = width;
		this.cardHeight = height;
		
		initialize();

		this.frontFaceBtimap = frontBitmap;
		this.backFaceBitmap = backBitmap;
	}
	
	public void setPosition(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public void setAnimation(Animation animation) {
		this.animation = animation;
	}

	@Override
	public void loadGLTexture(GL10 gl) {
		gl.glGenTextures(2, textures, 0);

		loadTexture(gl, textures[FRONT_FACE_INDEX], this.frontFaceBtimap);
		loadTexture(gl, textures[BACK_FACE_INDEX], this.backFaceBitmap);
	}

	@Override
	public void draw(GL10 gl) {
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[FRONT_FACE_INDEX]);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

		gl.glFrontFace(GL10.GL_CW);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, numberOfVertices);

		gl.glMatrixMode(GL10.GL_TEXTURE);
		gl.glLoadIdentity();
		gl.glScalef(-1.0f, 1.0f, 1.0f);

		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[BACK_FACE_INDEX]);

		gl.glFrontFace(GL10.GL_CCW);
		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, numberOfVertices);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
	}

	@Override
	public void update(GL10 gl) {
		gl.glTranslatef(this.x, this.y, this.z);

		if (animation != null) {
			animation.update(gl);
		}
	}

	private void initialize() {
		float halfWidth  = this.cardWidth  / 2;
		float halfHeight = this.cardHeight / 2;

		float vertices[] = { 
			-halfWidth, -halfHeight, 0.0f, 
			-halfWidth,  halfHeight, 0.0f,
			 halfWidth, -halfHeight, 0.0f, 
			 halfWidth,  halfHeight, 0.0f 
		};
		numberOfVertices = vertices.length / 3;

		float texture[] = { 
			0.0f, 1.0f, 
			0.0f, 0.0f, 
			1.0f, 1.0f, 
			1.0f, 0.0f 
		};

		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		vertexBuffer = byteBuffer.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		byteBuffer = ByteBuffer.allocateDirect(texture.length * 4);
		byteBuffer.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuffer.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
	}

	private void loadTexture(GL10 gl, int textureID, Bitmap textureData) {
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureID);

		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S, GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T, GL10.GL_REPEAT);

		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, textureData, 0);
	}
}
