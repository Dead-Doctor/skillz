package de.deaddoctor.skillz;

import de.deaddoctor.skillz.challenges.Challenge;
import de.deaddoctor.skillz.challenges.ChallengeEvents;
import de.deaddoctor.skillz.challenges.agility.DropperChallenge;
import de.deaddoctor.skillz.challenges.agility.ParkourChallenge;
import de.deaddoctor.skillz.challenges.combat.clicking.CpsChallenge;
import de.deaddoctor.skillz.challenges.intelligence.trolls.DemoScreenChallenge;
import de.deaddoctor.skillz.commands.CancelCommand;
import de.deaddoctor.skillz.commands.SkillCommand;
import de.deaddoctor.skillz.gui.InventoryEvents;
import de.deaddoctor.skillz.skill.*;
import de.deaddoctor.skillz.skill.data.SkillData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class Main extends JavaPlugin {

    private static Main INSTANCE;
    public static Logger LOGGER;

    private Skill rootSkill;

    public static void runAfter(int ticks, Runnable runnable) {
        INSTANCE.getServer().getScheduler().runTaskLater(INSTANCE, runnable, ticks);
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        LOGGER = getLogger();
        rootSkill = new SkillCategory("root", "Skill", "Your total skill level", Material.KNOWLEDGE_BOOK,
                new SkillCategory("agility", "Agility", "Your ability to move around", Material.LEATHER_BOOTS,
                        new SkillCategory("parkour", "Parkour", "You jump and you run", Material.SLIME_BLOCK,
                                new SkillLevels("precision", "Precision", "Master hard jumps", Material.LADDER,
                                        new Level("4 Blocks", "Jump across a four block gap",                                    Difficulty.EASY,   () -> new ParkourChallenge( -1000)),
                                        new Level("3+1 Blocks", "Jump across a three block gap while going up one block",        Difficulty.EASY,   () -> new ParkourChallenge( -2000)),
                                        new Level("4/2 Blocks", "Jump across a four block gap and go to side 2 Blocks",          Difficulty.EASY,   () -> new ParkourChallenge( -3000)),
                                        new Level("Headhitter", "Bump your head",                                                Difficulty.EASY,   () -> new ParkourChallenge( -8000)),
                                        new Level("Ice", "Slide to get further",                                                 Difficulty.EASY,   () -> new ParkourChallenge(-13000)),
                                        new Level("4 Blocks Wall to Wall", "Jump across a four block gap from a wall to a wall", Difficulty.EASY,   () -> new ParkourChallenge( -4000)),
                                        new Level("3 Blocks Diagonal", "Jump three blocks diagonal",                             Difficulty.EASY,   () -> new ParkourChallenge( -5000)),
                                        new Level("Soulsand", "Extremely Slow",                                                  Difficulty.EASY,   () -> new ParkourChallenge(-10000)),
                                        new Level("2b Neo", "Jump around two blocks",                                            Difficulty.MEDIUM, () -> new ParkourChallenge( -6000)),
                                        new Level("Water", "Water Source Block to Water Source Block",                           Difficulty.MEDIUM, () -> new ParkourChallenge(-11000)),
                                        new Level("5 Block Headhitter", "Boost your self over a 5 Block gap",                    Difficulty.MEDIUM, () -> new ParkourChallenge( -9000)),
                                        new Level("Ladder Neo", "Jump around one blocks from a ladder to ladder",                Difficulty.MEDIUM, () -> new ParkourChallenge(-12000)),
                                        new Level("3b Neo", "Jump around three blocks",                                          Difficulty.HARD,   () -> new ParkourChallenge( -7000))),
                                new SkillLevels("consistancy", "Consistancy", "Learn to not fail", Material.OAK_FENCE,
                                        new Level("Big Platforms", "Platforms that are easy to land on",                         Difficulty.EASY,   () -> new ParkourChallenge(  1000)),
                                        new Level("Thin Platforms", "Platforms that are harder to land on",                      Difficulty.EASY,   () -> new ParkourChallenge(  4000)),
                                        new Level("Long Jumps", "Long but flat jumps",                                           Difficulty.MEDIUM, () -> new ParkourChallenge(  2000)),
                                        new Level("Staircase", "Up, up, up...",                                                  Difficulty.MEDIUM, () -> new ParkourChallenge(  3000)),
                                        new Level("Neo Paradise", "Just Neos",                                                   Difficulty.MEDIUM, () -> new ParkourChallenge(  5000)),
                                        new Level("Ladder Sanctuary", "Praise Ladders",                                          Difficulty.HARD,   () -> new ParkourChallenge(  6000)))),
                        new SkillLevels("dropper", "Dropper", "Land in the water", Material.TARGET,
                                new Level("Nether", "Go through the nether portal and land in the lava", Difficulty.EASY,  () -> new DropperChallenge(0)),
                                new Level("Chess", "Find the back rank mate",                            Difficulty.EASY,  () -> new DropperChallenge(1000)),
                                new Level("3", "3", Difficulty.EASY,  () -> new DropperChallenge(2000)),
                                new Level("4", "4", Difficulty.EASY,  () -> new DropperChallenge(3000)),
                                new Level("5", "5", Difficulty.EASY,  () -> new DropperChallenge(4000)),
                                new Level("6", "6", Difficulty.EASY,  () -> new DropperChallenge(5000)),
                                new Level("7", "7", Difficulty.EASY,  () -> new DropperChallenge(6000)))),
                new SkillCategory("combat", "Combat", "Your ability to fight", Material.NETHERITE_SWORD,
                        new SkillLevels("clicking", "Clicking", "Your ability to click fast", Material.LEVER,
                                new Level("5 CPS", "Click 5 times per second for 3s",    Difficulty.EASY,       () -> new CpsChallenge( 5, 3)),
                                new Level("10 CPS", "Click 10 times per second for 5s",  Difficulty.MEDIUM,     () -> new CpsChallenge(10, 5)),
                                new Level("15 CPS", "Click 15 times per second for 10s", Difficulty.HARD,       () -> new CpsChallenge(15, 10)),
                                new Level("20 CPS", "Click 20 times per second for 15s", Difficulty.IMPOSSIBLE, () -> new CpsChallenge(20, 15)))),
                new SkillCategory("intelligence", "Intelligence", "Your ability to think", Material.LIGHT,
                        new SkillLevels("trolls", "Trolls", "Get Trolled! lol", Material.TNT,
                                new Level("Demo", "Please buy Minecraft", Difficulty.EASY, DemoScreenChallenge::new))),
                new SkillCategory("creativity", "Creativity", "Your ability to create", Material.CRAFTING_TABLE));

        try {
            BukkitObjectInputStream in = new BukkitObjectInputStream(new GZIPInputStream(new FileInputStream("skills.dat")));
            HashMap<UUID, SkillData> skillsData = (HashMap<UUID, SkillData>) in.readObject();
            in.close();
            rootSkill.loadSkillsData(skillsData);
        } catch (IOException e) {
            LOGGER.info("No skill data found. Creating new file on next shutdown.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Objects.requireNonNull(getCommand("skill")).setExecutor(new SkillCommand(rootSkill));
        Objects.requireNonNull(getCommand("cancel")).setExecutor(new CancelCommand());
        getServer().getPluginManager().registerEvents(new InventoryEvents(), this);
        getServer().getPluginManager().registerEvents(new ChallengeEvents(), this);
        getServer().getScheduler().runTaskTimer(this, Challenge::challengeTick, 0, 1);
    }

    @Override
    public void onDisable() {
        HashMap<UUID, SkillData> skillsData = rootSkill.saveSkillsData();

        try {
            BukkitObjectOutputStream out = new BukkitObjectOutputStream(new GZIPOutputStream(new FileOutputStream("skills.dat")));
            out.writeObject(skillsData);
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public @NotNull ChunkGenerator getDefaultWorldGenerator(@NotNull String worldName, @Nullable String id) {
        return new VoidChunkGenerator();
    }

    private static final int MAX_LINE_LENGTH = 30;
    private static final Pattern MAX_LINE_PATTERN = Pattern.compile("\\G.{1," + MAX_LINE_LENGTH + "}\\b");

    public static List<Component> textToItemLore(String text) {
        final Matcher matcher = MAX_LINE_PATTERN.matcher(text);

        final String result = matcher.replaceAll("$0\n");

        ArrayList<Component> components = new ArrayList<>();
        for (String line : result.split("\n")) {
            if (line.length() > MAX_LINE_LENGTH) {
                Main.LOGGER.warning("Line is too long (" + line.length() + "): " + line);
            }
            components.add(Component.text(line, NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, TextDecoration.State.FALSE));
        }
        return components;
    }
}
