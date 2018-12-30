package com.lagecompany.infinity.object;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.lagecompany.infinity.anim.Animator;

public class Player extends Group {

	private Animator animator;

	public Player() {
		animator = new Animator();
		animator.addAnimation("anim/player/walk_sw.anim");
		animator.playAnimation("walk_sw");

		animator.setPosition(50, 50);
		
		addActor(animator);
	}
}
