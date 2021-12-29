package net.dasunterstrich.dasshop.utils;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Holds a number of objects which can be registered, unregistered, searched and accessed.
 * @param <T> the type of this Registry.
 */
public class Registry<T> {

    private final Set<T> entries = new HashSet<>();

    /**
     * Adds an object to this Registry.
     * @param t the object which should be added
     */
    public void register(@NotNull T t) {
        entries.add(t);
    }

    /**
     * Removes an object from this Registry.
     * @param t the object which should be removed
     */
    public void unregister(@NotNull T t) {
        entries.remove(t);
    }

    /**
     * Returns all the elements of this Registry.
     * @return an immutable copy of the elements of this Registry
     */
    public @NotNull Set<T> getAll() {
        return Set.copyOf(entries);
    }

    /**
     * Searches all elements of this Registry.
     * @param predicate the filter of the search
     * @return an Optional containing the element, empty if none has been found
     */
    public @NotNull Optional<T> find(@NotNull Predicate<T> predicate) {
        return entries.stream()
                .filter(predicate)
                .findAny();
    }

}
