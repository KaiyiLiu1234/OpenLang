package main.questions;

import main.Language;
import main.Lesson;
import main.Main;
import processing.core.PConstants;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class WordBank extends Question {

    private static final int wordsPerRow = 15;
    private static final int GAP = 20;

    private String question;
    private ArrayList<Vocab> questionWords = new ArrayList<>();
    private ArrayList<ArrayList<String>> answers = new ArrayList<>();
    private ArrayList<String> choices; //wrong answers
    private ArrayList<Word> input = new ArrayList<>(); //the user's answer
    private ArrayList<Word> words = new ArrayList<>();

    public WordBank(JSONObject json, String question, String answer, String choices) {
        super(json);
        this.type = json.getInt("type");
        this.question = question;
        this.choices = new ArrayList<>(Arrays.asList(choices.split(",")));
        this.loadAnswers(answer);
    }

    private void loadAnswers(String answer) {
        this.answers.clear();
        for (String string : answer.split(";")) {
            ArrayList<String> list = new ArrayList<>(Arrays.asList(string.split(",")));
            this.answers.add(list);
        }
    }

    //length of a row in the word bank
    private float getLength(int row, ArrayList<Word> list) {
        float length = 0;
        for (int i = row * wordsPerRow; i < (row + 1) * wordsPerRow; i++) {
            if (i < list.size()) {
                length += (list.get(i).getWidth() + GAP);
            }
        }
        length -= GAP;
        return length;
    }

    private ArrayList<Word> getUnselectedWords() {
        ArrayList<Word> list = new ArrayList<>();
        for (Word word : this.words) {
            if (!this.input.contains(word)) {
                list.add(word);
            }
        }
        return list;
    }

    @Override
    public void setup() {
        this.getInstruction("word bank", true);

        input.clear();

        questionWords.clear();
        for (String string : question.split(" ")) {
            if (this.type == Question.toOld) {
                questionWords.add(new Vocab(string, main.getProfileSelected().getCourseSelected().getLanguage().getTranslation(string, main.getProfileSelected().getLanguage()), main.getProfileSelected().getCourseSelected().getLanguage()));
            } else {
                questionWords.add(new Vocab(string, main.getProfileSelected().getLanguage().getTranslation(string, main.getProfileSelected().getCourseSelected().getLanguage()), main.getProfileSelected().getLanguage()));
            }
        }

        words.clear();
        for (ArrayList<String> answer : this.answers) {
            for (String string : answer) {
                //if (!Word.contains(string, words)) { //for now allow repetition
                    words.add(new Word(string, this.input, this.getLanguage()));
                //}
            }
        }
        for (String choice : this.choices) {
            if (!Word.contains(choice, words)) {
                words.add(new Word(choice, this.input, this.getLanguage()));
            }
        }
        Collections.shuffle(words);

        this.setCompleted(false);
    }

    private float getQuestionLength() {
        float length = 0;
        for (Vocab vocab : questionWords) {
            length += (vocab.getLength() + GAP);
        }
        return length - GAP;
    }

    @Override
    public void draw() {
        drawInstruction(y);

        int vocabX = Math.round(Main.getScreenWidth() / 2 - getQuestionLength() / 2);
        Vocab.drawVocab(questionWords, vocabX, y + 80);

        //input
        int row = 0;
        int wordX = Math.round(Main.getScreenWidth() / 2 - this.getLength(row, this.input) / 2);
        int wordY = y + 180;
        for (Word word : this.input) {
            word.draw(wordX, wordY);
            wordX += (word.getWidth() + GAP);
            if ((this.input.indexOf(word) + 1) % wordsPerRow == 0) { //end of row
                row++;
                wordX = Math.round(Main.getScreenWidth() / 2 - this.getLength(row, this.input) / 2);
                wordY += 60;
            }
        }

        //choices
        //ArrayList<Word> words = this.getUnselectedWords();
        row = 0;
        wordX = Math.round(Main.getScreenWidth() / 2 - this.getLength(row, words) / 2);
        wordY = y + 280;
        for (Word word : words) {
            if (!this.input.contains(word)) {
                word.draw(wordX, wordY);
            }
            wordX += (word.getWidth() + GAP);
            if ((words.indexOf(word) + 1) % wordsPerRow == 0) { //end of row
                row++;
                wordX = Math.round(Main.getScreenWidth() / 2 - this.getLength(row, words) / 2);
                wordY += 60;
            }
        }
    }

    @Override
    public void clicked() {
        if (!this.isCompleted()) {
            for (Word word : this.words) {
                word.clicked();
            }
        }
    }

    @Override
    public void check() {
        for (ArrayList<String> answer : answers) {
            if (answer.size() == input.size()) {
                //order must match!
                for (int i = 0; i < answer.size(); i++) {
                    if (answer.get(i).equals(input.get(i).getWord())) {
                        this.setCorrect(true);
                        return;
                    }
                }
            }
        }
        this.setCorrect(false);
    }

    @Override
    public boolean canCheck() {
        return this.input.size() > 0;
    }

    @Override
    public String getAnswers() {
        StringBuilder builder = new StringBuilder();
        for (ArrayList<String> answer : this.answers) {
            for (String word : answer) {
                builder.append(word);
                builder.append(" ");
            }
            if (this.answers.indexOf(answer) != this.answers.size() - 1) {
                builder.append(" / ");
            }
        }
        return builder.toString();
    }

}
