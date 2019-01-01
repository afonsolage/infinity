package com.lagecompany.infinity.dev.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.github.czyzby.kiwi.util.gdx.asset.Disposables;
import com.github.czyzby.lml.parser.LmlParser;
import com.github.czyzby.lml.util.LmlApplicationListener;
import com.github.czyzby.lml.vis.util.VisLml;
import com.kotcrab.vis.ui.VisUI;
import com.lagecompany.infinity.dev.tools.views.MainView;
import com.lagecompany.infinity.dev.tools.views.MapSpriteSheetView;
import com.lagecompany.infinity.dev.tools.views.MapSpriteView;

public class DevGame extends LmlApplicationListener {

	public static final int WIDTH = 1600;
	public static final int HEIGHT = 900;

	private Batch batch;

	@Override
	public void create() {
		super.create();
		batch = new SpriteBatch();

		addClassAlias(MainView.ID, MainView.class);
		addClassAlias(MapSpriteSheetView.ID, MapSpriteSheetView.class);
		addClassAlias(MapSpriteView.ID, MapSpriteView.class);

		setView(MapSpriteSheetView.class);
	}

	public Batch getBatch() {
		return batch;
	}

	public static Stage newStage() {
		DevGame game = (DevGame) Gdx.app.getApplicationListener();
		return new Stage(new FitViewport(WIDTH, HEIGHT), game.getBatch());
	}

	@Override
	protected LmlParser createParser() {
		return VisLml.parser().build();
	}

	@Override
	public void dispose() {
		super.dispose();
		Disposables.disposeOf(batch);
		VisUI.dispose();
	}

	public static DevGame getInstance() {
		return (DevGame) Gdx.app.getApplicationListener();
	}
}
