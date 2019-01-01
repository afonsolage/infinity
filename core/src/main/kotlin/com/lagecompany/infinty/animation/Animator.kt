package com.lagecompany.infinty.animation

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Json

data class AnimationRegion(val x: Int = 0, val y: Int = 0, val width: Int = 0, val height: Int = 0)
data class AnimationData(
        val name: String = "",
        val texturePath: String = "",
        val frameTime: Float = 0.1f,
        val flip: Boolean = false,
        val loop: Boolean = true
) {
    val regions = mutableListOf<AnimationRegion>()

    fun addRegion(x: Int, y: Int, width: Int, height: Int) {
        regions.add(AnimationRegion(x, y, width, height))
    }
}

private const val LOG_TAG = "ANIMATOR"

class Animator : Actor() {
    private val animationMap = mutableMapOf<String, Animation<TextureRegion>>()

    private lateinit var currentAnimation: Animation<TextureRegion>

    var currentAnimationName = ""
        private set

    private val isPlaying: Boolean
        get() = currentAnimationName.isNotEmpty()

    private var stateTime = 0f

    fun addAnimation(path: String) {
        try {
            val fileHandle = Gdx.files.internal("anim/$path.anim")
            val data = Json().fromJson(AnimationData::class.java, fileHandle)

            if (data != null) {
                val texture = Texture(Gdx.files.internal(data.texturePath))

                var regions = mutableListOf<TextureRegion>()
                for (dataRegion in data.regions) {
                    val region = TextureRegion(texture, dataRegion.x, dataRegion.y, dataRegion.width, dataRegion.height)
                    if (data.flip)
                        region.flip(true, false)
                    regions.add(region)
                }

                var animation = Animation<TextureRegion>(data.frameTime, *regions.toTypedArray())
                animation.playMode = if (data.loop) PlayMode.LOOP else PlayMode.NORMAL
                animationMap.put(data.name, animation)
            } else {
                Gdx.app.error(LOG_TAG, "Failed to parse animation file $path")
            }
        } catch (e: Exception) {
            Gdx.app.error(LOG_TAG, "Failed to load animation at path $path", e)
        }
    }

    fun playAnimation(name: String): Boolean {
        if (name.equals(currentAnimationName, ignoreCase = true))
            return false

        val animation = animationMap.get(name) ?: return false

        currentAnimationName = name
        currentAnimation = animation
        stateTime = 0f

        return true
    }

    fun stopAnimation() {
        currentAnimationName = ""
    }

    override fun draw(batch: Batch?, parentAlpha: Float) {
        if (!isPlaying)
            return

        val region = currentAnimation.getKeyFrame(stateTime)
        batch?.draw(region, x, y, region.regionX.toFloat(), region.regionY.toFloat(), region.regionWidth.toFloat(),
                region.regionHeight.toFloat(), scaleX, scaleY, rotation)
    }

    override fun act(delta: Float) {
        super.act(delta)
        stateTime += delta
    }
}