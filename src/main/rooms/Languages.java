package main.rooms;

import main.Course;
import main.Language;
import main.Main;
import main.buttons.Back;
import processing.core.PConstants;

public class Languages extends Room {

    private static final int languagesPerPage = 8;
    private int languagesPage;

    private Back back;

    @Override
    public void setup() {
        this.languagesPage = 0;
        this.back = new Back();
    }

    @Override
    public void draw() {
        main.noStroke();
        main.fill(255);
        main.rect(0, 0, Main.getScreenWidth(), Main.getScreenHeight());

        main.textFont(getTitleFont(), 80);
        main.textAlign(PConstants.CENTER, PConstants.CENTER);
        main.fill(0);
        main.text("All Languages", Main.getScreenWidth() / 2, 100);

        int gapX = (Main.getScreenWidth() - Language.WIDTH * 4) / 5;
        int languagesX = gapX;
        int languagesY = 200;
        for (int i = languagesPage * languagesPerPage; i < (languagesPage + 1) * languagesPerPage; i++) {
            if (i < main.getLanguages().size()) {
                main.getLanguages().get(i).draw(languagesX, languagesY);
                languagesX += (gapX + Language.WIDTH);
            }
        }
        back.draw(40, 40);
    }

    public void mousePressed() {
        for (Language language : main.getLanguages()) {
            language.clicked();
        }
        back.clicked();
    }
}
