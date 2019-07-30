package main.questions;

import main.Main;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class MultipleChoice extends Question {

    private static final int GAP = 20;

    private String question;
    private ArrayList<Vocab> questionWords = new ArrayList<>();
    private ArrayList<Choice> answers = new ArrayList<>();
    private ArrayList<Choice> choices = new ArrayList<>();
    private ArrayList<Choice> input = new ArrayList<>();

    public MultipleChoice(JSONObject json, String question, String answers, String choices ) {
        super(json);
        this.type = json.getInt("type");
        this.question = question;
        for (String answer : answers.split(";")) {
            Choice choice = new Choice(answer, this.getLanguage(), this);
            this.answers.add(choice);
            this.choices.add(choice);
        }
        for (String choice : choices.split(";")) {
            this.choices.add(new Choice(choice, this.getLanguage(), this));
        }
    }

    @Override
    public void setup() {
        this.getInstruction("multiple choice", true);

        input.clear();

        questionWords.clear();
        for (String string : question.split(" ")) {
            if (this.type == Question.toOld) {
                questionWords.add(new Vocab(string, main.getProfileSelected().getCourseSelected().getLanguage().getTranslation(string, main.getProfileSelected().getLanguage()), main.getProfileSelected().getCourseSelected().getLanguage()));
            } else {
                questionWords.add(new Vocab(string, null, main.getProfileSelected().getLanguage()));
            }
        }

        Collections.shuffle(this.choices);

        this.setCompleted(false);
    }

    @Override
    public void draw() {
        drawInstruction(y);

        int vocabX = Math.round(Main.getScreenWidth() / 2 - getQuestionLength() / 2);
        Vocab.drawVocab(questionWords, vocabX, y + 80);

        int choiceX = Math.round(Main.getScreenWidth() / 2 - this.getChoiceLength() / 2);
        int choiceY = y + 200;
        for (Choice choice : this.choices) {
            choice.draw(choiceX, choiceY, this.getChoiceLength());
            choiceY += (choice.getHeight() + GAP);
        }
    }

    @Override
    public void check() {
        for (Choice answer : this.answers) {
            if (!this.input.contains(answer)) {
                this.setCorrect(false);
                return;
            }
        }
        this.setCorrect(true);
    }

    @Override
    public boolean canCheck() {
        return this.input.size() > 0;
    }

    @Override
    public String getAnswers() {
        StringBuilder builder = new StringBuilder();
        for (Choice answer : this.answers) {
            builder.append(answer.getText());
            if (this.answers.indexOf(answer) != this.answers.size() - 1) {
                builder.append(" & ");
            }
        }
        return builder.toString();
    }

    @Override
    public void clicked() {
        for (Choice choice : this.choices) {
            choice.clicked();
        }
    }

    public ArrayList<Choice> getInput() {
        return input;
    }

    private float getQuestionLength() {
        float length = 0;
        for (Vocab vocab : questionWords) {
            length += (vocab.getLength() + GAP);
        }
        return length - GAP;
    }

    private float getChoiceLength() {
        float length = 0;
        for (Choice choice : this.choices) {
            if (choice.getLength() > length) {
                length = choice.getLength();
            }
        }
        return length;
    }

}
