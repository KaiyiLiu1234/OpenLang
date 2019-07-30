package main.questions;

import main.Entity;
import main.Language;
import processing.core.PConstants;

import java.util.ArrayList;

public class Word extends Entity {

    private static final int fontSize = 40;

    private int x;
    private int y;

    private String word;
    private ArrayList<Word> input;
    private Language language;

    Word(String word, ArrayList<Word> input, Language language) {
        this.word = word;
        this.input = input;
        this.language = language;
    }

    public float getWidth() {
        main.textFont(this.language.getFont(), fontSize);
        return main.textWidth(word) + 22;
    }

    private float getHeight() {
        main.textFont(this.language.getFont(), fontSize);
        return main.textAscent() + main.textDescent() + 4;
    }

    public void draw(int x, int y) {
        this.x = x;
        this.y = y;

        float width = this.getWidth();
        float height = this.getHeight();

        main.noStroke();
        main.fill(230);
        main.rect(x, y, width, height);

        main.fill(0);
        main.textAlign(PConstants.CENTER, PConstants.CENTER);
        main.text(word, x + width / 2, y + height / 2 - 9);

        if (this.inBounds()) {
            main.noStroke();
            main.fill(255, 255, 255, 80);
            main.rect(x, y, width, height);
        }
    }

    private boolean inBounds() {
        return main.mouseX >= this.x && main.mouseX <= this.x + this.getWidth() && main.mouseY >= this.y && main.mouseY <= this.y + this.getHeight();
    }

    public void clicked() {
        if (this.inBounds()) {
            if (!this.input.contains(this)) {
                this.input.add(this);
            } else {
                this.input.remove(this);
            }
        }
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean contains(String string, ArrayList<Word> list) {
        for (Word word : list) {
            if (word.word.equals(string)) {
                return true;
            }
        }
        return false;
    }

    public String getWord() {
        return word;
    }
}
