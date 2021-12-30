package net.dasunterstrich.dasshop;

import com.google.inject.Binder;
import com.google.inject.BindingAnnotation;
import com.google.inject.Module;
import net.dasunterstrich.dasshop.commands.internal.CommandInvoker;
import net.dasunterstrich.dasshop.commands.internal.CommandRegistry;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.logging.Logger;

/**
 * Defines all the internal dependencies via Guice.
 */
public record MainDependencyModule(DasShop dasShop) implements Module {

    @BindingAnnotation
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PluginLogger {}

    /**
     * Binds all internal main dependencies of this plugin to their instance.
     * @param binder provided by Guice
     */
    @Override
    public void configure(@NotNull Binder binder) {
        binder.bind(CommandRegistry.class).toInstance(new CommandRegistry());
        binder.bind(CommandInvoker.class).toInstance(new CommandInvoker());
        binder.bind(PluginDescriptionFile.class).toInstance(dasShop.getDescription());
        binder.bind(Logger.class).annotatedWith(PluginLogger.class).toInstance(dasShop.getLogger());
    }

}
