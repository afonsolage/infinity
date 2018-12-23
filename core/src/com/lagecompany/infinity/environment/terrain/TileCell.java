package com.lagecompany.infinity.environment.terrain;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class TileCell extends Actor {

	// TODO: Add other business data
	private final Sprite sprite;
	private final int index;

	public TileCell(int index, Sprite sprite) {
		this.sprite = sprite;
		this.index = index;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public int getIndex() {
		return index;
	}

	@Override
	protected void positionChanged() {
		sprite.setPosition(getX(), getY());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		sprite.draw(batch);
	}

	@Override
	public String toString() {
		return "TileCell [index=" + index + ", name=" + getSprite() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TileCell other = (TileCell) obj;
		if (index != other.index)
			return false;
		return true;
	}

	public void dispose() {
	}

	public int getOrder() {
		return (int)getY() * -10000 - (int)getX();
	}
}
