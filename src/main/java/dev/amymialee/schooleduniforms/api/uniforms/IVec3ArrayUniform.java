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
import org.joml.Vector3i;
import org.joml.Vector3ic;

import java.util.List;

@Environment(EnvType.CLIENT)
public record IVec3ArrayUniform(int length, List<Vector3ic> value) implements UniformValue {
    public static MapCodec<IVec3ArrayUniform> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                    Codec.INT.fieldOf("length").forGetter(IVec3ArrayUniform::length),
                    ExtraCodecs.VECTOR3I.listOf().optionalFieldOf("value", List.of()).forGetter(IVec3ArrayUniform::value))
            .apply(i, IVec3ArrayUniform::new));

    @Override
    public void writeTo(Std140Builder builder) {
        for (int i = 0; i < length; i++) builder.putIVec3(value.size() > i ? value.get(i) : new Vector3i());
    }

    @Override
    public void addSize(Std140SizeCalculator calculator) {
        for (int i = 0; i < length; i++) calculator.putIVec3();
    }

    @Override
    public Type type() {
        return null;
    }
}