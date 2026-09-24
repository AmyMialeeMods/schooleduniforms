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
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.List;

@Environment(EnvType.CLIENT)
public record Vec3ArrayUniform(int length, List<Vector3fc> value) implements UniformValue {
    public static MapCodec<Vec3ArrayUniform> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                    Codec.INT.fieldOf("length").forGetter(Vec3ArrayUniform::length),
                    ExtraCodecs.VECTOR3F.listOf().optionalFieldOf("value", List.of()).forGetter(Vec3ArrayUniform::value))
            .apply(i, Vec3ArrayUniform::new));

    @Override
    public void writeTo(Std140Builder builder) {
        for (int i = 0; i < length; i++) builder.putVec3(value.size() > i ? value.get(i) : new Vector3f());
    }

    @Override
    public void addSize(Std140SizeCalculator calculator) {
        for (int i = 0; i < length; i++) calculator.putVec3();
    }

    @Override
    public Type type() {
        return null;
    }
}