package io.github.moehreag.wayland_fixes.util;

import java.nio.file.Path;
import java.nio.file.Paths;

public class XDGPathResolver {

	private static Path getHome(){
		String home = System.getenv().getOrDefault("HOME", System.getProperty("user.home"));
		if (home == null || home.isEmpty()) {
			throw new IllegalStateException("could not resolve user home");
		}
		return Paths.get(home);
	}

	public static Path getUserDataLocation() {
		String xdgDataHome = System.getenv("XDG_DATA_HOME");
		if (xdgDataHome == null || xdgDataHome.isEmpty()) {
			return getHome().resolve(".local/share/");
		}
		return Paths.get(xdgDataHome);
	}
}
