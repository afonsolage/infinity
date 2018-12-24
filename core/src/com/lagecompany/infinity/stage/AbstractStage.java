package com.lagecompany.infinity.stage;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.lagecompany.infinity.InfinityGame;

public abstract class AbstractStage extends Stage {

	private final StageType type;
	private final InfinityGame game;

	public AbstractStage(StageType type, InfinityGame game) {
		super(new ScreenViewport());
		this.type = type;
		this.game = game;
	}

	public StageType getType() {
		return type;
	}

	public InfinityGame getGame() {
		return game;
	}

	@Override
	public void dispose() {
		super.dispose();
		finalize();
	}

	public abstract void initialize();
	public abstract void finalize();
}
