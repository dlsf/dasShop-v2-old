package net.dasunterstrich.dasshop;

import com.google.inject.Binder;
import com.google.inject.BindingAnnotation;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import net.dasunterstrich.dasshop.commands.internal.Command;
import net.dasunterstrich.dasshop.commands.internal.CommandInvoker;
import net.dasunterstrich.dasshop.utils.Registry;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.logging.Logger;

public record MainDependencyModule(DasShop dasShop) implements Module {

    @BindingAnnotation
    @Retention(RetentionPolicy.RUNTIME)
    public @interface PluginLogger {}

    @Override
    public void configure(@NotNull Binder binder) {
        binder.bind(new TypeLiteral<Registry<Command>>() {}).toInstance(new Registry<Command>());
        binder.bind(CommandInvoker.class).toInstance(new CommandInvoker());
        binder.bind(PluginDescriptionFile.class).toInstance(dasShop.getDescription());
        binder.bind(Logger.class).annotatedWith(PluginLogger.class).toInstance(dasShop.getLogger());
    }

}
