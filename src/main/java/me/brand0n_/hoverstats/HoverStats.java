package me.brand0n_.hoverstats;

import me.brand0n_.hoverstats.Commands.CommandUtils;
import me.brand0n_.hoverstats.Events.EventUtils;
import me.brand0n_.hoverstats.Utils.Chat.ChatBuilder;
import me.brand0n_.hoverstats.Utils.Chat.Colors;
import me.brand0n_.hoverstats.Utils.Chat.Placeholders;
import me.brand0n_.hoverstats.Utils.Help.HelpUtils;
import me.brand0n_.hoverstats.Utils.Hover.HoverUtils;
import me.brand0n_.hoverstats.Utils.Variables.Variable;
import me.brand0n_.hoverstats.Utils.Version.VersionUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class HoverStats extends JavaPlugin {
    // Plugin Instance
    public static HoverStats getInstance;

    // Setup Classes
    public VersionUtils version;
    public Variable variable;
    public Colors colors;
    public Placeholders placeholders;
    public HelpUtils helpUtils;
    public ChatBuilder messages;
    public HoverUtils hoverUtils;

    // Variable
    public boolean usePAPI = this.getConfig().getBoolean("PAPI hook");

    @Override
    public void onEnable() {
        // Save the config
        saveDefaultConfig();
        // Setup classes
        setupClasses();
        // Check if Soft Depends are loaded
        checkDependentPlugins();
    }

    private void setupClasses() {
        getInstance = this;
        colors = new Colors();
        version = new VersionUtils();
        placeholders = new Placeholders();
        helpUtils = new HelpUtils();
        messages = new ChatBuilder();
        hoverUtils = new HoverUtils();
        CommandUtils.init();
        EventUtils.init();
    }

    private void checkDependentPlugins() {
        variable = new Variable();
        // Check if the plugin should try to hook into placeholderAPI
        if (usePAPI) {
            // Check if the PlaceholderAPI is installed
            if (placeholders.hasPAPI()) {
                getLogger().info(messages.papiHookSuccess());
            } else {
                getLogger().severe(messages.papiHookFailed());
                getServer().getPluginManager().disablePlugin(this);
            }
        }
    }

    public void reloadPlugin(CommandSender sender) {
        // Reload the config
        reloadConfig();
        // Save config with comments
        saveDefaultConfig();

        // Send reloaded message
        sender.sendMessage(messages.reloaded(sender));
    }
    public void reloadPluginConsole(CommandSender sender) {
        // Reload the config
        reloadConfig();
        // Save config with comments
        saveDefaultConfig();

        // Send reloaded message
        getLogger().info(messages.reloaded(sender));
    }
}
