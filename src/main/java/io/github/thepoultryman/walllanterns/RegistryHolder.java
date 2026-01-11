package io.github.thepoultryman.walllanterns;

import java.util.function.Supplier;

public record RegistryHolder<T>(Supplier<T> inner) {
    public T get() {
        return this.inner.get();
    }
}
