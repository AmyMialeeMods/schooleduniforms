package dev.amymialee.schooleduniforms.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.amymialee.schooleduniforms.cca.ExtraTrackingComponent;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.world.scores.Objective;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;

@Mixin(ServerScoreboard.class)
public class ServerScoreboardMixin {
    @WrapOperation(method = {
            "onScoreChanged",
            "onPlayerScoreRemoved",
            "onObjectiveChanged"
    }, at = @At(value = "INVOKE", target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z"))
    public boolean stopTrackingObjective(Set<?> instance, Object o, Operation<Boolean> original) {
        return original.call(instance, o) || o instanceof Objective o2 && ExtraTrackingComponent.KEY.get(this).contains(o2);
    }

    @WrapOperation(method = {
            "onPlayerRemoved"
    }, at = @At(value = "INVOKE", target = "Ljava/util/Set;isEmpty()Z"))
    public boolean stopTrackingObjective(Set<?> instance, Operation<Boolean> original) {
        return original.call(instance) && ExtraTrackingComponent.KEY.get(this).isEmpty();
    }

    @WrapOperation(method = {
            "onObjectiveRemoved"
    }, at = @At(value = "INVOKE", target = "Lnet/minecraft/server/ServerScoreboard;stopTrackingObjective(Lnet/minecraft/world/scores/Objective;)V"))
    public void stopTrackingObjective(ServerScoreboard instance, Objective objective, Operation<Void> original) {
        original.call(instance, objective);
        ExtraTrackingComponent.KEY.get(this).stopTrackingObjective(objective);
    }
}