package com.lagecompany.infinity;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.lagecompany.infinity.stage.AbstractStage;
import com.lagecompany.infinity.stage.GameStage;
import com.lagecompany.infinity.stage.StageType;

public class InfinityGame extends ApplicationAdapter {

	private AbstractStage currentStage;

	@Override
	public void create() {
		changeStage(StageType.Game);
		
//		ScreenViewport screenViewport = new ScreenViewport();
//		screenViewport.setUnitsPerPixel(2.5f);
//		gameStage = new Stage(screenViewport);
//
//		TerrainBuffer buffer = new TerrainBuffer();
//		buffer.allocate();
//
//		dummyFillBuffer(buffer);
//
//		gameStage.addActor(new IsometricTileMap(buffer));
//
//		// TODO: Add proper camera handling later
//		Camera cam = gameStage.getCamera();
//		cam.translate(new Vector3(50 - screenViewport.getWorldWidth() / 2, -50, 0));
	}

//	private void dummyFillBuffer(TerrainBuffer buffer) {
//		for (int x = 0; x < TerrainBuffer.X_SIZE; x++) {
//			for (int y = 0; y < TerrainBuffer.Y_SIZE; y++) {
//				CellRef cell = buffer.getCell(x, y, 2);
//				cell.setTileType(
//						x == 0 || y == 0 || x == TerrainBuffer.X_SIZE - 1 || y == TerrainBuffer.Y_SIZE - 1 ? 3 : 1);
//				
//				cell.save();
//			}
//		}
//		
//		CellRef cell = buffer.getCell(0,0,2);
//		cell.setTileType(0);
//		cell.save();
//		
//		cell = buffer.getCell(0,0,1);
//		cell.setTileType(2);
//		cell.save();
//		
//		cell = buffer.getCell(0,1,3);
//		cell.setTileType(2);
//		cell.save();
//		
//		cell = buffer.getCell(1,0,3);
//		cell.setTileType(2);
//		cell.save();
//	}

	@Override
	public void resize(int width, int height) {
		currentStage.getViewport().update(width, height);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		currentStage.act();
		currentStage.draw();
	}

	@Override
	public void dispose() {
	}

	public void changeStage(StageType type) {
		AbstractStage stage;
		switch (type) {
		case Game: {
			stage = new GameStage(this);
			break;
		}
		default: {
			throw new UnsupportedOperationException("Not implemented yet.");
		}
		}

		if (currentStage != null)
			currentStage.finalize();

		currentStage = stage;
		currentStage.initialize();
	}
}
