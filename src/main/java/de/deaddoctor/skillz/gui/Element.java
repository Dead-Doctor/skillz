package de.deaddoctor.skillz.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public record Element(int x, int y, Component name, List<Component> description, Material material, boolean enchanted, Action action) {

    public Element(int x, int y, Component name, List<Component> description, Material material, boolean enchanted) {
        this(x, y, name, description, material, enchanted, null);
    }

    @Override
    public int x() {
        return x;
    }

    @Override
    public int y() {
        return y;
    }

    public ItemStack item() {
        ItemStack itemStack = new ItemStack(material);
        itemStack.editMeta(meta -> {
            meta.displayName(name.decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            meta.lore(description);
            if (enchanted) {
                meta.addEnchant(Enchantment.DURABILITY, 1, false);
            }
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_DESTROYS, ItemFlag.HIDE_DYE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE);
        });
        return itemStack;
    }
}
