package net.dasunterstrich.dasshop;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIConfig;
import org.bukkit.plugin.java.JavaPlugin;

public class DasShop extends JavaPlugin {

    @Override
    public void onLoad() {
        CommandAPI.onLoad(new CommandAPIConfig().verboseOutput(true));
    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable(this);

        // TODO: Startup logic
    }

}
