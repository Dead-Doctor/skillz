package de.deaddoctor.skillz.skill;

import de.deaddoctor.skillz.challenges.Challenge;
import de.deaddoctor.skillz.gui.Screen;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.UUID;
import java.util.function.Supplier;

public class Level {
    private final String name;
    private final String description;

    private final Difficulty difficulty;

    private final Supplier<Challenge> challengeSupplier;

    private final HashSet<UUID> completedBy = new HashSet<>();

    public Level(String name, String description, Difficulty difficulty, Supplier<Challenge> challengeSupplier) {
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.challengeSupplier = challengeSupplier;
    }

    public boolean hasCompleted(UUID uuid) {
        return completedBy.contains(uuid);
    }

    public void complete(UUID uuid) {
        completedBy.add(uuid);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public HashSet<UUID> getCompletedBy() {
        return completedBy;
    }

    public void start(Player player) {
        Screen.getScreen(player).close();
        challengeSupplier.get().init(this, player);
    }
}
