package main.rooms;

import main.Course;
import main.Main;
import main.buttons.Back;
import processing.core.PConstants;

public class RemoveCourses extends Room {

    private static final int coursesPerPage = 8;
    private int coursesPage;

    private Back back;
    //private Remove remove;
    private Course removeCourse;

    @Override
    public void setup() {
        this.back = new Back();
        //this.remove = new Remove();
        this.coursesPage = 0;
    }

    @Override
    public void draw() {
        main.noStroke();
        main.fill(255);
        main.rect(0, 0, Main.getScreenWidth(), Main.getScreenHeight());

        main.textFont(getTitleFont(), 80);
        main.textAlign(PConstants.CENTER, PConstants.CENTER);
        main.fill(0);
        main.text("Remove Courses", Main.getScreenWidth() / 2, 100);

        int gapX = (Main.getScreenWidth() - Course.WIDTH * 4) / 5;
        int coursesX = gapX;
        int coursesY = 200;
        for (int i = coursesPage * coursesPerPage; i < (coursesPage + 1) * coursesPerPage; i++) {
            if (i < main.getProfileSelected().getCourses().size()) {
                main.getProfileSelected().getCourses().get(i).draw(coursesX, coursesY);
                coursesX += (gapX + Course.WIDTH);
            }
        }

        back.draw(40, 40);
        //remove.draw(Main.getScreenWidth() / 2 - remove.getWidth() / 2, Main.getScreenHeight() - 100);

        if (removeCourse != null) {
            main.getProfileSelected().getCourses().remove(removeCourse);
            main.getProfileSelected().save();
            removeCourse = null;
        }
    }

    public void mousePressed() {
        for (Course course : main.getProfileSelected().getCourses()) {
            course.clicked();
        }
        back.clicked();
        //remove.clicked();
    }

    public void setRemoveCourse(Course course) {
        this.removeCourse = course;
    }

}
