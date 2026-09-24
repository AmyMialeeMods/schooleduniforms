package dev.amymialee.schooleduniforms;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import dev.amymialee.schooleduniforms.cca.ExtraTrackingComponent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ObjectiveArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.scores.Objective;
import org.jspecify.annotations.NonNull;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.scoreboard.ScoreboardComponentInitializer;

public class SchooledUniforms implements ModInitializer, ScoreboardComponentInitializer {
    public static final String MOD_ID = "schooleduniforms";

    @Override public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, _, _) -> dispatcher.register(Commands.literal("scoreboard").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)).then(Commands.literal("objectives")
                .then(Commands.literal("uniform").then(Commands.argument("objective", ObjectiveArgument.objective())
                        .executes(c -> setTracking(c.getSource(), ObjectiveArgument.getObjective(c, "objective"), true))
                        .then(Commands.argument("tracking", BoolArgumentType.bool())
                                .executes(c -> setTracking(c.getSource(), ObjectiveArgument.getObjective(c, "objective"), BoolArgumentType.getBool(c, "tracking")))))))));
    }

    private static final SimpleCommandExceptionType ERROR_ALREADY_TRACKING = new SimpleCommandExceptionType(Component.translatable("schooleduniforms.commands.uniform.already_tracking"));
    private static final SimpleCommandExceptionType ERROR_ALREADY_NOT_TRACKING = new SimpleCommandExceptionType(Component.translatable("schooleduniforms.commands.uniform.already_not_tracking"));

    private static int setTracking(CommandSourceStack source, Objective objective, boolean should) throws CommandSyntaxException {
        var component = ExtraTrackingComponent.KEY.get(source.getServer().getScoreboard());
        if (should) {
            enableTracking(source, component, objective);
        } else {
            disableTracking(source, component, objective);
        }
        return 0;
    }

    private static void enableTracking(CommandSourceStack source, ExtraTrackingComponent component, Objective objective) throws CommandSyntaxException {
        if (component.contains(objective)) throw ERROR_ALREADY_TRACKING.create();
        component.startTrackingObjective(objective);
        source.sendSuccess(() -> Component.translatable("schooleduniforms.commands.uniform.start_tracking", objective.getDisplayName()), true);
    }

    private static void disableTracking(CommandSourceStack source, ExtraTrackingComponent component, Objective objective) throws CommandSyntaxException {
        if (!component.contains(objective)) throw ERROR_ALREADY_NOT_TRACKING.create();
        component.stopTrackingObjective(objective);
        source.sendSuccess(() -> Component.translatable("schooleduniforms.commands.uniform.stop_tracking", objective.getDisplayName()), true);
    }

    @Override public void registerScoreboardComponentFactories(@NonNull ScoreboardComponentFactoryRegistry registry) {
        registry.registerScoreboardComponent(ExtraTrackingComponent.KEY, ExtraTrackingComponent::new);
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}