package de.deaddoctor.skillz.commands;

import de.deaddoctor.skillz.gui.Screen;
import de.deaddoctor.skillz.skill.Skill;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SkillCommand implements CommandExecutor {
    private final Skill skill;

    public SkillCommand(Skill skill) {
        this.skill = skill;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only Players can use this command!");
            return true;
        }
        new Screen(player, skill.createMenu(player));
        return true;
    }
}
