package de.deaddoctor.skillz.gui;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Stack;
import java.util.UUID;

public class Screen {
    private static final HashMap<UUID, Screen> screens = new HashMap<>();

    private final Player player;
    private Inventory inventory;

    private Page page;

    private final Stack<Page> pages = new Stack<>();

    public Screen(Player player, Page page) {
        this.player = player;
        screens.put(player.getUniqueId(), this);

        open(page);
    }

    public static Screen getScreen(Player player) {
        return screens.get(player.getUniqueId());
    }

    public void open(Page page) {
        pages.push(page);
        showPage(page);
    }

    public void back() {
        pages.pop();
        if (pages.isEmpty()) {
            player.closeInventory();
            screens.remove(player.getUniqueId());
            return;
        }
        showPage(pages.peek());
    }

    private void showPage(Page page) {
        if (inventory == null || this.page.rows() != page.rows() || !this.page.name().equals(page.name())) {
            inventory = Bukkit.createInventory(null, (page.rows() + 1) * 9, Component.text(page.name()));
            player.openInventory(inventory);
        } else {
            inventory.clear();
        }
        this.page = page;

        for (Element element : page.elements()) {
            inventory.setItem(element.x() + element.y() * 9, element.item());
        }
        addNavigationRow();
    }

    private void addNavigationRow() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        item.editMeta((meta) -> meta.displayName(Component.empty()));
        for (int i = 0; i < 4; i++) {
            inventory.setItem(i + page.rows() * 9, item);
        }
        for (int i = 5; i < 9; i++) {
            inventory.setItem(i + page.rows() * 9, item);
        }
        addBackButton();
    }

    private void addBackButton() {
        ItemStack item;
        if (pages.size() <= 1) {
            item = new ItemStack(Material.BARRIER);
            item.editMeta((meta) -> meta.displayName(Component.text("Close", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)));
        } else {
            item = new ItemStack(Material.ARROW);
            item.editMeta((meta) -> meta.displayName(Component.text("Back", NamedTextColor.GOLD).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE)));
        }
        inventory.setItem(4 + page.rows() * 9, item);
    }

    public void close() {
        player.closeInventory();
        screens.remove(player.getUniqueId());
    }

    public void closedByPlayer() {
        screens.remove(player.getUniqueId());
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Page getPage() {
        return page;
    }
}
