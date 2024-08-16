package io.github.moehreag.wayland_fixes;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WaylandFixes implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger(WaylandFixes.class);

	@Override
	public void onInitialize() {
		// TODO:
		// fix fullscreen in wrong location
		//this.monitorTracker.getMonitor(Window.class.cast(this)).getCurrentVideoMode().getHeight();
		// ^ first step or soemthing
		// read glfw docs https://www.glfw.org/docs/3.4/intro_guide.html
		
		// put proper icon
		// ^^ icon doesn't get set but getting it works!!!
	}
}
