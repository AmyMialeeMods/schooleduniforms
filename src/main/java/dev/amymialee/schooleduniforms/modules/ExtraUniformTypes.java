package dev.amymialee.schooleduniforms.modules;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import dev.amymialee.schooleduniforms.SchooledUniforms;
import dev.amymialee.schooleduniforms.api.LineItUpCodec;
import dev.amymialee.schooleduniforms.api.special.*;
import dev.amymialee.schooleduniforms.api.uniforms.*;
import net.minecraft.client.renderer.UniformValue;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;

public interface ExtraUniformTypes {
    @ApiStatus.Internal LineItUpCodec<UniformValue> UNIFORM_TYPES = new LineItUpCodec<>();

    static <T extends UniformValue> void addUniformType(Identifier id, Class<T> type, MapCodec<T> codec) {
        UNIFORM_TYPES.add(Identifier.CODEC.partialDispatch("type",
                v -> type.isInstance(v) ? DataResult.success(id) : DataResult.error(() -> "Isn't one of this uniformType."),
                i -> i.equals(id) ? DataResult.success(codec) : DataResult.error(() -> "Doesn't match this uniformType")));
    }

    static void init() {
        ExtraUniformTypes.addUniformType(SchooledUniforms.id("float_array"), FloatArrayUniform.class, FloatArrayUniform.CODEC);
        ExtraUniformTypes.addUniformType(SchooledUniforms.id("int_array"), IntArrayUniform.class, IntArrayUniform.CODEC);
        ExtraUniformTypes.addUniformType(SchooledUniforms.id("ivec3_array"), IVec3ArrayUniform.class, IVec3ArrayUniform.CODEC);
        ExtraUniformTypes.addUniformType(SchooledUniforms.id("matrix4x4_array"), Matrix4x4ArrayUniform.class, Matrix4x4ArrayUniform.CODEC);
        ExtraUniformTypes.addUniformType(SchooledUniforms.id("vec2_array"), Vec2ArrayUniform.class, Vec2ArrayUniform.CODEC);
        ExtraUniformTypes.addUniformType(SchooledUniforms.id("vec3_array"), Vec3ArrayUniform.class, Vec3ArrayUniform.CODEC);
        ExtraUniformTypes.addUniformType(SchooledUniforms.id("vec4_array"), Vec4ArrayUniform.class, Vec4ArrayUniform.CODEC);
        ExtraUniformTypes.addUniformType(SchooledUniforms.id("scoreboard"), ScoreboardUniform.class, ScoreboardUniform.CODEC);
        ExtraUniformTypes.addUniformType(SchooledUniforms.id("scoreboard_self"), ScoreboardSelfUniform.class, ScoreboardSelfUniform.CODEC);
        ExtraUniformTypes.addUniformType(SchooledUniforms.id("scoreboard_array"), ScoreboardArrayUniform.class, ScoreboardArrayUniform.CODEC);
        ExtraUniformTypes.addUniformType(SchooledUniforms.id("camera_pos"), CameraPosUniform.class, CameraPosUniform.CODEC);
        ExtraUniformTypes.addUniformType(SchooledUniforms.id("projection"), ProjectionUniform.class, ProjectionUniform.CODEC);
        ExtraUniformTypes.addUniformType(SchooledUniforms.id("inv_projection"), InvProjectionUniform.class, InvProjectionUniform.CODEC);
        ExtraUniformTypes.addUniformType(SchooledUniforms.id("view"), ViewRotationUniform.class, ViewRotationUniform.CODEC);
        ExtraUniformTypes.addUniformType(SchooledUniforms.id("inv_view"), InvViewRotationUniform.class, InvViewRotationUniform.CODEC);
        ExtraUniformTypes.addUniformType(SchooledUniforms.id("tick_delta"), TickDeltaUniform.class, TickDeltaUniform.CODEC);
    }
}