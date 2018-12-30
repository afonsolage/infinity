package com.lagecompany.infinity.object;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.lagecompany.infinity.anim.Animator;
import com.lagecompany.infinity.input.InputObserver;

public class Player extends Group {

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
