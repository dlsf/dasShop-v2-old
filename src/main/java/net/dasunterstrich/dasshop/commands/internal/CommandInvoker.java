package net.dasunterstrich.dasshop.commands.internal;

import com.google.inject.Inject;
import net.dasunterstrich.dasshop.utils.Registry;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CommandInvoker implements TabExecutor {

    @Inject
    private Registry<Command> commandRegistry;

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
