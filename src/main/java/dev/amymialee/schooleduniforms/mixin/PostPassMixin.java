package dev.amymialee.schooleduniforms.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.device.GpuDevice;
import dev.amymialee.schooleduniforms.modules.HookedUniformManager;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.UniformValue;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mixin(PostPass.class)
public class PostPassMixin {
    @Shadow @Final private String name;

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lcom/mojang/renderpearl/api/device/GpuDevice;createBuffer(Ljava/util/function/Supplier;ILjava/nio/ByteBuffer;)Lcom/mojang/renderpearl/api/buffers/GpuBuffer;"))
    private GpuBuffer schooledUniforms$remember(GpuDevice instance, Supplier<String> stringSupplier, @GpuBuffer.Usage int usage, ByteBuffer byteBuffer, Operation<GpuBuffer> original,
                                                @Local(name = "uniformGroup") Map.Entry<String, List<UniformValue>> uniformGroup, @Local(name = "size") int size) {
        return HookedUniformManager.manageUniforms(uniformGroup, this.name, instance, stringSupplier, usage, byteBuffer, size, original);
    }
}