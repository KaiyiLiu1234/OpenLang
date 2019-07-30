package main;

import main.rooms.Languages;
import org.apache.commons.io.FilenameUtils;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Language extends Entity {

    public static final int WIDTH = 214;
    public static final int HEIGHT = 40;
    //TODO: these types are used to determine how the language is displayed (alignment (only I think))
    private static final int LATIN = 0;
    private static final int ASIAN = 1;
    public static HashMap<Language, PFont> fontOverrides = new HashMap<>();
    private static PFont font;
    private int x;
    private int y;

    private JSONObject json; //for further use
    private String name; //the display name
    private String id; //its unique identifier (must be english, all lowercase)
    private int type;
    private PImage image; //usually the language's origin country's flag
    private HashMap<String, HashMap<Language, ArrayList<String>>> vocabulary = new HashMap<>();

    /**
     * @param path the absolute path of the language's directory
     */
    private Language(JSONObject json, String path) {
        this.json = json;
        this.name = json.getString("name");
        this.id = json.getString("id");
        this.type = json.getInt("type");
        String image = json.getString("image");

        if (!new File(path + "/" + image).exists()) {
            this.image = main.loadImage("images/error.png");
        } else {
            this.image = main.loadImage(path + "/" + image);
        }
        this.image.resize(0, HEIGHT);

        if (font == null) {
            font = main.createFont("Open Sans", 128);
        }
    }

    private void loadVocabulary() {
        JSONArray array = json.getJSONArray("vocabulary");
        for (int i = 0;i < array.size();i++) {
            JSONObject object = (JSONObject) array.get(i);
            HashMap<Language, ArrayList<String>> map = new HashMap<>();
            String word = null;
            for (Object k : object.keys()) {
                String key = (String) k;
                if (!key.equals(this.getID())) {
                    map.put(getLanguage(key), new ArrayList<>(Arrays.asList(object.getString(key).split(";"))));
                } else {
                    word = object.getString(key);
                }
            }
            this.vocabulary.put(word, map);
        }
    }

    private boolean inBounds() {
        return main.mouseX >= this.x && main.mouseX <= this.x + WIDTH && main.mouseY >= this.y && main.mouseY <= this.y + HEIGHT;
    }

    public String getID() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, HashMap<Language, ArrayList<String>>> getVocabulary() {
        return vocabulary;
    }

    //only use this if new language to old language
    public ArrayList<String> getTranslation(String string, Language language) {
        string = string.toLowerCase();
        if (!vocabulary.containsKey(string)) {
            return null;
        } else {
            return vocabulary.get(string).get(language);
        }
    }

    public void draw(int x, int y) {
        this.x = x;
        this.y = y;
//        //TODO: Temporary
//        main.stroke(0);
//        main.noFill();
//        main.rect(x, y, WIDTH, HEIGHT);

        main.image(this.image, x, y);

        main.textFont(fontOverrides.getOrDefault(this, font), 24);
        main.textAlign(PConstants.LEFT, PConstants.CENTER);
        main.fill(0);
        main.text(this.name, x + this.image.width + 3, y + HEIGHT / 2 - 5);

        if (main.getRoom() instanceof Languages) {
            if (main.getProfileSelected().getLanguage().equals(this)) {
                main.stroke(0, 255, 0);
                main.fill(0, 255, 0, 80);
                main.rect(x, y, WIDTH, HEIGHT);
            } else if (this.inBounds()) {
                main.stroke(0);
                main.fill(255, 255, 255, 80);
                main.rect(x, y, WIDTH, HEIGHT);
            }
        }
    }

    public JSONObject getJson() {
        return json;
    }

    public void clicked() {
        if (this.inBounds() && !main.getProfileSelected().getLanguage().equals(this)) {
            main.getProfileSelected().setLanguage(this);
        }
    }

    public PFont getFont() {
        return Language.fontOverrides.getOrDefault(this, font);
    }

    static void loadLanguages() {
        File[] languageFiles = new File("languages").listFiles();

        if (languageFiles != null) {
            for (File directory : languageFiles) {
                if (directory.isDirectory() && directory.listFiles() != null) {
                    for (File file : Objects.requireNonNull(directory.listFiles())) {
                        if (FilenameUtils.getExtension(file.getName()).equals("json")) {
                            JSONObject json = main.loadJSONObject(file.getAbsolutePath());
                            Language language = new Language(json, directory.getAbsolutePath());
                            main.getLanguages().add(language);
                            if (!json.isNull("font")) {
                                fontOverrides.put(language, main.createFont(directory.getAbsolutePath() + "/" + json.getString("font"), 128));
                            }
                        }
                    }
                }
            }
        }

        for (Language language : main.getLanguages()) {
            language.loadVocabulary();
        }
    }

    static Language getLanguage(String name) {
        for (Language language : main.getLanguages()) {
            if (language.id.equals(name)) {
                return language;
            }
        }
        return null;
    }
}
