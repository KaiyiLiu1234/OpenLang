package main.rooms;

import main.Lesson;
import main.Main;
import main.buttons.Next;
import processing.core.PConstants;
import processing.core.PImage;

public class CompleteLesson extends Room {

    private static final int SIZE = 100;

    private Lesson lesson;
    private PImage questions;
    private PImage correct;
    private PImage wrong;
    private PImage time;
    private Next next;

    public CompleteLesson() {
        this.questions = main.loadImage("images/questions.png");
        this.questions.resize(SIZE, SIZE);
        this.correct = main.loadImage("images/correct_icon.png");
        this.correct.resize(SIZE, SIZE);
        this.wrong = main.loadImage("images/wrong_icon.png");
        this.wrong.resize(SIZE, SIZE);
        this.time = main.loadImage("images/time.png");
        this.time.resize(SIZE, SIZE);
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    @Override
    public void setup() {
        this.next = new Next(null);
    }

    @Override
    public void draw() {
        main.noStroke();
        main.fill(255);
        main.rect(0, 0, Main.getScreenWidth(), Main.getScreenHeight());

        main.textFont(getTitleFont(), 80);
        main.textAlign(PConstants.CENTER, PConstants.CENTER);
        main.fill(0);
        main.text(main.getProfileSelected().getLanguage().getJson().getString("lesson complete"), Main.getScreenWidth() / 2, 100);

        final int y = 260;
        main.textFont(main.getProfileSelected().getLanguage().getFont(), 40);
        drawIcon(questions, "Total number of questions: " + lesson.getQuestions().size(), y);
        drawIcon(correct, "Number of questions answered correctly: " + lesson.getCorrectQuestions(), y + 140);
        drawIcon(wrong, "Number of questions answered incorrectly: " + lesson.getWrongQuestions(), y + 280);
        drawIcon(time, "Time elapsed: " + lesson.getTime() + " seconds ", y + 420);

        this.next.draw(Main.getScreenWidth() / 2 - this.next.getWidth(), Main.getScreenHeight() - 100);
    }

    private void drawIcon(PImage icon, String text, int y) {
        final int gap = 20;
        float length = icon.width + gap + main.textWidth(text);
        float drawX = Main.getScreenWidth() / 2 - length / 2;
        main.image(icon, drawX, y - icon.height / 2);
        main.textAlign(PConstants.LEFT, PConstants.CENTER);
        main.fill(0);
        main.text(text, drawX + icon.width + gap, y - 7);
    }

    @Override
    public void mousePressed() {
        this.next.clicked();
    }
}
