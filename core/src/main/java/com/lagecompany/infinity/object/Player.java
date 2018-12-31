package com.lagecompany.infinity.object;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.lagecompany.infinity.anim.Animator;
import com.lagecompany.infinity.input.InputObserver;

public class Player extends Group {

	private static final String ANIM_PATH = "player/";
	private static final String WALK_S = "walk_s";
	private static final String WALK_SW = "walk_sw";
	private static final String WALK_SE = "walk_se";
	private static final String WALK_N = "walk_n";
	private static final String WALK_NW = "walk_nw";
	private static final String WALK_NE = "walk_ne";
	private static final String WALK_E = "walk_e";
	private static final String WALK_W = "walk_w";

	private static final String[] ANIMATIONS = { WALK_S, WALK_SW, WALK_W, WALK_NW, WALK_NE, WALK_SE, WALK_E, WALK_N };

	private Animator animator;

	private Vector2 dir;
	private float moveSpeed = 60f;

	public Player() {
		dir = new Vector2();

		animator = new Animator();
		for (String anim : ANIMATIONS) {
			animator.addAnimation(ANIM_PATH + anim);
		}

		InputObserver.onKeyDown(this::onKeyDown);
		InputObserver.onKeyUp(this::onKeyUp);

		animator.setPosition(50, 50);
		animator.playAnimation(WALK_N);

		addActor(animator);
	}

	private void onKeyDown(int keycode) {
		switch (keycode) {
		case Keys.W: {
			dir.y++;
			break;
		}
		case Keys.D: {
			dir.x++;
			break;
		}
		case Keys.S: {
			dir.y--;
			break;
		}
		case Keys.A: {
			dir.x--;
		}
		}
	}

	private void onKeyUp(int keycode) {
		switch (keycode) {
		case Keys.W: {
			dir.y--;
			break;
		}
		case Keys.D: {
			dir.x--;
			break;
		}
		case Keys.S: {
			dir.y++;
			break;
		}
		case Keys.A: {
			dir.x++;
		}
		}
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		Vector2 tmp = new Vector2(getX(), getY());

		if (dir.x == 0 && dir.y == 0 && animator.isPlaying()) {
			animator.stopAnimation();
		} else if (dir.x > 0 && dir.y == 0) {
			animator.playAnimation(WALK_E);
		} else if (dir.x < 0 && dir.y == 0) {
			animator.playAnimation(WALK_W);
		} else if (dir.x == 0 && dir.y < 0) {
			animator.playAnimation(WALK_S);
		} else if (dir.x == 0 && dir.y > 0) {
			animator.playAnimation(WALK_N);
		} else if (dir.x < 0 && dir.y > 0) {
			animator.playAnimation(WALK_NW);
		} else if (dir.x > 0 && dir.y > 0) {
			animator.playAnimation(WALK_NE);
		} else if (dir.x < 0 && dir.y < 0) {
			animator.playAnimation(WALK_SW);
		} else if (dir.x > 0 && dir.y < 0) {
			animator.playAnimation(WALK_SE);
		}

		if (dir.x == 0 && dir.y == 0)
			return;

		Vector2 norDir = new Vector2(dir);
		norDir.nor();

		tmp.x += norDir.x * moveSpeed * delta;
		tmp.y += norDir.y * moveSpeed * 0.5f * delta;

		setPosition(tmp.x, tmp.y);
	}

	@Override
	protected void positionChanged() {
		animator.setPosition(getX(), getY());
	}

}
