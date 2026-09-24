package dev.amymialee.schooleduniforms.api.uniforms;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.UniformValue;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector2f;
import org.joml.Vector2fc;

import java.util.List;

@Environment(EnvType.CLIENT)
public record Vec2ArrayUniform(int length, List<Vector2fc> value) implements UniformValue {
    public static MapCodec<Vec2ArrayUniform> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                    Codec.INT.fieldOf("length").forGetter(Vec2ArrayUniform::length),
                    ExtraCodecs.VECTOR2F.listOf().optionalFieldOf("value", List.of()).forGetter(Vec2ArrayUniform::value))
            .apply(i, Vec2ArrayUniform::new));

    @Override
    public void writeTo(Std140Builder builder) {
        for (int i = 0; i < length; i++) builder.putVec2(value.size() > i ? value.get(i) : new Vector2f());
    }

    @Override
    public void addSize(Std140SizeCalculator calculator) {
        for (int i = 0; i < length; i++) calculator.putVec2();
    }

    @Override
    public Type type() {
        return null;
    }
}