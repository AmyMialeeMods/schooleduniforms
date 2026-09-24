package dev.amymialee.schooleduniforms.api.uniforms;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.UniformValue;

import java.util.List;

@Environment(EnvType.CLIENT)
public record FloatArrayUniform(int length, List<Float> value) implements UniformValue {
    public static MapCodec<FloatArrayUniform> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                    Codec.INT.fieldOf("length").forGetter(FloatArrayUniform::length),
                    Codec.FLOAT.listOf().optionalFieldOf("value", List.of()).forGetter(FloatArrayUniform::value))
            .apply(i, FloatArrayUniform::new));

    @Override
    public void writeTo(Std140Builder builder) {
        for (int i = 0; i < length; i++) builder.putFloat(value.size() > i ? value.get(i) : 0);
    }

    @Override
    public void addSize(Std140SizeCalculator calculator) {
        for (int i = 0; i < length; i++) calculator.putFloat();
    }

    @Override
    public Type type() {
        return null;
    }
}