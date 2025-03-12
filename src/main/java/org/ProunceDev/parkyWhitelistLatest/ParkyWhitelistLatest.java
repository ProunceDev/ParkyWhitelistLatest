package org.ProunceDev.parkyWhitelistLatest;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;

public class ParkyWhitelistLatest extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        Objects.requireNonNull(Bukkit.getPluginCommand("whitelist")).setExecutor(new Commands());
        getLogger().info("Plugin Enabled!");
    }

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        String uuid = event.getUniqueId().toString().replace("-", "");

        if (WhitelistHandler.FILENAME.equals("event.whitelist")) {
            List<String[]> eventUsers = WhitelistHandler.loadUsers();
            WhitelistHandler.FILENAME = "staff.whitelist";
            List<String[]> staffUsers = WhitelistHandler.loadUsers();
            WhitelistHandler.FILENAME = "event.whitelist";
            boolean isWhitelisted = eventUsers.stream().anyMatch(user -> user[2].equals(uuid)) || staffUsers.stream().anyMatch(user -> user[2].equals(uuid));

            if (!isWhitelisted) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, Component.text("You are not whitelisted!"));
            }

            return;
        }

        List<String[]> users = WhitelistHandler.loadUsers();
        boolean isWhitelisted = users.stream().anyMatch(user -> user[2].equals(uuid));

        if (!isWhitelisted) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, Component.text("You are not staff whitelisted!"));
        }
    }
}