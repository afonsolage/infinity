package com.lagecompany.infinity.environment.terrain;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.lagecompany.infinity.logic.terrain.TerrainBuffer;

public class TileCell extends Actor {

	// TODO: Add other business data
	private final Sprite sprite;
	private final TileType type;
	private final int index;

	private final float z;

	public TileCell(int index, TileType type, Sprite sprite) {
		this.index = index;
		this.type = type;
		this.sprite = sprite;

		Vector3 isometric = IsometricTileMap.toIsometric(TerrainBuffer.toPosition(index));
		z = isometric.z;
		setPosition(isometric.x, isometric.y);
	}

	public float getZ() {
		return z;
	}

	public TileType getType() {
		return type;
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
}
