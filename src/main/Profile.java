package main;

import org.apache.commons.io.FilenameUtils;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class Profile extends Entity {

    public static final int WIDTH = 300;
    private static final int courseSize = 100;
    private static final int GAP = (WIDTH - courseSize * 2) / 3;
    private static final int HEIGHT = 120 + (courseSize + GAP) * 2;

    private static PImage outline;
    private static PFont font;

    private int x;
    private int y;

    private String name;
    private ArrayList<Course> learningCourses = new ArrayList<>();
    private Language language; //the language you are learning with
    private Course courseSelected;

    private Profile(String name, String language) {
        this.name = name;
        this.language = Language.getLanguage(language);
    }

    static void setup() {
        outline = main.loadImage("images/profile.png");
        font = main.createFont("Open Sans", 128);
    }

    static void loadProfiles() {
        File[] profiles = new File("profiles").listFiles();

        if (profiles != null) {
            for (File file : profiles) {
                if (FilenameUtils.getExtension(file.getName()).equals("json")) {
                    JSONObject json = main.loadJSONObject(file.getAbsolutePath());
                    Profile profile = new Profile(json.getString("name"), json.getString("language"));
                    main.getProfiles().add(profile);
                    JSONArray courses = json.getJSONArray("courses");
                    for (int i = 0;i < courses.size();i++) {
                        JSONObject object = (JSONObject) courses.get(i);
                        profile.learningCourses.add(Course.getCourse(object.getString("name")));
                    }
                }
            }
        }
    }

    public void draw(int x, int y) {
        this.x = x;
        this.y = y;

//        //TODO: Temporary
//        main.stroke(0);
//        main.noFill();
//        main.rect(x, y, WIDTH, HEIGHT);

        main.image(outline, x - 20, y - 20);

        main.textFont(font, 34);
        main.textAlign(PConstants.CENTER, PConstants.CENTER);
        main.fill(0);
        main.text(this.name, x + WIDTH / 2, y + 30);

        main.textSize(24);
        main.textAlign(PConstants.LEFT);
        main.text("Courses: " + this.learningCourses.size(), x + 20, y + 96);

        final int coursesPerPage = 4;
        int coursesX = x + GAP;
        int coursesY = y + 120;
        for (int i = 0; i < coursesPerPage; i++) {
            if (i < this.learningCourses.size()) {
                main.image(this.learningCourses.get(i).getImage(), coursesX, coursesY, courseSize, courseSize);
                coursesX += (GAP + courseSize);
                if ((i + 1) % 2 == 0) {
                    coursesX = x + GAP;
                    coursesY += (courseSize + GAP);
                }
            }
        }

        if (this.inBounds()) {
            main.noStroke();
            main.fill(255, 255, 255, 80);
            main.rect(x, y, WIDTH, HEIGHT);
        }
    }

    private boolean inBounds() {
        return main.mouseX >= this.x && main.mouseX <= this.x + WIDTH && main.mouseY >= this.y && main.mouseY <= this.y + HEIGHT;
    }

    public void clicked() {
        if (this.inBounds()) {
            main.setProfileSelected(this);
            main.switchRoom(Main.coursesRoom);
        }
    }

    public ArrayList<Course> getCourses() {
        return this.learningCourses;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public void save() {
        JSONObject profile = new JSONObject();
        profile.setString("name", this.name);
        profile.setString("language", this.language.getID());
        JSONArray courses = new JSONArray();
        for (Course course : this.learningCourses) {
            JSONObject object = new JSONObject();
            object.setString("name", course.getName());
            courses.append(object);
        }
        profile.setJSONArray("courses", courses);
        main.saveJSONObject(profile, "profiles/" + this.name + ".json");
    }

    public void setCourseSelected(Course course) {
        this.courseSelected = course;
    }

    public Course getCourseSelected() {
        return this.courseSelected;
    }

}
