package main.rooms;

import main.Course;
import main.Language;
import main.Main;
import main.buttons.Back;
import main.buttons.LanguageButton;
import main.buttons.Remove;
import processing.core.PConstants;

import java.util.ArrayList;

public class Courses extends Room {

    private static final int coursesPerPage = 8;
    private int coursesPage;

    private Back back;
    private Remove remove;
    private LanguageButton languageButton;
    private ArrayList<Course> courses = new ArrayList<>();

    @Override
    public void setup() {
        this.back = new Back();
        this.remove = new Remove();
        this.languageButton = new LanguageButton(main.getProfileSelected().getLanguage());
        this.coursesPage = 0;
        this.courses = this.getCourses();

        main.getProfileSelected().save();
    }

    @Override
    public void draw() {
        main.noStroke();
        main.fill(255);
        main.rect(0, 0, Main.getScreenWidth(), Main.getScreenHeight());

        main.textFont(getTitleFont(), 80);
        main.textAlign(PConstants.CENTER, PConstants.CENTER);
        main.fill(0);
        main.text("Your Courses", Main.getScreenWidth() / 2, 100);

        int gapX = (Main.getScreenWidth() - Course.WIDTH * 4) / 5;
        int coursesX = gapX;
        int coursesY = 200;
        for (int i = coursesPage * coursesPerPage; i < (coursesPage + 1) * coursesPerPage; i++) {
            if (i < this.courses.size()) {
                this.courses.get(i).draw(coursesX, coursesY);
                coursesX += (gapX + Course.WIDTH);
            }
        }

        this.languageButton.draw(Main.getScreenWidth() - Language.WIDTH - 80, 40);

        back.draw(40, 40);
        if (this.canRemove()) {
            remove.draw(Main.getScreenWidth() / 2 - remove.getWidth() / 2, Main.getScreenHeight() - 100);
        }
    }

    //deep copies the profile's courses and adds a add new course button
    private ArrayList<Course> getCourses() {
        ArrayList<Course> list = new ArrayList<>(main.getProfileSelected().getCourses());
        list.add(new Course());
        return list;
    }

    public void mousePressed() {
        for (Course course : courses) {
            course.clicked();
        }
        back.clicked();
        if (this.canRemove()) {
            remove.clicked();
        }
        this.languageButton.clicked();
    }

    //dont show remove button if there are no courses
    private boolean canRemove() {
        return main.getProfileSelected().getCourses().size() > 0;
    }
}
