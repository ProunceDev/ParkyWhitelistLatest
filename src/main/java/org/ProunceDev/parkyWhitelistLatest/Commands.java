package org.ProunceDev.parkyWhitelistLatest;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Commands implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, Command command, @NotNull String label, String[] args) {
        if (command.getName().equalsIgnoreCase("whitelist")) {
            if (args.length < 2) {
                commandSender.sendMessage("Usage: /whitelist <add|remove|file> <player|file>");
                return true;
            }
            String action = args[0];

            if (action.equalsIgnoreCase("add") || action.equalsIgnoreCase("remove")) {
                String playerName = args[1];
                String playerUUID = Utilities.getPlayerUUID(playerName);
                if (playerUUID == null) {
                    commandSender.sendMessage("Player not found!");
                    return true;
                }

                if (action.equalsIgnoreCase("add")) {
                    if (WhitelistHandler.addUser(playerUUID)) {
                        commandSender.sendMessage(playerName + " has been added to the whitelist.");
                    } else {
                        commandSender.sendMessage(playerName + " is already whitelisted.");
                    }
                } else {
                    if (WhitelistHandler.removeUser(playerUUID)) {
                        commandSender.sendMessage(playerName + " has been removed from the whitelist.");
                    } else {
                        commandSender.sendMessage(playerName + " is not on the whitelist.");
                    }
                }

            } else if (action.equalsIgnoreCase("file")) {
                String filename = args[1];
                if (!filename.equalsIgnoreCase("staff.whitelist") && !filename.equalsIgnoreCase("event.whitelist")) {
                    commandSender.sendMessage("Invalid filename! Use 'staff.whitelist' or 'event.whitelist'");
                } else {
                    WhitelistHandler.FILENAME = filename.toLowerCase();

                    if (WhitelistHandler.FILENAME.equals("staff.whitelist")) {

                        for (org.bukkit.entity.Player player : org.bukkit.Bukkit.getOnlinePlayers()) {
                            String uuid = player.getUniqueId().toString().replace("-", "");

                            boolean isWhitelisted = WhitelistHandler.checkWhitelisted(uuid);

                            if (!isWhitelisted) {
                                player.kick(Component.text("You are not staff whitelisted!"));
                            }
                        }
                    }
                    commandSender.sendMessage("Active whitelist has been set to " + filename.toLowerCase());
                }
            } else {
                commandSender.sendMessage("Invalid action! Use /whitelist <add|remove|file> <player|file>");
            }
            return true;
        }
        return false;
    }


    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            suggestions.add("add");
            suggestions.add("remove");
            suggestions.add("file");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("file")) {
                suggestions.add("staff.whitelist");
                suggestions.add("event.whitelist");
            } else {
                for (org.bukkit.entity.Player player : org.bukkit.Bukkit.getOnlinePlayers()) {
                    suggestions.add(player.getName());
                }
            }
        }

        return suggestions;
    }
}