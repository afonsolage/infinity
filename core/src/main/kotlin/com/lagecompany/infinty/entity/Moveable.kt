package com.lagecompany.infinty.entity

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Group
import com.lagecompany.infinity.anim.Animator
import com.lagecompany.infinity.input.InputObserver

private const val ANIM_PATH = "player/"
private const val WALK_S = "walk_s"
private const val WALK_SW = "walk_sw"
private const val WALK_SE = "walk_se"
private const val WALK_N = "walk_n"
private const val WALK_NW = "walk_nw"
private const val WALK_NE = "walk_ne"
private const val WALK_E = "walk_e"
private const val WALK_W = "walk_w"

private val animations = arrayOf(WALK_S, WALK_SW, WALK_SE, WALK_N, WALK_NW, WALK_NE, WALK_E, WALK_W)

class Moveable : Group() {

	val animator = Animator()
	val dir = Vector2()
	val moveSpeed = 60f

	init {
		for (anim in animations) {
			animator.addAnimation("$ANIM_PATH/$anim")
		}

		animator.setPosition(50f, 50f)
		animator.playAnimation(WALK_N)

		addActor(animator)

		InputObserver.onKeyDown {
			when (it) {
				Keys.W -> dir.y++
				Keys.D -> dir.x++
				Keys.S -> dir.y--
				Keys.A -> dir.x--
			}
		}

		InputObserver.onKeyUp {
			when (it) {
				Keys.W -> dir.y--
				Keys.D -> dir.x--
				Keys.S -> dir.y++
				Keys.A -> dir.x++
			}
		}
	}

	override fun act(delta: Float) {
		super.act(delta)

		var x = getX()
		var y = getY()

		if (dir.isZero && animator.isPlaying()) {
			animator.stopAnimation()
		} else if (dir.x > 0 && dir.y == 0.0f) {
			animator.playAnimation(WALK_E)
		} else if (dir.x < 0 && dir.y == 0.0f) {
			animator.playAnimation(WALK_W)
		} else if (dir.x == 0.0f && dir.y < 0) {
			animator.playAnimation(WALK_S)
		} else if (dir.x == 0.0f && dir.y > 0) {
			animator.playAnimation(WALK_N)
		} else if (dir.x < 0 && dir.y > 0) {
			animator.playAnimation(WALK_NW)
		} else if (dir.x > 0 && dir.y > 0) {
			animator.playAnimation(WALK_NE)
		} else if (dir.x < 0 && dir.y < 0) {
			animator.playAnimation(WALK_SW)
		} else if (dir.x > 0 && dir.y < 0) {
			animator.playAnimation(WALK_SE)
		}

		if (dir.isZero)
			return

		val norDir = Vector2(dir)
		norDir.nor()

		x += norDir.x * moveSpeed * delta
		y += norDir.y * moveSpeed * 0.5f * delta

		setPosition(x, y)
	}

	override fun positionChanged() {
		animator.setPosition(getX(), getY())
	}
}