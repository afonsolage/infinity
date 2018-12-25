package com.lagecompany.infinity.environment.terrain;

import java.util.concurrent.atomic.AtomicBoolean;

import com.lagecompany.infinity.logic.terrain.CellRef;
import com.lagecompany.infinity.logic.terrain.TerrainBuffer;
import com.lagecompany.infinity.rx.GdxSchedulers;

import io.reactivex.schedulers.Schedulers;

public class TerrainController {

	private TerrainBuffer frontBuffer;
	private TerrainBuffer backBuffer;

	public TerrainController() {
		frontBuffer = new TerrainBuffer(true);
		backBuffer = new TerrainBuffer(true);
	}

	public void start() {
		Schedulers.computation().scheduleDirect(this::generateTerrain);
	}

	public void stop() {
	}

	public TerrainBuffer getBuffer() {
		return frontBuffer;
	}

	private void generateTerrain() {
		for (int x = 0; x < TerrainBuffer.X_SIZE; x++) {
			for (int y = 0; y < TerrainBuffer.Y_SIZE; y++) {
				CellRef cell = backBuffer.getCell(x, y, 2);
				cell.setTileType(
						x == 0 || y == 0 || x == TerrainBuffer.X_SIZE - 1 || y == TerrainBuffer.Y_SIZE - 1 ? 3 : 1);

				cell.save();
			}
		}

		CellRef cell = backBuffer.getCell(0, 0, 2);
		cell.setTileType(0);
		cell.save();

		cell = backBuffer.getCell(0, 0, 1);
		cell.setTileType(2);
		cell.save();

		cell = backBuffer.getCell(0, 1, 3);
		cell.setTileType(2);
		cell.save();

		cell = backBuffer.getCell(1, 0, 3);
		cell.setTileType(2);
		cell.save();

		backBuffer.setDirt(true);
	}

	public boolean isBufferDirt() {
		if (frontBuffer.isDirt()) {
			return true;
		} else if (backBuffer.isDirt()) {
			swapBuffers();
			return true;
		} else {
			return false;
		}
	}

	public void cleanBufferDirtiness() {
		frontBuffer.setDirt(false);
		if (backBuffer.isDirt())
			swapBuffers();
	}

	private AtomicBoolean swapRequested = new AtomicBoolean(false);

	private void swapBuffers() {
		if (!swapRequested.compareAndSet(false, true))
			return;

		GdxSchedulers.main().scheduleDirect(() -> {
			TerrainBuffer tmp = frontBuffer;
			frontBuffer = backBuffer;
			backBuffer = tmp;
			swapRequested.set(false);
		});
	}
}
