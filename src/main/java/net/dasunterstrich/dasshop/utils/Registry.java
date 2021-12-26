package net.dasunterstrich.dasshop.utils;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class Registry<T> {

    private final Set<T> entries = new HashSet<>();

    public void register(@NotNull T t) {
        entries.add(t);
    }

    public void unregister(@NotNull T t) {
        entries.remove(t);
    }

    public @NotNull Set<T> getAll() {
        return Set.copyOf(entries);
    }

    public @NotNull Optional<T> find(@NotNull Predicate<T> predicate) {
        return entries.stream()
                .filter(predicate)
                .findAny();
    }

}
