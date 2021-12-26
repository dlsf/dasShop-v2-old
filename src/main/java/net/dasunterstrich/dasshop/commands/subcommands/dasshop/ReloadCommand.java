package net.dasunterstrich.dasshop.commands.subcommands.dasshop;

import net.dasunterstrich.dasshop.commands.internal.Command;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("reload", List.of(), "Reloads all plugin configuration files", "/das reload");
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] arguments) {
        throw new NotImplementedException("TODO");
    }

    @Override
    public List<String> getTabCompletions(CommandSender commandSender, String[] arguments) {
        return List.of();
    }

}
