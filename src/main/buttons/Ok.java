package main.buttons;

import main.Lesson;
import main.rooms.InLesson;

public class Ok extends Button {

    private InLesson room;

    public Ok(InLesson room) {
        super("ok", 60, 60);
        this.room = room;
    }

    @Override
    public void clicked() {
        if (this.inBounds() && this.room.getLesson().getCurrentQuestion().canCheck()) {
            this.room.getLesson().getCurrentQuestion().setCompleted(true);
            this.room.getLesson().getCurrentQuestion().check();
            if (this.room.getLesson().getCurrentQuestion().isCorrect()) {
                this.room.getLesson().incrementCorrectQuestions();
                this.room.getProgressBar().increment();
            } else {
                this.room.getLesson().incrementWrongQuestions();
            }
        }
    }
}
