import com.google.gson.*;
import java.io.*;

public class SettingsManager {
    private static final String DEFAULT_LANGUAGE = "english";
    private static final String DEFAULT_WELCOME_MESSAGE = "Welcome to MTG Collection Manager!";
    private String language;
    private String welcomeMessage;

    public SettingsManager() {
        this.language = DEFAULT_LANGUAGE;
        this.welcomeMessage = DEFAULT_WELCOME_MESSAGE;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public void resetToDefaults() {
        this.language = DEFAULT_LANGUAGE;
        this.welcomeMessage = DEFAULT_WELCOME_MESSAGE;
    }

    public void loadFromFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) return;

        try (Reader reader = new FileReader(file)) {
            Gson gson = new Gson();
            SettingsManager loadedSettings = gson.fromJson(reader, SettingsManager.class);
            this.language = loadedSettings.language;
            this.welcomeMessage = loadedSettings.welcomeMessage;
        }
    }

    public void saveToFile(String filePath) throws IOException {
        try (Writer writer = new FileWriter(filePath)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(this, writer);
        }
    }
}
