package net.dasunterstrich.dasshop.commands;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.dasunterstrich.dasshop.commands.internal.Command;
import net.dasunterstrich.dasshop.commands.subcommands.dasshop.AboutCommand;
import net.dasunterstrich.dasshop.commands.subcommands.dasshop.HelpCommand;
import net.dasunterstrich.dasshop.commands.subcommands.dasshop.ReloadCommand;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * The main dasShop command.<br>
 * Has various administrative sub-commands.
 */
public class DasShopCommand extends Command {

    /**
     * The main constructor.
     * @param injector the injector, provided by Guice
     */
    @Inject
    public DasShopCommand(Injector injector) {
        super("dasshop", List.of("das"), "The main dasShop command", "/das", "");

        addChild(injector.getInstance(AboutCommand.class));
        addChild(injector.getInstance(HelpCommand.class));
        addChild(injector.getInstance(ReloadCommand.class));
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
        returnWithMessage(getSyntax());
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
