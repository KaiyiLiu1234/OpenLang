package main.rooms;

import main.Lesson;
import main.Level;
import main.Main;
import main.buttons.Back;
import processing.core.PConstants;
import processing.event.MouseEvent;

public class Levels extends Room {

    private int yOffset = 0;
    private Back back;

    public Levels() {

    }

    @Override
    public void setup() {
        for (Level level : main.getProfileSelected().getCourseSelected().getLevels()) {
            level.loadLessons();
        }

        this.yOffset = 0;
        this.back = new Back();
    }

    @Override
    public void draw() {
        main.noStroke();
        main.fill(255);
        main.rect(0, 0, Main.getScreenWidth(), Main.getScreenHeight());

        int levelY = 200;
        for (Level level : main.getProfileSelected().getCourseSelected().getLevels()) {
            level.draw(0, levelY + yOffset);
        }

        //title goes on top of the levels
        main.noStroke();
        main.fill(255);
        main.rect(0, 0, Main.getScreenWidth(), 200);

        main.textFont(main.getProfileSelected().getCourseSelected().getLanguage().getFont(), 80);
        main.textAlign(PConstants.CENTER, PConstants.CENTER);
        main.fill(0);
        main.text(main.getProfileSelected().getCourseSelected().getName(), Main.getScreenWidth() / 2, 100);

        back.draw(40, 40);
    }

    @Override
    public void mousePressed() {
        back.clicked();

        for (Level level : main.getProfileSelected().getCourseSelected().getLevels()) {
            for (Lesson lesson : level.getLessons()) {
                lesson.clicked();
            }
        }
    }

    @Override
    public void mouseWheel(MouseEvent event) {
        final int multiplier = 6;
        if (this.getLevelsHeight() > (Main.getScreenHeight() - 200)) { //only scroll when the screen height can't display everything
            this.yOffset -= event.getCount() * multiplier;
            if (this.yOffset > 0) {
                this.yOffset = 0;
            }
        }
    }

    private int getLevelsHeight() {
        int height = 0;
        for (Level level : main.getProfileSelected().getCourseSelected().getLevels()) {
            height += (130 + level.getHeight());
        }
        return height;
    }

}
