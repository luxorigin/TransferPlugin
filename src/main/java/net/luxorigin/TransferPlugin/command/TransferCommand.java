package net.luxorigin.TransferPlugin.command;

import net.luxorigin.TransferPlugin.TransferPlugin;
import net.luxorigin.TransferPlugin.entity.TransferEntity;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TransferCommand extends Command {

    private final TransferPlugin plugin;

    public TransferCommand(TransferPlugin plugin) {
        super("tf");
        this.setUsage("/tf [create/delete/spawn/list] [name] [address] [port]");
        this.setPermission("transfer.command");
        this.plugin = plugin;
    }

    private List<String> match(Collection<String> source, String input) {
        return source.stream().filter(s -> s.startsWith(input)).collect(Collectors.toList());
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return List.of("create", "delete", "spawn", "list");
        }

        switch (args[0]) {
            case "delete", "spawn" -> {
                if (args.length == 2) {
                    return match(plugin.getCache().keySet(), args[1]);
                }
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission(getPermission())) {
                return true;
            }

            if (args.length == 0) {
                player.sendMessage(TransferPlugin.prefix + getUsage());
                return true;
            }

            TransferEntity entity = new TransferEntity(plugin);

            switch (args[0]) {
                case "create" -> {
                    if (args.length < 3) {
                        player.sendMessage(TransferPlugin.prefix + " Usage: /tf create <name> <address> <port>");
                        return true;
                    }
                    plugin.createTransferData(args[1].toLowerCase(), args[2], args[3]);
                    player.sendMessage(TransferPlugin.prefix + "Transfer data created");
                }

                case "delete" -> {
                    if (args.length < 2) {
                        player.sendMessage(TransferPlugin.prefix + "Usage : /tf delerte [name]");
                        return true;
                    }
                    plugin.deleteTransferData(args[1].toLowerCase());
                    player.sendMessage(TransferPlugin.prefix + "Transfer data deleted");
                }

                case "spawn" -> {
                    if (args.length < 2) {
                        player.sendMessage(TransferPlugin.prefix + "Usage : /tf spawn [name]");
                        return true;
                    }

                    if (!plugin.hasTransferData(args[1].toLowerCase())) {
                        player.sendMessage(TransferPlugin.prefix + "No Transfer data found");
                        return true;
                    }
                    entity.spawn(player, args[1].toLowerCase());
                }

                case "list" -> {
                    if (plugin.getCache().isEmpty()) {
                        player.sendMessage(TransferPlugin.prefix + "No transfer server registered");
                        return true;
                    }

                    player.sendMessage(TransferPlugin.prefix + "Registered transfer servers");
                    for (String name : plugin.getCache().keySet()) {
                        player.sendMessage("- " + name);
                    }
                }

                default -> player.sendMessage(TransferPlugin.prefix + getUsage());
            }
        }
        return true;
    }
}