package dev.amymialee.schooleduniforms;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public class SchooledUniformsDataGenerator implements DataGeneratorEntrypoint {
    public void generateTranslations(FabricLanguageProvider.TranslationBuilder translationBuilder) {
        translationBuilder.add("schooleduniforms.commands.uniform.start_tracking", "Set objective %s to be tracked by player uniforms.");
        translationBuilder.add("schooleduniforms.commands.uniform.stop_tracking", "Set objective %s to not be tracked by player uniforms.");
        translationBuilder.add("schooleduniforms.commands.uniform.already_tracking", "Nothing changed. That objective is already being tracked for uniforms.");
        translationBuilder.add("schooleduniforms.commands.uniform.already_not_tracking", "Nothing changed. That objective is already not being tracked for uniforms.");
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        fabricDataGenerator.createPack().addProvider((packOutput, registryLookup) -> new SchooledTranslations(this, packOutput, registryLookup));
    }

    public static class SchooledTranslations extends FabricLanguageProvider {
        private final SchooledUniformsDataGenerator datagen;

        protected SchooledTranslations(SchooledUniformsDataGenerator datagen, FabricPackOutput packOutput, CompletableFuture<HolderLookup.Provider> registryLookup) {
            super(packOutput, registryLookup);
            this.datagen = datagen;
        }

        @Override
        public void generateTranslations(HolderLookup.Provider registryLookup, TranslationBuilder translationBuilder) {
            datagen.generateTranslations(translationBuilder);
        }
    }
}