package com.lagecompany.infinity.dev.tools;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
	public static void main(String[] args) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = DevGame.WIDTH;
		config.height = DevGame.HEIGHT;
		new LwjglApplication(new DevGame(), config);
	}
}
