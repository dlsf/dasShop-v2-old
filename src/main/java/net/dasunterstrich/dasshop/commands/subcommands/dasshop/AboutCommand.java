package net.dasunterstrich.dasshop.commands.subcommands.dasshop;

import com.google.inject.Inject;
import net.dasunterstrich.dasshop.commands.internal.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.List;

/**
 * Command which displays various information about this plugin.
 */
public class AboutCommand extends Command {

    @Inject
    private PluginDescriptionFile pluginDescriptionFile;

    /**
     * The default constructor.
     */
    public AboutCommand() {
        super("about", List.of("info"), "Outputs information about this plugin", "/das about");
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
        commandSender.sendMessage("Name: " + pluginDescriptionFile.getName());
        commandSender.sendMessage("Version: " + pluginDescriptionFile.getVersion());
        commandSender.sendMessage("Author: " + String.join(", ", pluginDescriptionFile.getAuthors()));
        return true;
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
