package net.dasunterstrich.dasshop.commands.internal;

import net.dasunterstrich.dasshop.utils.Registry;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * A {@link Registry} which holds {@link Command}s containing utility methods tailored for commands.
 */
public class CommandRegistry extends Registry<Command> {

    /**
     * Returns a copy of this Registry containing all Commands of this Registry in addition to all of their
     * sub-commands.
     * @return all commands with their sub-commands
     */
    public Set<Command> getAllWithChildren() {
        return getAllWithChildren(command -> true);
    }

    /**
     * Returns a copy of this Registry containing all Commands of this Registry in addition to all of their
     * sub-commands which meet a certain condition.
     * @return all commands with their sub-commands
     */
    public Set<Command> getAllWithChildren(Predicate<Command> predicate) {
        var set = new HashSet<Command>();

        for (var command : getAll()) {
            if (!predicate.test(command)) {
                continue;
            }

            set.add(command);
            command.getChildren().stream()
                    .filter(predicate)
                    .forEach(set::add);
        }

        return set;
    }

}
