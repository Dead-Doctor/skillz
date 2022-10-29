package de.deaddoctor.skillz.challenges.agility;

import de.deaddoctor.skillz.challenges.ExternalChallenge;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerMoveEvent;

public class ParkourChallenge extends ExternalChallenge {

    public ParkourChallenge(int x) {
        super(x, 64, 0);
    }

    @Override
    protected boolean onEvent(Event event) {
        if (!(event instanceof PlayerMoveEvent e)) return false;
        if (e.getTo().clone().subtract(0, 1, 0).getBlock().getType() == Material.LIME_TERRACOTTA) complete();
        if (e.getTo().getY() < 50) teleportToStart();
        return false;
    }
}
