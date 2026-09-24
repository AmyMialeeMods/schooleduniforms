package dev.amymialee.schooleduniforms.api;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.ArrayList;
import java.util.List;

public class LineItUpCodec<S> implements Codec<S> {
    private final List<Codec<S>> queue = new ArrayList<>();
    private Codec<S> primary;

    public void add(Codec<S> codec) {
        this.queue.add(codec);
    }

    public LineItUpCodec<S> primary(Codec<S> codec) {
        this.primary = codec;
        return this;
    }

    @Override
    public <T> DataResult<Pair<S, T>> decode(final DynamicOps<T> ops, final T input) {
        var result = primary == null ? null : primary.decode(ops, input);
        if (result != null && result.isSuccess()) return result;
        for (var codec : this.queue) {
            var lineResult = codec.decode(ops, input);
            if (lineResult.isSuccess()) return lineResult;
            if (result == null) result = lineResult;
        }
        return result;
    }

    public <T> DataResult<T> encode(final S input, final DynamicOps<T> ops, final T prefix) {
        var result = primary == null ? null : primary.encode(input, ops, prefix);
        if (result != null && result.isSuccess()) return result;
        for (var codec : this.queue) {
            var lineResult = codec.encode(input, ops, prefix);
            if (lineResult.isSuccess()) return lineResult;
            if (result == null) result = lineResult;
        }
        return result;
    }
}