package main.rooms;

import main.Course;
import main.Main;
import main.Profile;
import processing.core.PConstants;

public class Profiles extends Room {

    private static final int profilesPerPage = 8;
    private int profilesPage;

    @Override
    public void setup() {
        this.profilesPage = 0;
    }

    @Override
    public void draw() {
        main.noStroke();
        main.fill(255);
        main.rect(0, 0, Main.getScreenWidth(), Main.getScreenHeight());

        main.textFont(getOverrideTitleFont(), 80);
        main.textAlign(PConstants.CENTER, PConstants.CENTER);
        main.fill(0);
        main.text("Profiles", Main.getScreenWidth() / 2, 100);

        int gapX = (Main.getScreenWidth() - Profile.WIDTH * 4) / 5;
        int profilesX = gapX;
        int profilesY = 200;
        for (int i = profilesPage * profilesPerPage; i < (profilesPage + 1) * profilesPerPage; i++) {
            if (i < main.getProfiles().size()) {
                main.getProfiles().get(i).draw(profilesX, profilesY);
                profilesX += (gapX + Course.WIDTH);
            }
        }
    }

    @Override
    public void mousePressed() {
        for (int i = profilesPage * profilesPerPage; i < (profilesPage + 1) * profilesPerPage; i++) {
            if (i < main.getProfiles().size()) {
                main.getProfiles().get(i).clicked();
            }
        }
    }
}
