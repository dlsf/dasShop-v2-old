package net.dasunterstrich.dasshop.commands.internal;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ArgumentParser {

    public int asInt(String argument) {
        if (isInt(argument)) {
            return Integer.parseInt(argument);
        } else {
            throw new CommandException("Not a number");
        }
    }

    public boolean isInt(String argument) {
        try {
            Integer.parseInt(argument);
            return true;
        } catch (NumberFormatException exception) {
            return false;
        }
    }

    public Player asPlayer(String argument) {
        var player = Bukkit.getPlayerExact(argument);
        if (player == null) {
            throw new CommandException("Player not found");
        }

        return player;
    }

}
