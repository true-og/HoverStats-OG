package me.brand0n_.hoverstats.Utils.Updates;

import me.brand0n_.hoverstats.HoverStats;
import me.brand0n_.hoverstats.Utils.Chat.Colors;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings({"ResultOfMethodCallIgnored", "BooleanMethodIsAlwaysInverted"})
public class ConfigChecker {
    private static final HoverStats plugin = HoverStats.getPlugin(HoverStats.class); // Get this from main

    public static void checkConfig() {
        // Check if the plugin is using HoverStats chat formatting
        if (!plugin.getConfig().getBoolean("Chat Formatting.Use Formatting", true)) {
            // Not using HoverStats chat formatting, exit checks
            return;
        }
        // Check if the chat format has the %message% placeholder
        if (!plugin.getConfig().getString("Chat Formatting.Format", "&7%displayname% &8&l> &7%message%").contains("%message%")) {
            // Tell the user the formatting is wrong
            plugin.getLogger().severe("Could not find \"%message%\" in the chat format. Please take a look to ensure that it is included, chat formatting will not be used.");
            // Turn off the chat formatting
            plugin.getConfig().set("Chat Formatting.Use Formatting", false);
            // Save the config
            plugin.saveConfig();
            // Reload the config
            plugin.reloadConfig();
            // Save config with comments
            plugin.saveDefaultConfig();
            // Save config
            plugin.saveConfig();
        }
    }

    public static void checkUpdates() {
        // Check if the config file exists
        plugin.reloadConfig();
        // Check if the plugin's config is out of date
        if (!isOutOfDate(plugin.getConfig().getString("Version", "0"))) {
            return;
        }
        // Get the config file
        File oldConfig = new File(plugin.getDataFolder() + "/config.yml");
        // Check if the config file exists
        if (!oldConfig.exists()) {
            // Config file doesn't exist, don't do anything.
            return;
        }
        // Get current time
        String currDate = getDate();
        // Declare a file number
        int num = 1;
        // Declare a variable for the new location of the file to save into
        String fileName = currDate + "-%num%.yml";
        // Save the new location for the config file
        File newConfig = new File(plugin.getDataFolder() + "/old configs", fileName.replace("%num%", String.valueOf(num)));

        // Get the proper file version
        fileName = fileVersion(newConfig, fileName, num);

        // Update the new config file
        newConfig = new File(plugin.getDataFolder() + "/old configs", fileName.replace("%num%", String.valueOf(num)));

        // Create the new config file
        newConfig = createFile(newConfig);

        // Check if the file was able to be created.
        if (newConfig == null) {
            // File wasn't able to be created, stop checking config.
            return;
        }

        if (!copyData(newConfig)) {
            // Stop checking config
            return;
        }
        // Delete old config file
        if (!deleteOldConfig(newConfig)) {
            // File wasn't able to be created, stop checking config.
            return;
        }
        // Create the new config file
        plugin.saveDefaultConfig();
        // Reload the config
        plugin.reloadConfig();
        // Save config with comments
        plugin.saveDefaultConfig();
        // Save config
        plugin.saveConfig();
        // Sync the data with the new config file
        transferOldData(newConfig);
    }


    public static boolean isOutOfDate(String earlierVersion) {
        // Create a variable saying if the plugin is out of date
        boolean outOfDate = false;

        // Remove any extra information
        earlierVersion = earlierVersion.split(" ")[0];

        // Check if the earlier version of the config is 0
        if (earlierVersion.equals("0")) {
            // Return true
            return true;
        }

        // Check if the current version of the config is equal to the defined config version
        if (!earlierVersion.equalsIgnoreCase(plugin.getDescription().getVersion().replace(" BETA", "").trim())) {
            // Config is out of date, set it accordingly
            outOfDate = true;
        }

        // Return the result of out of date
        return outOfDate;
    }

    /**
     * This method will format the date to the following format:
     * <br />
     * (year-day-month)
     * <br /><br />
     *
     * @return the formatted date as a string
     */
    private static String getDate() {
        // Format the current date
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-dd-MM");
        // Get the current time
        LocalDateTime currTime = LocalDateTime.now();
        // Return the formatted date time as a string
        return dateFormat.format(currTime);
    }


