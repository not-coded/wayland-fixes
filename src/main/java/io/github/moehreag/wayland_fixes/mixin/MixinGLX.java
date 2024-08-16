package io.github.moehreag.wayland_fixes.mixin;

import com.mojang.blaze3d.platform.GLX;
import io.github.moehreag.wayland_fixes.WaylandFixes;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.LongSupplier;

@Mixin(GLX.class)
public abstract class MixinGLX {
    @Inject(method = "_initGlfw", at = @At("HEAD"), remap = false)
    private static void preGLFWInit(CallbackInfoReturnable<LongSupplier> cir) {
        int mcLWJGLVersion = Integer.parseInt(GLX._getLWJGLVersion().substring(0, 5).replaceAll("\\.", ""));

        if(mcLWJGLVersion < 331) {
                WaylandFixes.LOGGER.warn("WaylandFixes is disabling itself due to the LWJGL Version being too low.");
            WaylandFixes.LOGGER.warn("Please update to a LWJGL above '3.2.2' such as '3.3.1' or higher.");
            WaylandFixes.disabled = true;
            return;
        }

        if (GLFW.glfwPlatformSupported(GLFW.GLFW_PLATFORM_WAYLAND)) {
            GLFW.glfwInitHint(GLFW.GLFW_PLATFORM, GLFW.GLFW_PLATFORM_WAYLAND); // enable wayland backend if supported
        }
    }
}
