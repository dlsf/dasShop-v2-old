package net.dasunterstrich.dasshop.commands.subcommands.dasshop;

import com.google.inject.Inject;
import net.dasunterstrich.dasshop.commands.internal.Command;
import net.dasunterstrich.dasshop.commands.internal.CommandInvoker;
import net.dasunterstrich.dasshop.utils.Registry;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class HelpCommand extends Command {

    @Inject
    private Registry<Command> commandRegistry;
    @Inject
    private CommandInvoker commandInvoker;

    public HelpCommand() {
        super("help", List.of("?"), "Provides help about the dasShop commands", "/das help [page/command]");
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] arguments) {
        if (arguments.length == 0) {
            sendCommandOverview(commandSender, 1);
            return true;
        }

        if (argumentParser.isInt(arguments[0])) {
            sendCommandOverview(commandSender, argumentParser.asInt(arguments[0]));
            return true;
        }

        var executingCommand = commandInvoker.findExecutingCommand(arguments[0], Arrays.copyOfRange(arguments, 1, arguments.length));
        executingCommand.ifPresentOrElse(
                command -> sendSpecificCommandHelp(commandSender, command),
                () -> sendCommandOverview(commandSender, 1)
        );
        return true;
    }

    private void sendCommandOverview(CommandSender commandSender, int page) {
        var registeredCommands = commandRegistry.getAll();

        // Correct invalid pages
        int availablePages = getAvailablePages();
        if (page > availablePages) {
            page = registeredCommands.size();
        } else if (page < 1) {
            page = 1;
        }

        commandSender.sendMessage(String.format("Available commands (page %d of %d):", page, availablePages));
        registeredCommands.stream()
                .flatMap(command -> {
                    List<Command> commands = new ArrayList<>();
                    commands.add(command);
                    commands.addAll(command.getChildren());
                    return commands.stream();
                })
                .skip(8L * (page - 1))
                .limit(8)
                .forEach(command -> commandSender.sendMessage(String.format("%s: %s", command.getSyntax(), command.getDescription())));
    }

    private int getAvailablePages() {
        var registeredCommands = commandRegistry.getAll();
        var childCommands = registeredCommands.stream()
                .flatMap(command -> command.getChildren().stream())
                .toList();
        var registeredCommandAmount = registeredCommands.size();
        var childCommandAmount = childCommands.size();
        return (int) Math.ceil((registeredCommandAmount + childCommandAmount) / 8D);
    }

    private void sendSpecificCommandHelp(CommandSender commandSender, Command command) {
        commandSender.sendMessage("Description: " + command.getDescription());
        getSyntax(command).forEach(commandSender::sendMessage);
    }

    public @NotNull List<String> getSyntax(Command command) {
        var syntax = new ArrayList<String>();

        syntax.add("Syntax: " + command.getSyntax());

        if (!command.getChildren().isEmpty()) {
            syntax.add("Subcommands:");
            command.getChildren().stream()
                    .map(Command::getName)
                    .forEach(childName -> syntax.add("- " + childName));
        }

        return syntax;
    }

    @Override
    public List<String> getTabCompletions(CommandSender commandSender, String[] arguments) {
        if (arguments.length != 1) {
            return List.of();
        }

        var completions = new ArrayList<String>();
        var registeredCommands = commandRegistry.getAll();

        // Add available page numbers to the completion
        IntStream.range(1, getAvailablePages())
                .forEach(page -> completions.add(String.valueOf(page)));

        // Add main commands to the completion
        registeredCommands.stream()
                .map(Command::getName)
                .forEach(completions::add);

        // Add child commands to the completion
        registeredCommands.stream()
                .flatMap(command -> command.getChildren().stream())
                .map(Command::getName).forEach(completions::add);

        return completions;
    }

}
