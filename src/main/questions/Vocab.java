package main.questions;

import main.Entity;
import main.Language;
import main.Main;
import processing.core.PConstants;

import java.util.ArrayList;

class Vocab extends Entity {

    private static final int GAP = 4;
    private static final int wordGap = 20;
    private static final int definitionSize = 30;

    private int x;
    private int y;

    private String word;
    private ArrayList<String> definitions;
    private Language language;

    Vocab(String word, ArrayList<String> definitions, Language language) {
        this.word = word;
        this.definitions = definitions;
        this.language = language;
    }

    void draw(int x,int y) {
        this.x = x;
        this.y = y;

        main.textFont(language.getFont(), 50);
        main.textAlign(PConstants.LEFT, PConstants.CENTER);
        main.fill(0);
        main.text(this.word, x, y + 12);

        if (this.inBounds()) {
            main.noStroke();
            main.fill(255,255,255,80);
            main.rect(x, y, this.getLength(), this.getHeight());

            if (this.definitions != null) {
                final int margin = 10;

                main.stroke(0);
                main.fill(255);
                main.rect(main.mouseX, main.mouseY, this.getDefinitionLength() + margin * 2, this.getDefinitionHeight() + margin * 2);

                main.textFont(main.getProfileSelected().getLanguage().getFont(), definitionSize);
                main.textAlign(PConstants.LEFT, PConstants.TOP);
                main.fill(0);

                int definitionX = main.mouseX + margin;
                int definitionY = main.mouseY + margin;
                for (String definition : this.definitions) {
                    main.text(definition, definitionX, definitionY);
                    definitionY += (main.textAscent() + main.textDescent() + GAP);
                }
            }
        }
    }

    public float getLength() {
        main.textFont(language.getFont(), 50);
        return main.textWidth(this.word);
    }

    private float getHeight() {
        main.textFont(language.getFont(), 50);
        return main.textAscent() + main.textDescent();
    }

    private float getDefinitionLength() {
        main.textFont(main.getProfileSelected().getLanguage().getFont(), definitionSize);
        float length = 0;
        for (String definition : this.definitions) {
            if (main.textWidth(definition) > length) {
                length = main.textWidth(definition);
            }
        }
        return length;
    }

    private float getDefinitionHeight() {
        main.textFont(main.getProfileSelected().getLanguage().getFont(), definitionSize);
        return (main.textAscent() + main.textDescent()) * this.definitions.size() + GAP * (this.definitions.size() - 1);
    }

    private boolean inBounds() {
        main.textFont(language.getFont(), 50);
        return main.mouseX >= this.x && main.mouseX <= this.x + this.getLength() && main.mouseY >= this.y && main.mouseY <= this.y + this.getHeight();
    }

    public static void drawVocab(ArrayList<Vocab> list, int drawX, int drawY) {
        Vocab selectedWord = getSelectedWord(list);
        int selectedX = 0;
        for (Vocab vocab : list) {
            if (selectedWord == null || !selectedWord.equals(vocab)) {
                vocab.draw(drawX, drawY);
            } else {
                selectedX = drawX;
            }
            drawX += (vocab.getLength() + wordGap);
        }
        if (selectedWord != null) {
            selectedWord.draw(selectedX, drawY);
        }
    }

    public static Vocab getSelectedWord(ArrayList<Vocab> list) {
        for (Vocab vocab : list) {
            if (vocab.inBounds()) {
                return vocab;
            }
        }
        return null;
    }

}
