package net.dasunterstrich.dasshop;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import net.dasunterstrich.dasshop.commands.DasShopCommand;
import net.dasunterstrich.dasshop.commands.internal.CommandInvoker;
import net.dasunterstrich.dasshop.commands.internal.CommandRegistry;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * The main class of this plugin.<br>
 * Contains the initialization logic.
 */
public class DasShop extends JavaPlugin {

    private final @NotNull Injector injector;

    /**
     * The default constructor.<p>
     * <STRONG>May only be called by Bukkit.</STRONG>
     */
    @Inject
    public DasShop() {
        this.injector = Guice.createInjector(new MainDependencyModule(this));
    }

    /**
     * The initialization logic for this plugin.
     */
    @Override
    public void onEnable() {
        registerCommands();

        getLogger().info("Plugin has been enabled successfully!");
    }

    /**
     * Registers all the commands for this plugin.
     */
    private void registerCommands() {
        var commandRegistry = injector.getInstance(CommandRegistry.class);
        commandRegistry.register(injector.getInstance(DasShopCommand.class));

        getCommand("dasshop").setExecutor(injector.getInstance(CommandInvoker.class));
    }

}
