package org.ProunceDev.parkyWhitelistLatest;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

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
            boolean isWhitelisted = WhitelistHandler.checkWhitelisted(uuid);

            if (!isWhitelisted) { // Check staff whitelist as fallback
                WhitelistHandler.FILENAME = "staff.whitelist";
                isWhitelisted = WhitelistHandler.checkWhitelisted(uuid);

                if (!isWhitelisted) {
                    event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, Component.text("You are not whitelisted!"));
                }

                WhitelistHandler.FILENAME = "event.whitelist";
            }

            return;
        }


        WhitelistHandler.FILENAME = "staff.whitelist";
        boolean isWhitelisted = WhitelistHandler.checkWhitelisted(uuid);

        if (!isWhitelisted) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST, Component.text("You are not staff whitelisted!"));
        }
    }
}