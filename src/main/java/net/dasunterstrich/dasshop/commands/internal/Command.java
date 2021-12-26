package net.dasunterstrich.dasshop.commands.internal;

import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public abstract class Command {

    private final @NotNull String name;
    private final @NotNull List<String> aliases;
    private final @NotNull String description;
    private final @NotNull String syntax;
    private final @NotNull Set<Command> childCommands = new HashSet<>();
    @Inject
    protected @NotNull ArgumentParser argumentParser;

    public Command(@NotNull String name, @NotNull List<String> aliases, @NotNull String description, @NotNull String syntax) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
        this.syntax = syntax;
    }

    public abstract boolean execute(CommandSender commandSender, String[] arguments);

    public abstract List<String> getTabCompletions(CommandSender commandSender, String[] arguments);

    protected void addChild(Command child) {
        childCommands.add(child);
    }

    public @NotNull List<Command> getChildren() {
        return List.copyOf(childCommands);
    }

    public @NotNull Optional<Command> getChildByName(String name) {
        return childCommands.stream()
                .filter(command -> command.name.equalsIgnoreCase(name) || command.aliases.contains(name.toLowerCase()))
                .findAny();
    }

    public @NotNull String getName() {
        return name;
    }

    public List<String> getAliases() {
        return List.copyOf(aliases);
    }

    public @NotNull String getDescription() {
        return description;
    }

    public @NotNull String getSyntax() {
        return syntax;
    }

    protected void returnWhen(boolean condition, String message) {
        if (condition) {
            throw new CommandException(message);
        }
    }

    protected void returnWithMessage(String message) {
        throw new CommandException(message);
    }

}
