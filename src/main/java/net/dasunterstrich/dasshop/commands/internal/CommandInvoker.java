package net.dasunterstrich.dasshop.commands.internal;

import com.google.inject.Inject;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Invokes internal commands which have been called by Bukkit and tab-completion.
 */
public class CommandInvoker implements TabExecutor {

    @Inject
    private CommandRegistry commandRegistry;

    /**
     * Called by Bukkit when a command has been executed on the Bukkit server.<p>
     * <STRONG>May only be called by Bukkit.</STRONG>
     * @param sender the sender of the command, provided by Bukkit
     * @param command the Bukkit representation of the command, provided by Bukkit
     * @param label the label of the command, provided by Bukkit
     * @param arguments the arguments of the command, provided by Bukkit
     * @return true if this command has been executed successfully, false otherwise
     */
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] arguments) {
        var executingCommand = findExecutingCommand(command.getName(), arguments)
                .orElseThrow(() -> new IllegalStateException("Unexpected command: /" + label + " " + String.join(" ", arguments)));;

        try {
            executingCommand.execute(sender, getArguments(executingCommand, arguments));
        } catch (CommandException commandException) {
            sender.sendMessage("Error occurred: " + commandException.getMessage());
        }

        return false;
    }

    /**
     * Tries to find the command which should handle this command, which may be a sub-command of a
     * registered command.
     * @param label the label of the command, also known as the base command
     * @param arguments the arguments provided by the command invocation
     * @return an Optional containing the Command which should handle this invocation, empty if there is none
     */
    public @NotNull Optional<Command> findExecutingCommand(String label, String[] arguments) {
        var executingCommandOptional = commandRegistry.find(command ->
                command.getName().equalsIgnoreCase(label) ||
                command.getAliases().contains(label.toLowerCase()));

        if (executingCommandOptional.isEmpty()) {
            return executingCommandOptional;
        }

        var executingCommand = executingCommandOptional.get();

        for (String argument : arguments) {
            Optional<Command> executingChild = executingCommand.getChildByName(argument);
            if (executingChild.isEmpty()) {
                break;
            }

            executingCommand = executingChild.get();
        }

        return Optional.of(executingCommand);
    }

    /**
     * Finds the arguments of a command invocation given the final executing command.<br>
     * The returned arguments do not contain the argument specifying the invoked sub-command or any
     * parent commands.
     * @param executingCommand the command which will be executed
     * @param arguments all the arguments of this invocation, including parent commands
     * @return the arguments of the executing command, might be empty
     */
    private @NotNull String[] getArguments(Command executingCommand, String[] arguments) {
        var index = 0;

        for (int i = 0; i < arguments.length; i++) {
            if (executingCommand.getName().equalsIgnoreCase(arguments[i])) {
                index = i + 1;
                break;
            }
        }

        return Arrays.copyOfRange(arguments, index, arguments.length);
    }

    /**
     * Returns the tab-completion for this command given the provided arguments and CommandSender.<p>
     * <STRONG>May only be called by Bukkit.</STRONG>
     * @param sender the sender of the command, provided by Bukkit
     * @param command the Bukkit representation of the command, provided by Bukkit
     * @param label the label of the command, provided by Bukkit
     * @param arguments the arguments of the command, provided by Bukkit
     * @return the list of tab-completions for this invocation
     */
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull org.bukkit.command.Command command, @NotNull String label, @NotNull String[] arguments) {
        var executingCommandOptional = findExecutingCommand(command.getName(), arguments);
        if (executingCommandOptional.isEmpty()) {
            return List.of();
        }

        var executingCommand = executingCommandOptional.get();

        // Add command tab completion + child commands to the suggestions
        List<String> completions = new ArrayList<>(executingCommand.getTabCompletions(sender, getArguments(executingCommand, arguments)));
        executingCommand.getChildren().stream()
                .map(Command::getName)
                .forEach(completions::add);

        // Remove suggestions that don't match the last argument
        if (arguments.length > 0) {
            completions.removeIf(suggestion -> !arguments[arguments.length - 1].toLowerCase().startsWith(suggestion.toLowerCase()));
        }

        return completions;
    }

}
