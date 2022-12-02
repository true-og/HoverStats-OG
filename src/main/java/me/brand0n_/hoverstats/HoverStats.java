package me.brand0n_.hoverstats;

import me.brand0n_.hoverstats.Commands.CommandUtils;
import me.brand0n_.hoverstats.Events.EventUtils;
import me.brand0n_.hoverstats.Utils.Chat.Messages;
import me.brand0n_.hoverstats.Utils.Chat.Placeholders;
import me.brand0n_.hoverstats.Utils.Config.ConfigChecks;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class HoverStats extends JavaPlugin {
    // Plugin Instance
    public static HoverStats getInstance;

    // Variable
    public boolean usePAPI = this.getConfig().getBoolean("PAPI hook");

    @Override
    public void onEnable() {
        // Check if the config has updates
        ConfigChecks.checkUpdates(true, "Event Priority");
        // Save the config
        saveDefaultConfig();
        // Setup classes
        setupClasses();
        // Check if SoftDepends on are loaded
        checkSoftDependentPlugins();
        // Check that the config is correct
        ConfigChecks.checkConfig();
    }

    private void setupClasses() {
        getInstance = this;
        CommandUtils.init();
        EventUtils.init();
    }

    private void checkSoftDependentPlugins() {
        // Check if the plugin should try to hook into placeholderAPI
        if (usePAPI) {
            // Check if PlaceholderAPI is installed
            if (Placeholders.hasPAPI()) {
                Bukkit.getServer().getConsoleSender().sendMessage("[HoverStats] "+Messages.papiHookSuccess());
            } else {
                Bukkit.getServer().getConsoleSender().sendMessage("[HoverStats] "+Messages.papiHookFailed());
                usePAPI = false;
            }
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
