package dev.amymialee.schooleduniforms.api.special;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public record TickDeltaUniform() implements HookedUniform {
    public static MapCodec<TickDeltaUniform> CODEC = RecordCodecBuilder.mapCodec(i -> i.point(new TickDeltaUniform()));

    @Override
    public void writeTo(Std140Builder builder) {
        builder.putFloat(0f);
    }

    @Override
    public void addSize(Std140SizeCalculator calculator) {
        calculator.putFloat();
    }

    @Override
    public Type type() {
        return null;
    }

    @Override
    public void writeTo(LevelExtractionContext context, Std140Builder builder) {
        builder.putFloat(context.deltaTracker().getGameTimeDeltaPartialTick(false));
    }
}