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

import java.util.List;

@Environment(EnvType.CLIENT)
public record ScoreboardArrayUniform(String objective, int length, List<Integer> value) implements HookedUniform {
    public static MapCodec<ScoreboardArrayUniform> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                    Codec.STRING.fieldOf("objective").forGetter(ScoreboardArrayUniform::objective),
                    Codec.INT.fieldOf("length").forGetter(ScoreboardArrayUniform::length),
                    Codec.INT.listOf().optionalFieldOf("value", List.of()).forGetter(ScoreboardArrayUniform::value))
            .apply(i, ScoreboardArrayUniform::new));

    @Override
    public void writeTo(Std140Builder builder) {
        for (int i = 0; i < length; i++) builder.putInt(value.size() > i ? value.get(i) : 0);
    }

    @Override
    public void addSize(Std140SizeCalculator calculator) {
        for(var i = 0; i < length; i++) calculator.putInt();
    }

    @Override
    public Type type() {
        return null;
    }

    @Override
    public void writeTo(LevelExtractionContext context, Std140Builder builder) {
        for(var i = 0; i < length; i++) {
            var value = this.value.size() > i ? this.value.get(i) : 0;
            Scoreboard scoreboard = context.level().getScoreboard();
            Objective objective = scoreboard.getObjective(objective());
            if (objective != null) {
                ReadOnlyScoreInfo score = scoreboard.getPlayerScoreInfo(ScoreHolder.forNameOnly(String.valueOf(i)), objective);
                if (score != null) value = score.value();
            }
            builder.putInt(value);
        }
    }
}