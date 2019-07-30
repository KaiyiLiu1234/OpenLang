package main;

import main.rooms.*;
import processing.core.PApplet;
import processing.event.MouseEvent;

import java.util.ArrayList;

public class Main extends PApplet {

    //gui variables
    private static final int screenWidth = 1600;
    private static final int screenHeight = 900;

    public static Courses coursesRoom;
    public static Profiles profilesRoom;
    public static AllCourses allCoursesRoom;
    public static RemoveCourses removeCoursesRoom;
    public static Languages languagesRoom;
    public static Levels levelsRoom;
    public static InLesson lessonRoom;
    public static CompleteLesson completeRoom;

    //room variables
    private Room room;

    //data variables
    private ArrayList<Language> languages = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<Profile> profiles = new ArrayList<>();

    private Profile profileSelected;

    public static void main(String[] args) {
        PApplet.main("main.Main");
    }

    public ArrayList<Language> getLanguages() {
        return this.languages;
    }

    public void settings() {
        size(screenWidth, screenHeight);
    }

    public void setup() {
        Entity.init(this);
        Course.setup();
        Profile.setup();
        Room.loadTitleFont();

        Language.loadLanguages();
        Course.loadCourses();
        Profile.loadProfiles();

        coursesRoom = new Courses();
        profilesRoom = new Profiles();
        allCoursesRoom = new AllCourses();
        removeCoursesRoom = new RemoveCourses();
        languagesRoom = new Languages();
        levelsRoom = new Levels();
        lessonRoom = new InLesson();
        completeRoom = new CompleteLesson();

        room = profilesRoom;
        profilesRoom.setup();
    }

    public void draw() {
        room.draw();
    }

    public void mousePressed() {
        room.mousePressed();
    }

    public void mouseWheel(MouseEvent event) {
        room.mouseWheel(event);
    }

    public void keyPressed() {
        room.keyPressed();
    }

    public void switchRoom(Room newRoom) {
        room = newRoom;
        room.setup();
    }

    public ArrayList<Course> getCourses() {
        return this.courses;
    }

    public ArrayList<Profile> getProfiles() {
        return this.profiles;
    }

    public Profile getProfileSelected() {
        return this.profileSelected;
    }

    public Room getRoom() {
        return this.room;
    }

    public void setProfileSelected(Profile profile) {
        profileSelected = profile;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

}
