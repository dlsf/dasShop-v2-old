package net.dasunterstrich.dasshop.commands.subcommands.dasshop;

import net.dasunterstrich.dasshop.commands.internal.Command;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Command which reloads all config files of this plugin.
 */
public class ReloadCommand extends Command {

    /**
     * The default constructor.
     */
    public ReloadCommand() {
        super("reload", List.of(), "Reloads all plugin configuration files", "/das reload");
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
        throw new NotImplementedException("TODO");
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
        return List.of();
    }

}
