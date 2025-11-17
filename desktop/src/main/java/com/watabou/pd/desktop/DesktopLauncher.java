package com.watabou.pd.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Preferences;
import com.badlogic.gdx.utils.SharedLibraryLoader;
import com.watabou.pixeldungeon.PixelDungeon;
import com.watabou.pixeldungeon.Preferences;

public class DesktopLauncher {
	public static void main (String[] arg) {
		String version = DesktopLauncher.class.getPackage().getSpecificationVersion();
		if (version == null) {
			version = "???";
		}
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

		String preferencesDirectory;
		if (SharedLibraryLoader.isMac) {
			preferencesDirectory = "Library/Application Support/Pixel Dungeon/";
		} else if (SharedLibraryLoader.isLinux) {
			preferencesDirectory = ".watabou/pixel-dungeon/";
		} else if (SharedLibraryLoader.isWindows) {
			preferencesDirectory = "Saved Games/";
		} else {
			preferencesDirectory = ".";
		}
		// FIXME: This is a hack to get access to the preferences before we have an application setup
		com.badlogic.gdx.Preferences prefs = new Lwjgl3Preferences(Preferences.FILE_NAME, preferencesDirectory);

		boolean isFullscreen = prefs.getBoolean(Preferences.KEY_WINDOW_FULLSCREEN, false);
		if (isFullscreen) {
			config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
		} else {
			config.setWindowedMode(
				prefs.getInteger(Preferences.KEY_WINDOW_WIDTH, Preferences.DEFAULT_WINDOW_WIDTH),
				prefs.getInteger(Preferences.KEY_WINDOW_HEIGHT, Preferences.DEFAULT_WINDOW_HEIGHT)
			);
		}

		new Lwjgl3Application(new PixelDungeon(preferencesDirectory, version), config);
	}
}
