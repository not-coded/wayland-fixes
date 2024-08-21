package io.github.moehreag.wayland_fixes;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WaylandFixes implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger(WaylandFixes.class);

	@Override
	public void onInitialize() {
		// TODO:

		// read glfw docs https://www.glfw.org/docs/3.4/intro_guide.html

		// fix fullscreen in wrong location
		// this is due to wayland doing its own window position bs
		// and onWindowPosChanged never gets called + wayland does not provide the window position

		/*
		// [Render thread/ERROR]: ########## GL ERROR ##########
		// [Render thread/ERROR]: @ Render
		// [Render thread/ERROR]: 65548: Wayland: The platform does not provide the window position

		int[] xPosArray = new int[1];
		int[] yPosArray = new int[1];
		GLFW.glfwGetWindowPos(window.getHandle(), xPosArray, yPosArray);
         */

		// so the x and y pos are basically unknown
		// due to that it defaults to a standard pos
		// 4493 (x), 5347 (x + window width), 840 (y), 1320 (y + window height) in my case
		// which is close to my 2nd monitor (4k resolution)

		// solution:
		// add config option whether to use primary screen or let user specify screen



		// optionally auto-scale gui scale depending on scaling
		// GLFW.glfwGetWindowContentScale(window.getHandle(), xScaleArray, yScaleArray);
		// e.g. 1920x1080 -> 1.0, 1.0
		// e.g. 3840x2168 at 200% scaling -> 2.0, 2.0
		// https://discourse.glfw.org/t/using-glfw-under-wayland/1547
		// GLFW_SCALE_TO_MONITOR
		// https://www.glfw.org/docs/3.3/window_guide.html#window_scale
	}
}
