package com.example.cardflip.opengles.animation;

import javax.microedition.khronos.opengles.GL10;

public class CardFlipAnimation implements Animation {

	private AnimationListener listener;

	private float angleInit;

	private float angleEnd;

	private float angle;

	private boolean isExecution;

	private long timeStart;

	private long timeEnd;

	private long duration;

	private int direction;

	public CardFlipAnimation(float angleInit, float angleEnd, long duration) {
		this.angleInit = angleInit;
		this.angleEnd = angleEnd;
		this.angle = angleInit;
		this.duration = duration;
		this.isExecution = false;
		this.direction = 1;
		this.timeStart = -1;
	}

	public void setRightToLeft() {
		this.direction = 1;
	}

	public void setLeftToRight() {
		this.direction = -1;
	}

	@Override
	public void update(GL10 gl) {
		if (this.isExecution) {
			if (this.timeStart == -1) {
				this.timeStart = System.currentTimeMillis();
				this.timeEnd = this.timeStart + this.duration;
			}

			long current = System.currentTimeMillis();
			if (current <= this.timeEnd) {
				float delta = this.angleEnd - this.angleInit;
				float percetage = (float) (current - this.timeStart) / (float) this.duration;

				this.angle = this.angleInit + (percetage * delta);
			} else {
				this.isExecution = false;

				if (this.listener != null) {
					this.listener.onComplete();
				}
			}
		}

		gl.glRotatef(this.angle, 0, this.direction, 0);
	}

	@Override
	public void setListener(AnimationListener listener) {
		this.listener = listener;
	}

	@Override
	public void start() {
		this.isExecution = true;
	}

	@Override
	public void restart() {
		this.timeStart = -1;
		this.start();
	}
}
