package com.dekay.olvido.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dekay.olvido.Constants;
import com.dekay.olvido.OlvidoGame;

import java.util.ConcurrentModificationException;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Constants.VIEWPORT_WIDTH;
		config.height = Constants.VIEWPORT_HEIGHT;
		new LwjglApplication(new OlvidoGame(), config);
	}
}
