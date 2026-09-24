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

@Environment(EnvType.CLIENT)
public record ScoreboardUniform(String objective, String name, int value) implements HookedUniform {
    public static MapCodec<ScoreboardUniform> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                    Codec.STRING.fieldOf("objective").forGetter(ScoreboardUniform::objective),
                    Codec.STRING.fieldOf("name").forGetter(ScoreboardUniform::name),
                    Codec.INT.optionalFieldOf("value", 0).forGetter(ScoreboardUniform::value))
            .apply(i, ScoreboardUniform::new));

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
        if (objective != null) {
            ReadOnlyScoreInfo score = scoreboard.getPlayerScoreInfo(ScoreHolder.forNameOnly(this.name), objective);
            if (score != null) value = score.value();
        }
        builder.putInt(value);
    }
}