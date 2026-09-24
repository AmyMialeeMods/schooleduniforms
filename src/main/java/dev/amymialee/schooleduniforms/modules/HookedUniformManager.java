package dev.amymialee.schooleduniforms.modules;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.device.GpuDevice;
import dev.amymialee.schooleduniforms.api.special.HookedUniform;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelExtractionEvents;
import net.minecraft.client.renderer.UniformValue;
import org.jetbrains.annotations.ApiStatus;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public interface HookedUniformManager {
    @ApiStatus.Internal HashMap<String, BiConsumer<LevelExtractionContext, Std140Builder>> HOOKED_UNIFORMS = new HashMap<>();
    @ApiStatus.Internal HashMap<String, HookedBuffer> HOOKED_BUFFERS = new HashMap<>();
    @ApiStatus.Internal record HookedBuffer(String name, int size, List<UniformValue> uniforms, GpuBuffer buffer) {}

    static void init() {
        LevelExtractionEvents.END_EXTRACTION.register(context -> {
            var closed = new HashSet<String>();
            for (var hookedBuffer : HOOKED_BUFFERS.values()) {
                if (hookedBuffer.buffer.isClosed()) {
                    closed.add(hookedBuffer.name);
                    continue;
                }
                var uniforms = hookedBuffer.uniforms;
                if (uniforms.isEmpty()) continue;
                try (MemoryStack stack = MemoryStack.stackPush()) {
                    Std140Builder builder = Std140Builder.onStack(stack, hookedBuffer.size);
                    for (int i = 0; i < uniforms.size(); i++) {
                        UniformValue uniform = uniforms.get(i);
                        var name = hookedBuffer.name + "/" + i;
                        if (HOOKED_UNIFORMS.containsKey(name)) {
                            HOOKED_UNIFORMS.get(name).accept(context, builder);
                        } else if (uniform instanceof HookedUniform hooked) {
                            hooked.writeTo(context, builder);
                        } else {
                            uniform.writeTo(builder);
                        }
                    }
                    RenderSystem.getDevice().createCommandEncoder().writeToBuffer(hookedBuffer.buffer.slice(), builder.get());
                }
            }
            closed.forEach(HOOKED_BUFFERS::remove);
        });
    }

    static void registerUniform(String name, BiConsumer<LevelExtractionContext, Std140Builder> consumer) {
        HOOKED_UNIFORMS.put(name, consumer);
    }

    static GpuBuffer manageUniforms(Map.Entry<String, List<UniformValue>> uniformGroup, String name, GpuDevice instance, Supplier<String> stringSupplier, @GpuBuffer.Usage int usage, ByteBuffer byteBuffer, int size, Operation<GpuBuffer> original) {
        var groupName = name + "/" + uniformGroup.getKey();
        var special = uniformGroup.getValue().stream().filter(HookedUniform.class::isInstance).anyMatch(_ -> true);
        var hooked = anyHooked(groupName, uniformGroup.getValue().size());
        var dynamic = special | hooked;
        if (dynamic) usage |= GpuBuffer.USAGE_COPY_DST;
        var buffer = original.call(instance, stringSupplier, usage, byteBuffer);
        if (dynamic) HOOKED_BUFFERS.put(groupName, new HookedBuffer(groupName, size, uniformGroup.getValue(), buffer));
        return buffer;
    }

    static boolean anyHooked(String name, int length) {
        for (var i = 0; i < length; i++) if (HOOKED_UNIFORMS.containsKey(name + "/" + i)) return true;
        return false;
    }
}