package main;

import org.apache.commons.io.FilenameUtils;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Level extends Entity {

    private static final int GAP = 40;
    private static PFont font;

    private int x;
    private int y;

    private HashMap<Language, String> names = new HashMap<>();
    private JSONArray lessonsArray; //lessons that are not loaded yet
    private ArrayList<Lesson> lessons = new ArrayList<>();

    Level(JSONArray nameArray, JSONArray lessonsArray) {
        JSONObject object = (JSONObject) nameArray.get(0);
        for (Object key : object.keys()) {
            names.put(Language.getLanguage((String) key), object.getString((String) key));
        }
        this.lessonsArray = lessonsArray;
    }

    public int getHeight() {
        int levelsX = (int) Math.floor((float) Main.getScreenWidth() / (Lesson.SIZE + GAP));
        int levelsY = (int) Math.ceil((float) this.lessons.size() / levelsX);
        return levelsY * Lesson.HEIGHT;
    }

    public ArrayList<Lesson> getLessons() {
        return lessons;
    }

    public void draw(int x, int y) {
        this.x = x;
        this.y = y;

        main.noStroke();
        main.fill(255);
        main.rect(x, y, Main.getScreenWidth(), 130 + this.getHeight());

        main.textFont(main.getProfileSelected().getLanguage().getFont(), 60);
        main.textAlign(PConstants.CENTER, PConstants.CENTER);
        main.fill(0);
        main.text(this.names.get(main.getProfileSelected().getLanguage()), Main.getScreenWidth() / 2, y + 50);

        int lessonX = x + GAP;
        int lessonY = y + 130;
        for (Lesson lesson : this.lessons) {
            lesson.draw(lessonX, lessonY);
            lessonX += (GAP + Lesson.SIZE);
            if (lessonX >= Main.getScreenWidth()) {
                lessonX = x + GAP;
                lessonY += (GAP + Lesson.SIZE);
            }
        }
    }

    public String getName() {
        return this.names.get(main.getProfileSelected().getLanguage());
    }

    public void loadLessons() {
        this.lessons.clear();
        for (int i = 0;i < lessonsArray.size();i++) {
            JSONObject object = (JSONObject) lessonsArray.get(i);
            this.lessons.add(new Lesson(object));
        }
    }

    static void setup() {
        font = main.createFont("Open Sans", 128);
    }

}
