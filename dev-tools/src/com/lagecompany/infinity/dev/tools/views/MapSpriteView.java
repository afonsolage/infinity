package com.lagecompany.infinity.dev.tools.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Json;
import com.github.czyzby.lml.annotation.LmlAction;
import com.github.czyzby.lml.annotation.LmlActor;
import com.github.czyzby.lml.parser.impl.AbstractLmlView;
import com.kotcrab.vis.ui.layout.DragPane;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisDialog;
import com.kotcrab.vis.ui.widget.VisImage;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisWindow;
import com.lagecompany.infinity.anim.AnimationData;
import com.lagecompany.infinity.dev.tools.DevGame;

public class MapSpriteView extends AbstractLmlView {

	public static final String ID = "map-sprite-view";

	public static List<TextureRegion> regions;
	public static Texture texture;
	public static String texturePath;

	@LmlActor("spriteSheetPane")
	private DragPane spriteSheetPane;

	@LmlActor("previewImage")
	private VisImage previewImage;

	@LmlActor("spriteName")
	private VisTextField spriteName;

	@LmlActor("nextBtn")
	private VisTextButton nextBtn;

	@LmlActor("skipBtn")
	private VisTextButton skipBtn;

	@LmlActor("prevBtn")
	private VisTextButton prevBtn;

	@LmlActor("animationPane")
	private DragPane animPane;

	@LmlActor("msgDialog")
	private VisDialog msgDialog;

	@LmlActor("msgDialogLabel")
	private VisLabel msgDialogLabel;

	@LmlActor("frameTimeField")
	private VisTextField frameTimeField;

	@LmlActor("flipField")
	private VisCheckBox flipField;

	@LmlActor("loopField")
	private VisCheckBox loopField;

	@LmlActor("animNameField")
	private VisTextField animNameField;

	@LmlActor("animPathField")
	private VisTextField animPathField;

	private Map<String, Drawable> drawableMap;
	private Animation<TextureRegion> animation;
	private float stateTime;

	private List<TextureRegion> regionsPreview;

	public MapSpriteView() {
		super(DevGame.newStage());
	}

	@LmlAction("avaiableSpritesX")
	public float getAvailableSpritesX() {
		return 0;
	}

	@LmlAction("avaiableSpritesY")
	public float getAvailableSpritesY() {
		return 0;
	}

	@LmlAction("avaiableSpritesWidth")
	public int getAvailableSpritesWidth() {
		return DevGame.WIDTH / 2 - 15;
	}

	@LmlAction("avaiableSpritesHeight")
	public int getAvailableSpritesHeight() {
		return DevGame.HEIGHT - 30;
	}

	@LmlAction("animationX")
	public float getAnimationX() {
		return DevGame.WIDTH / 2;
	}

	@LmlAction("animationY")
	public float getAnimationY() {
		return DevGame.HEIGHT;
	}

	@LmlAction("animationWidth")
	public int getAnimationWidth() {
		return DevGame.WIDTH / 2 - 15;
	}

	@LmlAction("animationHeight")
	public int getAnimationHeight() {
		return DevGame.HEIGHT / 2;
	}

	@LmlAction("closeDialog")
	public void closeDialog(Actor actor) {
		while (actor != null && !(actor instanceof VisWindow)) {
			actor = actor.getParent();
		}
		if (actor != null) {
			((VisWindow) actor).fadeOut();
		}
	}

	@LmlAction("goBack")
	public void goBack(final VisDialog dialog) {
		DevGame.getInstance().setView(MapSpriteSheetView.class);
	}

	@LmlAction("previewAnimation")
	public void previewAnimation() {
		if (animPane.getChildren().size < 1) {
			showMessage("You should add at least 2 sprites");
			return;
		}

		List<TextureRegion> regions = new ArrayList<>();

		animPane.getChildren().forEach(actor -> findTextureRegions(actor, regions));

		float frameTime = 0.1f;
		try {
			frameTime = Float.parseFloat(frameTimeField.getText());
		} catch (Exception e) {
		}

		if (flipField.isChecked())
			regions.forEach(r -> r.flip(true, false));

		regionsPreview = regions;
		PlayMode mode = ((loopField.isChecked()) ? PlayMode.LOOP : PlayMode.NORMAL);
		animation = new Animation<TextureRegion>(frameTime, regions.toArray(new TextureRegion[regions.size()]));
		animation.setPlayMode(mode);
		stateTime = 0;
	}

