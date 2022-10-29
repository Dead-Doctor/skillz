package de.deaddoctor.skillz.skill;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;

public enum Rank {
    NOOB(Component.text("n00b", NamedTextColor.WHITE)),
    VILLAGER(Component.text("Villager", NamedTextColor.GRAY)),
    STEVE(Component.text("Steve", NamedTextColor.AQUA)),
    STARTER(Component.text("Beginner", NamedTextColor.GREEN)),
    OK(Component.text("Ok", NamedTextColor.LIGHT_PURPLE)),
    EXPERT(Component.text("Expert", NamedTextColor.YELLOW)),
    GENIUS(Component.text("Genius", NamedTextColor.GOLD)),
    PRO(Component.text("Pro", NamedTextColor.DARK_BLUE)),
    GOD(Component.text("God", NamedTextColor.DARK_PURPLE)),
    HACKER(Component.text("Hacker", NamedTextColor.DARK_RED)),
    XYZ(Component.text("ABCDE", NamedTextColor.BLACK).decoration(TextDecoration.OBFUSCATED, TextDecoration.State.TRUE));

    private final Component name;

    Rank(Component name) {
        this.name = name;
    }

    public Component getName() {
        return name;
    }
}
