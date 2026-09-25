package dev.amymialee.schooleduniforms.api.special;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import org.joml.Matrix4f;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public record CameraPosUniform() implements HookedUniform {
    public static MapCodec<CameraPosUniform> CODEC = RecordCodecBuilder.mapCodec(i -> i.point(new CameraPosUniform()));

    @Override
    public void writeTo(Std140Builder builder) {
        builder.putVec3(new Vector3f());
    }

    @Override
    public void addSize(Std140SizeCalculator calculator) {
        calculator.putVec3();
    }

    @Override
    public Type type() {
        return null;
    }

    @Override
    public void writeTo(LevelExtractionContext context, Std140Builder builder) {
        var cameraPos = context.levelState().cameraRenderState.pos;
        builder.putVec3((float) cameraPos.x, (float) cameraPos.y, (float) cameraPos.z);
    }
}