package me.brand0n_.hoverstats.Utils.Version;

import org.bukkit.Bukkit;

import java.util.ArrayList;

public class VersionUtils {
    private static final ArrayList<String> versions = new ArrayList<>();

    public static boolean isCorrectVersionHex() {
        // Setup global versions variable
        addVersions();
        // Check if version is correct
        for (String version : versions) {
            if (Bukkit.getVersion().contains(version)) {
                return true;
            }
        }
        return false;
    }

    private static void addVersions() {
        int minValue = 16;
        int maxValue = 100;
        for (int i = minValue; i <= maxValue; i++) {
            versions.add("1." + i);
        }
    }
}
