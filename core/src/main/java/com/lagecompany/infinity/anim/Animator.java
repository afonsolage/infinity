package com.lagecompany.infinity.anim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Json;
import com.lagecompany.infinity.anim.AnimationData.AnimationDataRegion;

public class Animator extends Actor {
	private static final String LOG_TAG = Animator.class.getSimpleName();

	private final Map<String, Animation<TextureRegion>> animationMap;
	private String currentAnimationName;
	private Animation<TextureRegion> currentAnimation;
	private float stateTime;

	public Animator() {
		this.animationMap = new HashMap<>();
	}

	public String addAnimation(String path) {
		try {
			path = "anim/" + path + ".anim";
			FileHandle fileHandle = Gdx.files.internal(path);
			Json json = new Json();
			AnimationData data = json.fromJson(AnimationData.class, fileHandle);

			if (data == null) {
				Gdx.app.error(LOG_TAG, "Failed to parse animation file " + path);
			} else {
				Texture texture = new Texture(Gdx.files.internal(data.getTexturePath()));

				List<TextureRegion> regions = new ArrayList<>();
				for (AnimationDataRegion dataRegion : data.getRegions()) {
					TextureRegion region = new TextureRegion(texture, dataRegion.getX(), dataRegion.getY(),
							dataRegion.getWidth(), dataRegion.getHeight());

					if (data.isFlip())
						region.flip(true, false);

					regions.add(region);
				}

				Animation<TextureRegion> animation = new Animation<TextureRegion>(data.getFrameTime(),
						regions.toArray(new TextureRegion[regions.size()]));
				animation.setPlayMode((data.isLoop() ? PlayMode.LOOP : PlayMode.NORMAL));
				animationMap.put(data.getName(), animation);
				return data.getName();
			}
		} catch (Exception e) {
			Gdx.app.error(LOG_TAG, "Failed to load animation at path " + path, e);
		}
		return null;
	}

	public boolean playAnimation(String name) {
		if (name == null || name.equalsIgnoreCase(currentAnimationName))
			return false;

		Animation<TextureRegion> animation = animationMap.get(name);

		if (animation == null)
			return false;

		currentAnimationName = name;
		currentAnimation = animation;
		stateTime = 0f;
		return true;
	}

	public String getCurrentAnimationName() {
		return currentAnimationName;
	}

	public void stopAnimation() {
		currentAnimation = null;
		currentAnimationName = null;
	}

	public boolean isPlaying() {
		return currentAnimation != null;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (currentAnimation == null)
			return;

		TextureRegion region = currentAnimation.getKeyFrame(stateTime);

		batch.draw(region, getX(), getY(), region.getRegionX(), region.getRegionY(), region.getRegionWidth(),
				region.getRegionHeight(), getScaleX(), getScaleY(), getRotation());
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		stateTime += delta;
	}

}
