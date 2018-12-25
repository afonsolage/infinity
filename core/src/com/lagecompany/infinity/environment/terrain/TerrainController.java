package com.lagecompany.infinity.environment.terrain;

import com.lagecompany.infinity.logic.terrain.CellRef;
import com.lagecompany.infinity.logic.terrain.TerrainBuffer;

public class TerrainController implements Runnable {

	private TerrainBuffer frontBuffer;
	private TerrainBuffer backBuffer;

	public TerrainController() {
		frontBuffer = new TerrainBuffer(true);
		backBuffer = new TerrainBuffer(true);
	}

	private boolean running;
	private Thread thread;

	public void start() {
		thread = new Thread(this);
		thread.setName("TerrainControl");
		thread.start();
	}

	public void stop() {
		running = false;
	}

	public TerrainBuffer getBuffer() {
		return frontBuffer;
	}

	@Override
	public void run() {
		running = true;

		
		generateTerrain();

		while (running && !Thread.interrupted()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				break;
			}
		}

		frontBuffer.free();
		backBuffer.free();
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

	private void swapBuffers() {
		TerrainBuffer tmp = frontBuffer;
		frontBuffer = backBuffer;
		backBuffer = tmp;
	}
}
