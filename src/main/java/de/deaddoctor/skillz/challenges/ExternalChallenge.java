package de.deaddoctor.skillz.challenges;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.event.player.PlayerTeleportEvent;

public class ExternalChallenge extends Challenge {
    public final int x;
    public final int y;
    public final int z;

    public ExternalChallenge(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    protected void onStart() {
        player.sendActionBar(Component.text("Cancel with /cancel", NamedTextColor.DARK_GREEN));
        teleportToStart();
    }

    protected void teleportToStart() {
        player.teleport(new Location(player.getWorld(), x + 0.5, y, z + 0.5), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    @Override
    protected void onEnd() {
        teleportToSpawn();
    }
}
