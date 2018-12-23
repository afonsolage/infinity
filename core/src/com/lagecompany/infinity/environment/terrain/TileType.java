package com.lagecompany.infinity.environment.terrain;

public enum TileType {

	GRASS, DIRT, ROCK,

	NONE;

	public static final int TILE_WIDTH = 128;
	public static final int TILE_HEIGHT = 64;
	public static final int TILE_WIDTH_HALF = TILE_WIDTH / 2;
	public static final int TILE_HEIGHT_HALF = TILE_HEIGHT / 2;

	public String toString() {
		return name().toLowerCase();
	}
}
