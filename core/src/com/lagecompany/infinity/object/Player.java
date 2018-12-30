package com.lagecompany.infinity.object;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.lagecompany.infinity.anim.Animator;
import com.lagecompany.infinity.input.InputObserver;

public class Player extends Group {

	private static final String path = "player/";
	private static final String WALK_S = "walk_s";
	private static final String WALK_SW = "walk_sw";
	private static final String WALK_W = "walk_w";
	private static final String WALK_NW = "walk_nw";
	private static final String WALK_N = "walk_n";

	private Animator animator;

	public Player() {
		animator = new Animator();
		animator.addAnimation("anim/player/walk_sw.anim");
		animator.playAnimation("walk_sw");

		InputObserver.onTouchUp((x, y, pointer, button) -> {
			animator.setPosition(x, y);
		});

		addActor(animator);
	}
}
