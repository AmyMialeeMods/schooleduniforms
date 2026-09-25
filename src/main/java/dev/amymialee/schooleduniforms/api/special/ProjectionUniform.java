package dev.amymialee.schooleduniforms.api.special;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.ReadOnlyScoreInfo;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.Scoreboard;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public record ProjectionUniform() implements HookedUniform {
    public static MapCodec<ProjectionUniform> CODEC = RecordCodecBuilder.mapCodec(i -> i.point(new ProjectionUniform()));

    @Override
    public void writeTo(Std140Builder builder) {
        builder.putMat4f(new Matrix4f());
    }

    @Override
    public void addSize(Std140SizeCalculator calculator) {
        calculator.putMat4f();
    }

    @Override
    public Type type() {
        return null;
    }

    @Override
    public void writeTo(LevelExtractionContext context, Std140Builder builder) {
        builder.putMat4f(context.levelState().cameraRenderState.projectionMatrix);
    }
}