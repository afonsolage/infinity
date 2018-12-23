package com.lagecompany.infinity.environment.terrain;

import java.util.TreeMap;
import java.util.function.Predicate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.hash.TIntObjectHashMap;

public class IsometricTileMap extends Actor {
	private static final String LOG_TAG = IsometricTileMap.class.getSimpleName();
	public static final int WIDTH = 20;
	public static final int HEIGHT = 20;

	private TextureAtlas atlas;
	private TIntObjectHashMap<TileCell> cells;
	private TreeMap<Integer, TileCell> renderingOrder;
	private boolean dirtOrder;

	public IsometricTileMap() {
		cells = new TIntObjectHashMap<>(WIDTH * HEIGHT);
		renderingOrder = new TreeMap<>();

		atlas = new TextureAtlas(Gdx.files.internal("atlas/terrain.atlas"));

		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				createCellAt(x, y);
			}
		}
	}

	public void createCellAt(int x, int y) {
		int index = toIndex(x, y);
		if (cells.contains(index)) {
			throw new RuntimeException("Failed to create cell at position " + x + "," + "y" + ". Cell already exists");
		}

		// TODO: Create some noise generation later on
		TileType type = (x == 0 || y == 0 || x == WIDTH - 1 || y == HEIGHT - 1) ? TileType.ROCK : TileType.GRASS;
		Sprite sprite = atlas.createSprite(type.toString());
		TileCell cell = new TileCell(index, sprite);
		Vector2 pos = new Vector2(x, y);
		pos = toIsometric(pos);
		cell.setPosition(pos.x, pos.y);
		cells.put(index, cell);

		dirtOrder = true;
	}

	public void destroyCellAt(int x, int y) {
		TileCell removed = cells.remove(toIndex(x, y));

		if (removed == null) {
			Gdx.app.log(LOG_TAG, "Failed to destroy cell at " + x + ", " + y + ". Cell not found.");
		} else {
			dirtOrder = true;
			removed.dispose();
		}
	}

	public int getCellCount() {
		return cells.size();
	}

	public TileCell findCell(Predicate<TileCell> predicate) {
		TIntObjectIterator<TileCell> it = cells.iterator();

		while (it.hasNext()) {
			it.advance();
			if (it.value() != null && predicate.test(it.value()))
				return it.value();
		}

		return null;
	}

	public void dispose() {
		TIntObjectIterator<TileCell> it = cells.iterator();

		while (it.hasNext()) {
			it.advance();
			if (it.value() != null)
				it.value().dispose();
		}

		cells.clear();
		atlas.dispose();
	}

	@Override
	public void act(float delta) {
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		if (dirtOrder)
			computeRenderingOrder();

		for (TileCell cell : renderingOrder.values()) {
			cell.draw(batch, parentAlpha);
		}
	}

	private void computeRenderingOrder() {
		renderingOrder.clear();
		TIntObjectIterator<TileCell> it = cells.iterator();

		while (it.hasNext()) {
			it.advance();
			TileCell cell = it.value();
			if (cell != null) {
				renderingOrder.put(cell.getOrder(), cell);
			}
		}
	}

	public static int toIndex(float x, float y) {
		// We'll use row-major to store data in cell map
		return (int) y * WIDTH + (int) x;
	}

	public static Vector2 toPosition(int index) {
		return new Vector2((int) (index % WIDTH), (int) (index / WIDTH));
	}

	public static Vector2 toIsometric(Vector2 pos) {
		float x = pos.x;
		float y = pos.y;
		pos.x = (x - y) * TileType.TILE_WIDTH_HALF;
		pos.y = (x + y) * TileType.TILE_HEIGHT_HALF;
		return pos;
	}
}
