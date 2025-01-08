import java.util.HashMap;
import java.util.Map;

public class MessageProvider {
    private static final Map<String, String> ENGLISH_MESSAGES = new HashMap<>();
    private static final Map<String, String> ROMANIAN_MESSAGES = new HashMap<>();

    static {
        // English Messages
        ENGLISH_MESSAGES.put("choose_mode", "Choose a mode: (start/settings)");
        ENGLISH_MESSAGES.put("invalid_choice", "Invalid choice. Please enter 'start' or 'settings'.");
        ENGLISH_MESSAGES.put("loaded_collection", "Loaded existing collection from ");
        ENGLISH_MESSAGES.put("no_existing_collection", "No existing collection found. Starting a new collection.");
        ENGLISH_MESSAGES.put("interactive_prompt", "What would you like to do? (view/add/remove/filter/favorite/favorites/exit)");
        ENGLISH_MESSAGES.put("collection_saved", "Collection saved to ");
        ENGLISH_MESSAGES.put("error_message", "Error: ");
        ENGLISH_MESSAGES.put("exiting_program", "Exiting program. Goodbye!");

        // Settings menu messages
        ENGLISH_MESSAGES.put("settings_menu", "Settings Menu:");
        ENGLISH_MESSAGES.put("language_prompt", "(language) Change language (English/Romanian)");
        ENGLISH_MESSAGES.put("startmessage_prompt", "(startmessage) Change the start message");
        ENGLISH_MESSAGES.put("default_prompt", "(default) Restore all settings to default");
        ENGLISH_MESSAGES.put("start_prompt", "(start) Exit settings and start the program");
        ENGLISH_MESSAGES.put("reset_defaults", "Settings restored to default.");
        ENGLISH_MESSAGES.put("exiting_settings", "Exiting settings. Starting the program...");
        ENGLISH_MESSAGES.put("enter_language", "Choose a language: (english/romanian)");
        ENGLISH_MESSAGES.put("invalid_language", "Invalid language. Please choose 'english' or 'romanian'.");
        ENGLISH_MESSAGES.put("language_set", "Language set to ");

        // Add card messages
        ENGLISH_MESSAGES.put("enter_card_type", "Enter card type (Creature, Spell, or Land):");
        ENGLISH_MESSAGES.put("invalid_card_type", "Invalid card type. Accepted values are Creature, Spell, or Land.");
        ENGLISH_MESSAGES.put("enter_card_name", "Enter card name:");
        ENGLISH_MESSAGES.put("empty_card_name", "Card name cannot be blank.");
        ENGLISH_MESSAGES.put("enter_mana_cost", "Enter mana cost:");
        ENGLISH_MESSAGES.put("positive_integer_required", "Value must be a positive integer.");
        ENGLISH_MESSAGES.put("enter_rarity", "Enter rarity (Common, Uncommon, Rare, Mythic):");
        ENGLISH_MESSAGES.put("invalid_rarity", "Invalid rarity. Accepted values are Common, Uncommon, Rare, Mythic.");
        ENGLISH_MESSAGES.put("enter_set", "Enter set:");
        ENGLISH_MESSAGES.put("empty_set", "Set cannot be blank.");
        ENGLISH_MESSAGES.put("enter_power", "Enter power:");
        ENGLISH_MESSAGES.put("enter_toughness", "Enter toughness:");
        ENGLISH_MESSAGES.put("enter_effect", "Enter effect:");
        ENGLISH_MESSAGES.put("empty_effect", "Effect cannot be blank.");
        ENGLISH_MESSAGES.put("enter_tapped", "Does it enter tapped? (true/false):");
        ENGLISH_MESSAGES.put("enter_mana_produced", "Enter mana produced (1, 2, or 3):");
        ENGLISH_MESSAGES.put("mana_produced_invalid", "Mana produced must be between 1 and 3.");

        // Remove card messages
        ENGLISH_MESSAGES.put("enter_card_to_remove", "Enter the name of the card to remove:");
        ENGLISH_MESSAGES.put("card_removed", "Card removed: ");
        ENGLISH_MESSAGES.put("card_not_found", "Card not found: ");

        // Filter messages
        ENGLISH_MESSAGES.put("filter_by", "Filter by (type/manaCost/rarity):");
        ENGLISH_MESSAGES.put("filter_by_type", "Enter card type (Creature, Spell, Land):");
        ENGLISH_MESSAGES.put("filter_by_mana_cost", "Enter mana cost to filter by:");
        ENGLISH_MESSAGES.put("filter_by_rarity", "Enter rarity to filter by (Common, Uncommon, Rare, Mythic):");

        // Favorites messages
        ENGLISH_MESSAGES.put("mark_favorite", "Enter the name of the card to mark as favorite:");
        ENGLISH_MESSAGES.put("view_favorites", "Viewing favorite cards:");
        ENGLISH_MESSAGES.put("no_favorites", "No favorite cards.");

        // Romanian Messages
        ROMANIAN_MESSAGES.put("choose_mode", "Alegeți un mod: (start/settings)");
        ROMANIAN_MESSAGES.put("invalid_choice", "Alegere nevalidă. Vă rugăm să introduceți 'start' sau 'settings'.");
        ROMANIAN_MESSAGES.put("loaded_collection", "Colecție existentă încărcată din ");
        ROMANIAN_MESSAGES.put("no_existing_collection", "Nu s-a găsit nicio colecție existentă. Încep o colecție nouă.");
        ROMANIAN_MESSAGES.put("interactive_prompt", "Ce ați dori să faceți? (view/add/remove/filter/favorite/favorites/exit)");
        ROMANIAN_MESSAGES.put("collection_saved", "Colecția a fost salvată în ");
        ROMANIAN_MESSAGES.put("error_message", "Eroare: ");
        ROMANIAN_MESSAGES.put("exiting_program", "Ieșirea din program. La revedere!");

        // Settings menu messages
        ROMANIAN_MESSAGES.put("settings_menu", "Meniu Setări:");
        ROMANIAN_MESSAGES.put("language_prompt", "(language) Schimbați limba (Engleză/Română)");
        ROMANIAN_MESSAGES.put("startmessage_prompt", "(startmessage) Schimbați mesajul de întâmpinare");
        ROMANIAN_MESSAGES.put("default_prompt", "(default) Resetați toate setările la valorile implicite");
        ROMANIAN_MESSAGES.put("start_prompt", "(start) Ieșiți din setări și porniți programul");
        ROMANIAN_MESSAGES.put("reset_defaults", "Setările au fost resetate la valorile implicite.");
        ROMANIAN_MESSAGES.put("exiting_settings", "Ieșirea din setări. Programul începe...");
        ROMANIAN_MESSAGES.put("enter_language", "Alegeți o limbă: (english/romanian)");
        ROMANIAN_MESSAGES.put("invalid_language", "Limbă nevalidă. Alegeți 'english' sau 'romanian'.");
        ROMANIAN_MESSAGES.put("language_set", "Limba setată la ");

        // Add card messages
        ROMANIAN_MESSAGES.put("enter_card_type", "Introduceți tipul cardului (Creature, Spell, sau Land):");
        ROMANIAN_MESSAGES.put("invalid_card_type", "Tip de card nevalid. Valorile acceptate sunt Creature, Spell sau Land.");
        ROMANIAN_MESSAGES.put("enter_card_name", "Introduceți numele cardului:");
        ROMANIAN_MESSAGES.put("empty_card_name", "Numele cardului nu poate fi gol.");
        ROMANIAN_MESSAGES.put("enter_mana_cost", "Introduceți costul de mana:");
        ROMANIAN_MESSAGES.put("positive_integer_required", "Valoarea trebuie să fie un număr întreg pozitiv.");
        ROMANIAN_MESSAGES.put("enter_rarity", "Introduceți raritatea (Common, Uncommon, Rare, Mythic):");
        ROMANIAN_MESSAGES.put("invalid_rarity", "Raritate nevalidă. Valorile acceptate sunt Common, Uncommon, Rare, Mythic.");
        ROMANIAN_MESSAGES.put("enter_set", "Introduceți setul:");
        ROMANIAN_MESSAGES.put("empty_set", "Setul nu poate fi gol.");
        ROMANIAN_MESSAGES.put("enter_power", "Introduceți puterea:");
        ROMANIAN_MESSAGES.put("enter_toughness", "Introduceți rezistența:");
        ROMANIAN_MESSAGES.put("enter_effect", "Introduceți efectul:");
        ROMANIAN_MESSAGES.put("empty_effect", "Efectul nu poate fi gol.");
        ROMANIAN_MESSAGES.put("enter_tapped", "Intră în joc tap-at? (true/false):");
        ROMANIAN_MESSAGES.put("enter_mana_produced", "Introduceți mana produsă (1, 2, sau 3):");
        ROMANIAN_MESSAGES.put("mana_produced_invalid", "Mana produsă trebuie să fie între 1 și 3.");

        // Remove card messages
        ROMANIAN_MESSAGES.put("enter_card_to_remove", "Introduceți numele cardului de eliminat:");
        ROMANIAN_MESSAGES.put("card_removed", "Card eliminat: ");
        ROMANIAN_MESSAGES.put("card_not_found", "Cardul nu a fost găsit: ");

        // Filter messages
        ROMANIAN_MESSAGES.put("filter_by", "Filtrați după (tip/costMana/raritate):");
        ROMANIAN_MESSAGES.put("filter_by_type", "Introduceți tipul cardului (Creature, Spell, Land):");
        ROMANIAN_MESSAGES.put("filter_by_mana_cost", "Introduceți costul de mana pentru filtrare:");
        ROMANIAN_MESSAGES.put("filter_by_rarity", "Introduceți raritatea pentru filtrare (Common, Uncommon, Rare, Mythic):");

        // Favorites messages
        ROMANIAN_MESSAGES.put("mark_favorite", "Introduceți numele cardului pentru a-l marca drept favorit:");
        ROMANIAN_MESSAGES.put("view_favorites", "Vizualizarea cardurilor favorite:");
        ROMANIAN_MESSAGES.put("no_favorites", "Niciun card favorit.");
    }

    private String language;

    public MessageProvider(String initialLanguage) {
        this.language = initialLanguage.toLowerCase();
    }

    public void setLanguage(String language) {
        this.language = language.toLowerCase();
    }

    public String getMessage(String key) {
        Map<String, String> messages = "romanian".equals(language) ? ROMANIAN_MESSAGES : ENGLISH_MESSAGES;
        return messages.getOrDefault(key, "[Missing message for key: " + key + "]");
    }
}
