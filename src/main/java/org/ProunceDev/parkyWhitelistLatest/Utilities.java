package org.ProunceDev.parkyWhitelistLatest;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Utilities {

    public static String getPlayerUUID(String playerName) {
        // Check if the player is online first
        Player player = Bukkit.getPlayer(playerName);
        if (player != null) {
            return player.getUniqueId().toString().replace("-", ""); // Remove dashes
        }

        // If the player is offline, fetch UUID from Mojang API
        try {
            URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() == 200) {
                JSONParser parser = new JSONParser();
                JSONObject response = (JSONObject) parser.parse(new InputStreamReader(conn.getInputStream()));
                return (String) response.get("id"); // Mojang API returns UUID without dashes
            }
        } catch (Exception e) {
            Bukkit.getLogger().warning("[ParkyWhitelist] Failed to fetch UUID for " + playerName);
        }

        return null; // Return null if the player was not found
    }
}

