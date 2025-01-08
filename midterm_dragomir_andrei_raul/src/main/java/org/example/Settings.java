public class Settings {
    private String language;
    private String startMessage;

    public Settings() {
        resetToDefaults();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language.toLowerCase();
        updateDefaultMessage();
    }

    public String getStartMessage() {
        return startMessage;
    }

    public void setStartMessage(String startMessage) {
        this.startMessage = startMessage;
    }

    public void resetToDefaults() {
        this.language = "english";
        updateDefaultMessage();
    }

    private void updateDefaultMessage() {
        if ("romanian".equals(language)) {
            this.startMessage = "Bun venit la Managerul de Colecții MTG!";
        } else {
            this.startMessage = "Welcome to MTG Card Manager!";
        }
    }
}
