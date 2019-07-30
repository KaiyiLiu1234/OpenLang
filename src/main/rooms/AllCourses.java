package main.rooms;

import main.Course;
import main.Main;
import main.buttons.Back;
import processing.core.PConstants;

public class AllCourses extends Room {

    private static final int coursesPerPage = 8;
    private int coursesPage;

    private Back back;

    @Override
    public void setup() {
        this.coursesPage = 0;
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
        main.text("All Courses", Main.getScreenWidth() / 2, 100);

        int gapX = (Main.getScreenWidth() - Course.WIDTH * 4) / 5;
        int coursesX = gapX;
        int coursesY = 200;
        for (int i = coursesPage * coursesPerPage; i < (coursesPage + 1) * coursesPerPage; i++) {
            if (i < main.getCourses().size()) {
                main.getCourses().get(i).draw(coursesX, coursesY);

                if (main.getCourses().get(i).alreadyLearning()) {
                    main.noStroke();
                    main.fill(255,0,0,80);
                    main.rect(coursesX, coursesY, Course.WIDTH, Course.HEIGHT);
                }

                coursesX += (gapX + Course.WIDTH);
            }
        }
        back.draw(40, 40);
    }

    public void mousePressed() {
        for (Course course : main.getCourses()) {
            course.clicked();
        }
        back.clicked();
    }
}
