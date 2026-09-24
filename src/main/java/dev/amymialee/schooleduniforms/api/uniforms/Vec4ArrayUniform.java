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
import org.joml.Vector4f;
import org.joml.Vector4fc;

import java.util.List;

@Environment(EnvType.CLIENT)
public record Vec4ArrayUniform(int length, List<Vector4fc> value) implements UniformValue {
    public static MapCodec<Vec4ArrayUniform> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                    Codec.INT.fieldOf("length").forGetter(Vec4ArrayUniform::length),
                    ExtraCodecs.VECTOR4F.listOf().optionalFieldOf("value", List.of()).forGetter(Vec4ArrayUniform::value))
            .apply(i, Vec4ArrayUniform::new));

    @Override
    public void writeTo(Std140Builder builder) {
        for (int i = 0; i < length; i++) builder.putVec4(value.size() > i ? value.get(i) : new Vector4f());
    }

    @Override
    public void addSize(Std140SizeCalculator calculator) {
        for (int i = 0; i < length; i++) calculator.putVec4();
    }

    @Override
    public Type type() {
        return null;
    }
}