package main.buttons;

import main.Main;
import main.rooms.*;

public class Back extends Button {

    public Back() {
        super("back", 60, 60);
    }

    @Override
    public void clicked() {
        if (this.inBounds()) {
            if (main.getRoom() instanceof Courses) {
                main.switchRoom(Main.profilesRoom);
            } else if (main.getRoom() instanceof AllCourses || main.getRoom() instanceof RemoveCourses || main.getRoom() instanceof Languages || main.getRoom() instanceof Levels) {
                main.switchRoom(Main.coursesRoom);
            } else if (main.getRoom() instanceof InLesson) {
                main.switchRoom(Main.levelsRoom);
            }
        }
    }

}
