package com.lagecompany.infinity.anim;

import java.util.ArrayList;
import java.util.List;

public class AnimationData {
	public static class AnimationDataRegion {
		private int x;
		private int y;
		private int width;
		private int height;

		public AnimationDataRegion() {

		}

		public AnimationDataRegion(final int x, final int y, final int width, final int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		public int getX() {
			return x;
		}

		public void setX(final int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(final int y) {
			this.y = y;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(final int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(final int height) {
			this.height = height;
		}

	}

	private String name;
	private String texturePath;
	private float frameTime;
	private boolean flip;
	private boolean loop;
	private List<AnimationDataRegion> regions;

	public AnimationData() {
		regions = new ArrayList<>();
	}

	public AnimationData(String name, String texturePath, float frameTime, boolean flip, boolean loop) {
		this();
		this.name = name;
		this.texturePath = texturePath;
		this.frameTime = frameTime;
		this.flip = flip;
		this.loop = loop;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTexturePath() {
		return texturePath;
	}

	public void setTexturePath(String texturePath) {
		this.texturePath = texturePath;
	}

	public float getFrameTime() {
		return frameTime;
	}

	public void setFrameTime(float frameTime) {
		this.frameTime = frameTime;
	}

	public boolean isFlip() {
		return flip;
	}

	public void setFlip(boolean flip) {
		this.flip = flip;
	}

	public boolean isLoop() {
		return loop;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public void addRegion(int x, int y, int width, int height) {
		regions.add(new AnimationDataRegion(x, y, width, height));
	}

	public void setRegions(List<AnimationDataRegion> regions) {
		this.regions = regions;
	}

	public List<AnimationDataRegion> getRegions() {
		return regions;
	}
}
