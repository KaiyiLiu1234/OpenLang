package main;

import main.rooms.AllCourses;
import main.rooms.RemoveCourses;
import org.apache.commons.io.FilenameUtils;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class Course extends Entity {

    public static final int WIDTH = 260;
    public static final int HEIGHT = 320;
    private static final int imageSize = 160;

    private static PImage outline;
    private static PFont font;
    private static PImage add;

    private int x;
    private int y;

    private String name;
    private Language language; //the language its going to teach
    private PImage image;
    private ArrayList<Level> levels = new ArrayList<>();
    private String path;
    private JSONObject json;

    private Course(JSONObject json, String path) {
        if (json == null) {
            return;
        }
        this.json = json;
        this.name = json.getString("name");
        for (Language lang : main.getLanguages()) {
            if (lang.getID().equals(json.getString("language"))) {
                this.language = lang;
            }
        }
        String image = json.getString("image");
        if (!new File(path + "/" + image).exists()) {
            this.image = main.loadImage("images/error.png");
        } else {
            this.image = main.loadImage(path + "/" + image);
        }
        this.image.resize(imageSize, imageSize);
        this.path = path;
    }

    //add course button
    @SuppressWarnings("ConstantConditions")
    public Course() {
        new Course(null, null);
    }

    //you cannot select the course if the chosen language is the same as the language you are going to learn
    private boolean canLearn() {
        return this.language == null || !this.language.equals(main.getProfileSelected().getLanguage());
    }

    private boolean inBounds() {
        return main.mouseX >= this.x && main.mouseX <= this.x + WIDTH && main.mouseY >= this.y && main.mouseY <= this.y + HEIGHT;
    }

    public void draw(int x, int y) {
        this.x = x;
        this.y = y;

        main.image(outline, x - 20, y - 20);

        if (this.name != null) {
            int margin = (WIDTH - imageSize) / 2;
            main.image(this.image, x + margin, y + 30);

            main.textFont(Language.fontOverrides.getOrDefault(this.language, font), 30);
            main.textAlign(PConstants.LEFT, PConstants.CENTER);
            main.fill(0);
            main.text(this.name, x + 20, y + 30 + this.image.height + 25);

            this.language.draw(x + 20, y + 30 + this.image.height + 65);
        } else {
            main.textFont(font, 30);
            main.textAlign(PConstants.CENTER, PConstants.CENTER);
            main.fill(0);
            main.text("Add new course", x + WIDTH / 2, y + 100);

            main.image(add, x + WIDTH / 2 - add.width / 2, y + 140);
        }

        if (this.inBounds()) {
            main.noStroke();
            if ((main.getRoom() instanceof RemoveCourses || !this.canLearn()) && !(main.getRoom() instanceof AllCourses)) {
                main.fill(255, 0, 0, 80);
            } else {
                main.fill(255, 255, 255, 80);
            }
            main.rect(x, y, WIDTH, HEIGHT);
        }
    }

    public String getName() {
        return this.name;
    }

    public PImage getImage() {
        return this.image;
    }

    public boolean alreadyLearning() {
        return main.getProfileSelected().getCourses().contains(this);
    }

    public ArrayList<Level> getLevels() {
        return levels;
    }

    public String getPath() {
        return path;
    }

    public Language getLanguage() {
        return language;
    }

    public void clicked() {
        if (this.inBounds()) {
            if (this.name != null) {
                if (main.getRoom() instanceof AllCourses && !this.alreadyLearning()) { //add new course
                    main.getProfileSelected().getCourses().add(this);
                    main.getProfileSelected().save();
                    main.switchRoom(Main.coursesRoom);
                } else if (main.getRoom() instanceof RemoveCourses) { //remove courses
                    ((RemoveCourses) main.getRoom()).setRemoveCourse(this);
                } else if (!(main.getRoom() instanceof AllCourses) && this.canLearn()) {
                    //reload levels
                    this.levels.clear();
                    JSONArray levels = json.getJSONArray("levels");
                    for (int i = 0; i < levels.size(); i++) {
                        JSONObject object = (JSONObject) levels.get(i);
                        if (supportsProfileLanguage(object)) {
                            this.levels.add(new Level(object.getJSONArray("names"), object.getJSONArray("lessons")));
                        }
                    }
                    main.getProfileSelected().setCourseSelected(this);
                    main.switchRoom(Main.levelsRoom);
                }
            } else {
                main.switchRoom(Main.allCoursesRoom);
            }
        }
    }

    static void setup() {
        outline = main.loadImage("images/course.png");
        font = main.createFont("Open Sans", 128);
        add = main.loadImage("images/add.png");
        add.resize(64, 64);
    }

    static void loadCourses() {
        File[] courseFiles = new File("courses").listFiles();

        if (courseFiles != null) {
            for (File directory : courseFiles) {
                if (directory.isDirectory() && directory.listFiles() != null) {
                    File file = new File(directory.getAbsolutePath() + "/" + directory.getName() + ".json");
                    JSONObject json = main.loadJSONObject(file.getAbsolutePath());
                    Course course = new Course(json, directory.getAbsolutePath());
                    main.getCourses().add(course);
                }
            }
        }
    }

    private static boolean supportsProfileLanguage(JSONObject object) {
        JSONArray array = object.getJSONArray("names");
        JSONObject languages = (JSONObject) array.get(0);
        for (Object k : languages.keys()) {
            String key = (String) k;
            if (key.equals(main.getProfileSelected().getLanguage().getID())) {
                return true;
            }
        }
        return false;
    }

    static Course getCourse(String name) {
        for (Course course : main.getCourses()) {
            if (course.name.equals(name)) {
                return course;
            }
        }
        return null;
    }

}
