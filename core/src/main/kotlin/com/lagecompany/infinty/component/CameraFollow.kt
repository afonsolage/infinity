package com.lagecompany.infinty.component

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.scenes.scene2d.Actor

class CameraFollow(private val target: Actor, private val camera: Camera) : Actor() {
    override fun act(delta: Float) {
        camera.position.x = target.x
        camera.position.y = target.y
    }
}