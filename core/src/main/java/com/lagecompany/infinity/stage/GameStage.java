package com.lagecompany.infinity.stage;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lagecompany.infinity.InfinityGame;
import com.lagecompany.infinity.environment.terrain.IsometricTileMap;
import com.lagecompany.infinity.environment.terrain.TerrainController;
import com.lagecompany.infinity.environment.terrain.TileType;
import com.lagecompany.infinity.object.Player;
import com.lagecompany.infinty.entity.Moveable;

public class GameStage extends AbstractStage {

	private static final int UNITS_PER_PIXEL = 1;

	private IsometricTileMap map;
	private TerrainController terrainController;
	private OrthographicCamera cam;

	public GameStage(InfinityGame game) {
		super(StageType.Game);
	}

	@Override
	public void initialize() {
		initViewport();
		initTerrain();
		initCamera();
	}

	private void initCamera() {
		cam = (OrthographicCamera) getCamera();
		cam.translate(-getViewport().getWorldWidth() / 2 + TileType.TILE_WIDTH_HALF, 0);
	}

	private void initTerrain() {
		terrainController = new TerrainController();
		terrainController.init();
		map = new IsometricTileMap(terrainController);

		addActor(map);
		addActor(new Moveable());
//		addActor(new Player());
	}

	private void initViewport() {
		ScreenViewport viewport = (ScreenViewport) getViewport();
		viewport.setUnitsPerPixel(UNITS_PER_PIXEL);
	}

	@Override
	public void finalize() {
		map.dispose();
		terrainController.dispose();
	}

}
