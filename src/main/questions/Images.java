package main.questions;

import main.Main;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Images extends Question {

    private static final int GAP = 20;
    private static Random random = new Random();

    private ArrayList<Image> questions = new ArrayList<>();
    private Image input;
    private Image answer;

    public Images(JSONObject json) {
        super(json);
        JSONArray array = json.getJSONArray("array");
        for (int i = 0;i < array.size();i++) {
            JSONObject object = (JSONObject) array.get(i);
            this.questions.add(new Image(main.getProfileSelected().getCourseSelected().getPath() + "/" + object.getString("image"), object.getString("name"), getTranslation(object.getJSONArray("languages")), this));
        }
    }

    private static String getTranslation(JSONArray array) {
        for (int i = 0;i < array.size();i++) {
            JSONObject object = (JSONObject) array.get(i);
            if (object.getString("language").equals(main.getProfileSelected().getLanguage().getID())) {
                return object.getString("name");
            }
        }
        return null;
    }

    public static boolean hasLanguage(JSONObject object) {
        JSONArray array = object.getJSONArray("array");
        for (int i = 0;i < array.size();i++) {
            JSONObject image = (JSONObject) array.get(i);
            if (getTranslation(image.getJSONArray("languages")) == null) {
                return false;
            }
        }
        return true;
    }

    public void setInput(Image image) {
        this.input = image;
    }

    public Image getInput() {
        return input;
    }

    @Override
    public void setup() {
        this.answer = this.questions.get(random.nextInt(this.questions.size()));

        this.getInstruction("images", false);
        this.instruction = this.instruction.replace("@", this.answer.getTranslation());

        input = null;

        Collections.shuffle(this.questions);

        this.setCompleted(false);
    }

    @Override
    public void draw() {
        drawInstruction(y);

        int drawX = (Main.getScreenWidth() - Image.SIZE * this.questions.size() - GAP * (this.questions.size() - 1)) / 2;
        for (Image question : this.questions) {
            question.draw(drawX, y + 80);
            drawX += (Image.SIZE + GAP);
        }
    }

    @Override
    public void check() {
        this.setCorrect(this.input.equals(this.answer));
    }

    @Override
    public boolean canCheck() {
        return this.input != null;
    }

    @Override
    public String getAnswers() {
        return answer.getName();
    }

    @Override
    public void clicked() {
        for (Image question : this.questions) {
            question.clicked();
        }
    }
}
