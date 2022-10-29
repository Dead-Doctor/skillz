package de.deaddoctor.skillz.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class InventoryEvents implements Listener {

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        Screen screen = Screen.getScreen((Player) e.getWhoClicked());
        if (screen == null) return;
        if (screen.getInventory() != e.getInventory()) return;
        e.setCancelled(true);

        if (e.getSlot() >= screen.getPage().rows() * 9) {
            if (e.getSlot() % 9 == 4) screen.back();
            return;
        }

        for (Element element : screen.getPage().elements()) {
            if (element.x() + element.y() * 9 == e.getSlot()) {
                Action action = element.action();
                if (action != null) action.run(screen, e);
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent e) {
        Screen screen = Screen.getScreen((Player) e.getWhoClicked());
        if (screen == null) return;
        if (screen.getInventory() != e.getInventory()) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e) {
        Screen screen = Screen.getScreen((Player) e.getPlayer());
        if (screen == null) return;
        if (screen.getInventory() != e.getInventory()) return;
        screen.closedByPlayer();
    }
}
