package io.github.moehreag.wayland_fixes.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.github.moehreag.wayland_fixes.WaylandFixes;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.commons.io.IOUtils;

public class DesktopFileInjector {
	// The taskbar icon is set to the minecraft launcher's icon if you have it installed
	// Adding '.java-edition' fixes it (due to being recognized as a new app)
	public static final String APP_ID = "com.mojang.minecraft.java-edition";

	private static final String ICON_NAME = "minecraft.png";
	private static final String FILE_NAME = APP_ID + ".desktop";
	private static final String RESOURCE_LOCATION = "/assets/wayland_fixes/" + FILE_NAME;
	private static final List<Path> injectedLocations = new ArrayList<>();

	public static void inject() {
		Runtime.getRuntime().addShutdownHook(new Thread(DesktopFileInjector::uninject));

		try (InputStream stream = DesktopFileInjector.class.getResourceAsStream(RESOURCE_LOCATION)) {
			Path location = getDesktopFileLocation();

			String version = FabricLoader.getInstance().getModContainer("minecraft").orElseThrow(IllegalStateException::new)
					.getMetadata().getVersion().getFriendlyString();
			injectFile(location, String.format(IOUtils.toString(Objects.requireNonNull(stream), StandardCharsets.UTF_8),
					version, ICON_NAME.substring(0, ICON_NAME.lastIndexOf("."))).getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			WaylandFixes.LOGGER.error("Failed to inject icon: ", e);
		}

	}

	public static void setIcon(InputStream icon16, InputStream icon32) {
		byte[] icon16Byte;
		byte[] icon32Byte;

		try {
			// https://stackoverflow.com/questions/58534138/does-files-readallbytes-closes-the-inputstream-after-reading-the-file
			icon16Byte = icon16.readAllBytes();
			icon32Byte = icon32.readAllBytes();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		ArrayList<byte[]> icons = new ArrayList<>(Arrays.asList(icon16Byte, icon32Byte));

		for(byte[] bytes : icons) {
			try {
				BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
				Path target = getIconFileLocation(image.getWidth(), image.getHeight());
				injectFile(target, bytes);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
		updateIconSystem();
	}

	private static void injectFile(Path target, byte[] data) {
		try {
			Files.createDirectories(target.getParent());
			Files.write(target, data);
			injectedLocations.add(target);
		} catch (IOException e) {
			WaylandFixes.LOGGER.error("Failed to inject file: ", e);
		}
	}


	private static Path getIconFileLocation(int width, int height) {
		return XDGPathResolver.getUserDataLocation().resolve("icons/hicolor").resolve(width + "x" + height)
				.resolve("apps").resolve(ICON_NAME);
	}

	private static Path getDesktopFileLocation() {
		return XDGPathResolver.getUserDataLocation().resolve("applications").resolve(FILE_NAME);
	}

	private static void updateIconSystem() {
		ProcessBuilder builder = new ProcessBuilder("xdg-icon-resource", "forceupdate");
		try {
			builder.start();
		} catch (IOException ignored) {
		}
	}

	private static void uninject() {
		injectedLocations.forEach(p -> {
			try {
				Files.deleteIfExists(p);
			} catch (IOException ignored) {

			}
		});
		updateIconSystem();
	}
}
