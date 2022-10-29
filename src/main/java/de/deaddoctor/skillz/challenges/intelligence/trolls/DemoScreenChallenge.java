package de.deaddoctor.skillz.challenges.intelligence.trolls;

import de.deaddoctor.skillz.Main;
import de.deaddoctor.skillz.challenges.Challenge;

public class DemoScreenChallenge extends Challenge {
    @Override
    protected void onStart() {
        player.showDemoScreen();
        Main.runAfter(20, this::complete);
    }
}
