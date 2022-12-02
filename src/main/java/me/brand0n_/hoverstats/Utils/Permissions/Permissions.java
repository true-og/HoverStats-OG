package me.brand0n_.hoverstats.Utils.Permissions;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Permissions {
    public static boolean hasPermission(CommandSender sender, String permission) {
        if (!(sender instanceof Player)) {
            return true;
        }
        return sender.hasPermission(permission);
    }
}
