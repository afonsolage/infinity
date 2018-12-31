package com.lagecompany.infinty.animation

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.utils.Json

data class AnimationRegion(val x: Int, val y: Int, val width: Int, val height: Int)
data class AnimationData(
	val name: String,
	val texturePath: String,
	val frameTime: Float,
	val flip: Boolean,
	val loop: Boolean
) {
	val regions = mutableListOf<AnimationRegion>()
}

private const val LOG_TAG = "ANIMATOR"

class Animator : Actor() {
	private val animationMap = mutableMapOf<String, Animation<TextureRegion>>()

	private lateinit var currentAnimation: Animation<TextureRegion>
	private var curAnimName = ""
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
				animation.setPlayMode(if (data.loop) PlayMode.LOOP else PlayMode.NORMAL)
				animationMap.put(data.name, animation)
			} else {
				Gdx.app.error(LOG_TAG, "Failed to parse animation file $path")
			}
		} catch (e: Exception) {
			Gdx.app.error(LOG_TAG, "Failed to load animation at path $path", e)
		}
	}

	fun playAnimation(name: String): Boolean {
		if (name.equals(name, ignoreCase = true))
			return false

		

		return true
	}
}