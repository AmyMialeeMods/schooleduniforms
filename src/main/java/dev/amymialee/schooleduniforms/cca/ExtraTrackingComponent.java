package dev.amymialee.schooleduniforms.cca;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import dev.amymialee.schooleduniforms.SchooledUniforms;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSetObjectivePacket;
import net.minecraft.network.protocol.game.ClientboundSetScorePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerScoreEntry;
import net.minecraft.world.scores.Scoreboard;
import org.jspecify.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v8.component.CardinalComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ExtraTrackingComponent implements CardinalComponent {
    public static final ComponentKey<ExtraTrackingComponent> KEY = ComponentRegistry.getOrCreate(SchooledUniforms.id("extratracking"), ExtraTrackingComponent.class);
    public final Scoreboard scoreboard;
    public final @Nullable MinecraftServer server;
    private final Set<Objective> tracking = Sets.newHashSet();

    public ExtraTrackingComponent(Scoreboard scoreboard, @Nullable MinecraftServer server) {
        this.scoreboard = scoreboard;
        this.server = server;
    }

    public boolean isEmpty() {
        return tracking.isEmpty();
    }

    public boolean contains(Objective objective) {
        return tracking.contains(objective);
    }

    public void startTrackingObjective(Objective objective) {
        if (server == null) return;
        List<Packet<?>> packets = new ArrayList<>();
        packets.add(new ClientboundSetObjectivePacket(objective, 0));
        for (PlayerScoreEntry score : scoreboard.listPlayerScores(objective)) packets.add(new ClientboundSetScorePacket(score.owner(), objective.getName(), score.value(), Optional.ofNullable(score.display()), Optional.ofNullable(score.numberFormatOverride())));
        for (ServerPlayer player : this.server.getPlayerList().getPlayers()) for (Packet<?> packet : packets) player.connection.send(packet);
        this.tracking.add(objective);
    }

    public void stopTrackingObjective(Objective objective) {
        if (server == null) return;
        Packet<?> packet = new ClientboundSetObjectivePacket(objective, 1);
        for (ServerPlayer player : this.server.getPlayerList().getPlayers()) player.connection.send(packet);
        this.tracking.remove(objective);
    }

    @Override
    public void readData(ValueInput readView) {
        this.tracking.clear();
        readView.list("objectives", Codec.STRING).ifPresent(i -> i.forEach(name -> {
            Objective objective = scoreboard.getObjective(name);
            if (objective != null) this.tracking.add(objective);
        }));
    }

    @Override
    public void writeData(ValueOutput writeView) {
        var list = writeView.list("objectives", Codec.STRING);
        for (Objective objective : this.tracking) list.add(objective.getName());
    }
}