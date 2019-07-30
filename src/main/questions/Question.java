package main.questions;

import main.Entity;
import main.Language;
import main.Main;
import processing.core.PConstants;
import processing.data.JSONArray;
import processing.data.JSONObject;

public abstract class Question extends Entity {

    //old being the language you already know
    public static final int toOld = 0;
    public static final int toNew = 1;
    public static final int same = 2;

    static final int y = 240;

    int type;
    String instruction;

    private JSONObject json;
    private boolean completed;
    private boolean correct;

    Question(JSONObject json) {
        this.json = json;
    }

    void setCorrect(boolean bool) {
        this.correct = bool;
    }

    void drawInstruction(int y) {
        if (this.instruction.contains("@")) {
            int index = this.instruction.indexOf('@');

            main.textFont(main.getProfileSelected().getLanguage().getFont(), 30);
            float profileLangFrontLength = main.textWidth(this.instruction.substring(0, index));
            main.textFont(main.getProfileSelected().getCourseSelected().getLanguage().getFont(), 30);
            float courseLangLength = main.textWidth(main.getProfileSelected().getCourseSelected().getLanguage().getName());
            main.textFont(main.getProfileSelected().getLanguage().getFont(), 30);
            float profileLangBackLength = main.textWidth(this.instruction.substring(index + 1, this.instruction.length()));

            float totalLength = profileLangFrontLength + courseLangLength + profileLangBackLength;

            main.textAlign(PConstants.LEFT, PConstants.CENTER);
            main.fill(0);

            main.textFont(main.getProfileSelected().getLanguage().getFont(), 30);
            main.text(this.instruction.substring(0, index), Main.getScreenWidth() / 2 - totalLength / 2, y);
            main.textFont(main.getProfileSelected().getCourseSelected().getLanguage().getFont(), 30);
            main.text(main.getProfileSelected().getCourseSelected().getLanguage().getName(), Main.getScreenWidth() / 2 - totalLength / 2 + profileLangFrontLength, y);
            main.textFont(main.getProfileSelected().getLanguage().getFont(), 30);
            main.text(this.instruction.substring(index + 1, this.instruction.length()), Main.getScreenWidth() / 2 - totalLength / 2 + profileLangFrontLength + courseLangLength, y);
        } else {
            main.textFont(main.getProfileSelected().getLanguage().getFont(), 30);
            main.textAlign(PConstants.CENTER, PConstants.CENTER);
            main.fill(0);
            main.text(this.instruction, Main.getScreenWidth() / 2, y);
        }
    }

    void getInstruction(String key, boolean hasType) {
        if (!this.json.isNull("instructions")) { //override default instructions if specified
            JSONArray array = this.json.getJSONArray("instructions");
            for (int i = 0;i < array.size();i++) {
                JSONObject object = (JSONObject) array.get(i);
                if (object.getString("language").equals(main.getProfileSelected().getLanguage().getID())) {
                    this.instruction = object.getString("instruction");
                    return;
                }
            }
        }
        JSONArray array = main.getProfileSelected().getLanguage().getJson().getJSONArray("questions");
        JSONObject object = (JSONObject) array.get(0);
        if (hasType) {
            if (this.type == Question.toOld) {
                this.instruction = object.getString(key + " 0");
            } else if (this.type == Question.toNew) {
                this.instruction = object.getString(key + " 1");
            }
        } else {
            this.instruction = object.getString(key);
        }
    }

    Language getLanguage() {
        switch (this.type) {
            case Question.toOld:
                return main.getProfileSelected().getLanguage();
            case Question.toNew:
                return main.getProfileSelected().getCourseSelected().getLanguage();
            case Question.same:
                return main.getProfileSelected().getCourseSelected().getLanguage();
            default:
                return null;
        }
    }

    public abstract void setup();

    public abstract void draw();

    public abstract void clicked();

    public abstract void check();

    public abstract boolean canCheck();

    public abstract String getAnswers();

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean bool) {
        this.completed = bool;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void keyPressed() {

    }

}
