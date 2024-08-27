package io.github.moehreag.wayland_fixes.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(MinecraftClient.class)
public abstract class MixinMinecraftClient {
    @Shadow @Final private Window window;

    @ModifyArg(method = "onResolutionChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/Window;setScaleFactor(D)V"))
    private double fixHiDPIScaling(double d) {
        return d * getScaleFactor();
    }

    @Unique
    private float getScaleFactor() {
        float[] pos = new float[1];
        GLFW.glfwGetWindowContentScale(this.window.getHandle(), pos, pos);

        return pos[0]; // using x or y doesn't matter
    }
}
