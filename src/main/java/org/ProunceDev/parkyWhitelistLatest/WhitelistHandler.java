package org.ProunceDev.parkyWhitelistLatest;

import org.bukkit.Bukkit;

import java.io.*;
import java.util.*;

public class WhitelistHandler {
    private static final String HEADER = "[Discord ID                              |Discord Username                        |Minecraft UUID                          |Minecraft Username                      ]\n"
            + "[----------------------------------------|----------------------------------------|----------------------------------------|----------------------------------------]\n";
    public static String FILENAME = "staff.whitelist";

    public static List<String[]> loadUsers() {
        List<String[]> users = new ArrayList<>();

        // Check if the file exists, if not create it and write the header
        File file = new File(FILENAME);
        if (!file.exists()) {
            Bukkit.getLogger().info("[ParkyWhitelist] " + FILENAME + " doesn't exist, creating it...");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(HEADER); // Write the header to the file
            } catch (IOException e) {
                Bukkit.getLogger().info("[ParkyWhitelist] Failed to create file " + FILENAME);
            }
        }

        // Read the file content
        try (BufferedReader reader = new BufferedReader(new FileReader(FILENAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("[") || !line.contains("|")) continue;

                String[] parts = Arrays.stream(line.split("\\|"))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .toArray(String[]::new);

                if (parts.length == 4) {
                    users.add(parts);
                }
            }
        } catch (IOException e) {
            Bukkit.getLogger().info("[ParkyWhitelist] Failed to read from file " + FILENAME);
        }

        return users;
    }


    public static void saveUsers(List<String[]> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILENAME))) {
            writer.write(HEADER);
            for (String[] user : users) {
                writer.write(String.format("|%-40s|%-40s|%-40s|%-40s|\n",
                        user[0], user[1], user[2].replace("-", ""), user[3]));
            }
        } catch (IOException e) {
            Bukkit.getLogger().info("[ParkyWhitelist] Failed to write to file " + FILENAME);
        }
    }

    public static boolean addUser(String[] newUser) {
        List<String[]> users = loadUsers();
        for (String[] user : users) {
            if (user[2].replace("-", "").equals(newUser[2].replace("-", ""))) {
                return false; // UUID already exists
            }
        }
        users.add(newUser);
        saveUsers(users);
        return true;
    }

    public static boolean removeUser(String minecraftUuid) {
        List<String[]> users = loadUsers();
        boolean removed = users.removeIf(user -> user[2].equals(minecraftUuid.replace("-", "")));
        if (removed) {
            saveUsers(users);
        }
        return removed;
    }

    public static String[] createUser(String discordId, String discordUsername, String minecraftUuid, String minecraftUsername) {
        return new String[]{discordId, discordUsername, minecraftUuid.replace("-", ""), minecraftUsername};
    }
}
