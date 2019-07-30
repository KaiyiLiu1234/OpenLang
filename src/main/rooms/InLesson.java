package main.rooms;

import main.Lesson;
import main.Main;
import main.ProgressBar;
import main.buttons.Back;
import main.buttons.Next;
import main.buttons.Ok;
import processing.core.PConstants;
import processing.core.PImage;

public class InLesson extends Room {

    private static final int iconSize = 100;
    private static PImage correct;
    private static PImage correctIcon;
    private static PImage wrong;
    private static PImage wrongIcon;

    private Lesson lesson;
    private Ok ok;
    private Back back;
    private Next next;
    private ProgressBar progressBar;
    private int time;

    public InLesson() {
        correct = main.loadImage("images/correct.png");
        wrong = main.loadImage("images/wrong.png");
        correctIcon = main.loadImage("images/correct_icon.png");
        correctIcon.resize(iconSize, iconSize);
        wrongIcon = main.loadImage("images/wrong_icon.png");
        wrongIcon.resize(iconSize, iconSize);
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    //only call this method when starting the lesson from the beginning
    public void setProgressBar() {
        this.progressBar = new ProgressBar(lesson.getQuestions().size(), 1000);
    }

    @Override
    public void setup() {
        this.ok = new Ok(this);
        this.back = new Back();
        this.next = new Next(this.lesson);
        this.lesson.getCurrentQuestion().setup();
    }

    @Override
    public void draw() {
        main.noStroke();
        main.fill(255);
        main.rect(0, 0, Main.getScreenWidth(), Main.getScreenHeight());

        this.progressBar.draw(Main.getScreenWidth() / 2 - progressBar.getLength() / 2, 140);

        lesson.getCurrentQuestion().draw();

        if (!lesson.getCurrentQuestion().isCompleted()) {
            if (lesson.getCurrentQuestion().canCheck()) {
                this.ok.draw(Main.getScreenWidth() - 40 - this.ok.getWidth(), Main.getScreenHeight() - 40 - this.ok.getHeight());
            }
        } else {
            this.next.draw(Main.getScreenWidth() - 40 - this.next.getWidth(), Main.getScreenHeight() - 40 - this.next.getHeight());
        }
        this.back.draw(40, 40);

        if (lesson.getCurrentQuestion().isCompleted()) {
            String text;
            PImage image;
            PImage icon;
            if (lesson.getCurrentQuestion().isCorrect()) {
                text = main.getProfileSelected().getLanguage().getJson().getString("correct");
                image = correct;
                icon = correctIcon;
            } else {
                text = main.getProfileSelected().getLanguage().getJson().getString("wrong");
                image = wrong;
                icon = wrongIcon;
            }

            final int gapY = 0; //how many pixels from the bottom of the screen
            final int offset = 20; //to account for the extra pixels around the image
            int shift = 0; //shift icon and text upwards to allocate more space to display the correct answer
            if (!lesson.getCurrentQuestion().isCorrect()) {
                shift = 40;
            }

            main.image(image, Main.getScreenWidth() / 2 - image.width / 2 - offset, Main.getScreenHeight() - image.height - gapY - offset);

            main.textFont(main.getProfileSelected().getLanguage().getFont(), 40);
            main.textAlign(PConstants.LEFT, PConstants.CENTER);
            main.fill(255);
            final int gap = 20;
            float drawX = Main.getScreenWidth() / 2 - image.width / 2 + (image.width - (iconSize + gap + main.textWidth(text))) / 2 - offset;

            main.image(icon, drawX, Main.getScreenHeight() - image.height / 2 - icon.height / 2 - gapY - offset - shift);
            main.text(text, drawX + iconSize + gap, Main.getScreenHeight() - image.height / 2 - gapY - 10 - offset - shift);

            if (!lesson.getCurrentQuestion().isCorrect()) {
                main.textAlign(PConstants.CENTER, PConstants.CENTER);
                main.fill(255);
                main.text("Answer: " + lesson.getCurrentQuestion().getAnswers(), Main.getScreenWidth() / 2, Main.getScreenHeight() - gapY - offset - 80);
            }
        }

        if (main.millis() - time >= 1000) {
            this.time = main.millis();
            this.lesson.incrementTime();
        }
    }

    @Override
    public void mousePressed() {
        lesson.mousePressed();
        if (!lesson.getCurrentQuestion().isCompleted()) {
            ok.clicked();
        } else {
            next.clicked();
        }
        back.clicked();
    }

    @Override
    public void keyPressed() {
        lesson.getCurrentQuestion().keyPressed();
    }
}
