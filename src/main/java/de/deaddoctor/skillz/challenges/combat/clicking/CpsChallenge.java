package de.deaddoctor.skillz.challenges.combat.clicking;

import de.deaddoctor.skillz.challenges.Challenge;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class CpsChallenge extends Challenge {

    private final int minCps;
    private final int time;

    private boolean started = false;
    private int clicks = 0;
    private int startTime;

    public CpsChallenge(int minCps, int time) {
        this.minCps = minCps;
        this.time = time;
    }

    @Override
    protected void onTick() {
        if (!started) {
            player.sendActionBar(Component.text("Click to start!", NamedTextColor.DARK_GREEN));
            return;
        }

        int currentTick = Bukkit.getServer().getCurrentTick();
        if (currentTick - startTime >= time * 20) {
            if (clicks >= minCps * time) complete();
            else fail();
        } else {
            double timeElapsed = (currentTick - startTime) / 20.0;
            double cps = clicks / timeElapsed;
            player.sendActionBar(Component.text("CPS: " + String.format("%.1f", cps), NamedTextColor.DARK_GREEN));
        }
    }

    @Override
    protected boolean onEvent(Event event) {
        if (!(event instanceof PlayerInteractEvent e)) return false;
        if (e.getAction() != Action.LEFT_CLICK_AIR) return false;
        if (!started) {
            started = true;
            startTime = Bukkit.getServer().getCurrentTick();
        }
        clicks++;
        return true;
    }
}
