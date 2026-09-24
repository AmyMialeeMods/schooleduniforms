package dev.amymialee.schooleduniforms.api.special;

import com.mojang.blaze3d.buffers.Std140Builder;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.minecraft.client.renderer.UniformValue;

public interface HookedUniform extends UniformValue {
    void writeTo(LevelExtractionContext context, Std140Builder builder);
}