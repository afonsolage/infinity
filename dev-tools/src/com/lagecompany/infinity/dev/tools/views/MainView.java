package com.lagecompany.infinity.dev.tools.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;
import com.lagecompany.infinity.dev.tools.DevGame;

public class MainView extends AbstractLmlView {
	public static final String ID = "main-view";

	public MainView() {
		super(DevGame.newStage());
	}

	@Override
	public FileHandle getTemplateFile() {
		return Gdx.files.internal("views/main.lml");
	}

	@Override
	public String getViewId() {
		return ID;
	}
}
