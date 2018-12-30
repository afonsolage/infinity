package com.lagecompany.infinity.stage;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public abstract class AbstractStage extends Stage {

	private final StageType type;

	public AbstractStage(StageType type) {
		super(new ScreenViewport());
		this.type = type;
	}

	public StageType getType() {
		return type;
	}

	@SuppressWarnings("unchecked")
	public <T extends ApplicationListener> T getApp() {
		return (T) Gdx.app.getApplicationListener();
	}

	@Override
	public void dispose() {
		super.dispose();
		finalize();
	}

	public abstract void initialize();

	public abstract void finalize();
}
