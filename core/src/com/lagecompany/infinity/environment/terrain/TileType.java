package com.lagecompany.infinity.environment.terrain;

import com.badlogic.gdx.Gdx;

public enum TileType {

	NONE,
	
	GRASS, DIRT, ROCK,

	;

	public static final int TILE_WIDTH = 128;
	public static final int TILE_HEIGHT = 64;
	public static final int TILE_HEIGHT_OFFSET = 20;
	
	public static final int TILE_WIDTH_HALF = TILE_WIDTH / 2;
	public static final int TILE_HEIGHT_HALF = TILE_HEIGHT / 2;

	private static final TileType[] values = TileType.values();

	public String toString() {
		return name().toLowerCase();
	}

	public static TileType getById(int id) {
		if (id < 0 || id >= values.length) {
			Gdx.app.error(TileType.class.getSimpleName(), "Invalid ID: " + id);
			return TileType.NONE;
		} else {
			return values[id];
		}
	}
}