    /**
     * This method creates a formatted version of the old file, using the current date and checking if there are any other files that already exist.
     * <br /><br />
     * If the file already exists then this method will add a number after the file name to differentiate the files.
     * <br /><br />
     *
     * @return the accurate file name
     */
    private static String fileVersion(File newFile, String fileName, int num) {
        // Check if the file exists
        if (newFile.exists()) {
            do {
                // Check if the file exists
                if (!newFile.exists()) {
                    // File doesn't exist, exit the loop
                    break;
                }
                // File exists change the name to have a (#)
                newFile.renameTo(new File(plugin.getDataFolder() + "/old configs", fileName.replace("%num%", String.valueOf(num))));
                // Increment the file number
                num++;
            } while (newFile.exists());
            // Assign the file name to be the accurate file iteration
            fileName = fileName.replace("%num%", String.valueOf(num));
        }
        // Assign the file name to be the accurate file iteration
        return fileName.replace("%num%", String.valueOf(num));
    }


    /**
     * This method creates the new file and saves it to the file
     * <br />
     * "/old configs/[date](version).yml".
     * <br /><br />
     *
     * @return the file that was created <br /> if no file was created then the return will be "null"
     */
    private static File createFile(File newFile) {
        // Try to create the new location for the config file
        try {
            // Check if the parent file exists
            if (!newFile.getParentFile().exists()) {
                // Send message saying there was an issue with the parent file
                Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("[" + plugin.getName() + "]" + "&9⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯[ &fParent File Issue &9]⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
                // Tell user the parent file is being created
                Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("[" + plugin.getName() + "]" + " &cERROR &8| &7Parent file couldn't be found, attempting to create it."));
                // Try to create the new parent directory
                if (!newFile.getParentFile().mkdir()) {
                    // Print barrier
                    Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("[" + plugin.getName() + "]" + "&9⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
                    // Tell user there was an error with creating the parent directory
                    Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("[" + plugin.getName() + "]" + " &cERROR &8| &7Old config file was unable to be moved. Aborting updating configuration file! " +
                            "(Reason: Parent file couldn't be created.)"));
                    // Print barrier
                    Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("[" + plugin.getName() + "]" + "&9⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
                    return null;
                }
                // Tell the user the file was created
                Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("[" + plugin.getName() + "]" + " &eINFO  &8| &7Parent file Created."));
                // Print barrier
                Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("[" + plugin.getName() + "]" + "&9⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
            }
            // Create a new file for the config file
            newFile.createNewFile();
            Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("[" + plugin.getName() + "]" + " &eINFO  &8| &7Destination File created."));
        } catch (IOException e) {
            // Print barrier
            Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("[" + plugin.getName() + "]" + "&9⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
            // File failed to be moved or wasn't found, tell user
            Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("[" + plugin.getName() + "]" + " &cERROR &8| &7Old config file was unable to be moved. Aborting updating configuration file " +
                    "(Reason: Error creating new file."));
            // Print barrier
            Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("[" + plugin.getName() + "]" + "&9⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
            // Stop checking config
            return null;
        }
        return newFile;
    }


    /**
     * This method copies data from one file to another file
     * <br /><br />
     *
     * @param destination the destination file you'd like the copied data to go to
     */
    private static boolean copyData(File destination) {
        try {
            // Get the source input stream
            FileInputStream src = new FileInputStream(plugin.getDataFolder() + "/config.yml");
            // Get the destination output stream
            FileOutputStream dest = new FileOutputStream(destination);

            // Copy all data from the source file to the destination file
            dest.getChannel().transferFrom(src.getChannel(), 0, src.getChannel().size());
            // Close the source stream
            src.close();
            // Close the destination stream
            dest.close();
            return true;
        } catch (IOException | SecurityException e) {
            // Print barrier
            Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("&9⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
            // File failed to be moved or wasn't found, tell user
            Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("[" + plugin.getName() + "]" + " &cERROR &8| &7Old config file was unable to be moved. Aborting updating configuration file " +
                    "(Reason: Couldn't move config file.)"));
            // Print barrier
            Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("&9⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
            return false;
        }
    }


    /**
     * This method deletes the old file, if the file cannot be deleted it will remove the newly created config as well
     * <br />
     * "/old configs/[date](version).yml".
     * <br /><br />
     *
     * @param newFile the new file that was created
     *                <br /><br />
     * @return true if the file was successfully deleted & <br /> false if the file wasn't able to be deleted
     */
    private static boolean deleteOldConfig(File newFile) {
        // Save config
        File oldConfig = new File(plugin.getDataFolder() + "/config.yml");
        // Delete old config file
        if (!oldConfig.delete()) {
            // Print barrier
            Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("&9⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
            // File failed to be moved or wasn't found, tell user
            Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("[" + plugin.getName() + "]" + " &cERROR &8| &7Old config file was unable to be deleted. Aborting updating configuration file " +
                    "(Reason: Couldn't delete config file.)"));
            // Remove the new file
            if (newFile.delete()) {
                // Tell the user th new file was removed
                Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("[" + plugin.getName() + "]" + " &eINFO  &8| &7New configuration file cleaned up."));
                // Check if there is any data inside the parent file
                if (newFile.getParentFile().length() == 0) {
                    // Tell the user the new file was removed
                    Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("[" + plugin.getName() + "]" + " &eINFO  &8| &7There is nothing left inside of the parent directory, attempting to remove folder."));
                    // Remove the parent file
                    if (newFile.getParentFile().delete()) {
                        // Tell the user the directory was removed
                        Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("[" + plugin.getName() + "]" + " &eINFO  &8| &7Parent directory has been successfully deleted."));
                    } else {
                        // Tell the user the new file wasn't able to be removed
                        Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("[" + plugin.getName() + "]" + " &cERROR &8| &7Unable to delete the new file's parent directory."));
                    }
                }
                return false;
            } else {
                // Tell the user old config file was deleted
                Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("[" + plugin.getName() + "]" + " &cERROR &8| &7Unable to delete the new configuration file."));
            }
        } else {
            Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("[" + plugin.getName() + "]" + " &eINFO  &8| &7Old configuration file has been successfully deleted."));
            // Print barrier
        }
        Bukkit.getServer().getConsoleSender().sendMessage(Colors.chatColor("&9⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));
        return true;
    }


    /**
     * This method saves all data from previous versions of the plugin into the config file
     * <br /><br />
     *
     * @param file the file that you'd like to use to get the data back
     */
    private static void transferOldData(File file) {
        // Get the YAML properties of the old file
        FileConfiguration oldConfig = YamlConfiguration.loadConfiguration(file);

        // Loop through all data in oldConfig
        for (String elm : oldConfig.getKeys(false)) {
            // Check if the element is version
            if (elm.equalsIgnoreCase("Version")) {
                // Keep going this element must not be changed
                continue;
            }
            // Loop through all data, saving it;
            copyConfigSec(oldConfig, elm);
        }
        // Save the changes to the config file
        plugin.saveConfig();
    }

    /**
     * This method loops through all the data in each key set saving the solo keys as it goes
     * <br /><br />
     *
     * @param oldConfig the old config file you'd like to use as reference
     * @param element   the current element your checking
     */
    private static void copyConfigSec(FileConfiguration oldConfig, String element) {
        // Element is a config section, get the config section variable
        ConfigurationSection configSec = oldConfig.getConfigurationSection(element);

        // Ensure the config section isn't null
        if (configSec == null) {
            return;
        }
        // Loop through all element in the config section
        for (String data : configSec.getKeys(false)) {
            // Check if the element is a configuration section
            if (!(configSec.get(data) instanceof ConfigurationSection)) {
                // Check if the plugin contains the element
                if (plugin.getConfig().contains(element)) {
                    // Element isn't a config section, set the config file to be equal to the old data
                    plugin.getConfig().set(element, oldConfig.get(element));
                }
                // Go to next element, this one was all done
                continue;
            }
            // Keep looping
            copyConfigSec(oldConfig, data);
        }
    }
}
