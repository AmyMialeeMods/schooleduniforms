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
public record IntArrayUniform(int length, List<Integer> value) implements UniformValue {
    public static MapCodec<IntArrayUniform> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                    Codec.INT.fieldOf("length").forGetter(IntArrayUniform::length),
                    Codec.INT.listOf().optionalFieldOf("value", List.of()).forGetter(IntArrayUniform::value))
            .apply(i, IntArrayUniform::new));

    @Override
    public void writeTo(Std140Builder builder) {
        for (int i = 0; i < length; i++) builder.putInt(value.size() > i ? value.get(i) : 0);
    }

    @Override
    public void addSize(Std140SizeCalculator calculator) {
        for (int i = 0; i < length; i++) calculator.putInt();
    }

    @Override
    public Type type() {
        return null;
    }
}