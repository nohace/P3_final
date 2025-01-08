import java.util.List;
import java.util.Scanner;
import java.io.IOException;


public class MTGCardCollectionTest {
    private static final String SETTINGS_FILE = "settings.json";
    private static final String JSON_FILE = "cards.json";

    public static void main(String[] args) {
        try {
            SettingsManager settingsManager = new SettingsManager();
            settingsManager.loadFromFile(SETTINGS_FILE);
            MessageProvider messageProvider = new MessageProvider(settingsManager.getLanguage());

            Scanner scanner = new Scanner(System.in);
            
            System.out.println(messageProvider.getMessage("choose_mode"));
            while (true) {
                String mode = scanner.nextLine().trim().toLowerCase();
                if ("start".equals(mode)) {
                    runInteractiveMode(scanner, messageProvider, settingsManager);
                    break;
                } else if ("settings".equals(mode)) {
                    runSettingsMode(scanner, messageProvider, settingsManager);
                    break;
                } else {
                    System.out.println(messageProvider.getMessage("invalid_choice"));
                }
            }

        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    private static void runInteractiveMode(Scanner scanner, MessageProvider messageProvider, SettingsManager settingsManager) {
        try {
            CardCollection cardCollection = new CardCollection();

            try {
                cardCollection.loadFromJSON(JSON_FILE);
                System.out.println(messageProvider.getMessage("loaded_collection") + JSON_FILE);
            } catch (Exception e) {
                System.out.println(messageProvider.getMessage("no_existing_collection"));
            }

            System.out.println(settingsManager.getWelcomeMessage());

            while (true) {
                System.out.println(messageProvider.getMessage("interactive_prompt"));
                String action = scanner.nextLine().trim().toLowerCase();

                switch (action) {
                    case "view":
                        viewCollection(cardCollection);
                        break;
                    case "add":
                        addCardFromUserInput(cardCollection, scanner, messageProvider);
                        cardCollection.saveToJSON(JSON_FILE);
                        break;
                    case "remove":
                        removeCardFromUserInput(cardCollection, scanner, messageProvider);
                        cardCollection.saveToJSON(JSON_FILE);
                        break;
                    case "favorite":
                        manageFavorites(cardCollection, scanner, messageProvider);
                        cardCollection.saveToJSON(JSON_FILE);
                        break;
                    case "favorites":
                        viewFavorites(cardCollection);
                        break;
                    case "filter":
                        filterCards(cardCollection, scanner, messageProvider);
                        break;
                    case "exit":
                        System.out.println(messageProvider.getMessage("exiting_program"));
                        cardCollection.saveToJSON(JSON_FILE);
                        return;
                    default:
                        System.out.println(messageProvider.getMessage("invalid_choice"));
                }
            }

        } catch (Exception e) {
            System.out.println("Unexpected error in interactive mode: " + e.getMessage());
        }
    }

    public static void runSettingsMode(Scanner scanner, MessageProvider messageProvider, SettingsManager settingsManager) {
        while (true) {
            System.out.println(messageProvider.getMessage("settings_menu"));
            System.out.println(messageProvider.getMessage("language_prompt"));
            System.out.println(messageProvider.getMessage("startmessage_prompt"));
            System.out.println(messageProvider.getMessage("default_prompt"));
            System.out.println(messageProvider.getMessage("start_prompt"));

            String option = scanner.nextLine().trim().toLowerCase();

            switch (option) {
                case "language":
                    changeLanguage(settingsManager, messageProvider, scanner);
                    break;
                case "startmessage":
                    updateStartMessage(settingsManager, scanner);
                    break;
                case "default":
                    settingsManager.resetToDefaults();
                    System.out.println(messageProvider.getMessage("reset_defaults"));
                    break;
                case "start":
                    System.out.println(messageProvider.getMessage("exiting_settings"));
                    try {
                        settingsManager.saveToFile(SETTINGS_FILE);
                    } catch (IOException e) {
                        System.out.println("Error saving settings: " + e.getMessage());
                    }
                    runInteractiveMode(scanner, messageProvider, settingsManager);
                    return;
                default:
                    System.out.println(messageProvider.getMessage("invalid_choice"));
            }
        }
    }


    private static void changeLanguage(SettingsManager settingsManager, MessageProvider messageProvider, Scanner scanner) {
        System.out.println(messageProvider.getMessage("enter_language"));
        String language = scanner.nextLine().trim().toLowerCase();

        if ("english".equals(language) || "romanian".equals(language)) {
            settingsManager.setLanguage(language);
            messageProvider.setLanguage(language);
            System.out.println(messageProvider.getMessage("language_set") + language);
        } else {
            System.out.println(messageProvider.getMessage("invalid_language"));
        }
    }

    private static void updateStartMessage(SettingsManager settingsManager, Scanner scanner) {
        System.out.println("Enter a new start message:");
        String newMessage = scanner.nextLine().trim();
        settingsManager.setWelcomeMessage(newMessage);
        System.out.println("Start message updated.");
    }

    private static void manageFavorites(CardCollection cardCollection, Scanner scanner, MessageProvider messageProvider) {
        System.out.println(messageProvider.getMessage("mark_favorite"));
        String cardName = scanner.nextLine().trim();

        for (Card card : cardCollection.getAllCards()) {
            if (card.getName().equalsIgnoreCase(cardName)) {
                card.setFavorite(true);
                System.out.println("Card marked as favorite: " + card.getName());
                return;
            }
        }
        System.out.println("Card not found: " + cardName);
    }

    private static void viewFavorites(CardCollection cardCollection) {
        System.out.println("\n" + new MessageProvider("english").getMessage("view_favorites"));
        List<Card> favorites = cardCollection.getFavoriteCards();

        if (favorites.isEmpty()) {
            System.out.println(new MessageProvider("english").getMessage("no_favorites"));
        } else {
            for (Card card : favorites) {
                card.displayCard();
            }
        }
    }

    private static void viewCollection(CardCollection cardCollection) {
        if (cardCollection.getAllCards().isEmpty()) {
            System.out.println("The collection is empty.");
            return;
        }

        System.out.println("\n+-----------------------------------------+");
        System.out.println("|             Card Collection             |");
        System.out.println("+-----------------------------------------+");

        for (Card card : cardCollection.getAllCards()) {
            card.displayCard();
        }

        System.out.println("+-----------------------------------------+");
        System.out.println("|           End of Card Collection        |");
        System.out.println("+-----------------------------------------+\n");
    }

    private static void addCardFromUserInput(CardCollection cardCollection, Scanner scanner, MessageProvider messageProvider) {
        System.out.println(messageProvider.getMessage("enter_card_type"));
        String type = scanner.nextLine().trim().toLowerCase();

        if (!(type.equals("creature") || type.equals("spell") || type.equals("land"))) {
            System.out.println(messageProvider.getMessage("invalid_card_type"));
            return;
        }

        System.out.println(messageProvider.getMessage("enter_card_name"));
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println(messageProvider.getMessage("empty_card_name"));
            return;
        }

        System.out.println(messageProvider.getMessage("enter_mana_cost"));
        int manaCost = getValidPositiveInteger(scanner, messageProvider.getMessage("positive_integer_required"));

        System.out.println(messageProvider.getMessage("enter_rarity"));
        String rarity = scanner.nextLine().trim();
        if (!(rarity.equalsIgnoreCase("common") || rarity.equalsIgnoreCase("uncommon") ||
                rarity.equalsIgnoreCase("rare") || rarity.equalsIgnoreCase("mythic"))) {
            System.out.println(messageProvider.getMessage("invalid_rarity"));
            return;
        }

        System.out.println(messageProvider.getMessage("enter_set"));
        String set = scanner.nextLine().trim();
        if (set.isEmpty()) {
            System.out.println(messageProvider.getMessage("empty_set"));
            return;
        }

        switch (type) {
            case "creature":
                System.out.println(messageProvider.getMessage("enter_power"));
                int power = getValidPositiveInteger(scanner, messageProvider.getMessage("positive_integer_required"));
                System.out.println(messageProvider.getMessage("enter_toughness"));
                int toughness = getValidPositiveInteger(scanner, messageProvider.getMessage("positive_integer_required"));
                cardCollection.addCard(new CreatureCard(name, manaCost, rarity, set, power, toughness));
                break;

            case "spell":
                System.out.println(messageProvider.getMessage("enter_effect"));
                String effect = scanner.nextLine().trim();
                if (effect.isEmpty()) {
                    System.out.println(messageProvider.getMessage("empty_effect"));
                    return;
                }
                cardCollection.addCard(new SpellCard(name, manaCost, rarity, set, effect));
                break;

            case "land":
                System.out.println(messageProvider.getMessage("enter_tapped"));
                boolean entersTapped = getValidBoolean(scanner);
                System.out.println(messageProvider.getMessage("enter_mana_produced"));
                int manaProduced = getValidInteger(scanner, messageProvider.getMessage("mana_produced_invalid"));
                if (manaProduced < 1 || manaProduced > 3) {
                    System.out.println(messageProvider.getMessage("mana_produced_invalid"));
                    return;
                }
                cardCollection.addCard(new LandCard(name, rarity, set, entersTapped, manaProduced));
                break;
        }
    }

    private static void removeCardFromUserInput(CardCollection cardCollection, Scanner scanner, MessageProvider messageProvider) {
        System.out.println(messageProvider.getMessage("enter_card_to_remove"));
        String name = scanner.nextLine().trim();

        Card removedCard = cardCollection.removeCard(name);
        if (removedCard != null) {
            System.out.println(messageProvider.getMessage("card_removed") + name);
        } else {
            System.out.println(messageProvider.getMessage("card_not_found") + name);
        }
    }

    private static void filterCards(CardCollection cardCollection, Scanner scanner, MessageProvider messageProvider) {
        System.out.println(messageProvider.getMessage("filter_by"));
        String filterType = scanner.nextLine().trim().toLowerCase();

        switch (filterType) {
            case "type":
                System.out.println(messageProvider.getMessage("filter_by_type"));
                String cardType = scanner.nextLine().trim();
                List<Card> cardsByType = cardCollection.filter(card -> card.getType().equalsIgnoreCase(cardType));
                displayFilteredCards(cardsByType);
                break;

            case "manacost":
                System.out.println(messageProvider.getMessage("filter_by_mana_cost"));
                int manaCost = getValidPositiveInteger(scanner, messageProvider.getMessage("positive_integer_required"));
                List<Card> cardsByManaCost = cardCollection.filter(card -> card.getManaCost() == manaCost);
                displayFilteredCards(cardsByManaCost);
                break;

            case "rarity":
                System.out.println(messageProvider.getMessage("filter_by_rarity"));
                String rarity = scanner.nextLine().trim();
                List<Card> cardsByRarity = cardCollection.filter(card -> card.getRarity().equalsIgnoreCase(rarity));
                displayFilteredCards(cardsByRarity);
                break;

            default:
                System.out.println(messageProvider.getMessage("invalid_choice"));
        }
    }

    private static void displayFilteredCards(List<Card> cards) {
        if (cards.isEmpty()) {
            System.out.println("No cards matched the filter.");
        } else {
            cards.forEach(Card::displayCard);
        }
    }

    private static int getValidPositiveInteger(Scanner scanner, String errorMessage) {
        while (true) {
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value > 0) {
                    return value;
                } else {
                    System.out.println(errorMessage);
                }
            } catch (NumberFormatException e) {
                System.out.println(errorMessage);
            }
        }
    }

    private static int getValidInteger(Scanner scanner, String errorMessage) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(errorMessage);
            }
        }
    }

    private static boolean getValidBoolean(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("true") || input.equals("false")) {
                return Boolean.parseBoolean(input);
            } else {
                System.out.println("Please enter 'true' or 'false'.");
            }
        }
    }
}
