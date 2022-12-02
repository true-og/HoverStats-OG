package me.brand0n_.hoverstats.Commands;

import me.brand0n_.hoverstats.Utils.Permissions.Permissions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HoverStatsTabHandler implements TabCompleter {

    public List<String> onTabComplete(final @NotNull CommandSender sender, final @NotNull Command cmd, final @NotNull String label, final String[] args) {
        final List<String> result = new ArrayList<>();

        if (args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "help":
                    if (Permissions.hasPermission(sender, "hoverstats.help")) {
                        result.add("help");
                    }
                case "reload":
                    if (Permissions.hasPermission(sender, "hoverstats.reload")) {
                        result.add("reload");
                    }
            }
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }
}
