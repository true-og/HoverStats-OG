package me.brand0n_.hoverstats.Utils.Updates;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.brand0n_.hoverstats.HoverStats;
import me.brand0n_.hoverstats.Utils.Chat.Colors;
import me.brand0n_.hoverstats.Utils.Chat.Placeholders;
import me.brand0n_.hoverstats.Utils.Permissions.Permissions;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class UpdateChecker {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    public static boolean isOutOfDate() {
        // Get the latest update
        String latestUpdate = getLatestVersion();
        // Spit the latest version at its points
        String[] latestVersion = latestUpdate.split("\\.");

        // Split the current version at its points
        String[] currentVersions = plugin.getDescription().getVersion().split("\\.");

        // Get the number of times to loop
        int loopTimes = latestVersion.length;

        // Check if the current version has more numbers than the latest version
        if (currentVersions.length > loopTimes) {
            // Set the length to be equal to the current versions length
            loopTimes = currentVersions.length;
        }

        // Create a variable saying if the plugin is out of date
        boolean outOfDate = false;

        // Loop through all the numbers in the versions
        for (int i = 0; i < loopTimes; i++) {
            // Check if the latest version has enough info for the current iteration
            if (latestVersion.length - 1 == i) {
                // The current version is up-to-date, exit loop
                break;
            }
            // Check if the latest update is greater than the current version
            if (Integer.parseInt(latestVersion[i]) > Integer.parseInt(currentVersions[i])) {
                // The plugin is out of date
                outOfDate = true;
                // Nothing left to do break out of loop
                break;
            }
        }
        // Return the result of out of date
        return outOfDate;
    }

    public static String getLatestVersion() {
        try {
            URL url = new URL("https://api.spigotmc.org/simple/0.2/index.php?action=getResource&id=" + plugin.resourceID);
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            JsonElement currentVersion = jsonObject.get("current_version");
            return currentVersion.getAsString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getTotalResourceUpdates() {
        try {
            // Get the resource from the API
            URL url = new URL("https://api.spigotmc.org/simple/0.2/index.php?action=getResource&id=" + plugin.resourceID);
            // Read the API
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            // Get a json object
            JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
            // Get the stats of the resource
            JsonElement resourceStatus = jsonObject.get("stats");
            // Return the total number of updates
            return resourceStatus.getAsJsonObject().get("updates").getAsInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getResourceName() {
        try {
            // Get the resource from the API
            URL url = new URL("https://api.spigotmc.org/simple/0.2/index.php?action=getResource&id=" + plugin.resourceID);
            // Read the API
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            // Get a json object
            JsonObject jsonObject = new Gson().fromJson(reader, JsonObject.class);
            // Return the title of the resource
            return jsonObject.get("title").getAsString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonElement getLatestUpdate() {
        try {
            // Define a default page limit
            int pageNum = (getTotalResourceUpdates() / 10) + 1;
            // Get the latest update from the API
            URL url = new URL("https://api.spigotmc.org/simple/0.2/index.php?action=getResourceUpdates&id=" + plugin.resourceID + "&page=" + pageNum);
            // Read the API
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            // Get a json array
            JsonArray jsonArray = new Gson().fromJson(reader, JsonArray.class);

            // Return the latest update for the resource
            return jsonArray.get((getTotalResourceUpdates() % 10) - 1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getLatestUpdateLink() {
        // Get the id of the latest update
        JsonElement latestUpdateID = getLatestUpdate().getAsJsonObject().get("id");
        // Return the latest update link
        return "https://www.spigotmc.org/resources/" + getResourceName().toLowerCase() + "." + plugin.resourceID + "/update?update=" + latestUpdateID.getAsString();
    }

    public static void sendUpdateMessage(CommandSender sender, String permission) {
        // Check if the config allows for checking for updates
        if (!plugin.getConfig().getBoolean("Check for Updates", true)) {
            // The plugin doesn't allow viewing of this message, exit event
            return;
        }
        // Check if the player is op or has the required permission
        if (!Permissions.hasPermission(sender, permission) && !sender.isOp()) {
            // Player doesn't have the required permission, exit event
            return;
        }

        // Check if the current version is out of date
        if (!isOutOfDate()) {
            // The plugin is not out of date don't do anything
            return;
        }

        // Create the default
        List<String> path = List.of(
                "",
                "&7&l&m⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯",
                "&7There is a plugin update for &b" + UpdateChecker.getResourceName() + "&7!",
                "",
                "&7Current Version: &8[&a" + UpdateChecker.getLatestVersion() + "&8]",
                "&7Your Version: &8[&a" + plugin.getDescription().getVersion() + "&8]",
                "",
                "&7You can see the latest update here: &b" + "https://www.spigotmc.org/resources/" + UpdateChecker.getResourceName().toLowerCase() + "." + plugin.resourceID + "/",
                "&7&l&m⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯",
                "");
        // Create an empty base component to be used later
        BaseComponent baseComponent = new TextComponent("");
        for (int i = 0; i < path.size(); i++) {
            // Get the current message
            String message = path.get(i);
            // Get the player
            if (sender instanceof Player p) {
                // Format the message to have the right placeholders
                message = Placeholders.addPlaceholders(p, message);
                // Check if the current index is less than the total
                if (i < path.size() - 1) {
                    // Add in a line break
                    message = message + "\n";
                }
                // Convert the message to a text component
                TextComponent mainComponent = new TextComponent(TextComponent.fromLegacyText(Colors.chatColor(message)));
                // Add in a click event to open the latest update for the url
                mainComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, getLatestUpdateLink()));
                // Add the text component to the main component
                baseComponent.addExtra(mainComponent);
            }
        }

        // Send the messages to all other players that aren't ignoring
        sender.spigot().sendMessage(baseComponent);
    }

    public static void sendConsoleUpdateMessage() {
        // Check if the current version is out of date
        if (!isOutOfDate()) {
            // The plugin is not out of date don't do anything
            return;
        }

        // Tell the console that there is an update needed
        plugin.getLogger().warning(Placeholders.formatPlaceholders(
                "\\n" +
                        "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯" +
                        "The current build of " + plugin.getName() + "is outdated! Get the latest build to ensure your plugin functions properly." +
                        "Spigot: " + "https://www.spigotmc.org/resources/" + UpdateChecker.getResourceName().toLowerCase() + "." + plugin.resourceID + "/" +
                        "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
    }

}
