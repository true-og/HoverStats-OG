package me.brand0n_.hoverstats.Utils.Permissions;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class Permissions {
    public static boolean hasPermission(CommandSender sender, String permission) {
        // Check if the person trying to run this command is a server entity
        if (!(sender instanceof Player)) {
            // Player is a server entity, return true
            return true;
        }
        // Return weather or not the player has the permission to run this command
        return sender.hasPermission(permission);
    }

    /**
     * This method will check weather or not the player has a specific permission by checking if the
     * player has a specific permission prefix, followed by the final prefix node.
     * <br />
     * <br />
     * Ex: [permission node].[node]
     * <br />
     * Ex: home.1
     * <br />
     * <br />
     *
     * @param sender     the player you want to check their permission
     * @param permission the permission that you want to check
     * @param node       the node that you want to check
     * @return weather or not the player has that node
     */
    public static boolean hasPermission(CommandSender sender, String permission, String node) {
        // Check if the person trying to run this command is a server entity
        if (!(sender instanceof Player)) {
            // Player is a server entity, return true
            return true;
        }
        // Loop through all the permissions that they have with that starting permission
        for (PermissionAttachmentInfo permInfo : sender.getEffectivePermissions()) {
            // Create an element for the temp permission info
            String tempPermission = permInfo.getPermission();
            // Check if the current permission is the one were looking for
            if (permInfo.getPermission().startsWith(permission)) {
                // Get the final permission node after the permission
                String permissionNode = tempPermission.substring(tempPermission.lastIndexOf(".") + 1);
                // Check weather or not the player has the specified node
                if (permissionNode.equalsIgnoreCase(node)) {
                    // Player has the correct permission node, return true
                    return true;
                }
            }
        }
        // Player didn't have any permission node matching the one provided, return false
        return false;
    }
}
