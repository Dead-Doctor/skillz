package de.deaddoctor.skillz.skill;

import de.deaddoctor.skillz.Main;
import de.deaddoctor.skillz.gui.Element;
import de.deaddoctor.skillz.gui.Page;
import de.deaddoctor.skillz.skill.data.SkillData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Stream;

public class SkillCategory extends Skill {

    private final Skill[] skills;

    public SkillCategory(String id, String name, String description, Material icon, Skill... skills) {
        super(id, name, description, icon);
        this.skills = skills;
    }

    @Override
    public double getSkillLevel(UUID uuid) {
        double totalSkill = 1;
        for (Skill skill : skills) {
            totalSkill *= skill.getSkillLevel(uuid);
        }
        return Math.pow(totalSkill, 1.0 / skills.length);
    }

    @Override
    public HashSet<UUID> collectUniquePlayers() {
        HashSet<UUID> uniquePlayers = new HashSet<>();
        for (Skill skill : skills) {
            uniquePlayers.addAll(skill.collectUniquePlayers());
        }
        return uniquePlayers;
    }

    @Override
    SkillData getSkillData(UUID uuid) {
        return new SkillData(id, Stream.of(skills).map(skill -> skill.getSkillData(uuid)).toList());
    }

    @Override
    void setSkillData(UUID uuid, SkillData skillData) {
        for (SkillData data : skillData.getSubSkills()) {
            for (Skill skill : skills) {
                if (skill.id.equals(data.getId())) {
                    skill.setSkillData(uuid, data);
                    break;
                }
            }
        }
    }

    @Override
    Page createPageForSkill(Player player) {
        Element[] elements = new Element[skills.length];
        double contentRows = 0;

        double maxPerRow;
        do {
            contentRows++;
            maxPerRow = Math.ceil(skills.length / contentRows);
        } while (maxPerRow > 7);

        int totalLeft = skills.length;
        int i = 0;
        for (int y = 1; y <= contentRows; y++) {
            int elementsInRow = (int) (totalLeft / (contentRows - y + 1));
            totalLeft -= elementsInRow;
            int startX = 4 - elementsInRow / 2;
            for (int x = startX; x < startX + elementsInRow; x++) {
                int actualX = x;
                if (elementsInRow % 2 == 0 && x >= 4) actualX++;
                Skill skill = skills[i];
                elements[i++] = getLinkToPage(actualX, y, skill, player);
            }
        }
        return new Page(name, (int) (contentRows + 2), elements);
    }
}
