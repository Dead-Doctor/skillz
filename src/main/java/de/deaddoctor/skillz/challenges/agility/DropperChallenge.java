package de.deaddoctor.skillz.challenges.agility;

import de.deaddoctor.skillz.challenges.ExternalChallenge;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class DropperChallenge extends ExternalChallenge {

    public DropperChallenge(int x) {
        super(x, 300, 1000);
    }

    @Override
    protected boolean onEvent(Event event) {
        if (event instanceof EntityDamageEvent e) {
            if (e.getCause() != EntityDamageEvent.DamageCause.FALL) return false;
            teleportToStart();
        } else if (event instanceof PlayerMoveEvent e) {
            if (e.getTo().clone().subtract(0, 1, 0).getBlock().getType() == Material.LIME_TERRACOTTA) complete();
        }
        return false;
    }
}
