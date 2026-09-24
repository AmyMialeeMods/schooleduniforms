package dev.amymialee.schooleduniforms.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.amymialee.schooleduniforms.modules.ExtraUniformTypes;
import net.minecraft.client.renderer.UniformValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.function.Function;

@Mixin(UniformValue.class)
public interface UniformValueMixin {
    @WrapOperation(method = "<clinit>", at = @At(value = "INVOKE", target = "Lcom/mojang/serialization/Codec;dispatch(Ljava/util/function/Function;Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;"))
    private static <A> Codec<UniformValue> schooledUniforms$lineItUp(Codec<UniformValue.Type> instance, Function<? super UniformValue, ? extends A> type, Function<? super A, ? extends MapCodec<? extends UniformValue>> codec, Operation<Codec<UniformValue>> original) {
        return ExtraUniformTypes.UNIFORM_TYPES.primary(original.call(instance, type, codec));
    }
}