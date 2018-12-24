package com.lagecompany.infinity;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lagecompany.infinity.environment.terrain.IsometricTileMap;

public class InfinityGame extends ApplicationAdapter {

	private Stage gameStage;

	@Override
	public void create() {
		ScreenViewport screenViewport = new ScreenViewport();
//		screenViewport.setUnitsPerPixel(2.5f);
		gameStage = new Stage(screenViewport);

		gameStage.addActor(new IsometricTileMap());
		
		//TODO: Add proper camera handling later
		Camera cam = gameStage.getCamera();
		cam.translate(new Vector3(50 -screenViewport.getWorldWidth() / 2, -50, 0));
	}

	@Override
	public void resize(int width, int height) {
		gameStage.getViewport().update(width, height);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gameStage.act();
		gameStage.draw();
	}

	@Override
	public void dispose() {
	}
}
