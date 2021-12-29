package net.dasunterstrich.dasshop.commands.internal;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Class which is able to parse String command arguments as various data types.
 */
public class ArgumentParser {

    /**
     * Tries the parse the provided argument as an {@link Integer}.<p>
     * Throws an unchecked CommandException if the argument is no integer.
     * @param argument the argument which should be parsed as an int
     * @return the parsed integer
     */
    public int asInt(String argument) {
        if (isInt(argument)) {
            return Integer.parseInt(argument);
        } else {
            throw new CommandException("Not a number");
        }
    }

    /**
     * Checks if the provided argument is an {@link Integer}.
     * @param argument the argument which should be checked
     * @return true if the provided argument is an integer, false otherwise
     */
    public boolean isInt(String argument) {
        try {
            Integer.parseInt(argument);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    /**
     * Tries the parse the provided argument as an {@link Player}.<p>
     * Throws an unchecked CommandException if the argument is no Player.
     * @param argument the argument which should be parsed as a Player
     * @return the parsed Player
     */
    public Player asPlayer(String argument) {
        var player = Bukkit.getPlayerExact(argument);
        if (player == null) {
            throw new CommandException("Player not found");
        }

        return player;
    }

}
