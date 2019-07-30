package main.buttons;

import main.Entity;
import processing.core.PImage;

public abstract class Button extends Entity {

    private PImage image;
    private int width;
    private int height;
    private int x;
    private int y;

    Button(String name, int width, int height) {
        this.image = main.loadImage("images/" + name + ".png");
        this.width = width;
        this.height = height;
        this.image.resize(this.width, this.height);
    }

    public void draw(int x,int y) {
        this.x = x;
        this.y = y;

        main.image(this.image, x, y);

        if (this.inBounds()) {
            main.noStroke();
            main.fill(255,255,255,80);
            main.rect(x, y, width, height);
        }
    }

    boolean inBounds() {
        return main.mouseX >= this.x && main.mouseX <= this.x + this.width && main.mouseY >= this.y && main.mouseY <= this.y + this.height;
    }

    public abstract void clicked();

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
