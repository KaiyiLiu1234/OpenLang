package main.questions;

import main.Language;
import main.Main;
import processing.core.PConstants;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Typing extends Question {

    private static final int GAP = 20;
    private static final int maxLength = 100;

    private String question;
    private ArrayList<Vocab> questionWords = new ArrayList<>();
    private ArrayList<String> answers;
    private String input;
    private int time;
    private boolean showCursor;

    public Typing(JSONObject json, String question, String answer) {
        super(json);
        this.type = json.getInt("type");
        this.question = question;
        this.answers = new ArrayList<>(Arrays.asList(answer.split(";")));
    }

    private float getQuestionLength() {
        float length = 0;
        for (Vocab vocab : questionWords) {
            length += (vocab.getLength() + GAP);
        }
        return length - GAP;
    }

    @Override
    public void setup() {
        this.getInstruction("typing", true);

        this.input = "";

        this.questionWords.clear();
        for (String string : question.split(" ")) {
            if (this.type == Question.toOld) {
                this.questionWords.add(new Vocab(string, main.getProfileSelected().getCourseSelected().getLanguage().getTranslation(string, main.getProfileSelected().getLanguage()), main.getProfileSelected().getCourseSelected().getLanguage()));
            } else {
                this.questionWords.add(new Vocab(string, null, main.getProfileSelected().getLanguage()));
            }
        }

        this.time = main.millis();
        this.showCursor = true;

        this.setCompleted(false);
    }

    @Override
    public void draw() {
        this.drawInstruction(y);

        int vocabX = Math.round(Main.getScreenWidth() / 2 - getQuestionLength() / 2);
        for (Vocab vocab : questionWords) {
            vocab.draw(vocabX, y + 80);
            vocabX += (vocab.getLength() + GAP);
        }

        //input
        if (this.type == toOld) {
            main.textFont(main.getProfileSelected().getLanguage().getFont(), 40);
        } else if (this.type == toNew) {
            main.textFont(main.getProfileSelected().getCourseSelected().getLanguage().getFont(), 40);
        }
        main.textAlign(PConstants.CENTER, PConstants.CENTER);
        main.fill(0);
        main.text(this.input, Main.getScreenWidth() / 2, y + 180);
        if (this.showCursor) {
            float x = Main.getScreenWidth() / 2 + main.textWidth(this.input) / 2;
            main.stroke(0);
            main.line(x, y + 170, x, y + 205);
        }

        if (main.millis() - time > 500) {
            this.time = main.millis();
            this.showCursor = !this.showCursor;
        }
    }

    @Override
    public void clicked() {

    }

    @Override
    public void check() {
        for (String answer : this.answers) {
            if (answer.toLowerCase().equals(this.input.trim().toLowerCase())) {
                this.setCorrect(true);
                return;
            }
        }
        this.setCorrect(false);
    }

    @Override
    public boolean canCheck() {
        return !this.input.trim().equals("");
    }

    @Override
    public String getAnswers() {
        StringBuilder builder = new StringBuilder();
        for (String answer : this.answers) {
            builder.append(answer);
            if (this.answers.indexOf(answer) != this.answers.size() - 1) {
                builder.append(" / ");
            }
        }
        return builder.toString();
    }

    @Override
    public void keyPressed() {
        if (this.input.length() <= maxLength) {
            if (main.key == '\b' && this.input.length() > 0) {
                this.input = this.input.substring(0, this.input.length() - 1);
            } else if (validKey()) {
                this.input += main.key;
            }
            //force show cursor
            this.showCursor = true;
            this.time = main.millis();
        }
    }

    private static boolean validKey() {
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for (char c : alphabet) {
            if (c == main.key || Character.toUpperCase(c) == main.key) {
                return true;
            }
        }
        return main.key == ',' || main.key == '.' || main.key == ' ' || main.key == '!' || main.key == '\"' || main.key == ':' || main.key == ';' || main.key == '\'' || main.key == '?';
    }
}
