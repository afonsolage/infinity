package com.lagecompany.infinity.environment.terrain;

import java.util.ArrayList;
import java.util.function.Predicate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.lagecompany.infinity.logic.terrain.CellRef;
import com.lagecompany.infinity.logic.terrain.TerrainBuffer;

public class IsometricTileMap extends Actor {
	private static final String LOG_TAG = IsometricTileMap.class.getSimpleName();

	private final TextureAtlas atlas;
	private final TerrainBuffer buffer;
	private final TileCell[] cells;

	public IsometricTileMap(TerrainBuffer buffer) {
		if (buffer == null || !buffer.isAllocated()) {
			logAndThrow("Invalid buffed received");
		}

		this.buffer = buffer;
		this.cells = new TileCell[TerrainBuffer.BUFFER_LENGTH];
		this.atlas = new TextureAtlas(Gdx.files.internal("atlas/terrain.atlas"));
	}

	private void logAndThrow(String errorMessage) {
		RuntimeException exception = new RuntimeException(errorMessage);
		Gdx.app.error(LOG_TAG, errorMessage, exception);
		throw exception;
	}

	private void updateTileCells() {
		for (int i = 0; i < TerrainBuffer.BUFFER_LENGTH; i++) {
			CellRef cell = buffer.getCell(i);

			if (cell.getTileType() == 0) {
				destroyTileCell(i);
			} else {
				updateTileCell(cell);
			}
		}
	}

	private void updateTileCell(CellRef ref) {
		TileCell cell = cells[ref.getIndex()];
		TileType type = TileType.getById(ref.getTileType());

		if (type == TileType.NONE) {
			logAndThrow("Invalid tile type: " + ref.getTileType());
		}

		// If we'll change the cell type, we must destroy the existing one first and
		// create a new one
		if (cell != null && type != cell.getType()) {
			destroyTileCell(ref.getIndex());
			cell = null;
		}

		// If there is no cell or cell was destroyed before, let's create a new one
		if (cell == null) {
			Sprite sprite = atlas.createSprite(type.toString());

			if (sprite == null) {
				logAndThrow("Failed to create tile cell. Unable to find texture with name: " + type.toString());
			}

			cell = new TileCell(ref.getIndex(), type, sprite);
			cells[ref.getIndex()] = cell;
		}

		updateTileCell(ref, cell);
	}

	private void updateTileCell(CellRef ref, TileCell cell) {
		// TODO: Update other properties, like lighting, etc
	}

	private void destroyTileCell(int index) {
		if (cells[index] != null) {
			cells[index].dispose();
			cells[index] = null;
		}
	}

//	public void createCellAt(int x, int y, TileType type) {
//		int index = toIndex(x, y);
//		if (cells.contains(index)) {
//			throw new RuntimeException("Failed to create cell at position " + x + "," + "y" + ". Cell already exists");
//		}
//
//		Sprite sprite = atlas.createSprite(type.toString());
//
//		if (sprite == null) {
//			throw new RuntimeException("Failed to create cell at position " + x + "," + "y" + ". Sprite with name "
//					+ type.toString() + " not found.");
//		}
//
//		TileCell cell = new TileCell(index, sprite);
//		Vector2 pos = new Vector2(x, y);
//		pos = toIsometric(pos);
//		cell.setPosition(pos.x, pos.y);
//		cells.put(index, cell);
//
//		dirtOrder = true;
//	}

	public int countCells() {
		return getCells(c -> true).size();
	}

	public ArrayList<TileCell> getCells(Predicate<TileCell> predicate) {
		ArrayList<TileCell> list = new ArrayList<>();
		for (TileCell cell : cells) {
			if (cell != null && predicate.test(cell))
				list.add(cell);
		}
		return list;
	}

	public TileCell findCell(Predicate<TileCell> predicate) {
		for (TileCell cell : cells) {
			if (cell != null && predicate.test(cell))
				return cell;
		}
		return null;
	}

	public void dispose() {
		for (TileCell cell : cells) {
			if (cell != null)
				cell.dispose();
		}
		atlas.dispose();
	}

	@Override
	public void act(float delta) {
		if (buffer.isDirt()) {
			updateTileCells();
			buffer.setDirt(false);
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		for (int z = 0; z < TerrainBuffer.Z_SIZE; z++) {
			for (int x = TerrainBuffer.X_SIZE - 1; x >= 0; x--) {
				for (int y = TerrainBuffer.Y_SIZE - 1; y >= 0; y--) {
					int index = TerrainBuffer.toIndex(x, y, z);

					if (cells[index] == null)
						continue;

					cells[index].draw(batch, parentAlpha);
				}
			}
		}
	}

	public static Vector3 toIsometric(float x, float y, float z) {
		return toIsometric(new Vector3(x, y, z));
	}

	public static Vector3 toIsometric(Vector3 pos) {
		float x = pos.x;
		float y = pos.y;
		pos.x = (x - y) * TileType.TILE_WIDTH_HALF;
		pos.y = (x + y) * TileType.TILE_HEIGHT_HALF;
		return pos;
	}
}
