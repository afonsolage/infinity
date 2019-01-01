package com.lagecompany.infinty.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Group
import com.lagecompany.infinity.input.InputObserver
import com.lagecompany.infinty.animation.Animator

private const val N = 0
private const val NE = 1
private const val E = 2
private const val SE = 3
private const val S = 4
private const val SW = 5
private const val W = 6
private const val NW = 7

private const val ANIM_PATH = "player/"
private val animations = arrayOf("walk", "idle")
private val directions = arrayOf("n", "ne", "e", "se", "s", "sw", "w", "nw")

private const val LOG_TAG = "MOVABLE"

class Movable : Group() {

    private val animator = Animator()
    private val dir = Vector2()
    private val lastDir = Vector2(0f, 1f)
    private val moveSpeed = 60f

    init {
        for (anim in animations) {
            for (dir in directions) {
                animator.addAnimation("$ANIM_PATH/${anim}_$dir")
            }
        }

        animator.setPosition(50f, 50f)
        animator.playAnimation("idle_s")

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

        val tmp = Vector2(x, y)

        if (dir.isZero && !animator.currentAnimationName.startsWith("idle")) {
            val animationName = getAnimationName("idle", lastDir)
            if (!animator.playAnimation(animationName)) {
                Gdx.app.error(LOG_TAG, "Failed to play animation $animationName")
            }
        } else if (!dir.isZero) {
            animator.playAnimation(getAnimationName("walk", dir))
            lastDir.set(dir.x, dir.y)

            val norDir = Vector2(dir)

            norDir.nor()
            tmp.x += norDir.x * moveSpeed * delta

            tmp.y += norDir.y * moveSpeed * 0.5f * delta
            setPosition(tmp.x, tmp.y)
        }
    }

    override fun positionChanged() {
        animator.setPosition(x, y)
    }

    private fun getAnimationName(name: String, dir: Vector2): String {
        if (dir.isZero)
            throw IllegalArgumentException("Direction cannot be zero")

        val index = getDirIndex(dir)

        return "${name}_${directions[index]}"
    }

    private fun getDirIndex(dir: Vector2): Int {
        if (dir.x == 0f && dir.y > 0) {
            return N
        } else if (dir.x > 0 && dir.y > 0) {
            return NE
        } else if (dir.x > 0 && dir.y == 0f) {
            return E
        } else if (dir.x > 0 && dir.y < 0) {
            return SE
        } else if (dir.x == 0f && dir.y < 0) {
            return S
        } else if (dir.x < 0 && dir.y < 0f) {
            return SW
        } else if (dir.x < 0 && dir.y == 0f) {
            return W
        } else {
            return NW
        }
    }
}