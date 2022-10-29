package de.deaddoctor.skillz.challenges;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ChallengeEvents implements Listener {

    @EventHandler
    public void onPlayerConnect(PlayerJoinEvent e) {
        e.getPlayer().teleport(new Location(e.getPlayer().getServer().getWorld("main"), 0.5, 64, 0.5));
    }

    @EventHandler
    public void onPlayerLooseHunger(FoodLevelChangeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent e) {
        Challenge.cancelChallenge(e.getPlayer());
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent e) {
        e.setCancelled(Challenge.challengeEvent(e));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        e.setCancelled(Challenge.challengeEvent(e));
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.VOID) return;
        e.setCancelled(!Challenge.challengeEvent(e));
    }
}
