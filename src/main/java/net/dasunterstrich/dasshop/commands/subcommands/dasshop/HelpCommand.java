package net.dasunterstrich.dasshop.commands.subcommands.dasshop;

import com.google.inject.Inject;
import net.dasunterstrich.dasshop.commands.internal.Command;
import net.dasunterstrich.dasshop.commands.internal.CommandInvoker;
import net.dasunterstrich.dasshop.commands.internal.CommandRegistry;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Command which displays various helpful information regarding the registered commands and offers a
 * command overview.
 */
public class HelpCommand extends Command {

    private final long commandsPerHelpPage = 8L;

    @Inject
    private CommandRegistry commandRegistry;
    @Inject
    private CommandInvoker commandInvoker;

    /**
     * The default command.
     */
    public HelpCommand() {
        super("help", List.of("?"), "Provides help about the dasShop commands", "/das help [page/command]", "");
    }

    /**
     * Executes the logic of this command given the provided arguments and CommandSender.<p>
     * Will only be called when all other requirements (permissions, cooldown, ...)
     * of this command have been satisfied.
     * @param commandSender the CommandSender which invoked this command
     * @param arguments the arguments of this command, not including the parent command arguments
     * @return true if this command has been executed successfully, false otherwise
     */
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

        var executingCommand = commandInvoker.findExecutingCommand(commandSender, arguments[0], Arrays.copyOfRange(arguments, 1, arguments.length));
        executingCommand.ifPresentOrElse(
                command -> sendSpecificCommandHelp(commandSender, command),
                () -> sendCommandOverview(commandSender, 1)
        );
        return true;
    }

    /**
     * Sends a page of the overview over all the main commands with their direct sub-commands.
     * @param commandSender the CommandSender which should receive the command overview
     * @param requestedPage the page of the command list
     */
    private void sendCommandOverview(CommandSender commandSender, int requestedPage) {
        var registeredCommands = commandRegistry.getAll();

        // Correct invalid pages
        int page = requestedPage;
        int availablePages = getAvailablePages();
        if (page > availablePages) {
            page = registeredCommands.size();
        } else if (page < 1) {
            page = 1;
        }

        commandSender.sendMessage(String.format("Available commands (page %d of %d):", page, availablePages));
        commandRegistry.getAllWithChildren().stream()
                .skip(commandsPerHelpPage * (page - 1))
                .limit(commandsPerHelpPage)
                .forEach(command -> commandSender.sendMessage(String.format("%s: %s", command.getSyntax(), command.getDescription())));
    }

    /**
     * Returns the number of available pages for the command overview.
     * @return the number of available pages
     */
    private int getAvailablePages() {
        return (int) Math.ceil(commandRegistry.getAllWithChildren().size() / (double) commandsPerHelpPage);
    }

    /**
     * Sends help for a specific Command to the provided CommandSender.
     * @param commandSender the CommandSender which should receive the help
     * @param command the command whose information has been requested
     */
    private void sendSpecificCommandHelp(CommandSender commandSender, Command command) {
        commandSender.sendMessage("Description: " + command.getDescription());
        getSyntax(command).forEach(commandSender::sendMessage);
    }

    /**
     * Returns the syntax of the provided command consisting of messages containing the syntax and the
     * available sub-commands.
     * @param command the command whose syntax should be returned
     * @return the messages describing the syntax of this command
     */
    private @NotNull List<String> getSyntax(Command command) {
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

    /**
     * Returns the tab-completion for this command given the provided arguments and CommandSender.<p>
     * Will only be called when all other requirements (permissions, cooldown, ...)
     * of this command have been satisfied.
     * @param commandSender the CommandSender which requested this tab-completion
     * @param arguments the current arguments of this command, not including the parent command arguments
     * @return a list of all tab-completions for this command, will be filtered automatically
     */
    @Override
    public List<String> getTabCompletions(CommandSender commandSender, String[] arguments) {
        var completions = new ArrayList<String>();

        if (arguments.length == 1) {
            // Add available page numbers to the completion
            IntStream.range(1, getAvailablePages())
                    .forEach(page -> completions.add(String.valueOf(page)));

            // Add main commands and their children to the completion
            commandRegistry.getAllWithChildren(command -> command.hasPermission(commandSender)).stream()
                    .map(Command::getName)
                    .forEach(completions::add);

            return completions;
        }

        return List.of();
    }

}
