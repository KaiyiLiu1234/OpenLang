package main.rooms;

import main.Entity;
import main.Language;
import processing.core.PFont;
import processing.event.MouseEvent;

public abstract class Room extends Entity {

    private static PFont titleFont;

    public abstract void setup();

    public abstract void draw();

    public void mousePressed() {

    }

    public void mouseWheel(MouseEvent event) {

    }

    public void keyPressed() {

    }

    static PFont getTitleFont() {
        return Language.fontOverrides.getOrDefault(main.getProfileSelected().getLanguage(), titleFont);
    }

    //use this when the selected language does not matter
    static PFont getOverrideTitleFont() {
        return titleFont;
    }

    public static void loadTitleFont() {
        titleFont = main.createFont("Open Sans", 128);
    }
}
