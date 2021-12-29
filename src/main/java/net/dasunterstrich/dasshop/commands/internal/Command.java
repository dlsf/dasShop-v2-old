package net.dasunterstrich.dasshop.commands.internal;

import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Represents a Command which can be invoked by the internal command system.<br>
 * A Command may have a number of sub-commands (internally called child-commands).
 */
public abstract class Command {

    private final @NotNull String name;
    private final @NotNull List<String> aliases;
    private final @NotNull String description;
    private final @NotNull String syntax;
    private final @NotNull Set<Command> childCommands = new HashSet<>();
    @Inject
    protected @NotNull ArgumentParser argumentParser;

    /**
     * The default constructor.
     *
     * @param name the name of this command
     * @param aliases the aliases of this command, may not include the name itself
     * @param description the human-readable description of this command
     * @param syntax the human-readable syntax of this command including a slash and all parent commands
     */
    public Command(@NotNull String name, @NotNull List<String> aliases, @NotNull String description, @NotNull String syntax) {
        this.name = name;
        this.aliases = aliases;
        this.description = description;
        this.syntax = syntax;
    }

    /**
     * Executes the logic of this command given the provided arguments and CommandSender.<p>
     * Will only be called when all other requirements (permissions, cooldown, ...)
     * of this command have been satisfied.
     * @param commandSender the CommandSender which invoked this command
     * @param arguments the arguments of this command, not including the parent command arguments
     * @return true if this command has been executed successfully, false otherwise
     */
    public abstract boolean execute(CommandSender commandSender, String[] arguments);

    /**
     * Returns the tab-completion for this command given the provided arguments and CommandSender.<p>
     * Will only be called when all other requirements (permissions, cooldown, ...)
     * of this command have been satisfied.
     * @param commandSender the CommandSender which requested this tab-completion
     * @param arguments the current arguments of this command, not including the parent command arguments
     * @return a list of all tab-completions for this command, will be filtered automatically
     */
    public abstract List<String> getTabCompletions(CommandSender commandSender, String[] arguments);

    /**
     * Adds a child command to this command.
     * @param child the command which should be a child of this command
     */
    protected void addChild(Command child) {
        childCommands.add(child);
    }

    /**
     * Returns an immutable copy of all child commands.
     * @return an immutable copy containing all child commands
     */
    public @NotNull List<Command> getChildren() {
        return List.copyOf(childCommands);
    }

    /**
     * Tries to find a child command with the given name.
     * @param name the name of the child command
     * @return an Optional containing the child command, empty if none has been found
     */
    public @NotNull Optional<Command> getChildByName(String name) {
        return childCommands.stream()
                .filter(command -> command.name.equalsIgnoreCase(name) || command.aliases.contains(name.toLowerCase()))
                .findAny();
    }

    /**
     * Returns the name of this command.
     * @return the name of this command
     */
    public @NotNull String getName() {
        return name;
    }

    /**
     * Returns all the aliases for this command.
     * They are an optional replacement for the name returned by {@link Command#getName()}.
     * @return the aliases for this command
     */
    public List<String> getAliases() {
        return List.copyOf(aliases);
    }

    /**
     * Returns the description of this command.
     * @return the description of this command
     */
    public @NotNull String getDescription() {
        return description;
    }

    /**
     * Returns the syntax of this command including a slash and all parent commands.
     * @return the syntax of this command
     */
    public @NotNull String getSyntax() {
        return syntax;
    }

    /**
     * Exits the current {@link Command#execute(CommandSender, String[])} method body by throwing an unchecked
     * {@link CommandException} when a condition is met.
     * @param condition the condition which should be met when exiting the method
     * @param message the message which should be sent to the {@link CommandSender} after returning
     */
    protected void returnWhen(boolean condition, String message) {
        if (condition) {
            throw new CommandException(message);
        }
    }

    /**
     * Exits the current {@link Command#execute(CommandSender, String[])} method body by throwing an unchecked
     * {@link CommandException}.
     * @param message the message which should be sent to the {@link CommandSender} after returning
     */
    protected void returnWithMessage(String message) {
        returnWhen(true, message);
    }

}
