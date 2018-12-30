package com.lagecompany.infinity.dev.tools.views;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.lagecompany.infinity.dev.tools.DevGame;

public class MapSpriteView extends AbstractLmlView {

	public static final String ID = "map-sprite-view";

	public static List<TextureRegion> regions;
	public static Texture texture;

	@LmlActor("currentImage")
	private VisImage currentImage;

	@LmlActor("spriteName")
	private VisTextField spriteName;

	@LmlActor("nextBtn")
	private VisTextButton nextBtn;

	@LmlActor("skipBtn")
	private VisTextButton skipBtn;

	@LmlActor("prevBtn")
	private VisTextButton prevBtn;

	private int nextIndex = 113;

	public MapSpriteView() {
		super(DevGame.newStage());
	}

	@Override
	public void show() {
		if (regions.size() > 0)
			next();
		else
			nextBtn.setDisabled(true);
	}

	@Override
	public String getViewId() {
		return ID;
	}

	@Override
	public FileHandle getTemplateFile() {
		return Gdx.files.internal("views/map-sprite-view.lml");
	}

	@LmlAction("prev")
	public void prev() {
		if (prevBtn.isDisabled())
			return;

		TextureRegion region = regions.get(nextIndex - 1);
		currentImage.setDrawable(new SpriteDrawable(new Sprite(region)));
		
		nextIndex--;
		
		if (nextIndex <= 0) {
			prevBtn.setDisabled(true);
			nextIndex = 0;
		}

		if (nextIndex < regions.size() && (nextBtn.isDisabled() && skipBtn.isDisabled())) {
			nextBtn.setDisabled(false);
			skipBtn.setDisabled(false);
		}
	}

	@LmlAction("skip")
	public void skip() {
		next(true);
	}

	@LmlAction("next")
	public void next() {
		next(false);
	}

	public void next(boolean skip) {
		if (!skip && nextBtn.isDisabled())
			return;
		else if (skip && skipBtn.isDisabled())
			return;

		TextureRegion region = regions.get(nextIndex++);
		currentImage.setDrawable(new SpriteDrawable(new Sprite(region)));

		if (nextIndex >= regions.size()) {
			nextBtn.setDisabled(true);
			skipBtn.setDisabled(true);
			nextIndex = regions.size() - 1;
		}

		if (nextIndex > 0 && prevBtn.isDisabled()) {
			prevBtn.setDisabled(false);
		}
	}

	@Override
	public void dispose() {
		super.dispose();
		texture.dispose();
	}

}
