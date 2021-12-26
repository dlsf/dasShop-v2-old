package net.dasunterstrich.dasshop;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import net.dasunterstrich.dasshop.commands.DasShopCommand;
import net.dasunterstrich.dasshop.commands.internal.Command;
import net.dasunterstrich.dasshop.commands.internal.CommandInvoker;
import net.dasunterstrich.dasshop.utils.Registry;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class DasShop extends JavaPlugin {

    private final @NotNull Injector injector;

    @Inject
    public DasShop() {
        this.injector = Guice.createInjector(new MainDependencyModule(this));
    }

    @Override
    public void onEnable() {
        registerCommands();

        getLogger().info("Plugin has been enabled successfully!");
    }

    private void registerCommands() {
        Registry<Command> commandRegistry = injector.getInstance(Key.get(new TypeLiteral<Registry<Command>>() {}));
        commandRegistry.register(injector.getInstance(DasShopCommand.class));

        getCommand("dasshop").setExecutor(injector.getInstance(CommandInvoker.class));
    }

}
