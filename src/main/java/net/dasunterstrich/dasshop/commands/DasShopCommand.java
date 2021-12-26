package net.dasunterstrich.dasshop.commands;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.dasunterstrich.dasshop.commands.internal.Command;
import net.dasunterstrich.dasshop.commands.subcommands.dasshop.AboutCommand;
import net.dasunterstrich.dasshop.commands.subcommands.dasshop.HelpCommand;
import net.dasunterstrich.dasshop.commands.subcommands.dasshop.ReloadCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public class DasShopCommand extends Command {

    @Inject
    public DasShopCommand(Injector injector) {
        super("dasshop", List.of("das"), "The main dasShop command", "/das");

        addChild(injector.getInstance(AboutCommand.class));
        addChild(injector.getInstance(HelpCommand.class));
        addChild(injector.getInstance(ReloadCommand.class));
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] arguments) {
        returnWithMessage(getSyntax());
        return true;
    }

    @Override
    public List<String> getTabCompletions(CommandSender commandSender, String[] arguments) {
        return List.of();
    }

}
