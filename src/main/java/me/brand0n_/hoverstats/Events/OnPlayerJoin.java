package me.brand0n_.hoverstats.Events;

import me.brand0n_.hoverstats.HoverStats;
import me.brand0n_.hoverstats.Utils.Chat.Colors;
import me.brand0n_.hoverstats.Utils.Chat.Placeholders;
import me.brand0n_.hoverstats.Utils.Hover.HoverUtils;
import me.brand0n_.hoverstats.Utils.Permissions.Permissions;
import me.brand0n_.hoverstats.Utils.Updates.UpdateChecker;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.EventExecutor;
import org.jetbrains.annotations.NotNull;

public class OnPlayerJoin implements EventExecutor, Listener {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    @Override
    public void execute(@NotNull Listener listener, @NotNull Event event) {
        this.onPlayerChat((PlayerJoinEvent) event);
    }

    @EventHandler
    public void onPlayerChat(PlayerJoinEvent e) {
        // Send player the clickable message
        UpdateChecker.sendUpdateMessage(e.getPlayer(), "hoverstats.update");
        // Get the player
        Player p = e.getPlayer();

        // Check if the plugin is using its own formatting
        if (!plugin.getConfig().getBoolean("Join Formatting.Use Formatting", true)) {
            // Plugin doesn't want to use its own formatting, return
            return;
        }

        // Check if the player has the permission to run this
        if (!Permissions.hasPermission(p, "hoverstats.join-formatting")) {
            // Player doesn't have the permission to use join formatting, don't send a message
            return;
        }
        
        // Check if the join message is set to null
        if (e.getJoinMessage() == null) {
            // Check if the player has debug messages enabled
            if (plugin.getConfig().getBoolean("Debug Mode", false)) {
                // Print to console that another plugin may have already changed the join message
                Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("[HoverStats]&c The join message is currently set to 'null', this means that another plugin is likely interfering with the join message."));
                // Print to console possible solutions
                Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("[HoverStats]&c Changing the 'Join Event Priority' in the config may fix this issue, other than that contact the support discord."));
            }
            // Another plugin has changed the join  message, more than likely a vanish plugin don't do anything
            return;
        }

        // Get the message from config
        String joinMessage = plugin.getConfig().getString("Join Formatting.First Join Message Format", "&7Welcome &b%displayname%&7 to the server!");

        // Set the join message to be nothing
        e.setJoinMessage(null);

        // Check if this is the player's first time joining the server
        if (p.hasPlayedBefore()) {
            // Set the join message to be the joined before message
            joinMessage = plugin.getConfig().getString("Join Formatting.Join Message Format", "&8[&a+&8] &7%displayname%");
        }

        // Format the join message
        joinMessage = Placeholders.addPlaceholders(p, joinMessage);

        // Get the final output message
        TextComponent hoverMessage = formatJoinHoverMessage(p, joinMessage, p.hasPlayedBefore());

        // Get a list of all online players
        for (Player targetPlayer : Bukkit.getServer().getOnlinePlayers()) {
            // Send the target player the join message
            targetPlayer.spigot().sendMessage(hoverMessage);
        }
        // Send console the message
        Bukkit.getServer().getConsoleSender().sendMessage(joinMessage);
        // Cancel the event
        e.setJoinMessage(null);
    }

    private TextComponent formatJoinHoverMessage(Player p, String message, boolean isNotFirstJoin) {
        // Make an empty main message element for future use
        TextComponent mainMessage = new TextComponent();
        // Create the hover event by calling on another method
        TextComponent hoverEvents = HoverUtils.setupHoverFirstJoinMessage(p, message);

        // Check if this is the first join message
        if (isNotFirstJoin) {
            // Create the hover event by calling on another method
            hoverEvents = HoverUtils.setupHoverJoinMessage(p, message);
        }

        // Add the hover message to the final output
        mainMessage.addExtra(hoverEvents);
        // Return the final output
        return mainMessage;
    }
}