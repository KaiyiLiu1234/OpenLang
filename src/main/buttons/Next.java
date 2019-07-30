package main.buttons;

import main.Lesson;
import main.Main;
import main.rooms.CompleteLesson;
import main.rooms.InLesson;

public class Next extends Button {

    private Lesson lesson;

    public Next(Lesson lesson) {
        super("next", 60, 60);
        this.lesson = lesson;
    }

    @Override
    public void clicked() {
        if (this.inBounds()) {
            if (main.getRoom() instanceof InLesson) {
                if (!this.lesson.getCurrentQuestion().isCorrect()) { //if incorrect, add the question again until user gets it right
                    this.lesson.getCurrentQuestion().setup();
                    this.lesson.getQuestions().add(this.lesson.getCurrentQuestion());
                }
                if (!lesson.isLastQuestion()) {
                    this.lesson.nextQuestion();
                    main.switchRoom(Main.lessonRoom);
                } else {
                    Main.completeRoom.setLesson(this.lesson);
                    main.switchRoom(Main.completeRoom);
                }
            } else if (main.getRoom() instanceof CompleteLesson) {
                main.switchRoom(Main.levelsRoom);
            }
        }
    }
}
