package main.buttons;

import main.Main;

public class Remove extends Button {

    public Remove() {
        super("remove", 60, 60);
    }

    @Override
    public void clicked() {
        if (this.inBounds()) {
            main.switchRoom(Main.removeCoursesRoom);
        }
    }
}
