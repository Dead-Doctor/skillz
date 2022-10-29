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
import org.jetbrains.annotations.NotNull;

import java.util.*;

public abstract class Skill {
    final String id;
    final String name;
    final String description;

    final Material icon;
    public Skill(String id, String name, String description, Material icon) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
    }

    public abstract double getSkillLevel(UUID uuid);

    public abstract HashSet<UUID> collectUniquePlayers();

    public HashMap<UUID, SkillData> saveSkillsData() {
        HashSet<UUID> players = collectUniquePlayers();

        HashMap<UUID, SkillData> skillData = new HashMap<>();

        for (UUID player : players) {
            skillData.put(player, getSkillData(player));
        }

        return skillData;
    }

    public void loadSkillsData(HashMap<UUID, SkillData> skillsData) {
        for (UUID player : skillsData.keySet()) {
            setSkillData(player, skillsData.get(player));
        }
    }

    abstract SkillData getSkillData(UUID uuid);
    abstract void setSkillData(UUID uuid, SkillData skillData);

    abstract Page createPageForSkill(Player player);

    @NotNull Element getLinkToPage(int x, int y, Skill skill, Player player) {
        Page page = skill.createPageForSkill(player);
        ArrayList<Component> elementDescription = new ArrayList<>(Main.textToItemLore(skill.description));

        double xp = skill.getSkillLevel(player.getUniqueId());
        int rankLevel = (int) Math.floor(xp / 10);
        Rank rank = skill.getRank(player.getUniqueId());

        int rankProgress = (int) (xp % 10);

        Component titleComponent = Component.text(skill.name, NamedTextColor.GOLD).decoration(TextDecoration.UNDERLINED, TextDecoration.State.TRUE).decoration(TextDecoration.BOLD, TextDecoration.State.TRUE);

        elementDescription.add(Component.empty());

        Component levelComponent = Component.text("Level ", NamedTextColor.DARK_AQUA)
                .append(Component.text(rankLevel, NamedTextColor.GOLD))
                .append(Component.text(": ", NamedTextColor.DARK_AQUA))
                .append(rank.getName())
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
        elementDescription.add(levelComponent);

        Component levelProgressComponent = Component.text("[", NamedTextColor.DARK_AQUA)
                .append(Component.text("=".repeat(rankProgress), NamedTextColor.GOLD))
                .append(Component.text("-".repeat(10 - rankProgress), NamedTextColor.GOLD))
                .append(Component.text("]", NamedTextColor.DARK_AQUA))
                .decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE);
        elementDescription.add(levelProgressComponent);
        return page.link(x, y, titleComponent, elementDescription, skill.icon, false);
    }

    private Rank getRank(UUID player) {
        //TODO: Show rank in tab list and in nametag
        return Rank.values()[(int) Math.floor(getSkillLevel(player) / 10)];
    }

    public Page createMenu(Player player) {
        return new Page(name, 3, getLinkToPage(4, 1, this, player));
    }

}
