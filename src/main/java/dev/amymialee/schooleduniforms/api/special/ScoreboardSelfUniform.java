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
import net.minecraft.world.scores.Scoreboard;

@Environment(EnvType.CLIENT)
public record ScoreboardSelfUniform(String objective, int value) implements HookedUniform {
    public static MapCodec<ScoreboardSelfUniform> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                    Codec.STRING.fieldOf("objective").forGetter(ScoreboardSelfUniform::objective),
                    Codec.INT.optionalFieldOf("value", 0).forGetter(ScoreboardSelfUniform::value))
            .apply(i, ScoreboardSelfUniform::new));

    @Override
    public void writeTo(Std140Builder builder) {
        builder.putInt(value);
    }

    @Override
    public void addSize(Std140SizeCalculator calculator) {
        calculator.putInt();
    }

    @Override
    public Type type() {
        return null;
    }

    @Override
    public void writeTo(LevelExtractionContext context, Std140Builder builder) {
        var value = this.value;
        Scoreboard scoreboard = context.level().getScoreboard();
        Objective objective = scoreboard.getObjective(objective());
        var entity = context.camera().entity();
        if (objective != null && entity != null) {
            ReadOnlyScoreInfo score = scoreboard.getPlayerScoreInfo(entity, objective);
            if (score != null) value = score.value();
        }
        builder.putInt(value);
    }
}