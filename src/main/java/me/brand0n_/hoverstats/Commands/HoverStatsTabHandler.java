package me.brand0n_.hoverstats.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HoverStatsTabHandler implements TabCompleter {

    List<String> arguments1;

    public HoverStatsTabHandler() {
        this.arguments1 = new ArrayList<>();
    }

    public List<String> onTabComplete(final @NotNull CommandSender sender, final @NotNull Command cmd, final @NotNull String label, final String[] args) {
        if (this.arguments1.isEmpty()) {
            this.arguments1.add("help");
        }
        final List<String> result = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("hoverstats.*") || sender.hasPermission("hoverstats.reload") || sender.isOp()) {
                result.add("reload");
            }
            for (final String a : this.arguments1) {
                if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
                    result.add(a);
                }
            }
            return result;
        }

        return null;
    }
}