	private void findTextureRegions(Actor actor, List<TextureRegion> output) {
		if (actor instanceof Group) {
			Group group = (Group) actor;
			group.getChildren().forEach(childActor -> findTextureRegions(childActor, output));
		}

		if (actor instanceof VisImage) {
			Drawable drawable = ((VisImage) actor).getDrawable();
			if (drawable instanceof SpriteDrawable) {
				SpriteDrawable spriteDrawable = (SpriteDrawable) drawable;
				Sprite sprite = spriteDrawable.getSprite();
				output.add(new TextureRegion(sprite.getTexture(), sprite.getRegionX(), sprite.getRegionY(),
						sprite.getRegionWidth(), sprite.getRegionHeight()));
			}
		}
	}

	private void showMessage(String message) {
		msgDialogLabel.setText(message);
		msgDialog.setVisible(true);
		msgDialog.show(getStage());
		msgDialog.toFront();
	}

	@Override
	public String getViewId() {
		return ID;
	}

	@Override
	public FileHandle getTemplateFile() {
		return Gdx.files.internal("views/map-sprite-view.lml");
	}

	@Override
	public void render(float delta) {
		updateAnimation(delta);
		super.render(delta);
	}

	@Override
	public void show() {
		drawableMap = new HashMap<>();

		for (TextureRegion region : regions) {
			Drawable drawable = new SpriteDrawable(new Sprite(region));
			drawableMap.put(getRegionIdentity(region), drawable);
			spriteSheetPane.addActor(new Container<Actor>(new VisImage(drawable)));
		}
	}

	@Override
	public void hide() {
		reset();
	}

	public void reset() {
		msgDialog.hide();
		regions.clear();
		animPane.clear();
		spriteSheetPane.clear();
	}

	private void updateAnimation(float delta) {
		if (regionsPreview == null || regionsPreview.size() == 0)
			return;

		stateTime += delta;

		TextureRegion region = animation.getKeyFrame(stateTime);

		Sprite currentSprite = ((SpriteDrawable) previewImage.getDrawable()).getSprite();

		if (region.getRegionX() == currentSprite.getRegionX() && region.getRegionY() == currentSprite.getRegionY()
				&& currentSprite.isFlipX() == region.isFlipX())
			return;

		previewImage.setDrawable(new SpriteDrawable(new Sprite(region)));
	}

	private String getRegionIdentity(TextureRegion region) {
		return region.getRegionX() + "," + region.getRegionY() + "," + region.getRegionWidth() + ","
				+ region.getRegionHeight();
	}

	@Override
	public void dispose() {
		super.dispose();
		reset();
		texture.dispose();
	}

	@LmlAction("saveAnimation")
	public void saveAnimation() {
		previewAnimation();

		if (regionsPreview == null || regionsPreview.size() < 1) {
			showMessage("You must supply at least 2 frames to have an animation.");
			return;
		}

		if (animNameField.getText().isEmpty()) {
			showMessage("You must supply an animation name first.");
			return;
		}

		float frameTime = 0.1f;
		try {
			frameTime = Float.parseFloat(frameTimeField.getText());
		} catch (Exception e) {
			showMessage("Invalid frame time. It must be a float number.");
			return;
		}

		AnimationData data = new AnimationData(animNameField.getText(), texturePath, frameTime, flipField.isChecked(),
				loopField.isChecked());

		for (TextureRegion region : regionsPreview) {
			data.addRegion(region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight());
		}

		Json json = new Json();
		String path = animPathField.getText() + "/" + data.getName() + ".anim";
		FileHandle handle = Gdx.files.local(path);

		handle.writeString(json.toJson(data), false);

		animPane.clear();
		regionsPreview.clear();
		showMessage("Animation saved on file " + path);
	}
}
