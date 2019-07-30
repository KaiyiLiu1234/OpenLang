package main;

public class ProgressBar extends Entity {

    private static final int HEIGHT = 10;

    private int number;
    private int max;
    private int length;

    public ProgressBar(int max, int length) {
        this.number = 0;
        this.max = max;
        this.length = length;
    }

    public void draw(int x,int y) {
        main.noStroke();
        main.fill(200);
        main.rect(x, y, length, HEIGHT);

        main.fill(0, 255, 0);
        main.rect(x, y, (float) this.number / this.max * this.length, HEIGHT);
    }

    public void increment() {
        this.number++;
    }

    public int getLength() {
        return this.length;
    }

}
