package de.deaddoctor.skillz.skill;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

public enum Difficulty {
    EASY(NamedTextColor.DARK_GREEN, Material.LIME_STAINED_GLASS_PANE),
    MEDIUM(NamedTextColor.YELLOW, Material.YELLOW_STAINED_GLASS_PANE),
    HARD(NamedTextColor.RED, Material.RED_STAINED_GLASS_PANE),
    IMPOSSIBLE(NamedTextColor.DARK_PURPLE, Material.PURPLE_STAINED_GLASS_PANE);

    private final TextColor color;
    private final Material material;

    Difficulty(TextColor color, Material material) {
        this.color = color;
        this.material = material;
    }

    public TextColor getColor() {
        return color;
    }

    public Material getMaterial() {
        return material;
    }
}
