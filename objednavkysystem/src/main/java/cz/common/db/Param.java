package cz.common.db;

import java.util.Objects;

public record Param<T>(Integer order, T value, String type) {
    public Param {
        Objects.requireNonNull(order);
        Objects.requireNonNull(value);
        Objects.requireNonNull(type);
    }
}
