package de.deaddoctor.skillz.gui;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.util.List;

public record Page(String name, int rows, Element... elements) {

    public Element link(int x, int y, Component name, List<Component> description, Material material, boolean enchanted) {
        return new Element(x, y, name, description, material, enchanted, (screen, e) ->
                screen.open(this)
        );
    }
}
