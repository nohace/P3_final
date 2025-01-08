import com.google.gson.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CardCollection {
    private final List<Card> cards;

    public CardCollection() {
        this.cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        for (Card existingCard : cards) {
            if (existingCard.getName().equalsIgnoreCase(card.getName())) {
                throw new IllegalArgumentException("A card with this name already exists in the collection.");
            }
        }
        cards.add(card);
    }

    public Card removeCard(String name) {
        for (Card card : cards) {
            if (card.getName().equalsIgnoreCase(name)) {
                cards.remove(card);
                return card; // Return the removed card
            }
        }
        return null; // Return null if no card is found
    }


    public List<Card> filter(Filterable filter) {
        List<Card> filteredCards = new ArrayList<>();
        for (Card card : cards) {
            if (filter.matches(card)) {
                filteredCards.add(card);
            }
        }
        return filteredCards;
    }

    public List<Card> getAllCards() {
        return cards;
    }

    public List<Card> getFavoriteCards() {
        List<Card> favoriteCards = new ArrayList<>();
        for (Card card : cards) {
            if (card.isFavorite()) {
                favoriteCards.add(card);
            }
        }
        return favoriteCards;
    }

    public void saveToJSON(String filePath) throws IOException {
        Gson gson = createGsonInstance();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(cards, writer);
        }
    }

    public void loadFromJSON(String filePath) throws IOException {
        Gson gson = createGsonInstance();
        try (FileReader reader = new FileReader(filePath)) {
            Card[] loadedCards = gson.fromJson(reader, Card[].class);
            cards.clear();
            if (loadedCards != null) {
                for (Card card : loadedCards) {
                    cards.add(card);
                }
            }
        }
    }

    private Gson createGsonInstance() {
        RuntimeTypeAdapterFactory<Card> typeFactory = RuntimeTypeAdapterFactory.of(Card.class, "type")
                .registerSubtype(CreatureCard.class, "Creature")
                .registerSubtype(SpellCard.class, "Spell")
                .registerSubtype(LandCard.class, "Land");

        return new GsonBuilder()
                .registerTypeAdapterFactory(typeFactory)
                .setPrettyPrinting()
                .create();
    }
}
