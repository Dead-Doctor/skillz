package de.deaddoctor.skillz.commands;

import de.deaddoctor.skillz.challenges.Challenge;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CancelCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players can use this command!");
            return true;
        }
        if (Challenge.cancelChallenge(player)) return true;
        sender.sendMessage("You are not in a challenge!");
        return true;
    }
}
