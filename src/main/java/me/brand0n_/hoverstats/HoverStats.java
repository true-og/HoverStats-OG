package me.brand0n_.hoverstats;

import me.brand0n_.hoverstats.Commands.CommandUtils;
import me.brand0n_.hoverstats.Events.EventUtils;
import me.brand0n_.hoverstats.Utils.Chat.Colors;
import me.brand0n_.hoverstats.Utils.Chat.Messages;
import me.brand0n_.hoverstats.Utils.Chat.Placeholders;
import me.brand0n_.hoverstats.Utils.Updates.ConfigChecker;
import me.brand0n_.hoverstats.Utils.Updates.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class HoverStats extends JavaPlugin {
    // Spigot id
    public String resourceID = "100245";

    // Plugin Instance
    public static HoverStats getInstance;

    // Variable
    public boolean usePAPI = getConfig().getBoolean("PAPI hook");
    public boolean useHex = false;

    @Override
    public void onEnable() {
        // Save the config
        saveDefaultConfig();
        // Check hex version
        useHex = Colors.isCorrectVersionHex();
        // Check if the config is outdated
        ConfigChecker.checkUpdates();
        ConfigChecker.checkConfig();
        // Setup classes
        setupClasses();
        // Check if SoftDepends on are loaded
        checkSoftDependentPlugins();
        // Check if the plugin needs an update
        UpdateChecker.sendConsoleUpdateMessage();
    }

    private void setupClasses() {
        getInstance = this;
        CommandUtils.init();
        EventUtils.init();
    }

    private void checkSoftDependentPlugins() {
        // Check if PlaceholderAPI is installed
        if (Placeholders.hasPAPI()) {
            Bukkit.getServer().getConsoleSender().sendMessage("[HoverStats] " + Messages.papiHookSuccess());
        } else {
            Bukkit.getServer().getConsoleSender().sendMessage("[HoverStats] " + Messages.papiHookFailed());
            usePAPI = false;
        }
    }

    public void reloadPlugin(CommandSender sender) {
        // Reload the config
        reloadConfig();
        // Save config with comments
        saveDefaultConfig();
        // Save config
        saveConfig();

        // Uninitialized Events
        EventUtils.unRegister();
        // Reinitialize Events
        EventUtils.init();

        // Send reload message
        Messages.reloaded(sender);
    }
}
