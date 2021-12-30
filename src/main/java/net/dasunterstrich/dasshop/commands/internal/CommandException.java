package net.dasunterstrich.dasshop.commands.internal;

/**
 * Exception which represents a problem during the execution of this command. This is a part of the normal
 * control flow.<p>
 * Will automatically be handled by the {@link CommandInvoker} class, which sends the message of this exception to the
 * {@link org.bukkit.command.CommandSender}.
 */
public class CommandException extends RuntimeException {

    /**
     * The default constructor.
     * @param message the message of this exception which may get sent to the {@link org.bukkit.command.CommandSender}.
     */
    public CommandException(String message) {
        super(message);
    }

}
