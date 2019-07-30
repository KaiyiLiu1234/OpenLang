package main.questions;

import main.Entity;
import main.Language;
import processing.core.PConstants;

class Choice extends Entity {

    private static final int GAP = 15;
    private static final int fontSize = 40;
    private static final int circleGap = 10; //gap between circle and text

    private int x;
    private int y;

    private String text;
    private MultipleChoice question;
    private Language language;

    Choice(String text, Language language, MultipleChoice question) {
        this.text = text;
        this.language = language;
        this.question = question;
    }

    void draw(int x, int y, float length) {
        this.x = x;
        this.y = y;

        main.stroke(0);
        main.noFill();
        main.rect(x, y, length, this.getHeight());

        final int circleY = 3; //the circle is a bit off lol
        main.ellipse(x + GAP + this.getCircleSize() / 2, y + GAP + this.getCircleSize() / 2 + circleY, this.getCircleSize(), this.getCircleSize());

        main.textFont(this.language.getFont(), fontSize);
        main.textAlign(PConstants.LEFT, PConstants.CENTER);
        main.fill(0);
        main.text(this.text, x + GAP + this.getCircleSize() + circleGap, y + this.getHeight() / 2 - 6);

        if (this.inBounds()) {
            main.noStroke();
            main.fill(255, 255, 255, 80);
            main.rect(x, y, this.getLength(), this.getHeight());
        }

        if (this.question.getInput().size() > 0 && this.question.getInput().contains(this)) {
            main.stroke(0, 198, 255);
            main.fill(0, 198, 255);
            final int smallSize = 20; //how much smaller the circle should be
            main.ellipse(x + GAP + this.getCircleSize() / 2, y + GAP + this.getCircleSize() / 2 + circleY, this.getCircleSize() - smallSize, this.getCircleSize() - smallSize);
        }
    }

    float getLength() {
        main.textFont(this.language.getFont(), fontSize);
        return GAP + this.getCircleSize() + circleGap + main.textWidth(this.text) + GAP;
    }

    float getHeight() {
        main.textFont(this.language.getFont(), fontSize);
        return GAP + main.textAscent() + main.textDescent() + GAP;
    }

    private float getCircleSize() {
        main.textFont(this.language.getFont(), fontSize);
        return main.textAscent() + main.textDescent() - 6;
    }

    private boolean inBounds() {
        return main.mouseX >= this.x && main.mouseX <= this.x + this.getLength() && main.mouseY >= this.y && main.mouseY <= this.y + this.getHeight();
    }

    void clicked() {
        if (this.inBounds()) {
            if (!this.question.getInput().contains(this)) {
                this.question.getInput().add(this);
            } else {
                this.question.getInput().remove(this);
            }
        }
    }

    String getText() {
        return this.text;
    }

}
