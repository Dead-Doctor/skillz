package de.deaddoctor.skillz.challenges;

import de.deaddoctor.skillz.Main;
import de.deaddoctor.skillz.skill.Level;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.UUID;

public abstract class Challenge {
    private static final HashMap<UUID, Challenge> activeChallenges = new HashMap<>();

    protected Level level;
    protected Player player;

    public static void challengeTick() {
        for (Challenge challenge : activeChallenges.values()) {
            if (challenge == null) return;
            challenge.onTick();
        }
    }

    public static boolean challengeEvent(Event event) {
        Player player;
        if (event instanceof PlayerEvent e) player = e.getPlayer();
        else if (event instanceof EntityEvent e && e.getEntity() instanceof Player) player = (Player) e.getEntity();
        else return false;

        if (!activeChallenges.containsKey(player.getUniqueId())) {
            if (event instanceof PlayerMoveEvent e) {
                if (e.getTo().getY() < 50) {
                    e.getPlayer().teleport(new Location(e.getPlayer().getWorld(), 0.5, 64, 0.5), PlayerTeleportEvent.TeleportCause.PLUGIN);
                }
            }
            return false;
        }

        Challenge challenge = activeChallenges.get(player.getUniqueId());
        return challenge.onEvent(event);
    }

    public static boolean cancelChallenge(Player player) {
        if (!activeChallenges.containsKey(player.getUniqueId())) return false;
        Challenge challenge = activeChallenges.get(player.getUniqueId());
        challenge.fail();
        return true;
    }

    public void init(Level level, Player player) {
        this.level = level;
        this.player = player;
        if (activeChallenges.containsKey(player.getUniqueId())) {
            activeChallenges.get(player.getUniqueId()).fail();
            Main.runAfter(20, this::start);
        } else {
            start();
        }
    }

    private void start() {
        activeChallenges.put(player.getUniqueId(), this);
        player.showTitle(Title.title(
                Component.text("Challenge started!", NamedTextColor.GOLD),
                Component.text(level.getName(), NamedTextColor.DARK_AQUA)
        ));
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.MASTER, 1, 1);
        onStart();
    }

    protected void fail() {
        end();
        player.showTitle(Title.title(
                Component.text("Challenge failed!", NamedTextColor.DARK_RED),
                Component.text(level.getName(), NamedTextColor.DARK_AQUA)
        ));
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_LAND, SoundCategory.MASTER, 1, 1);
    }

    protected void complete() {
        end();
        player.showTitle(Title.title(
                Component.text("Challenge completed!", NamedTextColor.DARK_GREEN),
                Component.text(level.getName(), NamedTextColor.DARK_AQUA)
        ));
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.MASTER, 1, 1);
        level.complete(player.getUniqueId());
    }

    private void end() {
        onEnd();
        activeChallenges.remove(player.getUniqueId());
    }

    protected void teleportToSpawn() {
        player.teleport(new Location(player.getWorld(), 0.5, 64, 0.5), PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    protected void onStart() {
    }

    protected void onTick() {
    }

    protected boolean onEvent(Event e) {
        return false;
    }

    protected void onEnd() {
    }
}
