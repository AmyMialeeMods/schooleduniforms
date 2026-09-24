package dev.amymialee.schooleduniforms;

import dev.amymialee.schooleduniforms.modules.ExtraUniformTypes;
import dev.amymialee.schooleduniforms.modules.HookedUniformManager;
import net.fabricmc.api.ClientModInitializer;

public class SchooledUniformsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ExtraUniformTypes.init();
        HookedUniformManager.init();
    }
}