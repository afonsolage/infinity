package com.lagecompany.infinity.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Player extends Actor {

	private static final int FRAMES = 6;
	private static final float FRAME_TIME = 0.1f;
	private static final float ANIM_TIME = FRAMES * FRAME_TIME;

	private Animation<TextureRegion> walkAnimation;
	private Texture walkSheet;

	float stateTime;
	float limit;

	//TODO: Develop sprite sheet map or something like that and use ragna sprite sheet for dev
	
	public Player() {
		walkSheet = new Texture(Gdx.files.internal("W_E.png"));

		TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth() / FRAMES, walkSheet.getHeight());
		TextureRegion[] walkFrames = tmp[0];

		walkAnimation = new Animation<>(FRAME_TIME, walkFrames);
		stateTime = 0;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		TextureRegion region = walkAnimation.getKeyFrame(stateTime);
		batch.draw(region, 50, 50);
	}

	@Override
	public void act(float delta) {
		stateTime += delta;
		if (stateTime > ANIM_TIME)
			stateTime = 0;
	}
}
