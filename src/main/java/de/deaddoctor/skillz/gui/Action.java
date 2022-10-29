package de.deaddoctor.skillz.gui;

import org.bukkit.event.inventory.InventoryClickEvent;

public interface Action {
    void run(Screen screen, InventoryClickEvent e);
}