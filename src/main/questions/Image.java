package main.questions;

import main.Entity;
import processing.core.PConstants;
import processing.core.PImage;

public class Image extends Entity {

    static final int SIZE = 256;
    private static final int HEIGHT = SIZE + 60;

    private int x;
    private int y;

    private PImage image;
    private String name;
    private String translation;
    private Images question;

    Image(String image, String name, String translation, Images question) {
        this.image = main.loadImage(image);
        this.image.resize(SIZE, SIZE);
        this.name = name;
        this.translation = translation;
        this.question = question;
    }

    String getTranslation() {
        return translation;
    }

    String getName() {
        return this.name;
    }

    void draw(int x,int y) {
        this.x = x;
        this.y = y;

        main.image(this.image, x, y);

        main.textFont(main.getProfileSelected().getCourseSelected().getLanguage().getFont(), 40);
        main.textAlign(PConstants.CENTER, PConstants.CENTER);
        main.fill(0);
        main.text(this.name, x + this.image.width / 2, y + this.image.height + 24);

        if (this.inBounds()) {
            main.noStroke();
            main.fill(255,255,255,80);
            main.rect(x, y, SIZE, HEIGHT);
        }

        if (this.question.getInput() != null && this.question.getInput().equals(this)) {
            main.stroke(0,198,255);
            main.strokeWeight(5);
            main.noFill();
            main.rect(x, y, SIZE, HEIGHT);
            main.strokeWeight(1);
        }
    }

    private boolean inBounds() {
        return main.mouseX >= this.x && main.mouseX <= this.x + SIZE && main.mouseY >= this.y && main.mouseY <= this.y + HEIGHT;
    }

    void clicked() {
        if (this.inBounds()) {
            question.setInput(this);
        }
    }
}
