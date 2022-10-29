package de.deaddoctor.skillz.skill;

import de.deaddoctor.skillz.Main;
import de.deaddoctor.skillz.gui.Element;
import de.deaddoctor.skillz.gui.Page;
import de.deaddoctor.skillz.skill.data.SkillData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class SkillLevels extends Skill {

    private final Level[] levels;

    public SkillLevels(String id, String name, String description, Material icon, Level... levels) {
        super(id, name, description, icon);
        this.levels = levels;
    }

    @Override
    public double getSkillLevel(UUID uuid) {
        double completedLevels = 0;
        for (Level level : levels) {
            if (level.hasCompleted(uuid)) {
                completedLevels++;
            }
        }
        return completedLevels / levels.length * 100;
    }

    @Override
    public HashSet<UUID> collectUniquePlayers() {
        HashSet<UUID> uniquePlayers = new HashSet<>();
        for (Level level : levels) {
            uniquePlayers.addAll(level.getCompletedBy());
        }
        return uniquePlayers;
    }

    @Override
    SkillData getSkillData(UUID uuid) {
        int completedLevels = 0;

        while (completedLevels < levels.length && levels[completedLevels].hasCompleted(uuid)) {
            completedLevels++;
        }

        return new SkillData(id, completedLevels);
    }

    @Override
    void setSkillData(UUID uuid, SkillData skillData) {
        for (int i = 0; i < skillData.getCompletedLevels(); i++) {
            levels[i].complete(uuid);
        }
    }

    @Override
    Page createPageForSkill(Player player) {
        Element[] elements = new Element[levels.length];
        // 7 elements per row plus 1 above and 1 below
        int rows = (int) Math.ceil(levels.length / 7.0) + 2;

        for (int i = 0; i < levels.length; i++) {
            int x = 1 + (i % 7);
            int y = 1 + (i / 7);

            Level level = levels[i];
            boolean unlocked = i == 0 || levels[i - 1].hasCompleted(player.getUniqueId());
            Difficulty levelDifficulty = level.getDifficulty();
            boolean completed = unlocked && level.hasCompleted(player.getUniqueId());

            TextComponent levelName;
            List<Component> descriptionComponent;
            Material material;
            if (unlocked) {
                levelName = Component.text((i + 1) + ". " + level.getName(), levelDifficulty.getColor());

                descriptionComponent = Main.textToItemLore(level.getDescription());

                if (completed)
                    descriptionComponent.add(0, Component.text("COMPLETED", NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE).decoration(TextDecoration.BOLD, TextDecoration.State.TRUE));

                descriptionComponent.add(Component.text(levelDifficulty.name(), levelDifficulty.getColor()).decoration(TextDecoration.BOLD, TextDecoration.State.TRUE).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));

                material = levelDifficulty.getMaterial();
            } else {
                levelName = Component.text("???", NamedTextColor.GRAY);
                descriptionComponent = Main.textToItemLore("Complete previous level first");
                descriptionComponent.add(Component.text("UNKNOWN", NamedTextColor.GRAY).decoration(TextDecoration.OBFUSCATED, TextDecoration.State.TRUE).decoration(TextDecoration.BOLD, TextDecoration.State.TRUE).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
                material = Material.GRAY_STAINED_GLASS_PANE;
            }
            levelName = levelName.decoration(TextDecoration.BOLD, TextDecoration.State.TRUE).decoration(TextDecoration.UNDERLINED, TextDecoration.State.TRUE);
            elements[i] = new Element(x, y, levelName, descriptionComponent, material, completed, (screen, e) -> {
                if (unlocked) level.start(player);
            });
        }
        return new Page(name, rows, elements);
    }
}
