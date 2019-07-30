package main.buttons;

import main.Language;
import main.Main;
import processing.core.PImage;

public class LanguageButton extends Button {

    private static PImage outline;
    private Language language;

    public LanguageButton(Language language) {
        super("error", 250, 60);
        this.language = language;

        if (outline == null) {
            outline = main.loadImage("images/language.png");
        }
    }

    @Override
    public void draw(int x,int y) {
        this.setX(x);
        this.setY(y);

        main.image(outline, x - 20, y - 20);
        this.language.draw(x + (this.getWidth() - Language.WIDTH) / 2, y + (this.getHeight() - Language.HEIGHT) / 2);

        if (this.inBounds()) {
            main.noStroke();
            main.fill(255,255,255,80);
            main.rect(x, y, this.getWidth(), this.getHeight());
        }
    }

    @Override
    public void clicked() {
        if (this.inBounds()) {
            main.switchRoom(Main.languagesRoom);
        }
    }

}
