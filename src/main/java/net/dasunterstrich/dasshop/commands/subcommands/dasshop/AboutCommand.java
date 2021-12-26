package net.dasunterstrich.dasshop.commands.subcommands.dasshop;

import com.google.inject.Inject;
import net.dasunterstrich.dasshop.commands.internal.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginDescriptionFile;

import java.util.List;

public class AboutCommand extends Command {

    @Inject
    private PluginDescriptionFile pluginDescriptionFile;

    public AboutCommand() {
        super("about", List.of("info"), "Outputs information about this plugin", "/das about");
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] arguments) {
        commandSender.sendMessage("Name: " + pluginDescriptionFile.getName());
        commandSender.sendMessage("Version: " + pluginDescriptionFile.getVersion());
        commandSender.sendMessage("Author: " + String.join(", ", pluginDescriptionFile.getAuthors()));
        return true;
    }

    @Override
    public List<String> getTabCompletions(CommandSender commandSender, String[] arguments) {
        return List.of();
    }

}
