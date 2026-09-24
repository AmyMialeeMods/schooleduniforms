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
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

import java.util.List;

@Environment(EnvType.CLIENT)
public record Matrix4x4ArrayUniform(int length, List<Matrix4fc> value) implements UniformValue {
    public static MapCodec<Matrix4x4ArrayUniform> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                    Codec.INT.fieldOf("length").forGetter(Matrix4x4ArrayUniform::length),
                    ExtraCodecs.MATRIX4F.listOf().optionalFieldOf("value", List.of()).forGetter(Matrix4x4ArrayUniform::value))
            .apply(i, Matrix4x4ArrayUniform::new));

    @Override
    public void writeTo(Std140Builder builder) {
        for (int i = 0; i < length; i++) builder.putMat4f(value.size() > i ? value.get(i) : new Matrix4f());
    }

    @Override
    public void addSize(Std140SizeCalculator calculator) {
        for (int i = 0; i < length; i++) calculator.putMat4f();
    }

    @Override
    public Type type() {
        return null;
    }
}