package main;

import main.questions.*;
import processing.core.PConstants;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Lesson extends Entity {

    static final int SIZE = 100;
    static final int HEIGHT = SIZE + 40;

    private int x;
    private int y;

    private String name;
    private PImage image;
    private JSONObject json;
    private ArrayList<Question> questions = new ArrayList<>();
    private int questionIndex;

    private int correctQuestions;
    private int wrongQuestions;
    private long time;

    Lesson(JSONObject json) {
        this.name = json.getString("name");
        String image = main.getProfileSelected().getCourseSelected().getPath() + "/" + json.getString("image");
        if (!new File(image).exists()) {
            this.image = main.loadImage("images/error.png");
        } else {
            this.image = main.loadImage(image);
        }
        this.image.resize(SIZE, SIZE);
        this.json = json;
    }

    private boolean inBounds() {
        return main.mouseX >= this.x && main.mouseX <= this.x + SIZE && main.mouseY >= this.y && main.mouseY <= this.y + SIZE;
    }

    //is the current lesson supported by the profile language
    private boolean isSupported() {
        ArrayList<String> supportedLanguages = new ArrayList<>(Arrays.asList(json.getString("supports").split(",")));
        return supportedLanguages.contains(main.getProfileSelected().getLanguage().getID());
    }

    private void loadQuestions() {
        this.correctQuestions = 0;
        this.wrongQuestions = 0;
        this.time = 0;

        //word bank
        if (!json.isNull("word bank")) {
            for (int i = 0; i < json.getJSONArray("word bank").size(); i++) {
                JSONObject object = (JSONObject) json.getJSONArray("word bank").get(i);
                int type = object.getInt("type");
                JSONObject languageObjects = getLanguage(object.getJSONArray("languages"));
                if (languageObjects != null) {
                    if (type == Question.toOld) {
                        this.questions.add(new WordBank(object, object.getString("question"), languageObjects.getString("answer"), languageObjects.getString("choices")));
                    } else if (type == Question.toNew) {
                        this.questions.add(new WordBank(object, languageObjects.getString("question"), object.getString("answer"), object.getString("choices")));
                    }
                }
            }
        }

        //typing
        if (!json.isNull("typing")) {
            for (int i = 0; i < json.getJSONArray("typing").size(); i++) {
                JSONObject object = (JSONObject) json.getJSONArray("typing").get(i);
                int type = object.getInt("type");
                JSONObject languageObjects = getLanguage(object.getJSONArray("languages"));
                if (languageObjects != null) {
                    if (type == Question.toOld) {
                        this.questions.add(new Typing(object, object.getString("question"), languageObjects.getString("answer")));
                    } else if (type == Question.toNew) {
                        this.questions.add(new Typing(object, languageObjects.getString("question"), object.getString("answer")));
                    }
                }
            }
        }

        //images
        if (!json.isNull("images")) {
            for (int i = 0; i < json.getJSONArray("images").size(); i++) {
                JSONObject object = (JSONObject) json.getJSONArray("images").get(i);
                if (Images.hasLanguage(object)) {
                    this.questions.add(new Images(object));
                }
            }
        }

        //multiple choice
        if (!json.isNull("multiple choice")) {
            for (int i = 0; i < json.getJSONArray("multiple choice").size(); i++) {
                JSONObject object = (JSONObject) json.getJSONArray("multiple choice").get(i);
                int type = object.getInt("type");
                JSONObject languageObjects = getLanguage(object.getJSONArray("languages"));
                if (languageObjects != null) {
                    if (type == Question.toOld) {
                        this.questions.add(new MultipleChoice(object, object.getString("question"), languageObjects.getString("answer"), languageObjects.getString("choices")));
                    } else if (type == Question.toNew) {
                        this.questions.add(new MultipleChoice(object, languageObjects.getString("question"), object.getString("answer"), object.getString("choices")));
                    }
                } else if (type == Question.same) {
                    this.questions.add(new MultipleChoice(object, object.getString("question"), object.getString("answer"), object.getString("choices")));
                }
            }
        }

        Collections.shuffle(this.questions);
    }

    public void incrementCorrectQuestions() {
        this.correctQuestions++;
    }

    public void incrementWrongQuestions() {
        this.wrongQuestions++;
    }

    public int getCorrectQuestions() {
        return correctQuestions;
    }

    public int getWrongQuestions() {
        return wrongQuestions;
    }

    public void incrementTime() {
        this.time++;
    }

    public long getTime() {
        return this.time;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void nextQuestion() {
        this.questionIndex++;
    }

    public Question getCurrentQuestion() {
        return this.questions.get(this.questionIndex);
    }

    public void draw(int x, int y) {
        this.x = x;
        this.y = y;

        main.image(this.image, x, y);

        main.textFont(main.getProfileSelected().getLanguage().getFont(), 26);
        main.textAlign(PConstants.CENTER, PConstants.CENTER);
        main.fill(0);
        main.text(this.name, x + SIZE / 2, y + SIZE + 20);

        if (this.inBounds()) {
            main.noStroke();
            if (this.isSupported()) {
                main.fill(255, 255, 255, 80);
            } else {
                main.fill(255, 0, 0, 80);
            }
            main.rect(x, y, SIZE, SIZE);
        }

        //TODO: Temporary
//        main.stroke(0);
//        main.noFill();
//        main.rect(x, y, SIZE, HEIGHT);
    }

    public void clicked() {
        if (this.inBounds() && this.isSupported()) {
            this.loadQuestions();
            Main.lessonRoom.setLesson(this);
            Main.lessonRoom.setProgressBar();
            main.switchRoom(Main.lessonRoom);
        }
    }

    public void mousePressed() {
        this.getCurrentQuestion().clicked();
    }

    public boolean isLastQuestion() {
        return this.questionIndex == this.questions.size() - 1;
    }

    private static JSONObject getLanguage(JSONArray array) {
        if (array == null) {
            return null;
        }
        for (int i = 0; i < array.size(); i++) {
            JSONObject object = (JSONObject) array.get(i);
            if (object.getString("language").equals(main.getProfileSelected().getLanguage().getID())) {
                return object;
            }
        }
        return null;
    }

}
